package com.bright.spring.learn.file;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * file controller
 *
 * @author zhengyuan
 * @since 2021/01/15
 */
@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 单文件上传 多文件上传
     *
     * @param files 多文件
     * @param file  单文件
     */
    @PostMapping("/upload")
    public void upload(MultipartFile[] files, MultipartFile file) throws Exception {
        // 多文件处理
        for (MultipartFile e : files) {
            String originalFilename = e.getOriginalFilename();
            // 因为配置文件中配置了文件上传的存放路径，这里只需要配置文件上传后的名称即可
            if (StringUtils.isEmpty(originalFilename)) {
                logger.error("original file name is empty");
                return;
            }
            File dest = new File(originalFilename);
            // 将内存中的数据转存到指定目录
            e.transferTo(dest);
        }
        // 单文件处理
        // 获取上传文件的原始名称
        String originalFilename = file.getOriginalFilename();
        logger.info("Upload file size is {}", file.getSize());
        logger.info("Type of uploaded file is {}", file.getContentType());
        logger.info("The attribute name used when uploading the file is {}", file.getName());
        if (StringUtils.isEmpty(originalFilename)) {
            logger.error("original file name is empty");
            return;
        }
        File dest = new File(originalFilename);
        file.transferTo(dest);
    }

    /**
     * 文件下载
     *
     * @param response {@link HttpServletResponse}
     * @return success or fail
     * @throws UnsupportedEncodingException exception
     */
    @PostMapping("/download")
    public String downLoad(HttpServletResponse response) throws UnsupportedEncodingException {
        String result = "success";
        String fileName = "2.xlsx";
        String filePath = "D:/download";
        File file = new File(filePath + "/" + fileName);
        if (file.exists()) { // 判断文件是否存在
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];

            OutputStream os; // 输出流
            try (FileInputStream fis = new FileInputStream(file);   // 文件输入流
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                logger.error("download file {} error ", file.getAbsolutePath(), e);
                result = "fail";
            }
        } else {
            logger.error("file {} not exists", file.getAbsolutePath());
            result = "fail";
        }
        return result;
    }
}
