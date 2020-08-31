package com.fire.photoedit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Element;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private Button button;
    private FloatingActionButton floatingActionButton;
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private Button button1;
private Button button2;
private TextView textView;
   public Bitmap bitmap;
  public   Colorfull colorfull;
  public Bitmap extrabitmap;
  public ImageButton imageb;
  File file=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


           imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.save);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        seekBar1 = findViewById(R.id.seekBar3);
        seekBar2 = findViewById(R.id.seekBar4);
        seekBar3 = findViewById(R.id.seekBar5);
button1=findViewById(R.id.share);
textView = findViewById(R.id.textView);
button2 = findViewById(R.id.extract);

        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        floatingActionButton.setOnClickListener(MainActivity.this);
        imageb = findViewById(R.id.imageButton);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(b){

                    colorfull.setredcolorvalue( i%100);
                   // seekBar1.setProgress((int)(100*(colorfull.getRedcolor())));
                    Log.d("value",String.valueOf(i));
                     bitmap = colorfull.getBitmap();
                     imageView.setImageBitmap(bitmap);
                }


            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(b){

                    colorfull.setgreenvalue(i%100);
                    // seekBar1.setProgress((int)(100*(colorfull.getRedcolor())));
                    Log.d("value",String.valueOf(i));
                    bitmap = colorfull.getBitmap();
                    imageView.setImageBitmap(bitmap);
                }


            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(b){

                    colorfull.setbluecolorvalue( i%100);
                    // seekBar1.setProgress((int)(100*(colorfull.getRedcolor())));
                    Log.d("value",String.valueOf(i));
                    bitmap = colorfull.getBitmap();
                    imageView.setImageBitmap(bitmap);
                }


            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imageb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(intent,500);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},400);

                }else{

                   file= Savefile.savefile(MainActivity.this,bitmap);
                    Toast.makeText(MainActivity.this,"Downloaded "+file.getAbsolutePath(),Toast.LENGTH_LONG).show();
                    button1.setVisibility(View.VISIBLE);
                }



            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

Uri uri = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/png");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"My image");
intent.putExtra(Intent.EXTRA_STREAM,uri);
startActivity(Intent.createChooser(intent,"Share your picture"));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {

                        List<FirebaseVisionText.TextBlock> blockList = result.getTextBlocks();
                        String blockText="";
                        for(FirebaseVisionText.TextBlock block:result.getTextBlocks()){
                            blockText+= block.getText();

                        }
                        textView.setText(blockText);

                        String resultText = result.getText();
//                        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
//                            String blockText = block.getText();
//                            Float blockConfidence = block.getConfidence();
//                            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
//                            Point[] blockCornerPoints = block.getCornerPoints();
//                            Rect blockFrame = block.getBoundingBox();
//                            for (FirebaseVisionText.Line line: block.getLines()) {
//                                String lineText = line.getText();
//                                textView.setText(lineText);
//
//                                Float lineConfidence = line.getConfidence();
//                                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
//                                Point[] lineCornerPoints = line.getCornerPoints();
//                                Rect lineFrame = line.getBoundingBox();
//                                for (FirebaseVisionText.Element element: line.getElements()) {
//                                    String elementText = element.getText();
//
//                                    Float elementConfidence = element.getConfidence();
//                                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
//                                    Point[] elementCornerPoints = element.getCornerPoints();
//                                    Rect elementFrame = element.getBoundingBox();
//                                }
//                            }
//                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.floatingActionButton:

                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},100);

                }else{

                    opencamera();

                }

                break;


        }


    }

    private void opencamera() {
        PackageManager packageManager = getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,200);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            opencamera();

        }
        else if(requestCode==400 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200 && resultCode == RESULT_OK && data!=null){

            try {
                showimageinimageview(data);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if(requestCode == 500 && resultCode==RESULT_OK && data!=null){

            Uri uri = data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            colorfull = new Colorfull(bitmap,0.0f,0.0f,0.0f);

            imageView.setImageBitmap(bitmap);


        }


    }

    private void showimageinimageview(Intent data) throws IOException {

        this.bitmap = (Bitmap) data.getExtras().get("data");

         colorfull = new Colorfull(bitmap,0.0f,0.0f,0.0f);

        imageView.setImageBitmap(bitmap);

    }




}