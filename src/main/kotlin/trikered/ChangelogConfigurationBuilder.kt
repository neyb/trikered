package trikered

class ChangelogConfigurationBuilder(private val engine: Engine, private val defaultBinding: Bindings) {
    private val bindings = Bindings()

    fun bindDefault() {
        bindings.add(defaultBinding)
    }

    infix fun String.bindedAs(type: BindingType) {
        bindings.add(engine.bindingFor(this, type))
    }

    internal fun createChangelog(registers: List<Register>) = Changelog(bindings, registers)
}