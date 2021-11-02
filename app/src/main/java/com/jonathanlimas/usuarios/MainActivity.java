package com.jonathanlimas.usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText runUser, passwordUser;
    Button iniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runUser = (EditText) findViewById(R.id.RUN_Login);
        passwordUser = (EditText) findViewById(R.id.Password_Login);
        iniciarSesion = (Button) findViewById(R.id.iniciarSesion);

        buscarAdminPorDefecto("10100100");

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String run = runUser.getText().toString();
                String pass = passwordUser.getText().toString();

                if(run.equals("") || pass.equals("")){
                    Toast.makeText(MainActivity.this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                }
                else{
                    buscarUsuario(view);
                }
            }
        });
    }

    private void buscarUsuario(View view){
        try{
            DbHelper admin = new DbHelper(this, "dbUsuarios.db",null, 1);
            SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

            String runLogin = runUser.getText().toString();

            Cursor registro = BaseDeDatos.rawQuery("SELECT run, password, tipo_usuario FROM usuarios WHERE run = " + runLogin, null);


            if(registro.moveToFirst()){
                if(runUser.getText().toString().equals(registro.getString(0)) && passwordUser.getText().toString().equals(registro.getString(1))){

                    if(registro.getString(2).equals("Administrador")){
                        startActivity(new Intent(this, Formulario.class));
                    }else{
                        startActivity(new Intent(this, ListaUsuarios.class));
                    }
                    finish();
                }else if(!passwordUser.getText().equals(registro.getString(1))){
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
            }
            BaseDeDatos.close();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarAdminPorDefecto(String runAdmin){
        try{
            DbHelper admin = new DbHelper(this, "dbUsuarios.db",null, 1);
            SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

            Cursor registro = BaseDeDatos.rawQuery("SELECT run FROM usuarios WHERE run = " + runAdmin, null);

            if(!registro.moveToFirst()){
                ContentValues contentValues = new ContentValues();
                contentValues.put("run", 10100100);
                contentValues.put("nombre", "Jonathan");
                contentValues.put("apellido", "Limas");
                contentValues.put("edad", "21");
                contentValues.put("password", "123456");
                contentValues.put("genero", "Masculino");
                contentValues.put("tipo_usuario", "Administrador");

                BaseDeDatos.insert("usuarios", null, contentValues);

                Toast.makeText(this, "Se creo un admintrador por defecto", Toast.LENGTH_SHORT).show();
            }

            BaseDeDatos.close();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}