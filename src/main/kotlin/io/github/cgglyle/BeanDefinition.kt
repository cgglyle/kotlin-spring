package io.github.cgglyle

class BeanDefinition(
    val type: Class<*>,
    // 提供默认值
    val scope: BeanScope = BeanScope.SINGLETON,
){
    val propertyValues: MutableList<PropertyValue> = mutableListOf()
    fun setProperty(name: String, value: Any) {
        propertyValues.add(PropertyValue(name, value))
    }
}
