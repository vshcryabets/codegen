package ce.formatters

import javax.inject.Inject

class CodeFormatterJavaUseCaseImpl @Inject constructor(codeStyleRepo: CodeStyleRepo) :
    CodeFormatterUseCaseImpl(codeStyleRepo) {

}