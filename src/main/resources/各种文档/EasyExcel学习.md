## EasyExcel学习



### 读Excel

ReadWorkbook对象：理解成一个excel文件

ReadSheet对象：理解成excel里面的一个sheet表单



### 写Excel

WriteWorkbook对象：理解成一个excel文件

WriteSheet对象：理解成excel里面的一个sheet表单

WriteTable对象：一个sheet表单里面如果有多个实际的表格，则可以用WriteTable

~~~Java
EasyExcel.write(filename,XXX.class)
    //write方法之后，sheet方法之前都是设置WriteWorkbook的相关参数
    
    .sheet("模板")
    //sheet方法之后，doWrite方法之前，都是设置WriteSheet的相关参数
    
    .table()
    //table方法之后，dowrite方法之前，都是设置WriteTable相关参数
    
    .doWrite(
		()->{
            //模拟：分页查询数据
            return data;
        }
	);
~~~







### 填充Excel

参考写入excel即可