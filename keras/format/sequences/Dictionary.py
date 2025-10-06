from datetime import datetime
import json
import os

class Dictionary:
    def __init__(self, username: str, process: str):
        self.entries = {}
        self.name = "Kotlin AST Dictionary"
        self.type = "LSTMVocabulary"
        self.author = username
        self.version = "1.0"
        self.process = process
        self.updateDate = datetime.now().isoformat()

    def nextId(self):
        if not self.entries:
            return 1
        return max(entry["id"] for entry in self.entries.values()) + 1

class DictionaryOperations:
    def __init__(self):
        self.data = {}
    
    def load(self, filepath: str, username: str, process: str) -> Dictionary:
        if not os.path.exists(filepath):
            return Dictionary(username=username, process=process)
        with open(filepath, 'r') as f:
            self.data = json.load(f)
        dictionary = Dictionary(
            username=self.data.get("author", "unknown"),
            process=self.data.get("process", "unknown")
        )
        dictionary.entries = self.data.get("dictionary", {})
        dictionary.name = self.data.get("name", "unknown")
        dictionary.type = self.data.get("type", "unknown")
        dictionary.version = self.data.get("version", "unknown")
        dictionary.updateDate = self.data.get("updateDate", datetime.now().isoformat())
        return dictionary
    
    def store(self, dictionary: Dictionary, filepath: str):
        self.data = {
            "name": dictionary.name,
            "type": dictionary.type,
            "author": dictionary.author,
            "version": dictionary.version,
            "process": dictionary.process,
            "updateDate": dictionary.updateDate,
            "dictionary": dictionary.entries
        }
        with open(filepath, 'w') as f:
            json.dump(self.data, f, indent=4)