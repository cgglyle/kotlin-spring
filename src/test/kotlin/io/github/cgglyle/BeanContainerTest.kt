package io.github.cgglyle

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BeanContainerTest {
    @Test
    fun getBeanTest() {
        //given
        val beanContainer = BeanContainer()
        val testClassA = TestClassA()
        beanContainer.saveBean("testClassA", testClassA)

        //when
        val bean = beanContainer.getBean("testClassA")

        //then
        Assertions.assertEquals(testClassA, bean)
    }

    @Test
    fun getBeanButBeanNameDoseNotExistShouldThrowException() {
        //given
        val beanContainer = BeanContainer()
        val testClassA = TestClassA()
        beanContainer.saveBean("testClassA", testClassA)

        //when
        //这里断言应该抛出一个异常。这个写法是一个语法糖，当函数的最后一个变量是 lambda 语句
        //可以将 lambda 语句单独用 ｛｝花括号提出，更加清晰
        Assertions.assertThrows(BeanException::class.java) {
            val bean = beanContainer.getBean("testClassB")
        }
    }

    @Test
    fun getBeanNewInstance() {
        //given
        val beanContainer = BeanContainer()
        beanContainer.saveBeanDefinition("testClassA", TestClassA::class.java)

        //when
        val bean = beanContainer.getBean("testClassA")

        //then
        Assertions.assertNotNull(bean)
    }

    @Test
    fun getBeanButDefinitionDoesNotExistShouldThrowException() {
        //given
        val beanContainer = BeanContainer()

        //when
        Assertions.assertThrows(BeanException::class.java) {
            beanContainer.getBean("testClassB")
        }
    }
}