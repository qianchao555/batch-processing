## Python

Python提供了非常完善的基础代码库，覆盖率网络、文件、GUI、数据库、文本等大量内容，用Python进行开发，许多功能不必从零编写，直接使用现成的即可



Python适用哪些类型的应用？

> 1. 网络应用、网站、后台服务等等
> 2. 日常所需的小工具、脚本任务等



Python是跨平台的，指的是xxx.py可以在各个平台上执行



Python解释器：负责运行Python程序

1. CPython：默认官方解释器
2. IPython
3. PyPy
4. Jython：运行在Java平台上的Python解释器，可以把Python代码编译成Java字节码执行



### Python语法

> Python语法比较简单，才有缩进的方式

注意：Python程序是大小写敏感的





### python数据类型

1. 整数
2. 浮点数
3. 字符串
4. 布尔值
5. 空值：None
6. 常量
7. 变量：变量可以是任意数据类型，同一个变量可以反复赋值，并且可以是不同数据类型



这种变量本身类型不固定的语言称为：动态语言

静态语言：在定义变量的时候必须指定变量类型，不匹配则会报错





### list和tuple

> Python内置的数据类型
>
> 列表list
>
> 有序列表：元组tuple，与list类似，但是初始化后就不能修改



~~~python
#list列表用 []表示
my_list=['Aa','Bb','Cc']
my_list[0]
my_list[1]
~~~



~~~python
#tuple元组 用()表示
my_tuple=('Aa','Bb','Cc',['listA','listB'])
~~~





### 条件判断

> 注意，不要少写 冒号

~~~python
age=20
if age>2:
    print('你的年龄是：'，age)
else:
    print('你的年龄是：'，age)
~~~



~~~python
age=3
if age>3:
    print('')
elif age>5:
    print('')
else:
    print('')
~~~



### 循环



~~~python
names=['Aa','Bb','Cc']
for name in names:
    print(name)
~~~



~~~python
sum =0
n=100
while n>0:
    if n<10:
        break
    sum=sum+n
    n=n-1
print(sum)
~~~























