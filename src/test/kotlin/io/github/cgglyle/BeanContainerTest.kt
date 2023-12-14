package io.github.cgglyle

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BeanContainerTest {
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
            beanContainer.getBean("testClassB")
        }
    }

    @Test
    fun getBeanNewInstance() {
        //given
        val beanContainer = BeanContainer()
        val beanDefinition = BeanDefinition(TestClassA::class.java)
        beanContainer.saveBeanDefinition("testClassA", beanDefinition)

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

    @Test
    fun getBeanMustSame() {
        //given
        val beanContainer = BeanContainer()
        val beanDefinition = BeanDefinition(TestClassA::class.java)
        beanContainer.saveBeanDefinition("testClassA", beanDefinition)

        //when
        val beanA = beanContainer.getBean("testClassA")
        val beanB = beanContainer.getBean("testClassA")

        //then
        Assertions.assertEquals(beanA, beanB)
    }

    @Test
    fun getBeanPrototypeMustNotSame() {
        //given
        val beanContainer = BeanContainer()
        val beanDefinition = BeanDefinition(TestClassA::class.java, BeanScope.PROTOTYPE)
        beanContainer.saveBeanDefinition("testClassA", beanDefinition)

        //when
        val beanA = beanContainer.getBean("testClassA")
        val beanB = beanContainer.getBean("testClassA")

        //then
        Assertions.assertNotEquals(beanA, beanB)
    }

    @Test
    fun getBeanClassANameMustNotBeNull() {
        //given
        val beanContainer = BeanContainer()
        val beanDefinitionA = BeanDefinition(TestClassA::class.java)
        beanDefinitionA.setProperty("name", "testClassA")
        beanContainer.saveBeanDefinition("testClassA", beanDefinitionA)

        //when
        val beanA = beanContainer.getBean("testClassA")

        //then
        //as 关键字为强制转换的意思，等同于 (TestClasB) beanB
        Assertions.assertEquals("testClassA", (beanA as TestClassA).name)
    }

    @Test
    fun getBeanClassBPropertyClassAMustNotBeNull() {
        //given
        val beanContainer = BeanContainer()
        val beanDefinitionA = BeanDefinition(TestClassA::class.java)
        beanDefinitionA.setProperty("name", "testClassA")
        beanContainer.saveBeanDefinition("testClassA", beanDefinitionA)

        val beanDefinitionB = BeanDefinition(TestClassB::class.java)
        beanDefinitionB.setProperty("testClassA", BeanReference("testClassA"))
        beanContainer.saveBeanDefinition("testClassB", beanDefinitionB)

        //when
        val beanA = beanContainer.getBean("testClassA")
        val beanB = beanContainer.getBean("testClassB")

        //then
        //as 关键字为强制转换的意思，等同于 (TestClasB) beanB
        Assertions.assertEquals(beanA, (beanB as TestClassB).testClassA)
    }

    @Test
    fun circularDependencyTest() {
        //given
        val beanContainer = BeanContainer()

        val beanDefinitionC = BeanDefinition(TestClassC::class.java)
        beanDefinitionC.setProperty("testClassD", BeanReference("testClassD"))
        beanDefinitionC.setProperty("testClassE", BeanReference("testClassE"))
        beanContainer.saveBeanDefinition("testClassC", beanDefinitionC)

        val beanDefinitionD = BeanDefinition(TestClassD::class.java)
        beanDefinitionD.setProperty("testClassC", BeanReference("testClassC"))
        beanDefinitionD.setProperty("testClassE", BeanReference("testClassE"))
        beanContainer.saveBeanDefinition("testClassD", beanDefinitionD)

        val beanDefinitionE = BeanDefinition(TestClassE::class.java)
        beanDefinitionE.setProperty("testClassC", BeanReference("testClassC"))
        beanDefinitionE.setProperty("testClassD", BeanReference("testClassD"))
        beanContainer.saveBeanDefinition("testClassE", beanDefinitionE)

        //when
        val beanC = beanContainer.getBean("testClassC") as TestClassC
        val beanD = beanContainer.getBean("testClassD") as TestClassD
        val beanE = beanContainer.getBean("testClassE") as TestClassE

        //then
        Assertions.assertNotNull(beanC)
        Assertions.assertNotNull(beanD)
        Assertions.assertNotNull(beanE)

        Assertions.assertEquals(beanC.testClassD, beanD)
        Assertions.assertEquals(beanC.testClassE, beanE)
        Assertions.assertEquals(beanD.testClassC, beanC)
        Assertions.assertEquals(beanD.testClassE, beanE)
        Assertions.assertEquals(beanE.testClassC, beanC)
        Assertions.assertEquals(beanE.testClassD, beanD)
    }
}