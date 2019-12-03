package com.example.domain.enums

/**
 * Interface for invokable enumerators.
 *
 * @author Jeroen Flietstra
 */
interface Invokable {
    /**
     * Returns the assigned status value.
     *
     * @return The status value of the enumerator.
     */
    operator fun invoke(): Any
}
