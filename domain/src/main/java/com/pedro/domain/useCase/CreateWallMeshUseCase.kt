package com.pedro.domain.useCase

import com.pedro.domain.model.Mesh3D
import com.pedro.domain.model.Wall
import kotlin.math.sqrt

class CreateWallMeshUseCase {

    operator fun invoke(wall : Wall) : Mesh3D {
        val dx = wall.endX - wall.startX
        val dy = wall.endY - wall.startY

        val len = sqrt(dx * dx + dy * dy)

        val normalX = -dy / len
        val normalY = dx / len


        val halfThick = wall.thickness / 2f

        val h = wall.height

        // 4 sides base and top

        val base0 = floatArrayOf(wall.startX - normalX * halfThick, wall.startY - normalY * halfThick, 0f)
        val base1 = floatArrayOf(wall.startX + normalX * halfThick, wall.startY + normalY * halfThick, 0f)
        val base2 = floatArrayOf(wall.endX + normalX * halfThick, wall.endY + normalY * halfThick, 0f)
        val base3 = floatArrayOf(wall.endX - normalX * halfThick, wall.endY - normalY * halfThick, 0f)

        val top0 = floatArrayOf(base0[0], base0[1], h)
        val top1 = floatArrayOf(base1[0], base1[1], h)
        val top2 = floatArrayOf(base2[0], base2[1], h)
        val top3 = floatArrayOf(base3[0], base3[1], h)

        // Normals of 6 faces (outside direction)
        val nFront = floatArrayOf(-normalX, -normalY, 0f)   // frontside
        val nBack = floatArrayOf(normalX, normalY, 0f)      // backside
        val nLeft = floatArrayOf(-dx / len, -dy / len, 0f)  // left face
        val nRight = floatArrayOf(dx / len, dy / len, 0f)   // rigth face (end)
        val nTop = floatArrayOf(0f, 0f, 1f)       // Top
        val nBottom = floatArrayOf(0f, 0f, -1f)   // Base

        val positions = mutableListOf<Float>()
        val normals = mutableListOf<Float>()

        fun addSquare(v0: FloatArray, v1: FloatArray, v2: FloatArray, v3: FloatArray, normal: FloatArray) {
            // just trust in god
            listOf(v0, v1, v2, v3).forEach { v ->
                positions.addAll(v.toList())
                normals.addAll(normal.toList())
            }
        }



        addSquare(base0, base3, top3, top0, nFront)

        // Face back (b2, b1, t1, t2)
        addSquare(base2, base1, top1, top2, nBack)

        // Face left (b1, b0, t0, t1)
        addSquare(base1, base0, top0, top1, nLeft)

        // Face rigth (b3, b2, t2, t3)
        addSquare(base3, base2, top2, top3, nRight)

        // Face top (t0, t3, t2, t1)
        addSquare(top0, top3, top2, top1, nTop)

        // Face base (b0, b1, b2, b3)
        addSquare(base0, base1, base2, base3, nBottom)

        val indices = mutableListOf<Short>()
        for (face in 0 until 6) {
            val base = (face * 4).toShort()
            indices.add(base)
            indices.add((base + 1).toShort())
            indices.add((base + 2).toShort())
            indices.add(base)
            indices.add((base + 2).toShort())
            indices.add((base + 3).toShort())
        }

        return Mesh3D(
            positions = positions.toFloatArray(),
            normals = normals.toFloatArray(),
            indices = indices.toShortArray()
        )

    }
    //A wall is a box with 6 faces. For correct normal distributions,
    // each face needs 4 of its own (non-shared) vertices. Total: 24 vertices, 36 indices.

}