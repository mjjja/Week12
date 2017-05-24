package com.example.kmj.week12;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PaintActivity extends AppCompatActivity  {
    MyPainter myPainter;
    CheckBox isStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        myPainter = (MyPainter)findViewById(R.id.paint);
        isStamp = (CheckBox)findViewById(R.id.checkBox);
        isStamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    myPainter.StampMode=true;
                }else{
                    myPainter.StampMode=false;
                }
            }
        });
    }


    public void onClick(View v){
        myPainter.setOperationType((String)v.getTag());
        isStamp.setChecked(true);
        myPainter.StampMode=true;
    }

    public void onClick2(View v){
        if (v.getId()==R.id.eraser){
            myPainter.clear();
        }
        else if (v.getId()==R.id.open){
            File f = new File(getFilesDir()+"canvas.jpg");
            if (f.isFile()){
                myPainter.clear();
                Bitmap img = BitmapFactory.decodeFile(getFilesDir()+"canvas.jpg");
                myPainter.open(img);
            }else{
                Toast.makeText(this,"저장된 파일이 없습니다.",Toast.LENGTH_SHORT);
            }
        }
        else if (v.getId()==R.id.save){
            Bitmap img = myPainter.mBitmap;
            try {
                FileOutputStream stream = new FileOutputStream(getFilesDir()+"canvas.jpg");
                img.compress(Bitmap.CompressFormat.JPEG,100,stream);
                stream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.bluring){
            item.setChecked(!item.isChecked());
            myPainter.BluringMode(item.isChecked());
        }else if (item.getItemId()==R.id.coloring){
            item.setChecked(!item.isChecked());
            myPainter.ColoringMode(item.isChecked());
        }else if (item.getItemId()==R.id.penwbig){
            item.setChecked(!item.isChecked());
            myPainter.PenBigMode(item.isChecked());
        }else if (item.getItemId()==R.id.pencred){
            myPainter.mPaint.setColor(Color.RED);
        }else if (item.getItemId()==R.id.pencblue){
            myPainter.mPaint.setColor(Color.BLUE);
        }
        return super.onOptionsItemSelected(item);
    }

}
