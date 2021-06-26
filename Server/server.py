from threading import Thread
import socket
import queue
import firebase_admin
from firebase_admin import credentials, storage
from firebase_admin import db
import select
import time
import json
from LoudModel import ModelTrainig
from LoudModel import Location
import os
import PrallelCode
import MachineLearning
import random
import pickle
import enum
import numpy as np
import tensorflow as tf
import GetNamesByClass
import csv
import HebrewNlp
import config
import warnings
import datetime
from pathlib import Path

host = ''
port = 5500

users_connections = {}
users_threads = {}
model = ModelTrainig()

is_ai_works = False


# LENGTH = 4
#

def dictionaryTojson(message_dictionary):
    json_message = json.dumps(message_dictionary)
    return json_message


def setFromFolder(connection, username, path):
    print("set from_folder")
    ref = db.reference('/users/' + username + '/last_from_folder')
    ref.set(path)
    message = "from_folder updated in the data-base"
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))

def setToFolder(connection, username, path):
    print("set to_folder")
    ref = db.reference('/users/' + username + '/last_to_folder')
    ref.set(path)
    message = "to_folder updated in the data-base"
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))

def startAIRoutine(connection, username, from_folder_path, to_folder_path):
    ai_thread = Thread(target=aiHandle, args=(connection, username, from_folder_path, to_folder_path))
    ai_thread.start()
    message = "AI started analyzing"
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))
    print(message)


def aiHandle(connection, username, from_folder_path, to_folder_path):
    print("AI started analyzing")
    try:
        warnings.filterwarnings("ignore")
        print(from_folder_path)
        allfile = model.predictAndClasterfy(os.path.join(from_folder_path))
        print("\n", len(allfile), "\n")
        files_dictionary = {}
        if not allfile:
            message = "Folder Empty"
            print(message)
            connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
            connection.send(message.encode('UTF-8'))
            print("AI sended message :|")
        else:
            for file in allfile:
                file_name = Path(file[Location.Root]).stem
                owner= file[Location.clientname]
                type=file[Location.Lable]
                path=file[Location.Root]
                bag_of_words=file[Location.bagofword]

                print("file: ",file_name)

                ref = db.reference(
                    '/users/' + username + '/from_folders_list/' + from_folder_path + '/removed_files_list/' + file_name)
                snapshot = ref.order_by_key().get()
                if snapshot == None:
                    if owner == "NULL" or type == "NULL":
                        print("for manual sorting", "\n")

                    else:
                        # ref = db.reference('/users/' + username + '/files_list/' + file_name + '/from_folder')
                        # ref.set(from_folder_path)
                        print(from_folder_path)
                        #
                        # ref = db.reference('/users/' + username + '/files_list/' + file_name + '/to_folder')
                        # ref.set(to_folder_path)
                        print(to_folder_path)

                        ref = db.reference(
                            '/users/' + username + '/to_folders_list/' + to_folder_path + '/redirection_list/' + owner)
                        snapshot = ref.order_by_key().get()
                        if snapshot != None:
                            owner = snapshot
                        else:
                            ref.set(owner)
                        print(owner)


                        # ref = db.reference('/users/' + username + '/files_list/' + file_name + '/type')
                        # ref.set(type)
                        print(type)

                        # ref = db.reference('/users/' + username + '/files_list/' + file_name + '/bag_of_words')
                        # ref.set(bag_of_words)
                        # print(bag_of_words)

                        # current_time = str(datetime.now().time())
                        # ref = db.reference('/users/' + username + '/files_list/' + file_name + '/time')
                        # ref.set(current_time)
                        # print(current_time)

                        # ref = db.reference('/users/' + username + '/files_list/' + file_name + '/way_of_sorting')
                        # ref.set("automatic AI sorting")
                        print("automatic AI sorting", "\n")

                    files_dictionary.update({file_name: {"owner": owner,
                                                         "type": type,
                                                         "path": path}
                                             })
                else:
                    print("is removed", "\n")



            message = dictionaryTojson({"AI_ended_analyzing": files_dictionary})
            print("AI ended analyzing")
            connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
            connection.send(message.encode('UTF-8'))
            print("AI sended message :D")
    except Exception as e:
        message = "An exception occurred"
        connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
        connection.send(message.encode('UTF-8'))
        print("AI sended message :,(")
        raise e

