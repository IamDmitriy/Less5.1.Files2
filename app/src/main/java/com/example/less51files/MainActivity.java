package com.example.less51files;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 10;
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 11;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        //Проверяем есть ли разрешение
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            //Работа с файлами
            showToast("Разрешение есть");
            loadImage();
        } else {

            showToast("Разрешения нет");
            //loadImage(); подгрузки не происходит
            //Запрашиваем разрешения
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE); //Наша константа нужна чтоб отфильтровать
            //
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_CAMERA);

            loadImage();
        }

    }

    //Он вызывается как событие, когда пользователь отвечает можно ли дать  разрешение
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission,
                                          @NonNull int[] grantResult) {

        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE:
                if (grantResult.length > 0 &&
                        grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    //работаем с файлами
                    loadImage();
                } else {
                    //Разрешение от пользователяне пришло
                }
                break;
        }

    }

    //Проверки на доступность хранилищ
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

     public void loadImage() {
        //Проверяем доступно ли файловое хранлище
         if (isExternalStorageWritable()) {
/*             File file = new File(Environment.getExternalStoragePublicDirectory(
                     Environment.DIRECTORY_DOWNLOADS), "1");*/
             File file = getExternalFilesDir("/sdcard/Downloads/1");

             long x = file.length();

             Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

             imageView.setImageBitmap(bitmap);

             //во внешнее личное
             File logFile;
         }
     }

     void showToast(String message) {
         Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
     }

}
