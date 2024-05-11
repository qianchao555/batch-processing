package com.redis;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 银行信息表
 * </p>
 *
 * @author Author
 */
@Data
@TableName("NIFA_BANK_INFO")
public class BankInfo {

    private static final long serialVersionUID = 1L;

    private String nameOfBank;

    private String cnapsCode;

    private String confirmationType;

   

}
