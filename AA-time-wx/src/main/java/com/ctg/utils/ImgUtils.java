package com.ctg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger logger = LoggerFactory.getLogger(ImgUtils.class);
    /**
     * 将二进制转换成文件保存
     * @param instreams 二进制流
     * @param imgName 图片的名称
     * @return
     *      1：保存正常
     *      0：保存失败
     */
    public static File saveToImgByInputStream(InputStream instreams,String imgName){

        File file = null;
        if(instreams != null){
            try {
                //可以是任何图片格式.jpg,.png等
                file =new File(imgName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos=new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while ((nRead = instreams.read(b)) != -1) {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            } catch (Exception e) {
                logger.info("微信二维码保存失败");
                e.printStackTrace();
            } finally {

            }
        }
        return file;
    }
}
