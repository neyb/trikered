package trikered

class Bindings {
    private val bindings = linkedSetOf<Binding>()
    val size: Int get() = bindings.size

    fun registersThat(filter: (Binding) -> Boolean) = bindings.filter(filter).map { it.register }

    operator fun plusAssign(binding: Binding) {
        bindings += binding
    }
}