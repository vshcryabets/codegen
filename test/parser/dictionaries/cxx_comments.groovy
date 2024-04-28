import ce.parser.nnparser.RegexWord
import ce.parser.nnparser.Type

words.add(new RegexWord("//(.*)", 300, Type.COMMENTS))
words.add(new RegexWord("/\\*.*\\*/", 301, Type.COMMENTS))