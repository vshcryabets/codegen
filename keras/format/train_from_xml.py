import getpass


from sequences import Dictionary, DictionaryOperations
from sequences import Sequence, SequenceOperations
from lstm_formatter import XmlOperations

def read_and_split_out_tree(directory, filename):
    tree = etree.parse(directory + "/" + filename)
    root = tree.getroot()
    
    for child in root:
        output_filename = f"{directory}/{child.attrib['name']}.xml"
        # print(output_filename)
        with open(output_filename, "wb") as f:
            f.write(etree.tostring(child, pretty_print=True, xml_declaration=True, encoding="UTF-8"))    


if __name__ == "__main__":
    KOTLIN_VOCAB_FILE = "./data/kotlin_vocab.json"
    sequence_file = "./data/kotlin_sequence.json"
    process = "PrepareTrainDataFromASTXml"
    sequence_operations = SequenceOperations()
    dictionary_operations = DictionaryOperations()
    xml_operations = XmlOperations()

    dictionary = dictionary_operations.load(
        filepath = KOTLIN_VOCAB_FILE,
        username = getpass.getuser(),
        process = process
        )

    dictionary = xml_operations.refreshDictionaryUseCase(
        directory="../../generated/kotlin/",
        filename="output_tree_formatted_Kotlin.xml",
        dictionary = dictionary
    )
    dictionary_operations.store(dictionary, KOTLIN_VOCAB_FILE)

    sequences = Sequence(
        username = getpass.getuser(), 
        process = process
    )

    xml_operations.prepareTrainingSequenceUseCase(
        directory="../../generated/kotlin/",
        filename="output_tree_formatted_Kotlin.xml",
        dictionary= dictionary,
        sequences = sequences
    )
    sequence_operations.store(sequences, sequence_file)
