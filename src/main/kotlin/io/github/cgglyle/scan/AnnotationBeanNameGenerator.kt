package io.github.cgglyle.scan

import io.github.cgglyle.bean.BeanDefinition

class AnnotationBeanNameGenerator {
    fun generatorBeanName(beanDefinition: BeanDefinition): String {
        val type = beanDefinition.type
        val simpleName = type.simpleName
        return uncapitalizeAsProperty(simpleName)
    }

    fun uncapitalizeAsProperty(string: String): String {
        // 如果不为空，且第一个首字母和第二个字母为大写就直接返回。例如：DNS
        if (string.isNotBlank() && string.first().isUpperCase() && string.toCharArray()[1].isUpperCase()) {
            return string
        }
        return changeFirstCharacterCase(string, false)
    }

    /**
     * 通过控制 [capitalize] 来决定 [string] 的首字母大写或者小写
     */
    fun changeFirstCharacterCase(string: String, capitalize: Boolean): String{
        if (string.isBlank()) {
            return string
        }
        val baseChar = string.toCharArray()[0]
        val updateChar = if (capitalize) {
            baseChar.uppercaseChar()
        } else {
            baseChar.lowercaseChar()
        }
        if (baseChar == updateChar) {
            return string
        }
        val chars = string.toCharArray()
        chars[0] = updateChar
        return String(chars)
    }
}