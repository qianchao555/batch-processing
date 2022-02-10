package easypoiexceldemo.controller;

import easypoiexceldemo.MyExcelDownService;
import easypoiexceldemo.api.MyExcelDownApi;
import easypoiexceldemo.query.QueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MyExcelDownController
 * @Author qianchao
 * @Date 2022/2/9
 **/
@Controller
public class MyExcelDownController implements MyExcelDownApi {

    @Autowired
    MyExcelDownService myExcelDownService;

    @Override
    @GetMapping("/download")
    public void downloadExcelDemo(QueryVo queryVo, HttpServletResponse response) {
        myExcelDownService.downLoadExcel(queryVo, response);
    }



}
