from keras import optimizers
import numpy as np
from keras.models import load_model
import json
import queue

def read_project_data(filename):
    with open(filename, 'r') as file:
        data = json.load(file)
    return data

def findWord(dictionary, index):
    for word, i in dictionary.items():
        if i['openId'] == index or i['closeId'] == index:
            return i
    return None

modelFilename = 'lstm-formatting-model.h1.keras'
projectFilename = 'sources.json'
model = load_model(modelFilename)
project = read_project_data(projectFilename)
if project['name'] != "RNNSources":
    print(f"Error: Invalid training data file - {training_data['name']}")
    exit(1)

dictionary = project['dictionary']
samples = project['samples']
x = [0,0] + samples[0]
input = x[:4]
x = x[4:]
output = input.copy()

while len(x) > 0:
    # print(f"input1={input}")
    pred = model.predict(np.array([input]))
    pred = np.argmax(pred)
    takeSrc = False

    if (pred == 0):
        takeSrc = True
    elif (pred == x[0]):
        takeSrc = True
    else:
        wordPredicated = findWord(dictionary, pred)
        wordSrc = findWord(dictionary, x[0])
        if wordPredicated == None or wordSrc == None:
            print(f"Error: predicated word {wordPredicated} or source word {wordSrc} not found")
            break
        
        # print(f"wordPredicated={wordPredicated}")
        # print(f"wordSrc={wordSrc}")
        if wordSrc['priority'] >= wordPredicated['priority']:
            takeSrc = True

    if takeSrc:
        next = x[0]
        x.pop(0)
        output.append(next)
        input = input[1:] + [next]
    else:
        output.append(int(pred))
        input = input[1:] + [int(pred)]
output=output[2:]
print(f"Output={output}")
