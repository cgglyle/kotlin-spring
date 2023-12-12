package io.github.cgglyle

class BeanContainer {
    private val container: MutableMap<String, Any> = mutableMapOf()

    fun getBean(beanName: String): Any {
        // 如果 beanName 找出的实例为 null 就直接返回异常
        // container[beanName] 是语法糖，类似 container.get(beanName)
        // ?: 也是语法糖，a ?: b 意思就是：如果 a 为 null 就执行 : 后面的语句
        return container[beanName] ?: throw BeanException("$beanName 不存在!")
    }

    fun saveBean(beanName: String, bean: Any) {
        // 这里也是语法糖，等同 container.put(beanName, bean)
        container[beanName] = bean
    }
}