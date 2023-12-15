package io.github.cgglyle.bean

class TestClassB {
    // lateinit 意思延迟赋值，否则现在就要赋值，这是 kotlin 的特性
    lateinit var testClassA: TestClassA
}