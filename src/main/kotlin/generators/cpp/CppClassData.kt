package generators.cpp

import generators.obj.out.ClassData
import javax.xml.stream.events.Namespace

class CppClassData(namespace: String) : CppHeaderData(namespace) {
    val headerData = CppHeaderData(namespace)
}