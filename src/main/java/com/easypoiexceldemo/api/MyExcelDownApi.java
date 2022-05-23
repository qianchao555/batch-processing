
package com.easypoiexceldemo.api;


import org.springframework.web.bind.annotation.*;
import com.easypoiexceldemo.query.QueryVo;

import javax.servlet.http.HttpServletResponse;


/**
 *
 * 打包zip实现思路
 * 1. 先在工作路径合适的位置搞一个临时文件夹
 * 2. 把所有组装好的数据扔进去
 * 3. 打包这个临时文件夹，并把zip文件也放在临时文件夹下面
 * 4. 压缩包输出流，下载压缩包
 * 5. 删除临时文件夹及里面所有的东西
 *
 */

/**
 * 后期加上feign客户端
 */
//Api
//@FeignClient("")
public interface MyExcelDownApi {

    /**
     * 下载功能，easypoi导出为excel,单个excel下载,多个excel打包成zip后下载
     * 采用HttpServletResponse方式设置相应的响应头，利用OutputStream流方式，控制浏览器下载文件
     *
     * @param queryVo  查询数据的条件
     * @param response
     */
    @GetMapping("/download")
    void downloadExcelDemo(QueryVo queryVo, HttpServletResponse response);

}
