package com.pedro.render3d.ui
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.pedro.domain.model.Wall
import com.pedro.domain.useCase.CreateWallMeshUseCase
import com.pedro.render3d.factory.WallRenderableFactory
import io.github.sceneview.Scene
import io.github.sceneview.node.RenderableNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import dev.romainguy.kotlin.math.Float3

@Composable
fun Floor3DScreen() {

    val engine = rememberEngine()
    val materialLoader = rememberMaterialLoader(engine)
    val modelLoader = rememberModelLoader(engine)
    val scene = rememberScene(engine)
    val renderer = rememberRenderer(engine)

    val wallFactory = remember(engine, materialLoader) {
        WallRenderableFactory(engine, materialLoader)
    }


    val createWallMesh = remember { CreateWallMeshUseCase() }
    val wall = remember { Wall(0f, 0f, 4f, 0f, 3f, 0.2f) }
    val mesh = remember(wall) { createWallMesh(wall) }


    val wallEntity = remember(mesh) {
        wallFactory.create(mesh, floatArrayOf(0.6f, 0.3f, 0.1f, 1f)) // cor marrom
    }


    val wallNode = remember(wallEntity) {
        RenderableNode(engine, wallEntity)
    }


    DisposableEffect(Unit) {
        onDispose {
            wallFactory.destroy()
        }
    }

    Scene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        renderer = renderer,
        scene = scene,
        modelLoader = modelLoader,
        materialLoader = materialLoader,


        cameraNode = rememberCameraNode(engine) {
            position = Float3(5f, 5f, 5f)
            lookAt(Float3(2f, 0f, 1.5f))
        },


        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000f
        },


        childNodes = rememberNodes {
            add(wallNode)
        }
    )
}

