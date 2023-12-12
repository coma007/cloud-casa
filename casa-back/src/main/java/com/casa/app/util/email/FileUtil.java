package com.casa.app.util.email;

import java.io.File;
import java.sql.SQLOutput;

public class FileUtil {
    public static final String imagesDir =
            "src" + File.separator + "main" + File.separator + "resources"+ File.separator + "static" + File.separator + "images" + File.separator;

    public static final String pwdDir =
            "src" + File.separator + "main" + File.separator + "resources"+ File.separator + "pwd" + File.separator ;

    public static void createIfNotExists(String dir){
        File directory = new File(dir);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("kreirao :)");
        }

    }
}
