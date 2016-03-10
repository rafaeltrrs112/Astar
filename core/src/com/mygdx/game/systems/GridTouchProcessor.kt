package com.mygdx.game.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.mygdx.game.algorithm.ShortPathFinder
import com.mygdx.game.components.NodeComponent
import com.mygdx.game.components.TransformComponent
import com.rahulrav.futures.Future
import java.util.concurrent.Executor
import com.badlogic.gdx.utils.Array

data class TouchGridConfig(val entitiesComponents: List<Pair<NodeComponent, TransformComponent>>, val nodeWidth: Int, val nodeHeight: Int, val executor: Executor?) {
    companion object {
        fun generateConfig(entities: Array<Array<Entity>>, nodeWidth: Int, nodeHeight: Int, executor: Executor?): TouchGridConfig {
            val entitiesComponents = entities.flatten().map { ent ->
                Pair(ent.getComponent(NodeComponent::class.java), ent.getComponent(TransformComponent::class.java))
            }
            return TouchGridConfig(entitiesComponents, nodeWidth, nodeHeight, executor)
        }
    }
}

/**
 * Handler class for grid selection.
 */
class GridTouchProcessor(val config: TouchGridConfig, val shortPathFinder: ShortPathFinder) : InputProcessor {
    val gridNodes = config.entitiesComponents

    var startNode: NodeComponent? = null
    var endNode: NodeComponent? = null

    var currentNodes: Pair<NodeComponent?, NodeComponent?> = Pair(startNode, endNode)
    val  BAD_STATE_SET = "Both nodes should cannot be set at once."


    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        //        println("Point $screenX, $screenY")
        return true
    }

    override fun mouseMoved(p0: Int, p1: Int): Boolean = true

    override fun keyTyped(p0: Char): Boolean = true

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val touchedNode = gridNodes.find { node ->
            NodeComponent.isTouched(screenX, screenY, node.second.posit, config.nodeWidth, config.nodeHeight)
        }
        when (touchedNode) {
            null -> {
            }
            else -> {
                println(touchedNode)

                when (currentNodes) {
                    Pair(null, null) -> {

                        gridNodes.filterNot {
                            nodePair -> nodePair.first.color == Color.BLACK || nodePair.first.color == Color.WHITE

                        }.forEach {
                            nodePair -> nodePair.first.color = Color.GRAY
                        }

                        startNode = touchedNode.first
                        startNode!!.color = Color.RED
                        currentNodes = Pair(startNode, null)
                    }

                    Pair(startNode, null) -> {
                        when (config.executor) {
                            null -> Future.submit {
                                println("This future")
                                endNode = touchedNode.first
                                currentNodes = Pair(startNode, endNode)

                                shortPathFinder.executeSearch(startNode!!, endNode!!)
                                val path = shortPathFinder.getPath(endNode!!)

                                when (path) {
                                    null -> Unit
                                    else -> path.filter {p -> p !== startNode && p !== endNode}.forEach { ent -> ent.color = Color.YELLOW }
                                }

                                startNode!!.color = Color.RED
                                endNode!!.color = Color.BLUE

                                startNode = null
                                endNode = null
                                currentNodes = Pair(startNode, endNode)
                            }
                            else -> {
                                val block: () -> Unit = {
                                    endNode = touchedNode.first
                                    currentNodes = Pair(startNode, endNode)

                                    shortPathFinder.executeSearch(startNode!!, endNode!!)
                                    val path = shortPathFinder.getPath(endNode!!)


                                    when (path) {
                                        null -> Unit
                                        else -> Future.submit { path.filter{p -> p === startNode || p === endNode}.forEach { ent -> ent.color = Color.YELLOW } }
                                    }

                                    startNode!!.color = Color.RED
                                    endNode!!.color = Color.BLUE

                                    startNode = null
                                    endNode = null
                                    currentNodes = Pair(startNode, endNode)
                                }
                                Future.submit(config.executor, block)
                            }

                        }
                    }
                    else -> throw AssertionError(BAD_STATE_SET)
                }
            }
        }
        return true
    }


    override fun scrolled(p0: Int): Boolean = true

    override fun keyUp(p0: Int): Boolean = true

    override fun touchDragged(p0: Int, p1: Int, p2: Int): Boolean = true

    override fun keyDown(p0: Int): Boolean = true
}