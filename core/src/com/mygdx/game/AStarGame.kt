package com.mygdx.game

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.mygdx.game.algorithm.Graph
import com.mygdx.game.algorithm.ShortPathFinder
import com.mygdx.game.systems.GridConfig
import com.mygdx.game.systems.GridRenderingSystem
import com.mygdx.game.systems.GridTouchProcessor
import com.mygdx.game.systems.TouchGridConfig


class AStarGame(val graphConfig: GridConfig) : Game() {

    internal lateinit var batcher: SpriteBatch
    internal lateinit var img: Texture
    internal lateinit var atlas : TextureAtlas

    val engine = PooledEngine()

    override fun create() {
        batcher = SpriteBatch()
        atlas = TextureAtlas("orig/pack.atlas")
        img = atlas.regions.get(0).texture

        createGrid()
    }

    fun createGrid() {
        val graph = Graph(graphConfig)

        val shortPath : ShortPathFinder = ShortPathFinder(graph)

        val widthInset = (graphConfig.nodeWidth - graphConfig.gapXY).toInt()
        val heightInset = (graphConfig.nodeHeight - graphConfig.gapXY).toInt()

        val touchGridConfig = TouchGridConfig.generateConfig(graph.nodeEntities, widthInset, heightInset, graphConfig.executor)
        val gridTouchHandler = GridTouchProcessor(touchGridConfig, shortPath)

        Gdx.input.inputProcessor = gridTouchHandler

        graph.spill(engine)
        engine.addSystem(GridRenderingSystem(batcher, graphConfig))
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        engine.update(Gdx.graphics.deltaTime)
    }

    override fun dispose(){
        batcher.dispose()
        atlas.dispose()
        img.dispose()
    }

}