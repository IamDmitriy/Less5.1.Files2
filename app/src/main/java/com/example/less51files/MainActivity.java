package com.example.less51files;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PERMISSION_WRITE_STORAGE = 10;
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 11;

    ImageView imageView;
    private EditText edtInput;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();



/*        //Проверяем есть ли разрешение
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


        loadImage();
        saveTextFile();*/

    }

    private void init() {
        imageView = findViewById(R.id.imageView);
        Button btnLoadImage = findViewById(R.id.btnLoadImage);
        Button btnSaveText = findViewById(R.id.btnSaveText);
        Button btnLoadText = findViewById(R.id.btnLoadText);
        edtInput = findViewById(R.id.edtInput);
        textView = findViewById(R.id.textView);

        btnLoadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myCheckPermissionForWrite()) {
                    return;
                }

                loadTextFile();
            }
        });

        btnSaveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!myCheckPermissionForWrite()) {
                    return;
                }

                saveTextFile();

            }
        });


        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!myCheckPermissionForWrite()) {
                    return;
                }

                if (isExternalStorageReadable()) {
                    loadImage();
                } else {
                    showToast("Хранилище недоступно для чтения");
                }


            }
        });
    }

    private void loadTextFile() {
        if (!isExternalStorageReadable()) {
            showToast("Хранилище недоступно для чтения");
            return;
        }

        File txtFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "txtFile.txt");

        if (txtFile.exists()) {
            showToast("Файл существует");
        }

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(txtFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToast("Не создаётся FileReader");
            return;
        }

        Scanner scanner = new Scanner(fileReader);

        StringBuilder txt = new StringBuilder();
        while (scanner.hasNextLine()) {
            txt.append(scanner.nextLine());
        }

        textView.setText(txt.toString());

    }

    private void saveTextFile() {
        if (!isExternalStorageWritable()) {
            showToast("Хранилище недоступно для записи");
            return;
        }

        String txt = edtInput.getText().toString();
        if (txt.isEmpty()) {
            showToast("Пожалуйста, введите текст для записи");
            return;
        }

        File txtFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "txtFile.txt");

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(txtFile, true);

            if (txtFile.exists()) {
                fileWriter.append(txt);
            } else {
                fileWriter.write(txt);
            }


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

    private boolean myCheckPermissionForWrite() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_WRITE_STORAGE);
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage();
                } else {
                    showToast("Нет разрешения");
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
        //if (isExternalStorageWritable()) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "1.jpg");
        /*File file = getExternalFilesDir("/sdcard/Downloads/1");*/

        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath()); //Почему то тут null всегда

        imageView.setImageBitmap(bitmap);

        //}
    }

   /* public void saveTextFile() {
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
*//*            fileReader = new FileReader(logFile);
            Scanner scanner = new Scanner(fileReader);
            String message = scanner.nextLine();
*//**//*
            BufferedReader br = new BufferedReader(fileReader);

            String message = br.readLine();*//**//*

            showToast(message);*//*

     *//*            FileInputStream inputStream = new FileInputStream(logFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            showToast(stringBuilder.toString());*//*

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
