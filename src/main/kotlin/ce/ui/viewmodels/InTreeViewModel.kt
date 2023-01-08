package ce.ui.viewmodels

import generators.obj.input.Leaf
import kotlinx.coroutines.flow.MutableStateFlow

class InTreeViewModel(
    private val inTree: Leaf
) {
    val viewState = MutableStateFlow(InTreeViewState())
}