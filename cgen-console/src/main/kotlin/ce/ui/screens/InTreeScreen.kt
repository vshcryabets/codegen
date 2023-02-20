package ce.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ce.ui.FontTypes
import ce.ui.viewmodels.InTreeViewModel
import generators.obj.input.Leaf
import org.jetbrains.skia.TextLine

fun showInTree(tree: Leaf) = application {
    val vm = remember{ InTreeViewModel(tree) }
    val viewState = vm.viewState

    Window(
        onCloseRequest = ::exitApplication,
        title = "Input tree visualization",
        state = rememberWindowState(width = 1080.dp, height = 640.dp)
    ) {
        MaterialTheme {
            Column(
                Modifier.fillMaxSize()
                    .padding(horizontal = 34.dp, vertical = 22.dp),
                Arrangement.spacedBy(5.dp)
            ) {
                Text(text = "Test", fontSize = FontTypes.font17)

//                val textPaint = Paint().asFrameworkPaint().apply {
//                    isAntiAlias = true
////                    textSize = 24.sp.toPx()
//                    color = Color.Blue
////                    typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
//                }

                Canvas(modifier = Modifier.size(100.dp), onDraw = {
                    drawCircle(color = Color.Red, radius = 22.dp.value)
//                    drawIntoCanvas {
//                        it.nativeCanvas.drawTextLine(
//                            TextLine.Companion.make("My Jetpack Compose Text", 16.0f),
//                            0f,            // x-coordinates of the origin (top left)
//                            120.dp.toPx(), // y-coordinates of the origin (top left)
//                            textPaint
//                        )
//                    }
                })
            }
        }
    }
}