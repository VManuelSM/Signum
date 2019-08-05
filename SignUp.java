package com.example.manuel.signum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import Models.Usuario;

public class SignUp extends AppCompatActivity {
    EditText txtNom;
    EditText txtApePat;
    EditText txtApeMat;
    EditText txtEmail;
    EditText txtNickName;
    EditText txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        txtNom = findViewById(R.id.txtNom);
        txtApePat = findViewById(R.id.txtApePat);
        txtApeMat = findViewById(R.id.txtApeMat);
        txtEmail = findViewById(R.id.txtEmail);
        txtNickName = findViewById(R.id.txtNick);
        txtPassword = findViewById(R.id.txtPass);
    }
    public void signUp(View view){
        String address = getString(R.string.ip)+getString(R.string.signUpDir);
        Usuario usuario = new Usuario();
        usuario.setNombre(txtNom.getText().toString());
        usuario.setApellidoPaterno(txtApePat.getText().toString());
        usuario.setApellidoMaterno(txtApeMat.getText().toString());
        usuario.setEmail(txtEmail.getText().toString());
        usuario.setNickname(txtNickName.getText().toString());
        usuario.setPassword(txtPassword.getText().toString());
        usuario.setFotoPerfil(null);
        if (usuario.signUp(address,SignUp.this)) {
            Intent intent = new Intent(SignUp.this, LearningHome.class);
            startActivity(intent);
        }
    }
}
