from datetime import datetime
import json

class Sequence:
    def __init__(self, username: str, process: str):
        self.entries = {}
        self.name = "Kotlin AST Training Sequences"
        self.type = "LSTMTrainingSequences"
        self.author = username
        self.version = "1.0"
        self.process = process
        self.updateDate = datetime.now().isoformat()

class SequenceOperations:
    def __init__(self):
        pass

    def store(self, data: Sequence, filepath: str) -> Sequence:
        with open(filepath, 'w') as f:
            json.dump(data.__dict__, f, indent=2)
        return data