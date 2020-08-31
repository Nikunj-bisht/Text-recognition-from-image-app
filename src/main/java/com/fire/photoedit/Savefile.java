package com.fire.photoedit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class Savefile {

    public static File savefile(Activity activity, Bitmap bitmap){
        File file=null;

        try {


            Date date = new Date();
            long time = SystemClock.elapsedRealtime();
            String name = "/"+time+".png";

                  File  file1 = activity.getExternalFilesDir("myphotos");
            file=new File(file1+name);

                    try{
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG,50,fileOutputStream);
                        return file;

                    }catch (Exception e){
                        e.printStackTrace();
                    }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return  file;
    }


}
