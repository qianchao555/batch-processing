package com.boot.listener;

import com.boot.annotation.TestDataSet;
import com.boot.dbunit.excel.XlsTableWrappper;
import com.boot.dbunit.operation.MyTestDeleteOperation;
import com.boot.dbunit.operation.MyTestRefreshOperation;
import com.boot.utils.AnnotationUtil;
import com.boot.utils.ResourceLocalProcessUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Constants;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 数据集监听器
 * 
 * @Author qianchao
 * @Date 2022/9/6
 * @Version   V1.0
 **/
@Slf4j
public class DataSetExecutionListener extends AbstractTestExecutionListener {

    private static final String XLS = "xls";
    private static final Constants DATABASE_OPERATION = new Constants(DatabaseOperation.class);

    Map<Method, List<DataSetConfig>> datesetConfigCache = new HashMap<>();
    Pattern pattern = Pattern.compile("\\.");

    @Data
    /**
     * 数据集配置类
     */
    static class DataSetConfig {
        //数据集位置
        private String local;
        private String dataSourceName;
        //是否支持事务
        private boolean transactional;
        //开始时候执行的操作
        private String setupOperation;
        //结束时候执行的操作
        private String teardownOperation;
        //记录表名
        private List<String> tableNames = new ArrayList<>();
        //IDatabaseTester
        private IDatabaseTester databaseTester;

        public DataSetConfig(DefaultDatabaseTester databaseTester, boolean transactional) {
            this.databaseTester = databaseTester;
            this.transactional = transactional;
        }
    }

    /**
     * 执行test方法前
     * 这里完成的相关操作
     *
     */
    @Override
    public void beforeTestMethod(@NotNull TestContext testContext) throws Exception {
        List<DataSetConfig> datasetConfigs = getDatasetConfig(testContext);
        if (CollectionUtils.isEmpty(datasetConfigs)) {
            return;
        }
        datesetConfigCache.put(testContext.getTestMethod(), datasetConfigs);

        //启动databaseTester
        //从类路径下指定操作、数据源，加载数据集
        for (DataSetConfig datasetConfig : datasetConfigs) {
            log.info("加载数据集从类路径：{},使用操作：{},采用数据源：{}", datasetConfig.getLocal(),
                    datasetConfig.getSetupOperation(), datasetConfig.getDataSourceName());
            //启动databaseTester
            datasetConfig.databaseTester.onSetup();
        }
    }

    /**
     * 添加数据集配置
     *
     * @param testContext
     * @return
     */
    private List<DataSetConfig> getDatasetConfig(TestContext testContext) throws IOException, DatabaseUnitException, SQLException {
        //XXXTest
        Class<?> testClass = testContext.getTestInstance().getClass();
        TestDataSet annotation = findAnnotation(testClass, testContext.getTestMethod());
        if (annotation == null) {
            return null;
        }
        //确认数据集路径
        String[] datasetLocals = confirmDatasetLocal(testContext, annotation);
        //确认dataSourceName
        String[] dataSourceNames = confirmDataSourceName(testContext, annotation);
        if (dataSourceNames.length > 1 && datasetLocals.length != dataSourceNames.length) {
            log.error("dataSource个数：{}，不匹配数据集个数{}", dataSourceNames.length, datasetLocals.length);
        }
        //存放datasetConfigs
        List<DataSetConfig> dataSetConfigs = new LinkedList<>();
        for (int i = 0; i < datasetLocals.length; i++) {
            String datasetLocal = datasetLocals[i];
            String fileType = datasetLocal.substring(datasetLocal.lastIndexOf("." )+ 1);
            String dataSourceName = dataSourceNames.length == 1 ? dataSourceNames[0] : dataSourceNames[i];
            //开始构建数据集
            ReplacementDataSet dataSet;
            //其他类型的数据集，添加if判断即可
            if ("xls".equalsIgnoreCase(fileType)) {
                XlsDataSet xlsDataSet = new XlsDataSet(
                        new DefaultResourceLoader().getResource(datasetLocal).getInputStream());
                //sheet名就是对应的表名
                String[] sheetNames = xlsDataSet.getTableNames();
//                for (String sheetName : sheetNames) {
//                    String[] tmp = pattern.split(sheetName);
//                    String tableName=sheetName;
//                }
                int sheetCounts = sheetNames.length;
                //一个sheet表，使用一个数据集
                ITable[] tables = new ITable[sheetCounts];
                for (int j = 0; j <=sheetCounts - 1; j++) {
                    tables[j] = new XlsTableWrappper(sheetNames[j], xlsDataSet.getTable(sheetNames[j]));
                }
                dataSet = new ReplacementDataSet(new DefaultDataSet(tables));

                buildDatabaseConfig(testContext, annotation, dataSetConfigs, datasetLocal, dataSourceName, dataSet);
            }
        }
        return dataSetConfigs;
    }

