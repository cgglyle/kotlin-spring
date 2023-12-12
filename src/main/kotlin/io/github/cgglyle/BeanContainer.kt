package io.github.cgglyle

class BeanContainer {
    private val container: MutableMap<String, Any> = mutableMapOf()
    // 用于存储 bean 的定义，也就是 Class 信息
    private val beanDefinitions: MutableMap<String, Class<*>> = mutableMapOf()

    fun getBean(beanName: String): Any {
        // 如果 beanName 找出的实例为 null 就创建一个
        // container[beanName] 是语法糖，类似 container.get(beanName)
        // ?: 也是语法糖，a ?: b 意思就是：如果 a 为 null 就执行 : 后面的语句
        return container[beanName] ?: doCreateBean(beanName)
    }

    fun saveBean(beanName: String, bean: Any) {
        // 这里也是语法糖，等同 container.put(beanName, bean)
        container[beanName] = bean
    }

    fun saveBeanDefinition(beanName: String, beanClass: Class<*>) {
        beanDefinitions[beanName] = beanClass
    }

    private fun doCreateBean(beanName: String): Any {
        val beanClass = beanDefinitions[beanName] ?: throw BeanException("$beanName 的定义不存在！")
        // 反射创建
        return beanClass.getDeclaredConstructor().newInstance()
    }
}