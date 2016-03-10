package com.mygdx.game.algorithm

import com.badlogic.gdx.ai.pfa.Connection
import com.mygdx.game.components.NodeComponent

/**
 * An edge is the connection between one node and another.
 */
class Edge(val source: NodeComponent, val destination: NodeComponent, val weight: Int) : Connection<NodeComponent> {

    override fun getToNode(): NodeComponent? = destination

    override fun getFromNode(): NodeComponent? = source

    override fun getCost(): Float = weight.toFloat()
}