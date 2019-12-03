package com.example.domain.enums

/**
 * Enumerator for the status of a product dictionary.
 *
 * @author Jeroen Flietstra
 *
 * @property status The status value of the enumerator.
 */
enum class ProductDictionaryStatus(protected val status: String): Invokable {
    /** Error code for when a product could not be found in the remote databases */
    NOT_FOUND("@NOT_FOUND@") {
        /** {@inheritDoc} */
        override operator fun invoke() = status
    },
    /** Error code for when a dictionary has been created without valid values */
    NOT_READY("@NOT_READY@") {
        /** {@inheritDoc} */
        override operator fun invoke() = status
    }
}