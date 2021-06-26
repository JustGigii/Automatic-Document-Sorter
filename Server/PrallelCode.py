import os
import multiprocessing
import time
import numpy as np
import threading
import pickle
import Pdf2Text
import heapq
import config
theadcount = 0
processBagofwords = {}
prossesstokenarry =[]


def process(files,root,return_dict,procnum):
    if(len(files)>0):
        Bagofwords={}
        theadsarry =[]
        tokenarry = []
        global processBagofwords
        global prossesstokenarry
        processlock = threading.Lock()
        global theadcount
        splitedfilses = np.array_split(files,10)
        for newfiles in splitedfilses:
            t1 = threading.Thread(target=Theard, args=(root,newfiles,processlock,))
            theadsarry.append(t1)
            theadcount+=1
        time.sleep(0.5)
        for t in theadsarry:
            t.start()
        while(theadcount>0):
            if( len(processBagofwords)>0):
                processlock.acquire()
                Bagofwords = Pdf2Text.margedictionaries(Bagofwords,processBagofwords)
                processBagofwords= {}
                processlock.release()
            if(len(prossesstokenarry)>0):
                processlock.acquire()
                tokenarry += prossesstokenarry
                prossesstokenarry=[]
                processlock.release()
        return_dict[procnum] = [tokenarry,Bagofwords]


def Theard(root,files,processlock,):
    global processBagofwords
    global prossesstokenarry
    global theadcount
    theardbagofword = {}
    tokenarry = []
    try:
        for name in files:
            bagofword = Pdf2Text.BuildModelToTraing(os.path.join(os.path.join(root, name)), name, )
            theardbagofword = Pdf2Text.margedictionaries(theardbagofword, bagofword[1])
            tokenarry.append(bagofword[0])
        processlock.acquire()
        processBagofwords = Pdf2Text.margedictionaries(processBagofwords, theardbagofword)
        prossesstokenarry += tokenarry
        theadcount -= 1
        processlock.release()
    except Exception as err:
        processlock.acquire()
        theadcount -= 1
        processlock.release()
        raise err


def Startingproses(cpu_count, dircount, i, return_dict, theardonjobnow):
    for baseroot, directories, nonfiles in os.walk(os.path.join(os.path.join(config.WORKSPACE,config.TRANING)), topdown=False):
        allfiles = []
        for root, dirs, files in os.walk(baseroot):
            for f in files:
                allfiles.append(f)
            splitefile = np.array_split(files, cpu_count / dircount)
            for filestosend in splitefile:
                if (baseroot == (os.path.join(config.WORKSPACE,config.TRANING))):
                    p1 = multiprocessing.Process(target=process, args=(filestosend, root, return_dict, i))
                    theardonjobnow.append(p1)
                    i = i + 1
    for p in theardonjobnow:
        p.start()

def BuildBagOfWord(Bagofwords, cpu_count, dircount, i, return_dict, theardonjobnow, tokenarry):
    for baseroot, directories, nonfiles in os.walk(os.path.join(os.path.join(config.WORKSPACE,config.TRANING)), topdown=False):
        if len(directories) > dircount:
            dircount = len(directories)
    Startingproses(cpu_count, dircount, i, return_dict, theardonjobnow)
    while (len(theardonjobnow) > 0):
        for poress in theardonjobnow:
            if poress.is_alive() == False:
                theardonjobnow.remove(poress)
    time.sleep(0.2)
    for x in return_dict:
        Bagofwords = Pdf2Text.margedictionaries(return_dict[x][1], Bagofwords)
        tokenarry += return_dict[x][0]
    most_freq = heapq.nlargest(900, Bagofwords, key=Bagofwords.get)
    file = open(os.path.join(config.WORKSPACE,config.BAGOFWORDSFILE), 'wb')
    pickle.dump(most_freq, file)
    file = open(os.path.join(config.WORKSPACE,config.TOKENTOTRAINFILE), 'wb')
    pickle.dump(tokenarry, file)


def GetDataFromFile(tokentotrain):
    try:
        traningset = []
        file = open(os.path.join(config.WORKSPACE, config.BAGOFWORDSFILE), 'rb')
        Bagofwords = pickle.load(file)
        file.close()
        if (tokentotrain == None):
            file = open(os.path.join(config.WORKSPACE, config.TOKENTOTRAINFILE), 'rb')
            tokentotrain = pickle.load(file)
            file.close()
        for file in tokentotrain:
            data = file
            sent_vec = []
            for word in Bagofwords:
                if word in file[2]:
                    sent_vec.append(1)
                else:
                    sent_vec.append(0)
            sent_vec = np.asarray(sent_vec)
            data.append(sent_vec)
            traningset.append(data)
        return (Bagofwords, traningset)
    except:
        return config.ERRORLABEL

def begofword():
    cpu_count = multiprocessing.cpu_count()
    Bagofwords = {}
    tokenarry = []
    theardonjobnow = []
    manager = multiprocessing.Manager()
    return_dict = manager.dict()
    dircount = 0
    i = 0
    if(not os.path.exists(os.path.join(config.WORKSPACE, config.BAGOFWORDSFILE))):
        BuildBagOfWord(Bagofwords, cpu_count, dircount, i, return_dict, theardonjobnow, tokenarry)
    return GetDataFromFile(None)


def bagofwordonlytheard(when):
    Bagofwords = {}
    theadsarry = []
    tokenarry = []
    global processBagofwords
    global prossesstokenarry
    global theadcount
    processlock = threading.Lock()
    allfiles = []
    for root, directories, files in os.walk(when, topdown=False):
        allfiles += (files)
    if len(allfiles) >2:
        splitedfilses = np.array_split(allfiles, len(allfiles) / 2)
        for newfiles in splitedfilses:
            t1 = threading.Thread(target=Theard, args=(root, newfiles, processlock,))
            theadsarry.append(t1)
            theadcount += 1
    else:
        splitedfilses =allfiles
        t1 = threading.Thread(target=Theard, args=(root, splitedfilses, processlock,))
        theadsarry.append(t1)
        theadcount += 1
    time.sleep(0.5)
    for t in theadsarry:
        t.start()
    while (theadcount > 0):
        if (len(processBagofwords) > 0):
            processlock.acquire()
            Bagofwords = Pdf2Text.margedictionaries(Bagofwords, processBagofwords)
            processBagofwords = {}
            processlock.release()
        if (len(prossesstokenarry) > 0):
            processlock.acquire()
            tokenarry += prossesstokenarry
            prossesstokenarry = []
            processlock.release()
    send = GetDataFromFile(tokenarry)[1]
    return send