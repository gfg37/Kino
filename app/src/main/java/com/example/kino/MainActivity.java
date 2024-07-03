package com.example.kino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

                // Проверяем введенные данные в базе данных
                boolean userExists = databaseHelper.checkUser(email, password);

                if (userExists) {
                    if (email.equals("admin") && password.equals("admin")) {
                        Toast.makeText(getApplicationContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Admin.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Movies.class);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                }
            }
        });
                // Здесь должна быть проверка логина и пароля в базе данных
                // Если пользователь с введенными данными существует, то выполняется вход
                // Иначе отображается сообщение о неверном логине или пароле


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registoractivity.class);
                startActivity(intent);
            }
        });
    }
}
