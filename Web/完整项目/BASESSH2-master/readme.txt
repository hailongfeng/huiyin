通用dao、service接口，hibernate版本。经典之作

疑点一：通用dao怎么实现自动注入
答：因为是泛型实现，所以注入时候，泛型会找到真正的类型实例，根据类型实例去查找对应的 response对象。