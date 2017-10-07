package trikered

class ChangelogConfigurationBuilder(private val engine: Engine, private val defaultConfiguration: ChangelogConfigurationBuilder.() -> Unit) {
    private val bindings = linkedSetOf<Binding>()

    fun bindDefault() = defaultConfiguration()

    @JvmName("bind")
    infix fun String.bindedAs(type: BindingType) {
        if (!containsBinding(this, type))
            bindings += engine.bindingFor(this, type)
    }

    internal fun createChangelog(registers: List<Register>) = Changelog(bindings.toList(), registers)

    private fun containsBinding(registerName: String, type: BindingType) =
            bindings.any { it.type == type && it.register.name == registerName }

}