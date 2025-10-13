import os
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense, Embedding
from keras import optimizers
from tensorflow.keras.models import load_model
from sequences import Sequence
from sequences import Dictionary
import numpy as np
from tensorflow.keras.utils import to_categorical
from keras.callbacks import ModelCheckpoint

class LSTMFormatter:
    def __init__(self, inp_words: int = 4):
        self.inp_words = inp_words
        self.paddingVec = [0] * (inp_words - 1)
        self.filename = ""
        self.model = None
        self.rms = None

    def loadModel(self, filename: str) -> bool:
        self.filename = filename
        self.rms = optimizers.RMSprop(learning_rate=0.0005)
        if os.path.exists(filename):
            self.model = load_model(filename)
            self.model.compile(optimizer=self.rms, loss='sparse_categorical_crossentropy')
            return True
        return False


    def defineModel(self, units: int, dictionary: Dictionary, filename: str):
        self.filename = filename
        self.rms = optimizers.RMSprop(learning_rate=0.0005)
        if os.path.exists(filename):
            self.loadModel(filename)
            return
        self.model = Sequential()
        dictionary_size = dictionary.size() + 1 # +1 for padding token
        self.model.add(Embedding(dictionary_size,
                    output_dim=units,
                    input_length=self.inp_words,
                    mask_zero=True))
        self.model.add(LSTM(units)) 
        self.model.add(Dense(dictionary_size, activation='softmax'))
        self.model.build(input_shape=(None, self.inp_words))
        self.model.summary()
        self.model.compile(optimizer=self.rms, loss='sparse_categorical_crossentropy')        

    def trainModel(self, sequence: Sequence):
        vectors = [it['sequence'] for it in sequence.entries.values()]
        vectors = [self.paddingVec + sb for sb in vectors]
        X = []
        Y = []
        for sb in vectors:
            for i in range(len(sb) - self.inp_words):
                X.append(sb[i:i + self.inp_words])
                Y.append(sb[i + self.inp_words])
        X = np.array(X)
        Y = np.array(Y)
        print(f"X shape: {X.shape}, Y shape: {Y.shape}")

        checkpoint = ModelCheckpoint(self.filename, monitor='val_loss', verbose=1, save_best_only=True, mode='min')
        history = self.model.fit(x = X,
                y = Y,
                batch_size=16,
                validation_split = 0.2,
                callbacks=[checkpoint],
                epochs=4096)

