import getpass

from sequences import Dictionary, DictionaryOperations
from sequences import Sequence, SequenceOperations
from lstm_formatter import XmlOperations
from lstm_formatter import LSTMFormatter

if __name__ == "__main__":
    KOTLIN_VOCAB_FILE = "./data/kotlin_vocab.json"
    sequence_file = "./data/kotlin_sequence.json"
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
    model_filename = f"./data/lstm-kotlin-n{inp_words}_v{dictionary.size()}_u{units}.h1.keras"

    xml_operations.prepareTrainingSequenceUseCase(
        directory="../../generated/kotlin/",
        filename="output_tree_formatted_Kotlin.xml",
        dictionary= dictionary,
        sequences = sequences
    )
    sequence_operations.store(sequences, sequence_file)

    formatter = LSTMFormatter(inp_words=inp_words)
    formatter.defineModel(
        units=units,
        dictionary=dictionary,
        filename=model_filename
    )
    formatter.trainModel(sequences)
    # formatter.model.save("./data/lstm-kotlin-n4.h1.keras")

