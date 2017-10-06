package trikered

class Binding(val type: BindingType, val register: Register) {
    override fun toString() = "$type -> $register"
}