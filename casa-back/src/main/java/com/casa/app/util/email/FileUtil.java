package com.casa.app.util.email;

import java.io.File;

public class FileUtil {
    public static final String imagesDir =
            "src" + File.separator + "main" + File.separator + "resources"+ File.separator + "static" + File.separator + "images" + File.separator;

    public static void createImagesIfNotExists(){
        File directory = new File(imagesDir);
        if (! directory.exists()){
            directory.mkdir();
        }
    }
}
