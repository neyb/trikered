package trikered

class Changelog(
        private val bindings: Bindings,
        private val registers: List<Register>) {
    private val events = mutableListOf<Event>()

    fun add(event: Event) {
        events += event
        bindings.registersThat { it.type == BindingType.on_event }
                .forEach { it.trigger(event) }
    }

    fun trigger(vararg registerNames: String) =
            if (registerNames.isEmpty()) triggerOnTriggerAndClear()
            else triggerNamedAndClear(registerNames)

    private fun triggerOnTriggerAndClear() = triggerAndClear(
            bindings.registersThat { it.type == BindingType.on_trigger })

    private fun triggerNamedAndClear(registerNames: Array<out String>) =
            triggerAndClear(registers.filter { it.name in registerNames })

    private fun triggerAndClear(registers: List<Register>) {
        events.forEach { event -> registers.forEach { it.trigger(event) } }
        events.clear()
    }

    override fun toString() = "${events.size} events, ${bindings.size} bindings"
}