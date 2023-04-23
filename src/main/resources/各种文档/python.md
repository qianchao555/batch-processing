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



### python基础知识

#### Python语法

> Python语法比较简单，才有缩进的方式

注意：Python程序是大小写敏感的





#### python数据类型

1. 整数
2. 浮点数
3. 字符串
4. 布尔值
5. 空值：None
6. 常量
7. 变量：变量可以是任意数据类型，同一个变量可以反复赋值，并且可以是不同数据类型



这种变量本身类型不固定的语言称为：动态语言

静态语言：在定义变量的时候必须指定变量类型，不匹配则会报错





#### list和tuple

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





#### 条件判断

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



#### 循环



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



#### dict

> 类似于Java中的map结构，使用k-v键值对存储，具有极快的查找速度

~~~python
my_dict={'Aa':95,'Bb':90}
my_dict['Aa']
~~~



#### set

> set中，没有重复的key，无序的

~~~python
my_set=set([1,2,3])
~~~





---

### 函数

> Python内置了很多函数，可以直接调用

1. abs()、max()、min()等
2. 转换函数：简单列举
   - int('123')   =>123
   - int(12.34) =>12
   - str()
   - bool()



#### 自定义函数

> python中，定义一个函数使用 def 语句，依次写出函数名、参数、冒号、缩进块、return

~~~python
def my_fun(x):
    if x>=0:
        return x
    else:
        return x
~~~



#### 空函数

> 空函数：定义一个什么事也不做的函数，通过pass语句实现

~~~python
def my_em_fun():
    pass
~~~



pass充当占位符，还可以放在其他语句里面

~~~python
if age>18:
    pass
~~~





#### 参数检查

> 调用函数时，如果**参数个数**不对，Python解释器会自动检测出来，并抛出TypeEror

但是，**如果参数类型不对，Python解释器是无法帮我们检测出来的**。所以需要对参数类型进行检查，数据类型检查可以使用内置函数：isinstance()函数实现



例如：检测x是否是整数和浮点类型的参数

~~~python
def my_abs(x):
    if not isinstance(x,(int,float)):
        raise TypeError('参数类型错误')
    if x>=0:
        return x
    else:
        return x
~~~



#### 函数返回多个值

> 实际上，函数可以返回多个值是一个假象，其实返回的是一个tuple

~~~python
def move(x,y,step):
    nx=x+step
    ny=y-step
    return nx,ny

x,y=move(100,100,10)
print(x,y)  #结果：110 90


#其实返回的是一个tuple
r=move(100,100,10)
print(r)

#结果：(110,90)
~~~





---

### 高级特性

> Python里面，代码越少越好，越简单越好，能用1行代码实现的绝不用5行代码实现



#### 切片

> 获取list、tuple的部分元素，是非常常见的操作，利用切片可以很好的完成

~~~python
my_list=['A','C','B','Dd']

#例如取前3个元素，但是不包含索引位置为3的元素
my_list[0:3]

#取倒数第一个元素：-1
my_list[-1]
~~~



#### 列表生成器

> Python内置的、用于创建list的生成式
>
> 用生成式时，注意：把生成式放在前面



例如：生成【1x1,2x2,3x3...】这样的数字

~~~~python
>>> [x*x for x in range(1,11)]
[1,4,9,16,25,36,49,64,81,100]
~~~~



还可以使用2层循环

~~~python
>>> [m + n for m in 'ABC' for n in 'XYZ']
['AX', 'AY', 'AZ', 'BX', 'BY', 'BZ', 'CX', 'CY', 'CZ']
~~~



还可以加上一定的条件

~~~python
>>> [x if x%2==0 else -x for x in range(1,11)]
[-1,2,-3,4,-5,6,-7,8,-9,10]


>>> [x for x in range(1,11) if x%2==0]
[2,4,6,8,10]
~~~



#### 生成器

> generator，Python中，一边循环一边计算的机制叫做generator

通过next()函数获得generator的下一个返回值。

如果一个函数中，定义了`yeld`关键字，那么这个函数就不在是一个普通函数，而是一个generator函数，调用一个generator函数会返回一个generator（相当于一个对象）





#### 迭代器

> Iterable，迭代器对象可以作用于集合类、generator等对象上面。和Java迭代器类似





### python函数式编程

> python对函数式编程进行了支持。由于python运行使用变量，因此，python不是纯函数式编程语言



#### 高阶函数

python高阶函数：函数本身也可以赋值给变量，即变量可以指向函数。一个函数可以接收另一个函数作为参数，这种函数就是高阶函数

~~~python
>>> f=abs
>>>f
#结果
<bulit-in function abs>

>>> f(-10)
10
~~~



#### 传入函数

~~~~python
def add(x,y,f):
    return f(x)+f(y)


#调用
print(add(5,-6,abs))

#结果
11
~~~~



#### map/reduce

map()函数：接收两个参数，一个是函数，一个是Iterable，map将传入的函数依次作用于序列的每一个元素，并把结果作为新的Iterator返回

~~~python
def f(x):
    return x*x;


myList=[1,2,3,4,5]
r=map(f,myList)
list(r)

#结果:将列表内每个元素实现x*x
1,4,9,16,25
~~~



reduce()函数：接收两个参数，把结果继续和序列的下一个元素做操作



#### filter过滤序列

> 和map()类似，接收两个参数，一个函数和一个序列，然后函数作用于每一个元素，然后根据返回值是ture或false丢弃该元素



#### sorted排序

> 使用再看





---

### 模块

> 在Python中，一个.py文件就被称为一个模块(Module)



模块的好处？

1. 最大的好处是：大大提高了代码的可维护性
2. 其次，编码不必从零开始，一个模块编写完毕后，其他地方可以引用



通常，编写程序的时候，会经常引用其他模块，包括Python内置的模块和第三方模块

每一个包目录下面都会有一个_init _.py的文件，这个文件是必须存在的，否则Python会把这个目录当成普通目录而不是一个包



#### 使用模块

> 使用python模块时候，只需要安装即可，类似于Java的import导入包

~~~python

import sys

def test():
    xxx
~~~





#### 安装第三方模块

> Python中，安装第三方模块是通过包管理工具：pip完成的

一般来说，第三方库都会在python官方的pypi.python.org网站上注册，要安装一个第三方库，必须知道该库的名称

~~~python
pip install xxx
~~~



当然，还可以通过Anaconda等工具进行安装

---



### 面向对象编程

~~~python
class Student(object):
    #是两根下划线，类似于Java构造函数
    def __init__(self,name,score):
        self.name=name
        self.score=score
        
    def print_student(self):
        print('%s: %s' % (self.name,self.score))
        

        
        
####其他地方
aStudent=Student('qc',66)
bStudent=Student('yhc',77)
aStudent.print_student()
bStudent.print_student()
~~~



#### 访问限制

如果想要内部属性不能别外部访问，需要在属性的前面加两个下划线，这边会变成一个私有变量，类似Java的private
