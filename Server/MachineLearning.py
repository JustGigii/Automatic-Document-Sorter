import tensorflow as tf
import numpy as np
import enum
import config
#max_length = 100
training_size = 140


class Location(enum.IntEnum):
    Lable = 0 #סוג הדוח
    Root = 1 #מיקום הקובץ
    wordtoken =2 #המילים בקובץ
    bagofword = 3 #מבני הנתונים
    clientname =4 #שם הלקוח


def buildmodel(labels,input):
    training_input = input[0:training_size]
    testing_input = input[training_size:]
    training_labels = labels[0:training_size]
    testing_labels = labels[training_size:]
    training_labels = np.array(training_labels).astype(np.uint8)
    training_padded = np.array(training_input)
    testing_labels = np.array(testing_labels).astype(np.uint8)
    testing_padded = np.array(testing_input)
    model = tf.keras.models.Sequential()
    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(16, activation=config.ACTIVATION))
    model.add(tf.keras.layers.Dense(2, activation=config.ACTIVATION))
    model.compile(loss=config.LOSSFUNCTION, optimizer=config.OPTIMIZER, metrics=[config.METRICS])
    model.fit(training_padded, training_labels, epochs=200, validation_data=(testing_padded, testing_labels), verbose=2)
    model.save(config.MODELNANE)


def predict(model,data):
    for index, bagofword in enumerate(data):
        if len(bagofword)>3:
            testing_padded = np.array(bagofword[int(Location.bagofword)]).reshape(1, 900)
            predict = model.predict(testing_padded)[0]
            label = np.argmax(predict)
            if predict[0] > 0.9 or predict[1] > 0.9:
                if label == 1:
                    data[index][int(Location.Lable)] = config.REPORTTYPE2
                elif label == 0:
                    data[index][int(Location.Lable)] = config.REPORTTYPE1
    return data



