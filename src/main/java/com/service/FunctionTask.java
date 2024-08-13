package com.service;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.util.Date;


@Data
@FieldNameConstants
@TableName("function_task")
public class FunctionTask {

    private static final long serialVersionUID = 1L;

    /**
     * sequence
     */
    @TableField("sequence")
    private String sequence;

    /**
     * 基表名称
     */
    @TableField("base_table")
    private String baseTable;

    /**
     * 宽表名称
     */
    @TableField("wide_table")
    private String wideTable;

    /**
     * 存储过程名称
     */
    @TableField("function_name")
    private String functionName;

    /**
     * 数据开始时间
     */
    @TableField("start_time")
    private BigDecimal startTime;

    /**
     * 数据结束时间
     */
    @TableField("end_time")
    private BigDecimal endTime;

    /**
     * 是否能被操作,生成标识（Y/N）
     */
    @TableField("is_operation")
    private String isOperation;

    /**
     * 调用成功标识（Y/N）（ 成功/失败）
     */
    @TableField("status")
    private String status;

    /**
     * 操作人
     */
    @TableField("created_by")
    private String created_by;

    /**
     * 修改人
     */
    @TableField("Modified_by")
    private String Modified_by;

    /**
     * 操作时间
     */
    @TableField("operation_time")
    private Date operationTime;

    /**
     * 时间间隔
     */
    @TableField("time_interval")
    private BigDecimal timeInterval;

    /**
     * 单个function task 的表名
     */
    private String tableName;
}