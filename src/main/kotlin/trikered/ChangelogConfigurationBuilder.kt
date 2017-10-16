package trikered

class ChangelogConfigurationBuilder(private val engine: Engine, private val defaultBinding: Bindings) {
    private val bindings = Bindings()

    fun bindDefault() {
        bindings.add(defaultBinding)
    }

    infix fun BindingType.bind(registerName: String) {
        bindings.add(Binding(this, engine.getOrCreateRegister(registerName)))
    }

    infix fun unbind(registerName: String) {
        bindings.remove { it.register.name == registerName }
    }

    internal fun createChangelog(registers: List<Register>) = Changelog(bindings, registers)
}