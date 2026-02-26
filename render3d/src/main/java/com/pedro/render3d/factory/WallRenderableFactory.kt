package com.pedro.render3d.factory

import com.google.android.filament.Box
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.IndexBuffer
import com.google.android.filament.Material
import com.google.android.filament.MaterialInstance
import com.google.android.filament.RenderableManager
import com.google.android.filament.VertexBuffer
import com.pedro.domain.model.Mesh3D
import dev.romainguy.kotlin.math.Float4
import io.github.sceneview.loaders.MaterialLoader
import java.nio.ByteBuffer
import java.nio.ByteOrder

class WallRenderableFactory (
    private val engine : Engine,
    private val materialLoader: MaterialLoader

) {

    private var unlitMaterial : Material? = null
    private var materialInstance : MaterialInstance? = null
    private var lastColor : FloatArray? = null

    fun create(
        mesh3D: Mesh3D,
        color : FloatArray = floatArrayOf(0.8f,0.8f,0.8f,1f)
    ) : Int {
        // 1. Criar VertexBuffer
        val vertexBuffer = createVertexBuffer(mesh3D)

        // 2. Criar IndexBuffer
        val indexBuffer = createIndexBuffer(mesh3D)

        // 3. Obter material unlit
        val matInstance = getOrCreateUnlitMaterial(color)

        // 4. Criar Entity
        val entity = EntityManager.get().create()

        // 5. Calcular bounding box
        val boundingBox = calculateBoundingBox(mesh3D)

        // 6. Construir Renderable
        RenderableManager.Builder(1)
            .boundingBox(boundingBox)
            .geometry(
                0,
                RenderableManager.PrimitiveType.TRIANGLES,
                vertexBuffer,
                indexBuffer,
                0,
                mesh3D.indices.size
            )
            .material(0, matInstance)
            .castShadows(true)
            .receiveShadows(true)
            .build(engine, entity)

        return entity
    }

    private fun createVertexBuffer(mesh : Mesh3D) : VertexBuffer {
        val vertexCount = mesh.vertexCount


        val vertexSize = (3 + 3) * Float.SIZE_BYTES
        val buffer = ByteBuffer.allocateDirect(vertexCount * vertexSize)
            .order(ByteOrder.nativeOrder())

        for (i in 0 until vertexCount) {

            buffer.putFloat(mesh.positions[i * 3])
            buffer.putFloat(mesh.positions[i * 3 + 1])
            buffer.putFloat(mesh.positions[i * 3 + 2])

            buffer.putFloat(mesh.normals[i * 3])
            buffer.putFloat(mesh.normals[i * 3 + 1])
            buffer.putFloat(mesh.normals[i * 3 + 2])
        }
        buffer.flip()

        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(vertexCount)
            .bufferCount(1)
            .attribute(
                VertexBuffer.VertexAttribute.POSITION,
                0,
                VertexBuffer.AttributeType.FLOAT3,
                0,
                vertexSize
            )
            .attribute(
                VertexBuffer.VertexAttribute.TANGENTS,
                0,
                VertexBuffer.AttributeType.FLOAT3,
                3 * Float.SIZE_BYTES,
                vertexSize
            )
            .build(engine)

        vertexBuffer.setBufferAt(engine, 0, buffer)

        return vertexBuffer
    }
    private fun createIndexBuffer(mesh: Mesh3D): IndexBuffer {
        val buffer = ByteBuffer.allocateDirect(mesh.indices.size * Short.SIZE_BYTES)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
        buffer.put(mesh.indices)
        buffer.flip()

        val indexBuffer = IndexBuffer.Builder()
            .indexCount(mesh.indices.size)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)

        indexBuffer.setBuffer(engine, buffer)

        return indexBuffer
    }
    private fun getOrCreateUnlitMaterial(color: FloatArray): MaterialInstance {
        if (materialInstance == null || !color.contentEquals(lastColor)) {
            materialInstance?.let { engine.destroyMaterialInstance(it) }
            materialInstance = materialLoader.createColorInstance(
                color = Float4(color[0], color[1], color[2], color[3]),
                metallic = 0f,
                roughness = 1f,
                reflectance = 0f
            )
            lastColor = color.copyOf()
        }
        return materialInstance!!
    }
    private fun calculateBoundingBox(mesh: Mesh3D): Box {
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var minZ = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE
        var maxZ = Float.MIN_VALUE

        for (i in 0 until mesh.vertexCount) {
            val x = mesh.positions[i * 3]
            val y = mesh.positions[i * 3 + 1]
            val z = mesh.positions[i * 3 + 2]

            if (x < minX) minX = x
            if (y < minY) minY = y
            if (z < minZ) minZ = z
            if (x > maxX) maxX = x
            if (y > maxY) maxY = y
            if (z > maxZ) maxZ = z
        }

        val centerX = (minX + maxX) / 2f
        val centerY = (minY + maxY) / 2f
        val centerZ = (minZ + maxZ) / 2f
        val halfExtentX = (maxX - minX) / 2f
        val halfExtentY = (maxY - minY) / 2f
        val halfExtentZ = (maxZ - minZ) / 2f

        return Box(centerX, centerY, centerZ, halfExtentX, halfExtentY, halfExtentZ)
    }

    fun destroy() {
        materialInstance?.let {
            engine.destroyMaterialInstance(it)
        }
        materialInstance = null
        unlitMaterial = null
        lastColor = null
    }
}