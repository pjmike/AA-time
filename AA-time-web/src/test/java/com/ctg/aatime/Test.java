package com.ctg.aatime;

import java.io.File;
import java.io.IOException;

/**
 * @author pjmike
 * @create 2018-06-02 11:44
 */
public class Test {
    public static void main(String[] args) throws IOException {
        File file = new File("src/java/resource/static/code.png");
        System.out.println(file.exists());
        if (!file.exists()) {
            file.createNewFile();
        }
//        if (!file.exists()) {
//            file.mkdir();
//        }
        System.out.println(file.exists());
    }
}
