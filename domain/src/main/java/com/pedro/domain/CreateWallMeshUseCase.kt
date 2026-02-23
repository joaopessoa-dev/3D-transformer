package com.pedro.domain

import android.graphics.Mesh
import com.pedro.domain.model.Mesh3D
import com.pedro.domain.model.Wall
import kotlin.math.sqrt

class CreateWallMeshUseCase {

    operator fun invoke(wall : Wall) : Mesh3D {
        val dx = wall.endX - wall.startX
        val dy = wall.endY - wall.startY
        val length = sqrt(dx * dx + dy * dy)

        val nx = -dy / length
        val ny = dx / length

        val halfThickness = wall.thickness / 2f

        val offsetX = nx * halfThickness
        val offsetY = ny * halfThickness

        val v0 = floatArrayOf(wall.startX - offsetX, wall.startY - offsetY, 0f)
        val v1 = floatArrayOf(wall.startX + offsetX, wall.startY + offsetY, 0f)
        val v2 = floatArrayOf(wall.endX + offsetX, wall.endY + offsetY, 0f)
        val v3 = floatArrayOf(wall.endX - offsetX, wall.endY - offsetY, 0f)

        val v4 = floatArrayOf(v0[0], v0[1], wall.height)
        val v5 = floatArrayOf(v1[0], v1[1], wall.height)
        val v6 = floatArrayOf(v2[0], v2[1], wall.height)
        val v7 = floatArrayOf(v3[0], v3[1], wall.height)

        val vertices = floatArrayOf(
            *v0, *v1, *v2, *v3,
            *v4, *v5, *v6, *v7
        )

        val indices = intArrayOf(
            0,1,2, 0,2,3,
            4,5,6, 4,6,7,
            0,1,5, 0,5,4,
            1,2,6, 1,6,5,
            2,3,7, 2,7,6,
            3,0,4, 3,4,7
        )

        return Mesh3D(vertices, indices)
    }
}

// create 4 base vertices, duplicate those vertices on Z and connect all triangles