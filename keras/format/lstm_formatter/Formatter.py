from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense, Embedding
from keras import optimizers
from tensorflow.keras.models import load_model

class LSTMFormatter:
    def __init__(self):
        pass

    def define_model(self, vocab, in_timesteps, units):
        self.model = Sequential()
        self.model.add(Embedding(vocab,
                    output_dim=units,
                    input_length=in_timesteps,
                    mask_zero=True))
        self.model.add(LSTM(units)) 
        self.model.add(Dense(vocab, activation='softmax'))
        self.model.build(input_shape=(None, in_timesteps))
        self.model.summary()
        self.rms = optimizers.RMSprop(learning_rate=0.05)
        self.model.compile(optimizer=self.rms, loss='sparse_categorical_crossentropy')

    def load_model(self, filename):        
        self.model = load_model(filename)