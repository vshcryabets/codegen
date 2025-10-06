from lxml import etree
from sequences import Sequence
from sequences import Dictionary
import copy
from datetime import datetime

class XmlOperations:
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

    def __init__(self):
        pass

    def process_childs(self, elem, vocab, id):
        for child in elem:
            if child.tag in XmlOperations.SKIP_TAGS:
                ## Skip this tag and its children
                continue
            tagName = child.tag
            tagCanBeClosed = tagName not in XmlOperations.OPEN_ONLY_TAGS
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

            id = self.process_childs(child, vocab, id)
        
        return id
    
    def refreshDictionaryUseCase(self, directory, filename, dictionary: Dictionary) -> Dictionary:
        tree = etree.parse(directory + "/" + filename)
        root = tree.getroot()
        newDictionary = copy.deepcopy(dictionary)
        id = newDictionary.nextId()
        for child in root:
            print(f"Child tag: {child.tag}, attributes: {child.attrib}")
            id = self.process_childs(child, newDictionary.entries, id)
            
        print(f"Vocabulary size: {len(newDictionary.entries)}")
        newDictionary.updateDate = datetime.now().isoformat()
        return newDictionary

    def prepareTrainingSequenceUseCase(self, directory, filename, dictionary: Dictionary, sequences: Sequence):
        tree = etree.parse(directory + "/" + filename)
        root = tree.getroot()
        
        for child in root:
            blockName = child.attrib['name']
            # print(f"Child tag: {child.tag}, attributes: {child.attrib['name']}")
            sequence = []
            self.process_childs_for_sequence(child, dictionary, sequence)
            sequences.entries[blockName] = {"sequence": sequence}

        print(f"Training sequence length: {len(sequences.entries)}")
        return sequences

    def process_childs_for_sequence(self, elem, dictionary: Dictionary, sequence: list):
        for child in elem:
            if child.tag in XmlOperations.SKIP_TAGS:
                ## Skip this tag and its children
                continue
            tagName = child.tag
            tagCanBeClosed = tagName not in XmlOperations.OPEN_ONLY_TAGS
            openTag = f"{tagName}_open" if tagCanBeClosed else f"{tagName}"
            if tagName == "Keyword":
                openTag = f"{tagName}_{child.attrib['name']}"

            if openTag in dictionary.entries:
                sequence.append(dictionary.entries[openTag]["id"])

            if tagCanBeClosed:
                closeTag = f"{tagName}_close"
                if closeTag in dictionary.entries:
                    # Process children first (depth-first)
                    self.process_childs_for_sequence(child, dictionary, sequence)
                    sequence.append(dictionary.entries[closeTag]["id"])
                else:
                    raise Exception(f"'{closeTag}' not found in vocabulary")    
        return sequence