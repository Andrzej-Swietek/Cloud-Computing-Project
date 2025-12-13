package com.friendly.recommender.api

interface CrudService<T, IdType> {
    fun getAll(): List<T>
    fun get(id: IdType): T
    fun create(entity: T): T
    fun update(id: IdType, entity: T): T
    fun delete(id: IdType)
}
