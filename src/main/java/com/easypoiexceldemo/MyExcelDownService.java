package com.easypoiexceldemo;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.easypoiexceldemo.query.QueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName MyExcelDownService
 * @Author qianchao
 * @Date 2022/2/9
 **/
@Service
@Slf4j
public class MyExcelDownService {
    //学生名字
    private static final String CREATED_BY_ID = "createdById";
    //模板路径url
    private static final String TEMPLATES_URL = "templates/myExcelTemplate.xlsx";
    //临时目录路径
    private static final String TEMP_PATH = "tempMyExcelDownLoad";
    //临时文件路径
    private static final String TEMP_FILE_PATH = "tempMyExcelDownLoad/files";


    /**
     * @param queryVO  查询条件
     * @param response
     */
    public void downLoadExcel(QueryVo queryVO, HttpServletResponse response) {
        //定义map对象，所有在模板中使用的数据都会封装到该map中。
        // 查询需要写入excel模板表格的数据
        Map<String, List<Student>> dataMap = findTemplateData(queryVO);
        if (CollectionUtils.isEmpty(dataMap)) {
            return;
        }
        //easypoi的导入模板参数类
        TemplateExportParams exportParams = new TemplateExportParams(TEMPLATES_URL);
        //根据查询到的数据，生成excel模板所需的数据。类似于list里面包含多张excel模板，一个map对应一个excel
        List<Map<String, Object>> listMap = createTemplateParamMaps(dataMap);

        //单个Id下载,查询的时候根据具体的id查询的数据
        if (listMap.size() == 1) {
            //下载单个excel文件
            downloadExcelSingle(exportParams, response, listMap.get(0));
            return;
        }
        //多个excel生成zip的情况
        downloadExcelZip(listMap, exportParams, response);
    }


    /**
     * 分组查询 数据
     *
     * @param queryVO 查询条件
     * @return 查询到的分组数据
     */
    public Map<String, List<Student>> findTemplateData(QueryVo queryVO) {
//        List<Student> list = findDb(queryVO);查询数据库语句

        //模拟数据
        List<Student> list = new ArrayList<>();
        Student student = new Student();
        student.setCreatedById("qqq");
        student.setAshuxue(80);
        student.setByuwen(100);
        student.setBshuxue(80);
        Student student2 = new Student();
        student2.setCreatedById("qqcc");
        student2.setAshuxue(10);
        student2.setByuwen(100);
        student2.setBshuxue(180);
        list.add(student);
        list.add(student2);
        //通过学生分组
        return list.stream().collect(Collectors.groupingBy(Student::getCreatedById));
    }

