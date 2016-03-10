package com.mygdx.game.algorithm

import com.badlogic.gdx.graphics.Color
import com.mygdx.game.components.NodeComponent
import com.mygdx.game.systems.GridConfig

enum class NodeType {
    TOP_BORDER, BOTTOM_BORDER, LEFT_BORDER, RIGHT_BORDER, UPPER_LEFT_CORNER, UPPER_RIGHT_CORNER,
    LOWER_RIGHT_CORNER, LOWER_LEFT_CORNER, INTERNAL
}

data class Point(val x: Int, val y: Int)

data class GridEdgeGenerator(val grid: List<List<NodeComponent>>, val config: GridConfig) {

    val X_CEILING: Int = (config.gridWidth / config.nodeWidth).toInt() - 1
    val Y_CEILING: Int = (config.gridHeight / config.nodeHeight).toInt() - 1

    fun getEdges(): MutableList<Edge> {
        val nodes: List<NodeComponent> = grid.flatten()

        val borderTypes: List<List<NodeType>> = grid.map { row ->
            row.map { node ->
                borderType(node)
            }
        }

        val edges: MutableList<Edge> = grid.map { row ->
            row.map { node ->
                createEdge(node, node.posit, borderType(node))
            }.flatten()
        }.flatten().toMutableList()

        return edges
    }

    fun borderType(node: NodeComponent): NodeType {
        val location = node.posit
        when (location) {
            Point(0, 0) -> return NodeType.LOWER_LEFT_CORNER

            Point(X_CEILING, Y_CEILING) -> return NodeType.UPPER_RIGHT_CORNER

            Point(0, Y_CEILING) -> return NodeType.UPPER_LEFT_CORNER

            Point(X_CEILING, 0) -> return NodeType.LOWER_RIGHT_CORNER
        }

        val xPosit = location.x
        val yPosit = location.y

        when (xPosit) {
            0 -> return NodeType.LEFT_BORDER
            X_CEILING -> return NodeType.RIGHT_BORDER

        }

        when (yPosit) {
            0 -> return NodeType.BOTTOM_BORDER
            Y_CEILING -> return NodeType.TOP_BORDER
        }

        return NodeType.INTERNAL
    }

    fun createEdge(node: NodeComponent, nodePoint: Point, borderType: NodeType): List<Edge> {
        val nodeComp: List<NodeComponent> = when (borderType) {
            NodeType.LOWER_LEFT_CORNER -> listOf(grid[nodePoint.y][nodePoint.x + 1].self, grid[nodePoint.y + 1].first().self)
            NodeType.UPPER_LEFT_CORNER -> listOf(grid[nodePoint.y][nodePoint.x + 1].self, grid[nodePoint.y - 1].first().self)
            NodeType.LOWER_RIGHT_CORNER -> listOf(grid[nodePoint.y][nodePoint.x - 1].self, grid[nodePoint.y + 1].last().self)
            NodeType.UPPER_RIGHT_CORNER -> listOf(grid[nodePoint.y][nodePoint.x - 1].self, grid[nodePoint.y - 1].last().self)

            NodeType.LEFT_BORDER -> listOf(grid[nodePoint.y][nodePoint.x + 1].self, grid[nodePoint.y + 1].first().self, grid[nodePoint.y - 1].first().self)
            NodeType.RIGHT_BORDER -> listOf(grid[nodePoint.y][nodePoint.x - 1].self, grid[nodePoint.y + 1].last().self, grid[nodePoint.y - 1].last().self)
            NodeType.TOP_BORDER -> listOf(grid[nodePoint.y][nodePoint.x - 1].self, grid[nodePoint.y][nodePoint.x + 1].self, grid[nodePoint.y - 1][nodePoint.x].self)
            NodeType.BOTTOM_BORDER -> {
                listOf(grid[nodePoint.y][nodePoint.x - 1].self, grid[nodePoint.y][nodePoint.x + 1].self, grid[nodePoint.y + 1][nodePoint.x].self)
            }

            NodeType.INTERNAL -> listOf(
                    grid[nodePoint.y][nodePoint.x + 1].self, grid[nodePoint.y][nodePoint.x - 1].self,
                    grid[nodePoint.y + 1][nodePoint.x].self, grid[nodePoint.y - 1][nodePoint.x].self
            )
        }

        val edges: List<Edge> = nodeComp.map { destination -> Edge(node, destination, destination.arriveWeight) }
        return edges
    }


}
