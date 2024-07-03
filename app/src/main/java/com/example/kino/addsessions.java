package com.example.kino;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;

public class addsessions extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ImageView imageView;

    private Button choise;
    private TextView filmName;
    private TextView description;

    private Button addsession;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView sesi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsessions);


        dbHelper = new DatabaseHelper(this);
        imageView = findViewById(R.id.filmimage);
        choise = findViewById(R.id.choise);
        filmName = findViewById(R.id.filmName);
        description = findViewById(R.id.description);
        addsession = findViewById(R.id.addsession);
        sesi = findViewById(R.id.sesi);


        dbHelper.getAllData();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(addsessions.this, android.R.layout.simple_list_item_1, dbHelper.getAllData());


        choise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(addsessions.this);
                builder.setTitle("Данные из базы данных");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Действие при выборе элемента списка
                        String selectedData = dbHelper.getdescription().get(which);
                        String selectedData2 = dbHelper.getAllData().get(which);
                        String selectedFilm = filmName.getText().toString();

                        int filmid = dbHelper.getfilmid().get(which);

                        List<String> sessionsList = dbHelper.getSessionsForMovieId(filmid);
                        StringBuilder sessionsInfo = new StringBuilder();
                        for (String session : sessionsList) {
                            sessionsInfo.append(session).append("\n");
                        }


                        filmName.setText(selectedData2);
                        description.setText(selectedData);
                        sesi.setText(sessionsInfo.toString());


                        int movieId = getIntent().getIntExtra("movie_id", filmid);
                        byte[] imageBytes = getImageFromDatabase(movieId);

                        if (imageBytes != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            // Toast.makeText(this, "Изображение не найдено", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                // Показываем диалоговое окно
                builder.create().show();
            }


        });

        addsession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Показываем диалоговое окно для выбора даты
                DatePickerDialog datePickerDialog = new DatePickerDialog(addsessions.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                // Показываем диалоговое окно для выбора времени
                                TimePickerDialog timePickerDialog = new TimePickerDialog(addsessions.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                mHour = hourOfDay;
                                                mMinute = minute;
                                                String selectedFilmName = filmName.getText().toString();
                                                int filmid = dbHelper.getMovieId(selectedFilmName);

                                                // Сохраняем дату и время в базу данных
                                                // Например, вы можете использовать ContentValues для этого
                                                ContentValues values = new ContentValues();
                                                values.put("movie_id", filmid);
                                                values.put("data", mDay + "/" + mMonth + "/" + mYear);
                                                values.put("time", mHour + ":" + mMinute);

                                                // Записываем данные в базу данных
                                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                db.insert("Sessions", null, values);
                                                db.close();

                                                Toast.makeText(addsessions.this, "Дата и время добавлены к фильму", Toast.LENGTH_SHORT).show();
                                                List<String> sessionsList = dbHelper.getSessionsForMovieId(filmid);
                                                StringBuilder sessionsInfo = new StringBuilder();
                                                for (String session : sessionsList) {
                                                    sessionsInfo.append(session).append("\n");
                                                }
                                                sesi.setText(sessionsInfo.toString());
                                            }
                                        }, mHour, mMinute, true);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    private byte[] getImageFromDatabase(int movieId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"image"};
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(movieId)};

        Cursor cursor = db.query("Movies", projection, selection, selectionArgs, null, null, null);

        byte[] imageBytes = null;
        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
        }

        cursor.close();
        db.close();
        return imageBytes;
    }



}