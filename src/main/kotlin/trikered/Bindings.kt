package trikered

class Bindings {
    private val bindings = linkedSetOf<Binding>()
    val size: Int get() = bindings.size

    fun registersThat(filter: (Binding) -> Boolean) = bindings.filter(filter).map { it.register }

    fun add(bindings: Bindings) {
        this.bindings += bindings.bindings
    }

    fun add(binding: Binding) {
        bindings += binding
    }
}