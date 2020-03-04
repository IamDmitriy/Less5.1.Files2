package com.example.less51files;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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

        }

        saveTextFile();



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
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public void loadImage() {
        //Проверяем доступно ли файловое хранлище
        if (isExternalStorageWritable()) {
/*             File file = new File(Environment.getExternalStoragePublicDirectory(
                     Environment.DIRECTORY_DOWNLOADS), "1");*/
            File file = getExternalFilesDir("/sdcard/Downloads/1");

            long x = file.length();

            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath()); //Почему то тут null всегда

            imageView.setImageBitmap(bitmap);

        }
    }

    public void saveTextFile() {
        //теперь про внешнее личное
        File logFile = new File(getApplicationContext().getExternalFilesDir(null)
                , "log.txt"); //TODO Почему не сразу getExter...? - работает и сразу

        //Чтобы приложение на падало , если проблемы
        FileWriter fileWriter = null;
        FileReader fileReader = null;
        try {
            fileWriter = new FileWriter(logFile, true);
            fileWriter.append("приложение запущено\n");


            //TODO не корректно отражается - самый простой вариант - сканнер
/*            fileReader = new FileReader(logFile);
            Scanner scanner = new Scanner(fileReader);
            String message = scanner.nextLine();
*//*
            BufferedReader br = new BufferedReader(fileReader);

            String message = br.readLine();*//*

            showToast(message);*/

/*            FileInputStream inputStream = new FileInputStream(logFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            showToast(stringBuilder.toString());*/

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
