package io.github.cgglyle

class BeanContainer {
    private val container: MutableMap<String, Any> = mutableMapOf()
    // 用于存储 bean 的定义
    private val beanDefinitions: MutableMap<String, BeanDefinition> = mutableMapOf()

    fun getBean(beanName: String): Any {
        // 如果 beanName 找出的实例为 null 就创建一个
        // container[beanName] 是语法糖，类似 container.get(beanName)
        // ?: 也是语法糖，a ?: b 意思就是：如果 a 为 null 就执行 : 后面的语句
        val beanDefinition = beanDefinitions[beanName] ?: throw BeanException("$beanName 的定义不存在！")
        return container[beanName] ?: doCreateBean(beanName, beanDefinition)
    }

    fun saveBean(beanName: String, bean: Any) {
        // 这里也是语法糖，等同 container.put(beanName, bean)
        container[beanName] = bean
    }

    fun saveBeanDefinition(beanName: String, beanDefinition: BeanDefinition) {
        beanDefinitions[beanName] = beanDefinition
    }

    private fun doCreateBean(beanName: String, beanDefinition: BeanDefinition): Any {
        val beanClass = beanDefinition.type
        // 反射创建
        val instance = beanClass.getDeclaredConstructor().newInstance()
        // 如果 bean 的定义是单例模式，就将创建出的 bean 加入容器
        if (beanDefinition.scope == BeanScope.SINGLETON) saveBean(beanName, instance)
        return instance
    }
}