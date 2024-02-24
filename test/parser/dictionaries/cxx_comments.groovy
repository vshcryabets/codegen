import ce.parser.nnparser.Comment
import ce.parser.nnparser.Type

words.add(new Comment("//", true, "", 300, Type.COMMENTS))
words.add(new Comment("/*", false, "*/", 301, Type.COMMENTS))