# from tensorflow.keras.models import Sequential
# from tensorflow.keras.layers import Embedding, LSTM, RepeatVector, Dense
# from tensorflow.keras.utils import plot_model
# from keras import optimizers
# import numpy as np
# from tensorflow.keras.utils import to_categorical
# from keras.models import load_model
# from keras.callbacks import ModelCheckpoint
from lxml import etree
import json
import getpass
from datetime import datetime

from sequences import Dictionary, DictionaryOperations
from sequences import Sequence, SequenceOperations

OPEN_ONLY_TAGS = [
    "WorkingDirectory",
    "PackageDirectory",
    "VariableName",
    "CommentLeaf",
    "AstTypeLeaf",
    "ImportLeaf",
    "Space",
    "NlSeparator",
    "Indent",
    "Keyword"
    ]
SKIP_TAGS = [
    "FileMetaInformation"
]


def process_childs(elem,
                   vocab,
                   id):
    for child in elem:
        if child.tag in SKIP_TAGS:
            ## Skip this tag and its children
            continue
        tagName = child.tag
        tagCanBeClosed = tagName not in OPEN_ONLY_TAGS
        openTag = f"{tagName}_open" if tagCanBeClosed else f"{tagName}"
        if tagName == "Keyword":
            openTag = f"{tagName}_{child.attrib['name']}"

        if openTag not in vocab:
            vocab[openTag] = {"id": id, "priority": 0}
            id += 1

        if tagCanBeClosed:
            closeTag = f"{tagName}_close"
            if closeTag not in vocab:
                vocab[closeTag] = {"id": id, "priority": 0}
                id += 1

        id = process_childs(child, vocab, id)
    
    return id

def read_and_update_vocab(directory, filename, vocab):
    tree = etree.parse(directory + "/" + filename)
    root = tree.getroot()
    
    if vocab:
        id = max(entry["id"] for entry in vocab.values()) + 1
    else:
        id = 1

    for child in root:
        print(f"Child tag: {child.tag}, attributes: {child.attrib}")
        id = process_childs(child, vocab, id)
        
    print(f"Vocabulary size: {len(vocab)}")
    return vocab

def read_and_split_out_tree(directory, filename):
    tree = etree.parse(directory + "/" + filename)
    root = tree.getroot()
    
    for child in root:
        output_filename = f"{directory}/{child.attrib['name']}.xml"
        # print(output_filename)
        with open(output_filename, "wb") as f:
            f.write(etree.tostring(child, pretty_print=True, xml_declaration=True, encoding="UTF-8"))    

def process_childs_for_sequence(elem, vocab, sequence):
    for child in elem:
        if child.tag in SKIP_TAGS:
            ## Skip this tag and its children
            continue
        tagName = child.tag
        tagCanBeClosed = tagName not in OPEN_ONLY_TAGS
        openTag = f"{tagName}_open" if tagCanBeClosed else f"{tagName}"
        if tagName == "Keyword":
            openTag = f"{tagName}_{child.attrib['name']}"

        if openTag in vocab:
            sequence.append(vocab[openTag]["id"])

        if tagCanBeClosed:
            closeTag = f"{tagName}_close"
            if closeTag in vocab:
                # Process children first (depth-first)
                process_childs_for_sequence(child, vocab, sequence)
                sequence.append(vocab[closeTag]["id"])
            else:
                raise Exception(f"'{closeTag}' not found in vocabulary")    
    return sequence

def read_and_prepare_training_sequence(directory, filename, vocab, sequences: Sequence):
    tree = etree.parse(directory + "/" + filename)
    root = tree.getroot()
    
    for child in root:
        blockName = child.attrib['name']
        # print(f"Child tag: {child.tag}, attributes: {child.attrib['name']}")
        sequence = []
        process_childs_for_sequence(child, vocab, sequence)
        sequences.entries[blockName] = {"sequence": sequence}

    print(f"Training sequence length: {len(sequences.entries)}")
    return sequences


if __name__ == "__main__":
    KOTLIN_VOCAB_FILE = "./data/kotlin_vocab.json"
    sequence_file = "./data/kotlin_sequence.json"
    process = "PrepareTrainDataFromASTXml"
    sequence_operations = SequenceOperations()
    dictionary_operations = DictionaryOperations()

    dictionary = dictionary_operations.load(
        filepath = KOTLIN_VOCAB_FILE,
        username = getpass.getuser(),
        process = process
        )

    read_and_update_vocab(
        directory="../../generated/kotlin/",
        filename="output_tree_formatted_Kotlin.xml",
        vocab = dictionary.entries
    )
    dictionary.updateDate = datetime.now().isoformat()
    dictionary_operations.store(dictionary, KOTLIN_VOCAB_FILE)

    sequences = Sequence(
        username = getpass.getuser(), 
        process = process
    )

    read_and_prepare_training_sequence(
        directory="../../generated/kotlin/",
        filename="output_tree_formatted_Kotlin.xml",
        vocab = dictionary.entries,
        sequences = sequences
    )
    sequence_operations.store(sequences, sequence_file)
