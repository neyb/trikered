package trikered

import io.github.neyb.shoulk.shouldEqual
import org.junit.jupiter.api.Test

class ToStringTest {
    @Test fun `binding`() {
        Binding(BindingType.on_event, Register("the register")).toString() shouldEqual
                "on_event -> \"the register\": 0 listeners"
    }

    @Test fun `changelog`() {
        class MyEvent : Event

        val changelog = Engine().listen<MyEvent> {}.createChangelog()
        changelog.add(MyEvent())
        changelog.toString() shouldEqual "1 events, 1 bindings"
    }

    @Test fun `Register`() {
        class MyEvent : Event
        Register("test").apply {
            addListener(MyEvent::class.java) {}
            toString() shouldEqual "\"test\": 1 listeners"
        }
    }
}