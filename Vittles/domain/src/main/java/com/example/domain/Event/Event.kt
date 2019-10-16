package com.example.domain.Event

/**
 * Is used to declare and handle events .
 * This is here so events like the ones in C# can be used.
 *
 * @author Arjen Simons
 *
 * @param T Data type to pass into the event.
 */
class Event<T> {
    private val handlers = arrayListOf<(Event<T>.(T) -> Unit)>()
    operator fun plusAssign(handler: Event<T>.(T) -> Unit) { handlers.add(handler) }
    operator fun minusAssign(handler: Event<T>.(T) -> Unit) { handlers.remove(handler) }
    operator fun invoke(value: T) { for (handler in handlers) handler(value) }
}