package com.pedro.render3d.ui
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.sceneview.Scene
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView

@Composable
fun Floor3DScreen() {

    // Cria engine (Filament) compartilhada
    val engine = rememberEngine()

    // Loaders para recursos
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)

    Scene(
        modifier = Modifier.fillMaxSize(),

        // Passar os componentes essenciais
        engine = engine,
        view = rememberView(engine),
        renderer = rememberRenderer(engine),
        scene = rememberScene(engine),

        modelLoader = modelLoader,
        materialLoader = materialLoader,

        // Adiciona luz principal
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000f
        },

        // Aqui você adiciona nós 3D
        childNodes = rememberNodes {


        }
    )
}

