from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Embedding, LSTM, RepeatVector, Dense
from tensorflow.keras.utils import plot_model
from keras import optimizers
import numpy as np
from tensorflow.keras.utils import to_categorical
from keras.models import load_model
from keras.callbacks import ModelCheckpoint

def read_vectors(filename):
    with open(filename, 'r') as file:
        vectors = [list(map(int, line.strip().split(','))) for line in file]
    return vectors

def define_model(vocab,in_timesteps,units):
    model = Sequential()
    model.add(Embedding(vocab, 
                output_dim = units,
                input_length=in_timesteps, 
                mask_zero=True))
    model.add(LSTM(units)) 
    model.add(Dense(vocab, activation='softmax'))
    model.build(input_shape=(None, in_timesteps))
    model.summary()
    return model

NEW_MODEL = True
TRAIN_MODEL = True
filename = 'lstm-formatting-model.h1.keras'
inp_words = 4
paddingVec = [0] * (inp_words - 1)
# vectors = read_vectors("./outs1_t1.csv")
vectors = read_vectors("./outs1_add_open_bracket.csv")
vocab = max(max(sublist) for sublist in vectors) + 1
max_length_out = max(len(sublist) for sublist in vectors)
vectors = [paddingVec + sb for sb in vectors]
#print(vectors[0])
#print(vectors[1])
print(f"vocab={vocab} max_length_out={max_length_out}")
X = []
Y = []
for sb in vectors:
    for i in range(len(sb) - inp_words):
        X.append(sb[i:i + inp_words ])
        Y.append(sb[i + inp_words])
X = np.array(X)        
Y= np.array(Y)

units = 64
in_timesteps = inp_words
out_timesteps = max_length_out

if NEW_MODEL:
    # model compilation
    model = define_model(
        vocab=vocab,
        in_timesteps=in_timesteps,
        units=units
    )
    rms = optimizers.RMSprop(learning_rate=0.05) ##lr=0.001
    # model.compile(loss='categorical_crossentropy', metrics=['accuracy'], optimizer='adam')
    # Y= to_categorical(Y, num_classes=vocab)

    model.compile(optimizer=rms, loss='sparse_categorical_crossentropy')
else:
    model = load_model(filename)

if TRAIN_MODEL:
    checkpoint = ModelCheckpoint(filename, monitor='val_loss', verbose=1, save_best_only=True, mode='min')

    # train model
    history = model.fit(x = X, 
                    y = Y, 
                    batch_size=16,
                    validation_split = 0.2,
                    callbacks=[checkpoint],
                    epochs=128)

x = [0,0,1,3]
inp = np.expand_dims(x, axis=0)
print(x)
print(inp)
pred = model.predict(inp)
print(f"pred={pred}")