package trikered

class Changelog(private val bindings: List<Binding>) {
    private val events = mutableListOf<Event>()

    fun add(event: Event) {
        trigger(BindingType.on_event, event)
        events += event
    }

    fun trigger() {
        events.forEach { trigger(BindingType.on_trigger, it) }
        events.clear()
    }

    private fun trigger(type: BindingType, event: Event) =
            bindings.asSequence()
                    .filter { it.type == type }
                    .forEach { it.register.trigger(event) }

    override fun toString() = "${events.size} events, ${bindings.size} bindings"
}