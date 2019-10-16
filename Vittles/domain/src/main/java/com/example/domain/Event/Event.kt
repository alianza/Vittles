package com.example.domain.Event

/**
 * Is used to declare and handle events.
 * This is here so events like the ones in C# can be used.
 *
 * @author Arjen Simons
 *
 * @param T Data type to pass into the event.
 *
 * @property handlers A list of units (in the form of lambda expressions) subscribed to an event.
 */
class Event<T> {
    /**
     * A list of units (in the form of lambda expressions) subscribed to an event.
     *
     */
    private val handlers = arrayListOf<(Event<T>.(T) -> Unit)>()

    /**
     * Used to subscribe to an event.
     *
     * @param handler The unit that should be subscribed.
     */
    operator fun plusAssign(handler: Event<T>.(T) -> Unit) { handlers.add(handler) }

    /**
     * Used to unsubscribe from an event.
     *
     * @param handler The handler that should be unsubscribed.
     */
    operator fun minusAssign(handler: Event<T>.(T) -> Unit) { handlers.remove(handler) }

    /**
     * Used to invoke the event (makes it so all the subscribed handlers execute.
     *
     * @param value The value that is passed into the event
     */
    operator fun invoke(value: T) { for (handler in handlers) handler(value) }
}