    /**
     * 下载单个excel文件
     *
     * @param exportParams 模板类
     * @param response
     * @param mapData      查询到的数据
     */
    private void downloadExcelSingle(TemplateExportParams exportParams, HttpServletResponse response, Map<String, Object> mapData) {
        //将模板数据exportParmas与实际数据mapFirst整合
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, mapData);
        try {
            //生成响应头，文件名以created_by_id字段拼接命名
            HttpUtils.createResponseHeader(response, "测试单个excel下载-" + mapData.get(CREATED_BY_ID).toString() + ".xlsx");
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(response.getOutputStream());
            //Workbookr对象的流直接输出到response的out对象中
            workbook.write(bufferedOutput);
            bufferedOutput.flush();
            bufferedOutput.close();
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 生成excel模板所需的map数据
     *
     * @param map 查询到的学生分组数据
     * @return List<Map < String, Object>>   多张excel表
     */
    private List<Map<String, Object>> createTemplateParamMaps(Map<String, List<Student>> map) {
        //每一张excel模板, 等价于：list里面有多个excel就是一个模板
        List<Map<String, Object>> list = new ArrayList<>();
        //每一个用户会生成一个excel
        for (Map.Entry<String, List<Student>> entry : map.entrySet()) {
            //创建存放模板数据的对象。一个map就是一张excel
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put(CREATED_BY_ID, entry.getKey());
            //listTables有规律的表格
            List<Student> listTables = entry.getValue();

            //定义的模板字段
            List<StudentDto> dtos = new ArrayList<>();

            //将查询到的数据，赋值给excel模板表格, 设置excel模板每一行数据
            for (int i = 0; i < listTables.size(); i++) {
                Student row = listTables.get(i);
                StudentDto dto = listTables2Dto(row);
                dto.setOderNum(i + 1 + "");
                dtos.add(dto);
            }
            //“list"对应excel模板上的list   例如：{{$fe: list t.oderNum}}
            paramsMap.put("list", dtos);

            //模板上有规律表格下方的表格行   需要求和的行
            StudentSumDto sumDTO = new StudentSumDto();
            //求和字段赋值
            setSum(dtos, sumDTO);
            //赋值给模板map
            putSum(paramsMap, sumDTO);
            //添加map,相当于添加一张excel表了
            list.add(paramsMap);
        }
        return list;
    }

    /**
     * excel模板上的字段赋值
     *
     * @param row 每一行
     * @return
     */
    private StudentDto listTables2Dto(Student row) {
        StudentDto dto = new StudentDto();
        dto.setAyuwen(row.getAyuwen() == null ? 0 : row.getAyuwen());
        dto.setAshuxue(row.getAshuxue() == null ? 0 : row.getAshuxue());
        dto.setByuwen(row.getByuwen() == null ? 0 : row.getByuwen());
        dto.setBshuxue(row.getBshuxue() == null ? 0 : row.getBshuxue());
        return dto;
    }

    /**
     * excel模板 对应map上的求和数据
     *
     * @param paramsMap
     * @param sumDTO
     */
    private void putSum(Map<String, Object> paramsMap, StudentSumDto sumDTO) {
        paramsMap.put("ayuwenSum", sumDTO.getAyuwenSum());
        paramsMap.put("ashuxueSum", sumDTO.getAshuxueSum());
        paramsMap.put("byuwenSum", sumDTO.getByuwenSum());
        paramsMap.put("bshuxueSum", sumDTO.getBshuxueSum());
    }

    /**
     * 字段求和
     *
     * @param dtos
     * @param sumDTO
     */
    private void setSum(List<StudentDto> dtos, StudentSumDto sumDTO) {
        for (StudentDto studentDto : dtos) {
            sumDTO.setAyuwenSum(
                    sumDTO.getAyuwenSum() + studentDto.getAyuwen());
            sumDTO.setAshuxueSum(
                    sumDTO.getAshuxueSum() + studentDto.getAshuxue());
        }
    }


    /**
     * 多个excel文件，打包成一个zip并下载
     * 打包zip实现思路
     * 1. 先在工作路径合适的位置搞一个临时文件夹
     * 2. 把所有组装好的数据扔进去
     * 3. 打包这个临时文件夹，并把zip文件也放在临时文件夹下面
     * 4. 压缩包输出流，下载压缩包
     * 5. 删除临时文件夹及里面所有的东西
     *
     * @param listMap
     * @param exportParams
     * @param response
     */
    private void downloadExcelZip(List<Map<String, Object>> listMap, TemplateExportParams exportParams, HttpServletResponse response) {
        //删除临时文件夹及里面的所有文件
        FileUtil.deleteDirectory(TEMP_PATH);
        //临时文件目录
        File file = new File(TEMP_FILE_PATH);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                log.error("文件目录路径创建出错！");
            }
        }
        //在临时文件目录内，通过模板生成excel
        for (Map<String, Object> dataMap : listMap) {
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, dataMap);
            File pathAndName = FileUtils.getFile(TEMP_FILE_PATH, "测试excel-" + dataMap.get(CREATED_BY_ID) + ".xls");
            try (FileOutputStream fileOutputStream = new FileOutputStream(pathAndName)) {
                workbook.write(fileOutputStream);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        //压缩包目录路径+名称
        String filePathName = TEMP_PATH + "/测试excel.zip";
        try {
            //开始压缩
            ZipOutputStream zipOutput = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(filePathName)));
            FileUtil.compressZip(zipOutput, file, file.getName());
        } catch (FileNotFoundException e) {
            log.error("没找到对应的文件！");
        }
        //将压缩好的文件进行下载
        FileUtil.download("测试excel.zip", filePathName, response);
        //删除临时目录
        FileUtil.deleteDirectory(TEMP_PATH);
    }


}
