package com.pedro.domain

import com.pedro.domain.model.Wall
import com.pedro.domain.useCase.CreateWallMeshUseCase
import junit.framework.Assert.assertEquals
import org.junit.Test


class CreateWallMeshTest {

    @Test
    fun `generate 8 vertex` () {
        val wall = Wall(
            startX = 0f,
            startY = 0f,
            endX = 4f,
            endY = 0f,
            height = 3f,
            thickness = 0.2f
        )

        val mesh = CreateWallMeshUseCase()(wall)

//        assertEquals(24,mesh.vertices.size)
        assertEquals(36,mesh.indices.size)



    }
}