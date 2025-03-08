from keras import optimizers
import numpy as np
from keras.models import load_model
import json
import queue
import sys
import os
import tensorflow as tf

def read_project_data(filename):
    with open(filename, 'r') as file:
        data = json.load(file)
    return data

def findWord(dictionary, index):
    for word, i in dictionary.items():
        if i['openId'] == index or i['closeId'] == index:
            return i
    return None

def processSample(sample, dictionary, idx, total):
    print(f"\rProcessing sample {idx} of {total}")
    sys.stdout.flush()
    x = [0,0] + sample
    input = x[:4]
    x = x[4:]
    output = input.copy()

    while len(x) > 0:
        pred = model.predict(np.array([input]))
        pred = np.argmax(pred)
        takeSrc = False

        if (pred == 0):
            takeSrc = True
        elif (pred == x[0]):
            takeSrc = True
        else:
            wordPredicated = dictionary[pred]
            wordSrc = dictionary[x[0]]
            if wordPredicated == None or wordSrc == None:
                print(f"Error: predicated word {wordPredicated} or source word {wordSrc} not found")
                break

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
    return output[2:]

modelFilename = 'lstm-formatting-model.h1.keras'
projectFilename = 'sources.json'
model = load_model(modelFilename)
project = read_project_data(projectFilename)
if project['type'] != "RNNSources":
    print(f"Error: Invalid data file type - {project['type']}")
    exit(1)

dictionary = project['dictionary']['map']
id_to_word_map = {}
for word, details in dictionary.items():
    id_to_word_map[details['openId']] = details
    closeId = details['closeId']
    if (closeId > 0):
        id_to_word_map[closeId] = details

samples = project['samples']
total = len(samples)
mapped_samples = [processSample(sample, id_to_word_map, index, total) for index, sample in enumerate(samples)]

project['samples'] = mapped_samples
project['type'] = "RNNFormattedSources"
output_filename = 'formated_sources.json'
with open(output_filename, 'w') as outfile:
    json.dump(project, outfile, indent=4)

print(f"Processed project data saved to {output_filename}")
