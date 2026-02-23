package com.pedro.render3d.ui

import androidx.lifecycle.ViewModel
import com.pedro.domain.CreateWallMeshUseCase
import com.pedro.domain.model.Mesh3D
import com.pedro.domain.model.Wall

class Floor3DViewModel : ViewModel() {

    private val createWallMesh = CreateWallMeshUseCase()

    fun createTestWall() : Mesh3D {
        val wall = Wall(0f, 0f, 4f, 0f, 3f, 0.2f)
        return createWallMesh(wall)
    }
}