package com.fire.photoedit;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;


public class Colorfull {

private Bitmap bitmap;
private float redcolor;
    private float greencolor;
    private float bluecolor;

    public Colorfull(Bitmap bitmap, float redcolor, float greencolor, float bluecolor) {
        this.bitmap = bitmap;
        setredcolorvalue(redcolor);
        setgreenvalue(greencolor);
        setbluecolorvalue(bluecolor);

    }

    public void setbluecolorvalue(float bluecolor) {

        if(this.bluecolor>=0 && this.bluecolor<=1){
            this.bluecolor = bluecolor;
        }

    }

    public void setgreenvalue(float greencolor) {
        if(this.greencolor>=0 && this.greencolor<=1){
            this.greencolor = greencolor;
        }

    }

    public void setredcolorvalue(float red) {

        if(this.redcolor>=0 && this.redcolor<=1){
            this.redcolor = red;
        }

    }

    public Bitmap getBitmap(){

        int bitmapwidth = bitmap.getWidth();
        int bitmapheight = bitmap.getHeight();

        Bitmap.Config bitc =  bitmap.getConfig();
        Bitmap localbitmap = Bitmap.createBitmap(bitmapwidth,bitmapheight,bitc);

        for(int row = 0;row<bitmapwidth;row++){

            for (int col=0;col<bitmapheight;col++){

                int pixelcolor = bitmap.getPixel(row,col);

                pixelcolor= Color.argb(Color.alpha(pixelcolor),(int)
                        this.redcolor*Color.red(pixelcolor),(int)
                        this.greencolor*Color.green(pixelcolor),(int)this.bluecolor*Color.blue(pixelcolor));


                localbitmap.setPixel(row,col,pixelcolor);
            }

        }



        return localbitmap;

    }

    public float getRedcolor() {
        return redcolor;
    }

    public float getGreencolor() {
        return greencolor;
    }

    public float getBluecolor() {
        return bluecolor;
    }
}
