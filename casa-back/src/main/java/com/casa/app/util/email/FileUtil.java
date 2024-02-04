package com.casa.app.util.email;

import java.io.File;
import java.sql.SQLOutput;

public class FileUtil {
    public static final String imagesDir =
            "src" + File.separator + "main" + File.separator + "resources"+ File.separator + "static" + File.separator + "images" + File.separator;
    public static final String profileDir = imagesDir + "profile" + File.separator;
    public static final String estateDir = imagesDir + "estate" + File.separator;
    public static final String deviceDir = imagesDir + "device" + File.separator;
    public static final String pwdDir =
            "src" + File.separator + "main" + File.separator + "resources"+ File.separator + "pwd" + File.separator ;

    public static String getExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static void createIfNotExists(String dir){
        File directory = new File(dir);
        if (! directory.exists()){
            directory.mkdir();
            System.out.println("kreirao :)");
        }

    }

    public static void deleteFolder(String dir) {
        File folder = new File(dir);
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
        }
    }
}
