package generators.obj.out

import generators.obj.input.NamespaceMap
import generators.obj.input.TreeRoot

class ProjectOutput(val namespaceMap : NamespaceMap) : OutNode("ROOT", TreeRoot)