package easypoiexceldemo.controller;

import easypoiexceldemo.MyExcelDownService;
import easypoiexceldemo.api.MyExcelDownApi;
import easypoiexceldemo.query.QueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName MyExcelDownController
 * @Author qianchao
 * @Date 2022/2/9
 **/
@RestController
public class MyExcelDownController implements MyExcelDownApi {

    @Autowired
    MyExcelDownService myExcelDownService;

    @Override
    public void downloadExcelDemo(QueryVo queryVo, HttpServletResponse response) {
        myExcelDownService.downLoadExcel(queryVo, response);
    }



}
