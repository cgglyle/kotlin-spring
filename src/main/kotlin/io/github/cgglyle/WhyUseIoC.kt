package io.github.cgglyle

class A(
    val b: B
)

class B(
    val c: C
)

class C

fun main() {
    val a = A(B(C()))
}