package trikered

class Listener<T : Event>(
        val javaClass: Class<T>,
        private val function: (T) -> Unit
) {
    fun trigger(event: T) {
        function(event)
    }
}