def updateMovedFile(connection, username, file_name, from_folder_path, to_folder_path, owner, type, way_of_sorting):
    print("file: ", file_name)

    ref = db.reference('/users/' + username + '/files_list/' + file_name + '/from_folder')
    ref.set(from_folder_path)
    print(from_folder_path)

    ref = db.reference('/users/' + username + '/files_list/' + file_name + '/to_folder')
    ref.set(to_folder_path)
    print(to_folder_path)

    if way_of_sorting=="automatic AI sorting":

        ref = db.reference(
            '/users/' + username + '/to_folders_list/' + to_folder_path + '/redirection_list/' + owner)
        snapshot = ref.order_by_key().get()
        if snapshot != None:
            owner = snapshot
        else:
            ref.set(owner)
        ref = db.reference('/users/' + username + '/files_list/' + file_name + '/owner')
        ref.set(owner)
        print(owner)

        ref = db.reference('/users/' + username + '/files_list/' + file_name + '/type')
        ref.set(type)
        print(type)

    current_time = str(datetime.now().time())
    ref = db.reference('/users/' + username + '/files_list/' + file_name + '/time')
    ref.set(current_time)
    print(current_time)

    ref = db.reference('/users/' + username + '/files_list/' + file_name + '/way_of_sorting')
    # ref.set("automatic AI sorting")
    ref.set(way_of_sorting)
    print(way_of_sorting, "\n")

    message = "file "+file_name+" is updated in the data-base"
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))
    print(message)

def removeFile(connection, username, from_folder_path, file_name ,location):
    print("remove file ", file_name)
    ref = db.reference('/users/' + username + '/from_folders_list/' + from_folder_path + '/removed_files_list/' + file_name)
    ref.set(location)
    message = "removed file were updated in the data-base"
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))

def unRemoveFile(connection, username, from_folder_path, file_name):
    print("unremove file")
    ref = db.reference(
        '/users/' + username + '/from_folders_list/' + from_folder_path + '/removed_files_list/' + file_name)
    ref.set(None)
    message = "removed files were updated in the data-base"
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))

def sendRemovedFilesList(connection, username, from_folder_path):
    print("send removed files")
    files_dictionary = {}
    removed_files = db.reference(
        '/users/' + username + '/from_folders_list/' + from_folder_path + '/removed_files_list').get()

    if removed_files==None:
        print("non")
    else:
        print(removed_files)
        for name in removed_files:
            location=removed_files[name]
            files_dictionary.update({name: location}) #name location
            print(name," ",location)
    print("----------------------")
    message = dictionaryTojson({"removed_files": files_dictionary})
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))
    print("removed files were sent")

def sendRedirectionList(connection, username, to_folder_path):
    print("send rediractions")
    files_dictionary = {}
    redirections = db.reference(
        '/users/' + username + '/to_folders_list/' + to_folder_path + '/redirection_list').get()

    if redirections == None:
        print("non")
    else:
        print(redirections)
        for from_owner in redirections:
            to_owner = redirections[from_owner]
            files_dictionary.update({from_owner: to_owner})
            print(from_owner, " ", to_owner)
    print("----------------------")
    message = dictionaryTojson({"redirections": files_dictionary})
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))
    print("redirections were sent")

def reDirect(connection, username, to_folder_path, from_owner, to_owner):
    print("redirect in data-base")
    if from_owner == to_folder_path:
        print("no need to redirect")
    else:
        ref = db.reference(
            '/users/' + username + '/to_folders_list/' + to_folder_path + '/redirection_list/' + from_owner)
        original_direction = ref.order_by_key().get()
        ref.set(to_owner)
        files = db.reference(
            '/users/' + username + '/files_list/').get()
        redirection_files={}
        for file in files:
            print(files[file]['to_folders_list']," ",to_folder_path," , ",files[file]['owner']," ",original_direction)
            if files[file]['to_folders_list'] == to_folder_path and files[file]['owner'] == original_direction:
                redirection_files.update({file: {"owner": original_direction,
                                                 "type": db.reference(
                                                     '/users/' + username + '/files_list/' + file + '/type').order_by_key().get(),
                                                 "path": db.reference(
                                                     '/users/' + username + '/files_list/' + file + '/path').order_by_key().get(),}
                                         })
        print (redirection_files)
        message = dictionaryTojson({"redirection_files": redirection_files})
        connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
        connection.send(message.encode('UTF-8'))
        print("redirection files were sent")

