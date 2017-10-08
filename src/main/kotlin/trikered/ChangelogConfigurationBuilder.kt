package trikered

class ChangelogConfigurationBuilder(private val engine: Engine, private val defaultConfiguration: ChangelogConfigurationBuilder.() -> Unit) {
    private val bindings = Bindings()

    fun bindDefault() = defaultConfiguration()

    infix fun String.bindedAs(type: BindingType) {
            bindings += engine.bindingFor(this, type)
    }

    internal fun createChangelog(registers: List<Register>) = Changelog(bindings, registers)
}