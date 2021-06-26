import pathlib
import os
import cv2
import numpy as np
import tensorflow as tf
from PIL import Image
import Pdf2Text
import sys
import config
sys.path.insert(0,config.OBJECT_DECTECTION)
from object_detection.utils import label_map_util
from object_detection.utils import ops as utils_ops
from object_detection.utils import visualization_utils as vis_util



def run_inference_for_single_image(model, image):
    image = np.asarray(image)
    # The input needs to be a tensor, convert it using `tf.convert_to_tensor`.
    input_tensor = tf.convert_to_tensor(image)
    # The model expects a batch of images, so add an axis with `tf.newaxis`.
    input_tensor = input_tensor[tf.newaxis, ...]

    # Run inference
    model_fn = model.signatures[config.MODELD_DECTECTION_NAME]
    output_dict = model_fn(input_tensor)

    # All outputs are batches tensors.
    # Convert to numpy arrays, and take index [0] to remove the batch dimension.
    # We're only interested in the first num_detections.
    num_detections = int(output_dict.pop(config.NUM_DETECTIONS))
    output_dict = {key: value[0, :num_detections].numpy()
                   for key, value in output_dict.items()}
    output_dict[config.NUM_DETECTIONS] = num_detections

    # detection_classes should be ints.
    output_dict[config.DETECTIONS_CLASSES] = output_dict[config.DETECTIONS_CLASSES].astype(np.int64)

    # Handle models with masks:
    if config.DETECTION_MASK in output_dict:
        # Reframe the the bbox mask to the image size.
        detection_masks_reframed = utils_ops.reframe_box_masks_to_image_masks(
            output_dict[config.DETECTION_MASK], output_dict[config.DETECTION_BOXES],
            image.shape[0], image.shape[1])
        detection_masks_reframed = tf.cast(detection_masks_reframed > 0.5,
                                           tf.uint8)
        output_dict[config.DETECTION_MASKS_REFRMED] = detection_masks_reframed.numpy()
    return output_dict


def crop_select_only(image_np, output_dict,name,image_path):
    img_height, img_width, img_channel = image_np.shape
    absolute_coord = []
    N = len(output_dict[config.DETECTION_BOXES])
    for i in range(N):
        if output_dict[config.DETECTION_SCORES][i] > 0.9:
            box = output_dict[config.DETECTION_BOXES][i]
            ymin, xmin, ymax, xmax = box
            x_up = int(xmin * img_width)
            y_up = int(ymin * img_height)
            x_down = int(xmax * img_width)
            y_down = int(ymax * img_height)
            absolute_coord.append((x_up, y_up, x_down, y_down))

    bounding_box_img = []
    for c in absolute_coord:
        bounding_box_img.append(image_np[c[1]:c[3], c[0]:c[2], :])
    root_name = os.path.join(image_path)
    print(root_name)
    os.remove(image_path)
    cv2.imwrite(root_name,  bounding_box_img[0])
    #display(Image.fromarray(image))


def show_inference(model, image_path,category_index,name):
  # the array based representation of the image will be used later in order to prepare the
  # result image with boxes and labels on it.
  image_np = np.array(Image.open(os.path.join(image_path)))
  # Actual detection.
  output_dict = run_inference_for_single_image(model, image_np)
  # Visualization of the results of a detection.
  vis_util.visualize_boxes_and_labels_on_image_array(
      image_np,
      output_dict[config.DETECTION_BOXES],
      output_dict[config.DETECTIONS_CLASSES],
      output_dict[config.DETECTION_SCORES],
      category_index,
      instance_masks=output_dict.get(config.DETECTION_MASKS_REFRMED, None),
      use_normalized_coordinates=True,
      line_thickness=8)
  crop_select_only(image_np, output_dict,name,image_path)


def Classtfy(category_index, detection_model ,pdf_path,name,allname,allfname):
    image_path =Pdf2Text.ReadPdf(pdf_path,name,True)
    show_inference(detection_model, image_path,category_index,name)
    (newname,dot) = name.split('.')
    pname = newname +'.'+config.IMAGETYPE
    name = Pdf2Text.GetName(pname,allname,allfname)
    os.remove(os.path.join(image_path))
    return name
def Load_Pic_Model():
    # patch tf1 into `utils.ops`
    utils_ops.tf = tf.compat.v1
    # Patch the location of gfile
    tf.gfile = tf.io.gfile
    PATH_TO_LABELS = config.PATH_TO_LABELS
    category_index = label_map_util.create_category_index_from_labelmap(PATH_TO_LABELS, use_display_name=True)
    detection_model = tf.saved_model.load(config.LOAD_DETCTION_MODEL)
    return [category_index, detection_model]


