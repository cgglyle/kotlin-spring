package io.github.cgglyle

import java.io.File

class ComponentScanProcessor {
    fun scan(path: String): List<BeanDefinition> {
        val classList = scanClassPath(path)
        return classList.map { doBeanDefinitionParse(it) }
    }

    private fun scanClassPath(path: String): List<Class<*>> {
        // io.github.cgglyle -> io/github/cgglyle
        val filePath = path.replace(".", "/")
        // 从 ClassLoader 中取出资源，因为在系统启动时会配置 ClassPath，这样我们就可以取出绝对路径
        val resource = this.javaClass.classLoader.getResource(filePath)
        resource ?: throw ScanException("扫描 '$path' 不存在！")

        val file = File(resource.file)
        val scanFilePath = scanFilePath(file, mutableListOf())
        // 通过获取 “” 的资源可以获得类路径
        val absoluteFilePath =
            this.javaClass.classLoader.getResource("") ?: throw ScanException("扫描 ClassPath 绝对路径失败")
        val classList = scanFilePath.map {
            val classPath = it.substring(absoluteFilePath.path.length)
                .replace("/", ".")
                .substringBeforeLast(".class")
            Class.forName(classPath)
        }.filter { isBean(it) }
        return classList
    }

    private fun scanFilePath(file: File, pathList: MutableList<String>): List<String> {
        // 如果是目录就继续处理，如果是文件就直接假如列表
        if (file.isDirectory) {
            val listFiles = file.listFiles()
            // 语法糖，如果 listFiles 不为 null 执行 let 后面的语句，否则跳过
            listFiles?.let {
                for (f in listFiles) {
                    if (f.isFile) {
                        pathList.add(f.path)
                    } else if (f.isDirectory) {
                        // 如果是目录就递归扫描
                        scanFilePath(f, pathList)
                    }
                }
            }
        } else if (file.isFile) {
            pathList.add(file.path)
        }
        return pathList.toList()
    }

    private fun isBean(clazz: Class<*>): Boolean {
        val componentAnnotation = clazz.getDeclaredAnnotation(Component::class.java)
        return componentAnnotation != null
    }

    private fun doBeanDefinitionParse(clazz: Class<*>): BeanDefinition {
        return BeanDefinition(clazz)
    }
}