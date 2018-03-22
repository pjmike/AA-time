package com.ctg.aatime.commons.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 与微信服务器连接的工具类
 *
 * @author pjmike
 * @create 2018-03-13 19:17
 */
public class WxConnecter {
    public static String GetUrl(String url){
        String result = "";
        BufferedReader reader = null;
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        try {
            //java.net包中定义了URL类，该类用来处理有关URL的内容。对于URL类的创建和使用
            URL realUrl = new URL(url);
            //对URL进行操作
            URLConnection conn = realUrl.openConnection();
            //连接到URL
            conn.connect();
            //返回URL的输入流，用于读取资源
            inputStream = conn.getInputStream();
            streamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭相关资源
            try {
                if(reader!=null){
                    reader.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
                if(streamReader!=null){
                    streamReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
