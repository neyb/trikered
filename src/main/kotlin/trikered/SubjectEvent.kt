package trikered

abstract class SubjectEvent<out T>(val subject: T) : Event