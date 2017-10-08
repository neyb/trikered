package trikered

import trikered.BindingType.on_event

class Engine {
    private val registers = LinkedHashSet<Register>()
    private var defaultConfigurations = mutableListOf<ChangelogConfigurationBuilder.() -> Unit>(
            { "default" bindedAs on_event })

    @JvmOverloads inline fun <reified T : Event> listen(
            registerName: String = "default",
            type: BindingType? = null,
            noinline listener: (T) -> Unit) =
            listen(T::class.java, registerName, type, listener)

    @JvmOverloads fun <T : Event> listen(
            eventClass: Class<T>,
            registerName: String = "default",
            type: BindingType? = null,
            listener: (T) -> Unit) = apply {
        getOrCreateRegister(registerName).addListener(eventClass, listener)
        if (type != null) defaultConfigurations.add { registerName bindedAs type }
    }

    fun createChangelog(configuration: ChangelogConfigurationBuilder.() -> Unit = { bindDefault() }) =
            ChangelogConfigurationBuilder(this) { defaultConfigurations.forEach { it() } }
                    .apply(configuration)
                    .createChangelog(registers.toList())


    internal fun bindingFor(registerName: String, type: BindingType) =
            Binding(type, getOrCreateRegister(registerName))

    private fun getOrCreateRegister(registerName: String) =
            registers.firstOrNull { it.name == registerName } ?: createRegister(registerName)

    private fun createRegister(registerName: String) = Register(registerName).also { registers += it }
}


