package kt.osrs.event

class Event<T> {
    private val handlers = arrayListOf<(Event<T>.(T) -> Unit)>()
    operator fun plusAssign(handler: Event<T>.(T) -> Unit) {
        handlers.add(handler)
    }

    operator fun invoke(value: T) {
        for (handler in handlers) handler(value)
    }
}