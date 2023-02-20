package generators.obj.out

import ce.defs.Target
import generators.obj.input.NamespaceMap
import generators.obj.input.TreeRoot

class ProjectOutput(val namespaceMap : NamespaceMap, val target: Target) : OutNode("/", TreeRoot)