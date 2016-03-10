package com.mygdx.game.algorithm

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.ai.pfa.Connection
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Array
import com.mygdx.game.components.NodeComponent
import com.mygdx.game.systems.GridConfig
import com.mygdx.game.systems.GridRenderingSystem

/**
 * Represent a graph of nodes.
 */
data class Graph(val config : GridConfig){

    val nodes: List<NodeComponent>
    var edges: MutableList<Edge>
    val nodeComponents : List<List<NodeComponent>>
    val nodeEntities : Array<Array<Entity>>

    init {
        nodeEntities = GridRenderingSystem.createEntityGrid(config)

        nodeComponents  = nodeEntities.map { row ->
            row.map { ent ->
                val comp = ent.getComponent(NodeComponent::class.java)!!
                comp
            }
        }

        val gridEdgeGen = GridEdgeGenerator(nodeComponents, config)

        edges = gridEdgeGen.getEdges().filter { e -> e.destination.barrier }.toMutableList()
        nodes = nodeComponents.flatten()

    }

    fun spill(engine: PooledEngine) {
        nodeEntities.flatten().forEach{ ent ->
            engine.addEntity(ent)
        }
    }

}