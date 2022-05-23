package com.easypoiexceldemo;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Http工具类
 * @ClassName HttpUtils
 * @Author qianchao
 * @Date 2022/2/9
 **/
public class HttpUtils {
    private static final String UTF_8 = "UTF-8";

    private HttpUtils() {
    }

    /**
     * 设置response相关信息
     * @param response
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static HttpServletResponse createResponseHeader(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        //设置content-disposition响应头控制浏览器以下载的形式打开文件,中文文件名使用URLEncoder.encode方法进行编码，否则会出现文件名乱码
        //attachment作为Content-Disposition参数，作为附件下载
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, UTF_8));
        //没有缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");

        //设置过期的时间期限
        response.setDateHeader("Expires", 0L);

        //导出到Excel
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        return response;
    }

}
