package easypoiexceldemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName FileUtil
 * @Author qianchao
 * @Date 2022/2/9
 **/
@Slf4j
public class FileUtil {

    /**
     * 删除指定文件夹下所有文件和文件夹
     *
     * @param directoryPath 目录
     */
    public static void deleteDirectory(String directoryPath) {

        try {
            FileUtils.deleteDirectory(new File(directoryPath));
        } catch (IOException e) {
            log.error("文件{}删除失败", directoryPath);
        }


    }


    /**
     * 压缩成zip
     *
     * @param zipOutput ZipOutputStream
     * @param file      临时目录
     * @param fileName  文件或者目录的名称  这里是目录名称
     */
    public static void compressZip(ZipOutputStream zipOutput, File file, String fileName) {

        //是否是一个目录
        if (file.isDirectory()) {
            //返回一个抽象路径名数组，这些路径名表示此抽象路径名所表示目录中的文件。
            File[] files = file.listFiles();
            //多文件压缩
            for (File f : files) {
                zip(zipOutput, f, fileName);
            }
        } else {
            //单个文件压缩
            zip(zipOutput, file, fileName);
        }

        try {
            zipOutput.close();
        } catch (IOException e) {
            log.error("文件压缩失败：" + file);
        }

    }

    /**
     * 压缩
     *
     * @param zipOutput
     * @param file      具体的文件
     * @param fileName  文件或目录名称  这里是目录名称
     */
    private static void zip(ZipOutputStream zipOutput, File file, String fileName) {

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            //创建具有指定名称的新zip条目
            ZipEntry zEntry = new ZipEntry(fileName + File.separator + file.getName());
            //将创建好的条目加入到压缩文件中
            zipOutput.putNextEntry(zEntry);
            //写入当前条目所对应的具体内容
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = bis.read(buffer)) != -1) {
                zipOutput.write(buffer, 0, read);
            }

        } catch (IOException e) {
            log.error("文件压缩{}失败：", file);
        }
    }

    /**
     * 下载方法.
     *
     * @param path     压缩包路径+名称
     * @param response HttpServletResponse
     * @param fileName 文件名字
     */
    public static void download(String fileName, String path, HttpServletResponse response) {
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        log.info("开始下载文件:{}", path);
        File file = new File(path);
        //文件或者目录是否存在
        if (file.exists()) {
            try (InputStream in = new FileInputStream(path); OutputStream out = response.getOutputStream()) {
                //利用spring中的FileCopyUtils.copy()复制文件 in复制给out。利用流的方式浏览器直接下载
                //使用OutputStream将缓冲区的数据输出到客户端浏览器
                FileCopyUtils.copy(in, out);
            } catch (IOException e) {
                log.error("文件{}下载失败", path);
            }
        } else {
            log.error("报错，文件不存在！");
        }
    }

}
