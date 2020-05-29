package com.example.appthoitiet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appthoitiet.db.UserDB;
import com.example.appthoitiet.entities.User;

public class LoginActivity  extends AppCompatActivity {
    EditText txtEmail, txtPassword;
    Button btnLogin, btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setEvent();
        setControl();

    }

    private void setEvent() {
        userDB = new UserDB(this);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setControl() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString();
                if (!email.matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"Địa chỉ email không hợp lệ",Toast.LENGTH_LONG).show();
                    txtEmail.requestFocus();
                } else {
                    if(password.equals("")) {
                        Toast.makeText(getApplicationContext(),"Mật khẩu không được bỏ trống",Toast.LENGTH_LONG).show();
                        txtPassword.requestFocus();
                    } else {
                        Cursor cursor = userDB.layUser(email);
                        if(cursor != null) {
                           if( cursor.moveToFirst()) {
                               if(password.equals(cursor.getString(2))) {
                                   Toast.makeText(getApplicationContext(),"Login OKE",Toast.LENGTH_LONG).show();
                                   Intent i = new Intent(LoginActivity.this, SearchLocationActivity.class);
                                   startActivity(i);
                               } else {
                                   Toast.makeText(getApplicationContext(),"Password sai vui lòng thử lại",Toast.LENGTH_LONG).show();
                               }
                           }else {
                               Toast.makeText(getApplicationContext(),"Người dùng không tồn tại",Toast.LENGTH_LONG).show();
                           }

                        }
                    }
                }

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
