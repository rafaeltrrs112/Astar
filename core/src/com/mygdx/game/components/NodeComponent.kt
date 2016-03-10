package com.mygdx.game.components

import com.badlogic.ashley.core.Component
import com.mygdx.game.algorithm.Point
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture

class TextureNode(val texture : Texture) : Component
/**
 * Node class
 */
class NodeComponent(val id: String, val posit: Point, val arriveWeight: Int, var color: Color, val nodeWidth : Int, val nodeHeight : Int, val barrier : Boolean = false) : Component {

    var self : NodeComponent = this

    companion object {
        fun isTouched(touchX: Int, touchY: Int, posit: Point, nodeWidth: Int, nodeHeight: Int): Boolean {
            if (touchY > 720) {
                val leftX: Int = posit.x.toInt()
                val rightX: Int = posit.x + nodeWidth

                val bottomY: Int = posit.y
                val topY: Int = posit.y + nodeHeight

                val diffY = (touchY - 720) * 2

                val touchYActual = touchY - diffY

                val isInY = touchYActual >= bottomY && touchYActual <= topY
                val isInX = touchX >= leftX && touchX <= rightX

                return isInY && isInX
            } else {
                val leftX: Int = posit.x.toInt()
                val rightX: Int = posit.x + nodeWidth

                val bottomY: Int = posit.y
                val topY: Int = posit.y + nodeHeight

                val diffY = (720 - touchY) * 2

                val touchYActual = touchY + diffY

                val isInY = touchYActual >= bottomY && touchYActual <= topY
                val isInX = touchX >= leftX && touchX <= rightX

                return isInY && isInX
            }
        }
    }

}
