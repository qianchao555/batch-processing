package com.boot.dbunit.excel;

import org.dbunit.dataset.*;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

/**
 * @ClassName XlsTableWrappper
 * @Author qianchao
 * @Date 2022/9/6
 * @Version  V1.0
 **/
public class XlsTableWrappper extends AbstractTable {
    private ITable delegate;
    private String tableName;

    public XlsTableWrappper(String tableName, ITable table) {
        this.delegate=table;
        this.tableName=tableName;
    }

    @Override
    public ITableMetaData getTableMetaData() {
        ITableMetaData metaData = delegate.getTableMetaData();
        try {
            return
                    new DefaultTableMetaData(tableName,metaData.getColumns(),metaData.getPrimaryKeys());
        } catch (DataSetException e) {
            throw new RuntimeException("没有获取到元数据信息："+metaData,e);
        }
    }

    @Override
    public int getRowCount() {
        return delegate.getRowCount();
    }

    @Override
    public Object getValue(int row, String column) throws DataSetException {
        Object value = delegate.getValue(row,column);
        if(value instanceof String){
            if(StringUtils.isEmpty((String) value)){
                return null;
            }
        }
        return value;
    }
}
