package com.jordansilva.map4.data.mapper

interface Mapper<ApiResponse, Domain> {
    fun mapToDomain(response: ApiResponse): Domain
}