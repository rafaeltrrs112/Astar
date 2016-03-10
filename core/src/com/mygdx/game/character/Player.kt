package com.mygdx.game.character

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.uwsoft.editor.renderer.components.DimensionsComponent
import com.uwsoft.editor.renderer.components.TransformComponent
import com.uwsoft.editor.renderer.scripts.IScript
import com.uwsoft.editor.renderer.utils.ComponentRetriever

/**
 * Script for moving a character.
 */
class Player() : IScript {
    var player: Entity? = null

    var transformComponent: TransformComponent? = null
    var dimensionsComponent: DimensionsComponent? = null

    override fun act(delta: Float) {

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            transformComponent!!.x = transformComponent!!.x - (180 * delta)

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            transformComponent!!.x = transformComponent!!.x + (180 * delta)
    }

    override fun init(entity: Entity?) {
        initPlayer(entity!!)

    }

    private fun initPlayer(player: Entity) {


        this.player = player
        transformComponent = ComponentRetriever.get(player, TransformComponent::class.java)
        dimensionsComponent = ComponentRetriever.get(player, DimensionsComponent::class.java)
    }

    override fun dispose() {
        throw UnsupportedOperationException()
    }

}