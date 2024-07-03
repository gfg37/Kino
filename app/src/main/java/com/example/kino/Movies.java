package com.example.kino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Movies extends AppCompatActivity {

    private DatabaseHelper db,dbHelper;
    private LinearLayout moviesLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        db = new DatabaseHelper(this);
        moviesLayout = findViewById(R.id.moviesLayout);

        List<String> movieTitles = db.gettitle();
        List<String> movieDescriptions = db.getdescription();
        List<String> movieDates = db.getdate();
        List<String> movieTimes = db.gettime();

        // Получение списка изображений в формате PNG из базы данных
        List<byte[]> imageBytesList = db.getImagesAsBytes();

        for (int i = 0; i < movieTitles.size(); i++) {
            String title = movieTitles.get(i);
            String description = movieDescriptions.get(i);
            String date = movieDates.get(i);
            String time = movieTimes.get(i);

            TextView movieTextView = new TextView(this);
            movieTextView.setText("Название: " + title + "\nОписание: " + description);
            moviesLayout.addView(movieTextView);

            byte[] imageBytes = imageBytesList.get(i);

            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                ImageView movieImageView = new ImageView(this);
                movieImageView.setImageBitmap(bitmap);
                moviesLayout.addView(movieImageView);
            }

            Button sessionsButton = new Button(this);
            sessionsButton.setText("Сеансы для " + title);
            final int movieId = db.getMovieId(title);
            sessionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> sessions = db.getSessionsForMovieId(movieId);
                    Intent intent = new Intent(Movies.this, Sessions.class);
                    intent.putStringArrayListExtra("sessions", new ArrayList<>(sessions));
                    intent.putExtra("movieId", movieId);
                    startActivity(intent);
                }
            });
            moviesLayout.addView(sessionsButton);
        }
    }



}
