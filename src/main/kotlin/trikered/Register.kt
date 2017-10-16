package trikered

class Register(val name: String) {
    private val listeners = mutableMapOf<Class<out Event>, MutableList<Listener<Event>>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Event> addListener(javaClass: Class<T>, listener: (T) -> Unit) =
        listeners.computeIfAbsent(javaClass) { mutableListOf() }
                .add(Listener(listener) as Listener<Event>)

    fun trigger(event: Event) =
        listeners.asSequence()
                .filter { (key, _) -> key == event.javaClass }
                .flatMap { it.value.asSequence() }
                .forEach { it.trigger(event) }

    override fun toString() = "\"$name\": ${listeners.size} listeners"
}