package com.boot.dbunit.operation;

import lombok.extern.slf4j.Slf4j;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.operation.AbstractBatchOperation;
import org.dbunit.operation.AbstractOperation;
import org.dbunit.operation.DatabaseOperation;

import java.sql.SQLException;

/**
 * @ClassName MyTestDeleteOperation
 * @Author qianchao
 * @Date 2022/9/6
 * @Version  V1.0
 **/
@Slf4j
public class MyTestDeleteOperation extends AbstractOperation {

    @Override
    public void execute(IDatabaseConnection connection, IDataSet dataSet) throws DatabaseUnitException, SQLException {
        log.info("执行(connection={},dataSet)开始",connection);
        //遍历表
        ITableIterator iterator = dataSet.iterator();
        while (iterator.next()){
            ITable table = iterator.getTable();
            //不能处理空的表
//            if(isEmpty(table)){
//                continue;
//            }
        }
    }


}
