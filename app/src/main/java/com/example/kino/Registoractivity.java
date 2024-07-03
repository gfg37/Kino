package com.example.kino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registoractivity extends AppCompatActivity {


    private static final String TABLE_NAME = "Users";

    private EditText editTextEmail, editTextPassword;
    private Button buttonRegister;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registoractivity);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();



                // Сохранение данных в базу данных
                DatabaseHelper dbHelper = new DatabaseHelper(Registoractivity.this);
                long result = dbHelper.addUser(email, password);

                if (result != -1) {
                    Toast.makeText(Registoractivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                }
                if (result == -1){
                    Toast.makeText(Registoractivity.this, "Пользователь с такими email существует", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}