package me.thenightmancodeth.classi.data.model

/**
 * Created by joe on 4/5/17.
 */
data class Response<out T>(val type: String, val value: List<T>)