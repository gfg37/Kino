package com.example.kino;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Sessions extends AppCompatActivity {

    private LinearLayout sessionsLayout;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        db = new DatabaseHelper(this);
        sessionsLayout = findViewById(R.id.sessionsLayout);

        List<String> sessions = getIntent().getStringArrayListExtra("sessions");
        int movieId = getIntent().getIntExtra("movieId", -1); // Получаем переданный movieId

        for (String session : sessions) {
            // Создание кнопки сеанса
            Button sessionButton = new Button(this);
            sessionButton.setText("Сеанс: " + session);
            sessionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTicketDialog(session);
                }
            });
            sessionsLayout.addView(sessionButton);
        }
    }

    private void showTicketDialog(String session) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите количество билетов");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int movieId = getIntent().getIntExtra("movieId", -1);
                String ticketsStr = input.getText().toString();
                if (!ticketsStr.isEmpty()) {
                    int tickets = Integer.parseInt(ticketsStr);
                    List<String> sessions2 = getIntent().getStringArrayListExtra("sessions");
                    db.createTicketsForMovie(movieId, tickets, sessions2);
                    Toast.makeText(Sessions.this, "Билеты забронированны",Toast.LENGTH_SHORT).show();
                } else {
                    // Добавьте обработку ситуации, когда поле ввода пустое
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}

