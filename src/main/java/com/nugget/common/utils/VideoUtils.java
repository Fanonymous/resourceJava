package com.nugget.common.utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoUtils {
    /**
     * 获取指定视频的帧并保存为图片至指定目录
     * @param videofile  源视频文件路径
     * @param framefile  截取帧的图片存放路径
     * @throws Exception
     */
    public static void fetchFrame(String videofile, String framefile)
            throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = new File(framefile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);//videofile视频路径，我用的是网络路径
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片
            f = ff.grabFrame();
            if ((i > 96) && (f.image != null)) {
                break;
            }
            i++;
        }
        int owidth = f.imageWidth ;
        int oheight = f.imageHeight ;
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage fecthedImage =converter.getBufferedImage(f);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, "jpg", targetFile);
        //ff.flush();
        ff.stop();

        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * 获取视频文件第一帧JPG图片
     * @param file
     * @param jpgFile
     * @throws Exception
     */
    public static void fetchFrame(MultipartFile file, File jpgFile)
            throws Exception {
        long start = System.currentTimeMillis();
        File targetFile = jpgFile;
        //MultipartFile转File
        File viedoFile = multipartFileToFile(file);

        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(viedoFile);//videofile视频路径，我用的是网络路径
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片
            f = ff.grabFrame();
            if ((i > 96) && (f.image != null)) {
                break;
            }
            i++;
        }
        int owidth = f.imageWidth ;
        int oheight = f.imageHeight ;
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage fecthedImage =converter.getBufferedImage(f);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                0, 0, null);
        ImageIO.write(bi, "jpg", targetFile);
        //ff.flush();
        ff.stop();

        //删除临时生成的文件
        if (viedoFile.exists()) {
            viedoFile.delete();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * multipartFile转File
     * @param multipartFile
     * @return
     */
    public static File multipartFileToFile(MultipartFile multipartFile) {
        File file = null;
        if(multipartFile.equals("")||multipartFile.getSize()<=0){
            file = null;
        }else{
            try {
                InputStream ins = multipartFile.getInputStream();
                file = new File(multipartFile.getOriginalFilename());
                OutputStream os = new FileOutputStream(file);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                ins.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
