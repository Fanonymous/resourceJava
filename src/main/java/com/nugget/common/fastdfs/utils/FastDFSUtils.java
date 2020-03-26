package com.nugget.common.fastdfs.utils;

import com.nugget.common.fastdfs.base.FileInfo;
import com.nugget.common.fastdfs.server.FastDFSClient;
import com.nugget.common.fastdfs.server.FastDFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author zhaoyifan
 * @Date 2019/6/17 19:01
 * @Description TODO
 */
public class FastDFSUtils {

    private static Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);


    /**
     * 保存图片到fastDFS
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String saveFile(MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath={};
        String fileName=multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream=multipartFile.getInputStream();
        if(inputStream!=null){
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file); //upload to fastdfs
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileAbsolutePath==null) {
            logger.error("upload file failed,please upload again!");
        }
        String path = fileAbsolutePath[1];
        return path;
    }

    /**
     * 保存文件
     * @param file
     * @return
     * @throws IOException
     */
    public static String saveFile(File file) throws IOException {
        String[] fileAbsolutePath={};
        String fileName=file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream=new FileInputStream(file);
        if(inputStream!=null){
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile fastDFSFile = new FastDFSFile(fileName, file_buff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(fastDFSFile); //upload to fastdfs
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileAbsolutePath==null) {
            logger.error("upload file failed,please upload again!");
        }
        String path = fileAbsolutePath[1];
        return path;
    }

    /**
     * 保存文件
     * @param inputStream
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String saveFile(InputStream inputStream,String fileName) throws IOException {
        String[] fileAbsolutePath={};
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        if(inputStream!=null){
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file); //upload to fastdfs
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileAbsolutePath==null) {
            logger.error("upload file failed,please upload again!");
        }
        String path = fileAbsolutePath[1];
        return path;
    }

    /**
     * 获取文件
     * @param filePath
     * @return
     */
    public static InputStream downFile(String filePath){
        return FastDFSClient.downFile(filePath);
    }

    /**
     * 获取fileInfo
     * @param filePath
     * @return
     */
    public static FileInfo getFileInfo(String filePath){
        return FastDFSClient.getFile(filePath);
    }


}
