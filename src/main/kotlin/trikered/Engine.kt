package trikered

import trikered.BindingType.on_event

class Engine {
    private val registers = LinkedHashSet<Register>()
    private val defaultBindings = Bindings().apply {
        add(Binding(on_event, getOrCreateRegister("default")))
    }

    @JvmOverloads
    inline fun <reified T : Event> listen(registerName: String = "default", noinline listener: (T) -> Unit) =
            listen(T::class.java, registerName, listener)

    @JvmOverloads
    fun <T : Event> listen(eventClass: Class<T>, registerName: String = "default", listener: (T) -> Unit) = apply {
        getOrCreateRegister(registerName).addListener(eventClass, listener)
    }

    fun createChangelog(configuration: ChangelogConfigurationBuilder.() -> Unit = { bindDefault() }) =
            ChangelogConfigurationBuilder(this, defaultBindings)
                    .apply(configuration)
                    .createChangelog(registers.toList())

    fun bindByDefault(registerName: String, type: BindingType) {
        defaultBindings.add(Binding(type, getOrCreateRegister(registerName)))
    }

    internal fun bindingFor(registerName: String, type: BindingType) =
            Binding(type, getOrCreateRegister(registerName))

    private fun getOrCreateRegister(registerName: String) =
            registers.firstOrNull { it.name == registerName } ?: createRegister(registerName)

    private fun createRegister(registerName: String) = Register(registerName).also { registers += it }
}


