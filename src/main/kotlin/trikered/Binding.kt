package trikered

import java.util.*

class Binding(val type: BindingType, val register: Register) {

    override fun hashCode() = Objects.hash(type, register)

    override fun equals(other: Any?) = when {
        other === this -> true
        other !is Binding -> false
        else -> other.type == type && other.register.name == register.name
    }

    override fun toString() = "$type -> $register"
}