package io.github.cgglyle

import io.github.cgglyle.bean.BeanScope

@Retention(AnnotationRetention.RUNTIME)
// 这里和 java 不同，使用这种方式限制只能标记在类上
@Target(AnnotationTarget.CLASS)
annotation class Component(
    val scope: BeanScope = BeanScope.SINGLETON
)
