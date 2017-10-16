package trikered

class Listener<in T : Event>(
        private val function: (T) -> Unit
) {
    fun trigger(event: T) {
        function(event)
    }
}