    private void buildDatabaseConfig(TestContext testContext, TestDataSet annotation, List<DataSetConfig> dataSetConfigs, String datasetLocal, String dataSourceName, ReplacementDataSet dataSet) throws SQLException, DatabaseUnitException {
        DataSource dataSource = (DataSource) testContext.getApplicationContext().getBean(dataSourceName);
        Connection connection = DataSourceUtils.getConnection(dataSource);
        //构建databaseTester
        DatabaseConnection databaseConnection = getDatabaseConnection(dataSource, connection);
        String dbType=connection.getMetaData().getDatabaseProductName();
        //可有可无暂时没有
        DatabaseConfig config = databaseConnection.getConfig();

        //databaseTester
        DefaultDatabaseTester databaseTester = new DefaultDatabaseTester(databaseConnection);
        databaseTester.setDataSet(dataSet);
        String setup = annotation.setupOperation();
        //refresh、delete操作自己去实现,不用dbunit自己实现的
        DatabaseOperation setupOperation = "REFRESH".equals(setup) ? new MyTestRefreshOperation()
                : (DatabaseOperation) DATABASE_OPERATION.asObject(setup);
        databaseTester.setSetUpOperation(setupOperation);
        String teardown = annotation.teardownOperation();
//        DatabaseOperation teardownOperation = "DELETE".equals(setup) ? new MyTestDeleteOperation()
//                : (DatabaseOperation) DATABASE_OPERATION.asObject(teardownOperation);
        //构建databaseTester结束

        //判断是否需要事务
        boolean transactional = DataSourceUtils.isConnectionTransactional(connection, dataSource);
        DataSetConfig dataSetConfig = new DataSetConfig(databaseTester, transactional);
        dataSetConfig.setLocal(datasetLocal);
        dataSetConfig.setDataSourceName(dataSourceName);
        dataSetConfig.setSetupOperation(annotation.setupOperation());
        dataSetConfig.setTeardownOperation(annotation.teardownOperation());
        dataSetConfigs.add(dataSetConfig);

        //记录一下 已经准备过数据的表名
        for (String tableName : dataSet.getTableNames()) {
            dataSetConfig.getTableNames().add(tableName);
        }
    }

    private DatabaseConnection getDatabaseConnection(DataSource dataSource, Connection connection) throws SQLException, DatabaseUnitException {
        String schemaName = getSchemaName(dataSource, connection);
        if (schemaName != null) {
            return new DatabaseConnection(connection, schemaName) {
                @Override
                public void close() throws SQLException {
//                    super.close();
//                    DataSourceUtils.releaseConnection(connection,dataSource);
                }
            };
        } else {
            return new DatabaseConnection(connection) {
                @Override
                public void close() throws SQLException {
//                    super.close();
//                    DataSourceUtils.releaseConnection(connection,dataSource);
                }
            };
        }
    }

    private String getSchemaName(DataSource dataSource, Connection connection) throws SQLException {
        if (connection.getMetaData().getDriverName().contains("Oracle")) {
            return "SYSTEM";
        }
        if (connection.getMetaData().getDriverName().contains("MySQL")) {
            return "XXX";
        }
        return "PUBLIC";
    }

    private String[] confirmDataSourceName(TestContext testContext, TestDataSet annotation) {
        return annotation.dataSourceName();
    }

    private String[] confirmDatasetLocal(TestContext testContext, TestDataSet annotation) {
        Class<?> testClass = testContext.getTestInstance().getClass();
        String fileType = annotation.fileType();
        String[] locals = annotation.locals();
        //把local处理完标准的路径
        if (ArrayUtils.isEmpty(locals)) {
            //稍后处理
        } else {
            locals = ResourceLocalProcessUtil.modifyLocal(testClass, locals);
        }
        return locals;
    }

    /**
     * 根据类型、Method,查找标注TestDataSet注解的地方
     *
     * @param testClass
     * @param testMethod
     */
    private TestDataSet findAnnotation(Class<?> testClass, Method testMethod) {
        TestDataSet annotation = AnnotationUtils.findAnnotation(testMethod, TestDataSet.class);
        if (annotation == null) {
            //自定义Utils工具  在类上面查找这个注解
            annotation = AnnotationUtil.findAnnotationByClass(testClass, TestDataSet.class);
            return annotation;
        }
        return annotation;
    }


    /**
     * test方法执行完成后的后续操作
     * @param testContext
     * @throws Exception
     */
    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        //获取对应test方法的配置集
        List<DataSetConfig> dataSetConfigs = datesetConfigCache.get(testContext.getTestMethod());
        if(CollectionUtils.isEmpty(dataSetConfigs)){
            return;
        }

        for (DataSetConfig dataSetConfig : dataSetConfigs) {
            log.info("tearing down操作，采用：{}操作方式",dataSetConfig.getTeardownOperation());
            try {
                dataSetConfig.getDatabaseTester().onTearDown();
            } catch (Exception e) {
                throw e;
            }finally {
                //非事务
                if(!dataSetConfig.isTransactional()){
                    //关闭连接
                    dataSetConfig.getDatabaseTester().getConnection().close();
                }
            }
        }
        //移除该test方法的配置
        datesetConfigCache.remove(testContext.getTestMethod());
    }
}
