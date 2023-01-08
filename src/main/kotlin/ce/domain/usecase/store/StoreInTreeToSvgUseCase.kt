package ce.domain.usecase.store

import generators.obj.input.DataField
import generators.obj.input.Leaf
import generators.obj.input.Namespace
import generators.obj.input.Node
import org.abego.treelayout.Configuration
import org.abego.treelayout.NodeExtentProvider
import org.abego.treelayout.TreeForTreeLayout
import org.abego.treelayout.TreeLayout
import org.abego.treelayout.util.DefaultConfiguration
import org.abego.treelayout.util.DefaultTreeForTreeLayout
import org.jfree.svg.SVGGraphics2D
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.io.FileOutputStream

interface StoreInTreeToSvgUseCase {
    operator fun invoke(outputSvgFile : String, tree: Leaf)
}

// Using https://treelayout.sourceforge.net/
class StoreInTreeToSvgUseCaseImpl : StoreInTreeToSvgUseCase {

    private val LIGHTBLUE = Color(154,192,205)
    private val FONT_SANS_REGULAR = Font("SansSerif", Font.PLAIN, 12)
    private val FONT_SANS_BOLD = Font("SansSerif", Font.BOLD, 12)

    override operator fun invoke(outputSvgFile : String, root: Leaf)  {
        val tree: TreeForTreeLayout<Leaf> = DefaultTreeForTreeLayout(root).apply {
            // add subnodes
            addSubnodes(this, null, root)
        }

        val minWidth = 40.0
        val gapBetweenLevels = 50.0
        val gapBetweenNodes = 10.0
        val configuration: DefaultConfiguration<Leaf> = DefaultConfiguration<Leaf>(gapBetweenLevels, gapBetweenNodes,
            Configuration.Location.Left)
        val affinetransform = AffineTransform()
        val fontRenderContext = FontRenderContext(affinetransform, true, true)

        val nodeExtentProvider = object : NodeExtentProvider<Leaf> {
            override fun getWidth(treeNode: Leaf): Double {
                val line1 = maxOf(minWidth, FONT_SANS_BOLD.getStringBounds(treeNode.toDisplayString(), fontRenderContext).width + 15.0)
                val line2 = maxOf(minWidth,
                    FONT_SANS_REGULAR.getStringBounds(treeNode.javaClass.simpleName, fontRenderContext).width + 15.0)
                return maxOf(line1, line2)
            }

            override fun getHeight(treeNode: Leaf): Double {
                val height = FONT_SANS_REGULAR.size
                return (height * 2 + 15).toDouble()
            }
        }

        val treeLayout: TreeLayout<Leaf> = TreeLayout(tree, nodeExtentProvider, configuration)
        val size: Dimension = treeLayout.bounds.bounds.size
        val graphics = SVGGraphics2D(size.getWidth(), size.getHeight())
        graphics.font = FONT_SANS_REGULAR

        val boxStroke = BasicStroke(1.5f)

        generateEdges(graphics, treeLayout, root, boxStroke)
        for (textInBox in treeLayout.nodeBounds.keys) {
            generateBox(graphics, treeLayout, textInBox, boxStroke)
        }

        FileOutputStream(outputSvgFile).use {
            it.write(graphics.svgElement.toByteArray())
        }
    }

    private fun addSubnodes(tree: DefaultTreeForTreeLayout<Leaf>, parent: Leaf?, leaf: Leaf) {
        if (parent != null) {
            tree.addChild(parent, leaf)
        }
        if (leaf is Node) {
            leaf.subs.forEach {
                addSubnodes(tree, leaf, it)
            }
        }
    }

    private fun generateBox(g2: SVGGraphics2D, treeLayout: TreeLayout<Leaf>, leaf: Leaf, boxStroke: BasicStroke) {
        // draw the box in the background
        val box = treeLayout.nodeBounds[leaf]!!
        g2.setPaint(LIGHTBLUE)
        g2.fillRoundRect(box.x.toInt(), box.y.toInt(),
            box.width.toInt(), box.height.toInt(),
            10, 10)

        g2.setPaint(Color.BLACK)
        g2.stroke = boxStroke
        g2.drawRoundRect(box.x.toInt(), box.y.toInt(),
            box.width.toInt(), box.height.toInt(),
            10, 10)

        g2.font = FONT_SANS_REGULAR
        g2.drawString(leaf.javaClass.simpleName, box.x.toInt() + 7, box.y.toInt() + g2.fontMetrics.height + 1)
        g2.font = FONT_SANS_BOLD
        g2.drawString(leaf.toDisplayString(), box.x.toInt() + 7, box.y.toInt() + g2.fontMetrics.height * 2 + 1)
    }

    private fun generateEdges(g2: SVGGraphics2D, treeLayout: TreeLayout<Leaf>, parent: Leaf, boxStroke: BasicStroke) {
        g2.setPaint(Color.BLACK)
        if (!treeLayout.tree.isLeaf(parent)) {
            val b1: Rectangle2D.Double = treeLayout.nodeBounds[parent]!!
            val x1 = b1.centerX.toInt()
            val y1 = b1.centerY.toInt()
            for (child in treeLayout.tree.getChildren(parent)) {
                val b2: Rectangle2D.Double = treeLayout.nodeBounds[child]!!
                g2.drawLine(x1, y1, b2.getCenterX().toInt(), b2.getCenterY().toInt())
                generateEdges(g2, treeLayout, child, boxStroke)
            }
        }
    }

    fun Leaf.toDisplayString() : String =
        when  {
            this is Namespace && name.isEmpty() -> "ROOT"
            this is DataField && this.value.isDefined() -> "${this.name} = ${this.value.value}"
            else -> this.name
        }

}