def login(connection, username, password):
    print("login")
    if user_in_data_base(username):
        print("login1")
        ref = db.reference('/users/' + username + '/password')
        snapshot = ref.order_by_key().get()
        ref = db.reference('/users/' + username + '/status')
        snapshot2 = ref.order_by_key().get()
        print(snapshot + " " + snapshot2)
        if snapshot == password:
            print("login2")
            if snapshot2 == 'not in a game':
                print("login3")
                image_url = db.reference('/users/' + username + '/user_image').order_by_key().get()
                ref = db.reference('/users/' + username + '/status')
                ref.set('in a game')
                message_dictionary = {
                    "login": image_url}
                message = json.dumps(message_dictionary)
                print(message)
            else:
                message = 'Error: Someone already logged in with this account'
        else:
            message = 'Error: Wrong password, Sorry'
    else:
        message = 'Error: The name was not found, try again'

    print("1-login")
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))
    print(message)

def logout(connection, username, password):
    print("signout")
    ref = db.reference('/users/' + username + '/status')
    ref.set('not in a game')
    message = "success"
    print("1-signout")
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))
    print(message)















            # ---------------------------------------------------------------------------------------


def user_in_data_base(username):
    ref = db.reference('/users/' + username)
    snapshot = ref.order_by_key().get()
    if snapshot == None:
        return False
    else:
        return True


def handle(address):
    connection = users_connections[address]
    print("new thread with " + str(address))
    message = "Connected with service"
    connection.send(len(message.encode('UTF-8')).to_bytes(2, byteorder='big'))
    connection.send(message.encode('UTF-8'))
    while True:
        try:
            # Broadcasting Messages
            length_of_message = int.from_bytes(connection.recv(2), byteorder='big')
            message = connection.recv(length_of_message).decode("UTF-8")
            print("message", message)
            dict = json.loads(message)

            if dict['type'] == 'connection_test':
                print ('connection_test')
                # testConnection(connection, dict["username"],dict["password"])

            elif dict['type'] == 'ai_request':
                startAIRoutine(connection, dict["username"], dict["from_folder_path"], dict["to_folder_path"])

            elif dict['type'] == 'move_file':
                updateMovedFile(connection, dict["username"], dict["file_name"], dict["from_folder_path"], dict["to_folder_path"], dict["owner"], dict["label"], dict["way_of_sorting"])

            elif dict['type'] == 'set_from_folder':
                setFromFolder(connection, dict["username"], dict["path"])
            elif dict['type'] == 'set_to_folder':
                 setToFolder(connection, dict["username"], dict["path"])


            elif dict['type'] == 'remove_file':
                 removeFile(connection, dict["username"], dict["from_folder_path"], dict["file_name"], dict["location"])
            elif dict['type'] == 'unremove_file':
                 unRemoveFile(connection, dict["username"], dict["from_folder_path"], dict["file_name"])

            elif dict['type'] == 'redirect':
                 reDirect(connection, dict["username"], dict["to_folder_path"], dict["from_owner"], dict["to_owner"])


            elif dict['type'] == 'removed_files_list_request':
                 sendRemovedFilesList(connection, dict["username"], dict["from_folder_path"])
            elif dict['type'] == 'redirection_list_request':
                 sendRedirectionList(connection, dict["username"], dict["to_folder_path"])

            elif dict['type']=='login':
                 login(connection,dict["username"],dict["password"])
            elif dict['type']=="logout":
                 logout(connection,dict["username"],dict["password"])
            else:
                print(dict['type'])
        except:
            # Removing And Closing users
            users_connections.pop(address)
            users_threads[address].join
            break


def connection_receive(service_socket):
    while True:
        #         print("users_connections ", users_connections)
        try:
            # Accept Connection
            connection, address = service_socket.accept()
            print("Connected with " + str(address))

            print(connection)
            service_socket.settimeout(10)
            users_connections[address] = connection
            users_threads[address] = Thread(target=handle, args=(address,))
            print('good!')
            users_threads[address].start()
        except Exception as e:
            if e.args != ('timed out',):
                print("9:", e.args)


def startService():
    try:
        service_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        service_socket.bind((host, port))
        service_socket.listen()
        connection_receive(service_socket)
    except Exception as e:
        print("1:", e.args)

    finally:
        print("end")
        service_socket.close()


def main():
    cred = credentials.Certificate('document_sorter_firebase_admin_sdk.json')
    firebase_admin.initialize_app(cred, {
        'databaseURL': 'https://document-sorter-default-rtdb.firebaseio.com',
        #     'storageBucket': 'gs://pdf-sorter.appspot.com'
    })
    print("connected to firebase")

    try:
        model.LoadTheApi()
        is_ai_works=True

    finally:
        print("is AI works: ",is_ai_works)

    startService()


if __name__ == '__main__':
    main()
