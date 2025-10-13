import getpass

from sequences import Dictionary, DictionaryOperations
from sequences import Sequence, SequenceOperations
from lstm_formatter import XmlOperations
from lstm_formatter import LSTMFormatter

if __name__ == "__main__":
    KOTLIN_VOCAB_FILE = "./data/kotlin_vocab.json"
    process = "PrepareTrainDataFromASTXml"
    inp_words = 4
    units = 96
    sequence_operations = SequenceOperations()
    dictionary_operations = DictionaryOperations()
    xml_operations = XmlOperations()

    dictionary = dictionary_operations.load(
        filepath = KOTLIN_VOCAB_FILE,
        username = getpass.getuser(),
        process = process
        )

    model_filename = f"./data/lstm-kotlin-n{inp_words}_v{dictionary.size()}_u{units}.h1.keras"
    sequences = xml_operations.loadSequencesUseCase(
        directory="../../generated/kotlin/",
        filename="output_tree_Kotlin.xml",
        dictionary= dictionary
    )
    if sequences.is_err():
        print(f"Error loading sequences: {sequences.unwrap_err()}")
        exit(1)
    sequences = sequences.unwrap()
    sequences.author = getpass.getuser()
    sequences.process = process

    formatter = LSTMFormatter(inp_words=inp_words)
    if (not formatter.loadModel(model_filename)):
        print(f"Error loading model")
        exit(1)

    # formatter.trainModel(sequences)
    # formatter.model.save("./data/lstm-kotlin-n4.h1.keras")

