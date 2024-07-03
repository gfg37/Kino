package com.example.kino;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class cratefilm2 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String DATABASE_NAME = "Kino";
    private static final String TABLE_NAME = "Movies";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_IMAGE = "image";
    private Uri imageUri;
    private EditText editTextTitle, editTextDescription;
    private ImageView imageView;
    private DatabaseHelper dbHelper;
    private long movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cratefilm2);


        Button buttonChooseImage = findViewById(R.id.buttonChooseImage);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageView = findViewById(R.id.imageView);

        dbHelper = new DatabaseHelper(this);
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Выберите Постер"), PICK_IMAGE_REQUEST);
            }
        });

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovieToDB();
            }
        });

        Button buttondelete = findViewById(R.id.buttondelite);
        buttondelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHelper.showDataPopup(cratefilm2.this);
            }
        });


    }

    private void saveMovieToDB() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        byte[] imageBytes = getImageAsByteArray(imageUri);

        if (!title.isEmpty() || !description.isEmpty() || imageBytes != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COL_TITLE, title);
            values.put(COL_DESCRIPTION, description);
            values.put(COL_IMAGE, imageBytes);
            db.insert(TABLE_NAME, null, values);

            ContentValues sessionValues = new ContentValues();
            sessionValues.put(COL_ID, movieId); // Сохраняем id фильма в таблицу Sessions
            db.insert("Sessions", null, sessionValues);
            Toast.makeText(this, "Фильм сохранен в базе", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getImageAsByteArray(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] imageBytes = stream.toByteArray();
            stream.close();
            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}