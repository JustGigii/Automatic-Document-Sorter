import os
import warnings
import PrallelCode
import MachineLearning
import random
import pickle
import enum
import numpy as np
import tensorflow as tf
import GetNamesByClass
import csv
import config
import HebrewNlp
import CleanText

class Location(enum.IntEnum):
    Lable = 0 #סוג הדוח
    Root = 1 #מיקום הקובץ
    wordtoken =2 #המילים בקובץ
    bagofword = 3 #מבני הנתונים
    clientname =4 #שם הלקוח


class ModelTrainig:
    def __int__(self,):
        self.bagofwords = {}
        self.trainiset= []
        self.category_index
        self.detection_model
        self.allfirstname
        self.alllastname


    def LoadTheApi(self):
        self.Loadbegofworld()
        self.LoadModel()
        self.LoadNames()
        try:
            HebrewNlp.HebrewTokenizer(config.ERRORLABEL)
            CleanText.Translate2Word("english")
        except Exception as e:
             raise e





    def Loadbegofworld(self):
        if (not os.path.exists(os.path.join(config.WORKSPACE,config.SUFFLETRAINFILE))):
            (self.bagofwords, self.trainiset) = PrallelCode.begofword()
            random.shuffle(self.trainiset)
            file = open(os.path.join(config.WORKSPACE, config.SUFFLETRAINFILE), 'wb')
            pickle.dump(self.trainiset, file)
            file.close()
        else:
            file = open(os.path.join(config.WORKSPACE, config.BAGOFWORDSFILE), 'rb')
            self.bagofwords = pickle.load(file)
            file.close()
            file = open(os.path.join(config.WORKSPACE, config.SUFFLETRAINFILE), 'rb')
            self.trainiset = pickle.load(file)
            file.close()


    def buildthemodel(self):
        labels=[]
        files = []
        for item in self.trainiset:
             npitem = np.array(item[int(Location.bagofword)])
             shape = npitem.reshape(1,-1)
             files.append(item[int(Location.bagofword)])
             if item[int(Location.Lable)] == config.REPORTTYPE2:
                 labels.append(1)
             else:
                 labels.append(0)

        MachineLearning.buildmodel(labels,files)



    def LoadModel(self):
        try:
            self.model = tf.keras.models.load_model(config.MODELNANE)
        except Exception as e:
            self.model = self.buildthemodel()
        self.category_index, self.detection_model = GetNamesByClass.Load_Pic_Model()

    def predictAndClasterfy(self,when):
        data = PrallelCode.bagofwordonlytheard(when)
        data_after_subject = MachineLearning.predict(self.model, data)
        allthesetup = []
        for file in data_after_subject:
            if file[int(Location.Lable)] in config.REPORTTYPE1:
                root =file[int(Location.Root)]
                allname = root.split('\\')
                name = allname[(len(allname))-1]
                append_lst = self.NameWithCat(file, name, root)
                allthesetup.append(append_lst)
            else:
                print(data_after_subject)
                append_lst = file.copy()
                append_lst.append(HebrewNlp.GetName(file[int(Location.wordtoken)],self.allfirstname,self.alllastname))
                allthesetup.append(append_lst)
        return allthesetup

    def NameWithCat(self, file, name, root):
        append_lst = file.copy()
        append_lst.append(
            GetNamesByClass.Classtfy(
                self.category_index,
                self.detection_model,
                root, name,
                self.allfirstname,
                self.alllastname))
        return append_lst

    def LoadNames(self):
        allname = []
        with open(config.FIRSTNAME, encoding=config.ENCODING, newline='') as csvfile:
            reader = csv.reader(csvfile, delimiter='\t')
            for row in reader:
                data = ', '.join(row)
                allname.append(data)
        self.allfirstname = allname
        allname = []
        with open(config.LASTNAME, encoding=config.ENCODING, newline='') as csvfile:
            reader = csv.reader(csvfile, delimiter='\t')
            for row in reader:
                data = ', '.join(row)
                allname.append(data)
        self.alllastname = allname


def main():
    #warnings.filterwarnings("ignore")
    model = ModelTrainig()
    model.LoadTheApi()
    allfile = model.predictAndClasterfy(os.path.join("File\here"))
    print(allfile)
if __name__ == '__main__':
    main()
    #flaxs