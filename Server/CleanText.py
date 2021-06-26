import re
from num2words import num2words
#from googletrans import Translator
from google_trans_new import google_translator
import config


# pip3 install git+https://github.com/alainrouillon/py-googletrans@feature/enhance-use-of-direct-api


def CleanTheText(Text):
    Text =" ".join(Text.split())
    Text = Translate2Word(Text)
    Text = CleanRound1(Text)
    return (Text)


def Translate2Word(text):
 #   translator = Translator(['translate.googleapis.com','translate.google.com','translate.google.co.kr'])
    translator = google_translator()
    wordsarr =text.split(' ')
    for i, element in enumerate(wordsarr):
        if(element.isnumeric()):
            text2 = num2words(int(element), lang='en')
            #text = translator.translate(text2, dest='he').text
            text = translator.translate(text2,lang_tgt=config.SYSTEMLANGUAGE)
            wordsarr[i] = text
    text = " ".join(wordsarr)
    return text


def CleanRound1(Text):
    Text = re.sub('[…]', '', Text)
    Text = re.sub('[%s]' % re.escape(r'!#$%&*₪+\/,©-():;<=>[]^_-־`?{|}~."\''), '', Text)
    Text = ' '.join([w for w in Text.split() if len(w)>1] )
    Text = re.sub('[ץףםןך^]', ' ', Text)
    Text = re.sub('   ', ' ', Text)
    Text = re.sub('  ', ' ', Text)
    Text = re.sub('\d+', '', Text)
    return Text



