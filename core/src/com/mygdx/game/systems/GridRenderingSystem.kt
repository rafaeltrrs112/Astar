package com.mygdx.game.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.game.algorithm.Point
import com.mygdx.game.components.NodeComponent
import com.mygdx.game.components.TransformComponent
import java.util.concurrent.Executor
import com.badlogic.gdx.utils.Array
import com.mygdx.game.parse.ConfigParser
import com.mygdx.game.parse.ConfigPoint

data class GridConfig(val nodeWidth: Float, val nodeHeight: Float, val gapXY: Float, val gridWidth: Float, val gridHeight: Float, val executor: Executor?)

/**
 * Rendering system class.
 */
class GridRenderingSystem(val batch: SpriteBatch, val config: GridConfig) : IteratingSystem(Family.all(TransformComponent::class.java, NodeComponent::class.java).get()) {

    var transformM = ComponentMapper.getFor(TransformComponent::class.java)
    var nodeM = ComponentMapper.getFor(NodeComponent::class.java)

    val shapeRenderer = ShapeRenderer()

    val font: BitmapFont = BitmapFont()

    override fun processEntity(entity: Entity, delta: Float) {
        val transformComponent = transformM.get(entity)
        val nodeComponent = nodeM.get(entity)

        shapeRenderer.projectionMatrix = (batch.projectionMatrix);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.color = if (nodeComponent.id.contains("T")) Color.WHITE else nodeComponent.color

        shapeRenderer.rect(transformComponent.posit.x.toFloat(), transformComponent.posit.y.toFloat(), config.nodeHeight - config.gapXY, config.nodeWidth - config.gapXY)
        shapeRenderer.end()

        batch.begin()

        font.color = Color.BLACK

        if(nodeComponent.id.contains("T") || !nodeComponent.id.contains("F")) font.draw(batch, "${nodeComponent.id}", transformComponent.posit.x + nodeComponent.nodeWidth.toFloat() / 2f, transformComponent.posit.y + nodeComponent.nodeHeight.toFloat() / 2f)

        batch.end()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
    }

    companion object {
        fun createEntityGrid(config: GridConfig): Array<Array<Entity>> {
            val parseSettings: List<List<ConfigPoint>> = ConfigParser.parseSettings()
            val height_ceiling = (config.gridHeight - config.nodeHeight).toInt()
            val width_ceiling = (config.gridWidth - config.nodeWidth).toInt()

            val stepNum = (config.nodeHeight).toInt()
            val grid: Array<Array<Entity>> = Array()

            for (y in 0..height_ceiling step stepNum) {
                val row: Array<Entity> = Array()
                for (x in 0..width_ceiling step stepNum) {
                    val currX = x / stepNum
                    val currY = y / stepNum
                    val nodeSet = parseSettings[currY][currX]
                    val firstColor = if (nodeSet.weight == Int.MAX_VALUE) Color.BLACK else Color.GRAY
                    val isBarrier = firstColor === Color.GRAY
                    val boxEntity: Entity = Entity()
                            .add(TransformComponent(x.toFloat(), y.toFloat()))
                            .add(NodeComponent(nodeSet.id, nodeSet.posit, nodeSet.weight, firstColor, config.nodeWidth.toInt(), config.nodeHeight.toInt(), isBarrier))
                    row.add(boxEntity)
                }
                grid.add(row)
            }
            val portals = grid.flatten().filter { p ->
                when (p) {
                    null -> throw AssertionError()
                    else -> {
                        p.getComponent(NodeComponent::class.java).id.contains("T")
                    }
                }
            }
            portals.forEach { startPortal ->
                val brotherLink = startPortal.getComponent(NodeComponent::class.java)
                val sisterPortal = getSisterPortal(brotherLink.posit, brotherLink.id, parseSettings)
                val sisterLink = grid.flatten().find { n ->
                    n.getComponent(NodeComponent::class.java).posit === sisterPortal
                }

                when (sisterLink) {
                    null -> throw AssertionError()
                    else -> brotherLink.self = sisterLink.getComponent(NodeComponent::class.java)
                }
            }


            return grid
        }

        fun getSisterPortal(startPort: Point, id: String, configs: List<List<ConfigPoint>>): Point {
            val portals = configs.flatten().filter { c -> c.id == id }
            println(portals)
            val sisterPortal = portals.find { t -> t.posit != startPort }
            when (sisterPortal) {
                null -> throw AssertionError("Missing sister portal")
                else -> return sisterPortal.posit
            }
        }
    }
}