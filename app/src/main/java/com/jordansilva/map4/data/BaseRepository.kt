package com.jordansilva.map4.data

abstract class BaseRepository {

    inline fun <reified T> executeSafe(block: () -> T): T {
        return try {
            block.invoke()
        } catch (e: Throwable) {
            e.printStackTrace()
            throw Error(e.message)
        }
    }
}