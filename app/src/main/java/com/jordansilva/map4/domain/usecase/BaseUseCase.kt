package com.jordansilva.map4.domain.usecase

import com.jordansilva.map4.util.UseCaseResult
import kotlinx.coroutines.flow.Flow

abstract class UseCase<in Params : Any, out Type : Any> {

    suspend fun execute(params: Params): UseCaseResult<Type> {
        return try {
            doWork(params)
        } catch (e: Throwable) {
            e.printStackTrace()
            UseCaseResult.Failure(e.message)
        }
    }

    protected abstract suspend fun doWork(params: Params): UseCaseResult<Type>
}

abstract class StreamUseCase<in Params : Any, out Type : Any> {

    fun execute(params: Params) = doWork(params)

    protected abstract fun doWork(params: Params): Flow<Type>
}

fun <T : Any> StreamUseCase<Unit, T>.execute() = execute(Unit)
suspend fun <T : Any> UseCase<Unit, T>.execute() = execute(Unit)