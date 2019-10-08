package com.example.domain.repositories

/**
 * Data mapper used to map database entities to model classes and vice versa.
 */
interface ProductModelMapper<E, M> {
    fun fromEntity(from: E): M
    fun toEntity(from: M): E
}