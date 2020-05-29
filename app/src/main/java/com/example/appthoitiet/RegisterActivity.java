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

public class RegisterActivity  extends AppCompatActivity {
    EditText txtEmail, txtPassword,txtRePassword;
    Button btnLogin, btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    UserDB userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setControl();
        setEvent();
    }

    private void setControl() {
        userDB = new UserDB(this);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString();
                String rePassword = txtRePassword.getText().toString();
                if (!email.matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"Địa chỉ email không hợp lệ",Toast.LENGTH_LONG).show();
                    txtEmail.requestFocus();
                } else {
                    if(password.equals("")) {
                        Toast.makeText(getApplicationContext(),"Mật khẩu không được bỏ trống",Toast.LENGTH_LONG).show();
                        txtPassword.requestFocus();
                    } else {
                        if(rePassword.equals("")) {
                            Toast.makeText(getApplicationContext(),"Nhập lại mật khẩu không được bỏ trống",Toast.LENGTH_LONG).show();
                            txtRePassword.requestFocus();
                        } else {
                            if(!password.equals(rePassword)){
                                Toast.makeText(getApplicationContext(),"Nhập lại mật khẩu không khớp",Toast.LENGTH_LONG).show();
                                txtRePassword.requestFocus();
                            } else {
                                User user = new User();
                                user.setEmail(email);
                                user.setPassword(password);
                                Cursor cursor = userDB.layUser(email);
                                if(cursor != null) {
                                    if(cursor.moveToFirst()) {
                                        Toast.makeText(getApplicationContext(),"Người dùng tồn tại vui lòng kiểm tra",Toast.LENGTH_LONG).show();
                                    } else {
                                        userDB.them(user);
                                        Intent i = new Intent(RegisterActivity.this
                                                , LoginActivity.class);
                                        startActivity(i);
                                    }
                                }

                            }
                        }
                    }
                }

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this
                        , LoginActivity.class);
                startActivity(i);
            }
        });
    }
}

