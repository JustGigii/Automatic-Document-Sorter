import os
from pdf2image import convert_from_path
import pytesseract
import CleanText
import HebrewNlp
from collections import Counter
from numba import jit
import config


@jit(parallel=True)
def ReadPdf(root,name,iscrop):
    pages = []
    pages = convert_from_path(root, poppler_path=os.path.join(config.POPPLER))
    (Name,file) = name.split('.')
    PicName =Name+"."+config.IMAGETYPE
    pages[0].save(os.path.join(config.WORKSPACE, PicName), config.IMAGETYPE)
    if iscrop:
        return os.path.join(config.WORKSPACE, PicName)
    else:
        words = imageToword(PicName)
        os.remove(os.path.join(config.WORKSPACE, PicName))
        return words

@jit(parallel=True)
def imageToword(PicName):
    pytesseract.pytesseract.tesseract_cmd = config.PYTESSERACT
    return pytesseract.image_to_string(os.path.join(config.WORKSPACE, PicName), lang=config.PYTESSERACT_LANGUAGE)

def Tokenize(root,name):
    fileName = ReadPdf(root,name,False)
    text = CleanText.CleanTheText(fileName)

    tokenizer = HebrewNlp.HebrewTokenizer(text)
    for i, element in enumerate(tokenizer):
        if (len(element) <= 1):
            tokenizer.pop(i)
    return tokenizer

def GetName(PicName,allname,allfname):
    text = imageToword(PicName)
    new_text  =" ".join(text.split())
    return HebrewNlp.GetName(new_text,allname,allfname)

def margedictionaries(dictionarie1,dictionarie2):
    ini_dictionary1 = Counter(dictionarie1)
    ini_dictionary2 = Counter(dictionarie2)
    return ini_dictionary1 + ini_dictionary2


def BuildModelToTraing(root,name):
    rap = ()
    try:
        wordfreq = {}
        wordtoken = Tokenize(root,name)
        for token in wordtoken:
            if token not in wordfreq.keys():
                wordfreq[token] = 1
            else:
                wordfreq[token] += 1
        rap = [config.ERRORLABEL, root, wordtoken]
        if config.REPORTTYPE1 in root:
            rap = [config.REPORTTYPE1, root,wordtoken]
        if config.REPORTTYPE2 in root:
            rap = [config.REPORTTYPE2,root, wordtoken]
        return [rap,wordfreq]
    except Exception as e:
        return [config.ERRORLABEL,root,config.ERRORLABEL]




