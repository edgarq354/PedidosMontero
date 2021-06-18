package com.elisoft.pedidosmontero;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registro extends AppCompatActivity {
    EditText et_nombre;
    EditText et_paterno;
    EditText et_materno;
    EditText et_celular;
    EditText et_correo;
    EditText et_token;
    Button bt_registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_nombre=findViewById(R.id.et_nombre);
        et_paterno=findViewById(R.id.et_paterno);
        et_materno=findViewById(R.id.et_materno);
        et_celular=findViewById(R.id.et_celular);
        et_correo=findViewById(R.id.et_correo);
        et_token=findViewById(R.id.et_token);
        bt_registrar=findViewById(R.id.bt_registrar);




        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this,Principal.class));
            }
        });
    }
}