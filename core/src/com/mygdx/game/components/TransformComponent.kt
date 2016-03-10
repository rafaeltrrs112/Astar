package com.mygdx.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.algorithm.Point

/**
 * Class used to move character.
 */
class TransformComponent(x : Float, y : Float) : Component {
    val posit = Point(x.toInt(), y.toInt())
    val scale = Vector2(1.0f, 1.0f)
    var rotation = 0.0f
}
