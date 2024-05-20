import ce.parser.nnparser.RegexWord
import ce.parser.nnparser.Type

words.add(new RegexWord("0x[\\dABCDEFabcdef]+", 5000, Type.DIGIT))
words.add(new RegexWord("\\d+\\.*\\d*f*", 5001, Type.DIGIT))
