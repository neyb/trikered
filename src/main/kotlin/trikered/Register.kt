package trikered

class Register(val name: String) {
    private val listeners = mutableMapOf<Class<out Event>, MutableList<(Event) -> Unit>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Event> addListener(javaClass: Class<T>, listener: (T) -> Unit) =
        listeners.computeIfAbsent(javaClass) { mutableListOf() }
                .add(listener as (Event) -> Unit)

    fun trigger(event: Event) =
        listeners.asSequence()
                .filter { (key, _) -> key == event.javaClass }
                .flatMap { it.value.asSequence() }
                .forEach { it(event) }

    override fun toString() = "\"$name\": ${listeners.size} listeners"
}