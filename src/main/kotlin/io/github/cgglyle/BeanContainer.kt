package io.github.cgglyle

import kotlin.text.StringBuilder

class BeanContainer {
    private val container: MutableMap<String, Any> = mutableMapOf()

    // 用于存储 bean 的定义
    private val beanDefinitions: MutableMap<String, BeanDefinition> = mutableMapOf()
    // 早期未实例化的 Bean 容器
    private val earlyBeanContainer: MutableMap<String, Any> = mutableMapOf()
    // 循环依赖标记
    private val earlyMarks: MutableList<String> = mutableListOf()

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
        // 注入依赖
        // 注入前添加标记
        earlyMarks.add(beanName)
        doInjectProperty(beanDefinition, instance, beanClass)
        // 注入后删除标记
        earlyMarks.remove(beanName)
        // 如果 bean 的定义是单例模式，就将创建出的 bean 加入容器
        if (beanDefinition.scope == BeanScope.SINGLETON) saveBean(beanName, instance)
        return instance
    }

    private fun doInjectProperty(beanDefinition: BeanDefinition, bean: Any, beanClass: Class<*>) {
        // 循环依赖
        beanDefinition.propertyValues.forEach {
            // 使用反射找到与依赖名称相同的方法
            val declaredField = beanClass.getDeclaredField(it.name)
            // 如果方法不能访问，就设置访问
            if (!declaredField.canAccess(bean)) {
                declaredField.trySetAccessible()
            }
            // is 关键字等同于 instanceof, 而且 is 关键字会自动将在上下文中将 value 转换为 BeanReference，不用显式转换。
            if (it.value is BeanReference) {
                // 当标记中已经存在正要注入的名字，就是循环依赖
                if (earlyMarks.contains(it.name)) {
                    val stringBuilder = StringBuilder()
                    val firstBeanName = earlyMarks[0]
                    earlyMarks.forEach{
                        stringBuilder.append(it).append(" -> ")
                    }
                    stringBuilder.append(firstBeanName)
                    throw BeanException("发生循环依赖！$stringBuilder")
                }
                // 通过 getBean 获得 Bean
                declaredField.set(bean, getBean(it.value.beanName))
            } else {
                // 注入依赖
                declaredField.set(bean, it.value)
            }
        }
    }
}