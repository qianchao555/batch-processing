@Order定义Spring容器加载Bean的顺序

1. order的值越小，优先级越高
2. order如果不标注数字，默认最低优先级，因为其默认值是int最大值
3. order可以作用在类、方法、属性。影响加载顺序，若不加，spring的加载顺序是随机的

@Priority与order类似，也是值越小优先级越高，但是priority能够控制组件的加载顺序，因此它侧重于单个注入的优先级

两者同时存在时，优先加载priority
