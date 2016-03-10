package com.mygdx.game.algorithm

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.ObjectSet
import com.mygdx.game.components.NodeComponent
import java.util.*

/**
 * Dijkstra in Kotlin
 */
class ShortPathFinder(graph: Graph) {
    private val edges: MutableList<Edge> = graph.edges
    private var settledNodes: ObjectSet<NodeComponent> = ObjectSet()
    private var unSettledNodes: ObjectSet<NodeComponent> = ObjectSet()
    private var predecessors: ObjectMap<NodeComponent, NodeComponent> = ObjectMap()
    private var distance: ObjectMap<NodeComponent, Int> = ObjectMap()

    fun clear(){
        settledNodes.clear()
        unSettledNodes.clear()
        predecessors.clear()
        distance.clear()
    }

    fun executeSearch(source: NodeComponent, destination: NodeComponent) {
        clear()
        distance.put(source, 0)
        unSettledNodes.add(source)
        while (unSettledNodes.size > 0 && !settledNodes.contains(destination)) {
            val node = getMinimum(unSettledNodes)
            settledNodes.add(node)
            unSettledNodes.remove(node)
            findMinimalDistances(node)
        }

        settledNodes.forEach { node -> node.color = Color.MAROON }

        source.color = Color.RED
        destination.color = Color.BLUE
    }

    private fun findMinimalDistances(node: NodeComponent) {
        val adjacentNodes = getNeighbors(node)
        for (target in adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node) + getDistance(node, target))
                predecessors.put(target, node)
                unSettledNodes.add(target)
            }
        }
    }

    private fun getDistance(node: NodeComponent, target: NodeComponent): Int {
        for (edge in edges) {
            if (edge.source == node && edge.destination == target) {
                return edge.weight
            }
        }
        throw RuntimeException("Should not happen")
    }

    private fun getNeighbors(node: NodeComponent): List<NodeComponent> {
        val neighbors = ArrayList<NodeComponent>()
        for (edge in edges) {
            if (edge.source == node && !isSettled(edge.destination)) {
                neighbors.add(edge.destination)
            }
        }
        return neighbors
    }

    private fun getMinimum(nodes: ObjectSet<NodeComponent>): NodeComponent {
        var minimum: NodeComponent? = null
        for (vertex in nodes) {
            if (minimum == null) {
                minimum = vertex
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex
                }
            }
        }
        return minimum!!
    }

    private fun isSettled(node: NodeComponent): Boolean {
        return settledNodes.contains(node)
    }

    private fun getShortestDistance(destination: NodeComponent): Int {
        val d = distance[destination]
        if (d == null) {
            return Integer.MAX_VALUE
        } else {
            return d
        }
    }

    fun getPath(target: NodeComponent): LinkedList<NodeComponent>? {
        val path = LinkedList<NodeComponent>()
        var step = target
        // check if a path exists
        if (predecessors[step] == null) {
            return null
        }
        path.add(step)
        while (predecessors[step] != null) {
            step = predecessors[step]!!
            path.add(step)
        }
        // Put it into the correct order
        Collections.reverse(path)
        return path
    }
}
