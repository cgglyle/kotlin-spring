package io.github.cgglyle.scan.test.dir

import io.github.cgglyle.Autowired
import io.github.cgglyle.Component
import io.github.cgglyle.scan.test.TestA

@Component
class TestC {
    @Autowired
    lateinit var testA: TestA
    lateinit var testC: TestC

    fun testCIsInitialized(): Boolean {
        // 判断 testC 是否已经初始化
        // 因为没有自动注入，我们希望它是没有初始化的
        return this::testC.isInitialized
    }
}