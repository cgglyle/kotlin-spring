package io.github.cgglyle.scan.test

import io.github.cgglyle.Autowired
import io.github.cgglyle.Component

@Component
class TestA {
    @Autowired
    lateinit var testA: TestA
}