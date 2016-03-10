package com.mygdx.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

/**
 * 
 */
class TextureComponent(val texture : Texture) : Component {
    companion object {
        fun insertOver(entities : Iterable<Entity>, texture : Texture) = entities.forEach { e ->
            e.add(TextureComponent(texture))
        }
    }
}