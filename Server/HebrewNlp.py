import requests
import Pdf2Text
import os
import csv
import numpy as np
import config

def HebrewTokenizer(Text):
    try:
        request = {"token":  config.TOKENHEBNLP ,"text": Text}
        return requests.post(config.TOKENIZERURL, json=request).json()[0]
    except:
        arry = Text.split(' ')
        return  arry

def GetName(text,allname,allfname):
    if(type(text)== str):
        arry = text.split(" ")
    else:
        arry = text
    i= 0
    firstname = ""
    lastname = ""
    havefullname = False
    while i < (len(arry)) and not havefullname:
        if lastname == "" and not (i+1==len(arry)):
            two=arry[i]+ " "+arry[i+1]
            if(two in allfname ):
                lastname = two
                i+=1
            elif str(arry[i]) in allfname:
                lastname = arry[i]
        elif(firstname == "" and  i+2 <len(arry)):
            two = arry[i+1]+ " "+ arry[i+2]
            if(str(arry[i]) in allname and two != "שם האב."):
                    firstname = arry[i]
        if (not(firstname == "") and not (lastname=="")):
            havefullname = True
        i+=1
    if havefullname:
        return firstname + " "+ lastname
    else:
        return config.ERRORLABEL







