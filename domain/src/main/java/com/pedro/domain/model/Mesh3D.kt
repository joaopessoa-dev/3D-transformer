package com.pedro.domain.model

data class Mesh3D(
    val positions : FloatArray,
    val normals : FloatArray,
    val indices : ShortArray
) {

    val vertexCount : Int get() = positions.size / 3
    val triangleCount : Int get() = indices.size / 3
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mesh3D

        if (!positions.contentEquals(other.positions)) return false
        if (!normals.contentEquals(other.normals)) return false
        if (!indices.contentEquals(other.indices)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = positions.contentHashCode()
        result = 31 * result + normals.contentHashCode()
        result = 31 * result + indices.contentHashCode()
        return result
    }

}
