import ce.parser.nnparser.RegexWord
import ce.parser.nnparser.Type

words.add(new RegexWord("//(.*)", 3000, Type.COMMENTS))
words.add(new RegexWord("/\\*.*\\*/", 3001, Type.COMMENTS))