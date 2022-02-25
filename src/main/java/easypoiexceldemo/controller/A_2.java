package easypoiexceldemo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import easypoiexceldemo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/2/10 20:53
 * @version:1.0
 */
@Controller
@Slf4j
public class A_2 {


    @GetMapping("export.do")
    public void export(HttpServletResponse response) {
        try {
            // 设置响应输出的头类型
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=user.xls");

            ExportParams exportParams = new ExportParams();
            // exportParams.setDataHanlder(null);//和导入一样可以设置一个handler来处理特殊数据
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, data());
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /***
     * 创建模拟数据
     *
     * @return
     */
    private List<User> data() {
        List<User> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            list.add(new User("id-" + i, "Leftso-" + i, 15 + i, new Date()));
        }
        return list;
    }
}
