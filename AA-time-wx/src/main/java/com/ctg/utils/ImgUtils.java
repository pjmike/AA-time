package com.ctg.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 二进制转换为图片工具类
 *
 * @author pjmike
 * @create 2018-03-22 16:15
 */
public class ImgUtils {
    /**
     * 将二进制转换成文件保存
     * @param instreams 二进制流
     * @param imgPath 图片的保存路径
     * @param imgName 图片的名称
     * @return
     *      1：保存正常
     *      0：保存失败
     */
    public static int saveToImgByInputStream(InputStream instreams, String imgPath, String imgName){
        int status = 1;
        if(instreams != null){
            try {
                //可以是任何图片格式.jpg,.png等
                File file =new File(imgPath,imgName);
                FileOutputStream fos=new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                status = 0;
            } finally {

            }
        }
        return status;
    }
}
