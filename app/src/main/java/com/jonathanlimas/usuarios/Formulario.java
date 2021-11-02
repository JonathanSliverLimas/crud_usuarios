package com.jonathanlimas.usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Formulario extends AppCompatActivity {

    private EditText RUN, nombre, apellido, edad, password;
    private Button registrar, modificar, eliminar, ListaUsuariosActividad, buscar;
    private RadioButton administrador, usuario, masculino, femenino;
    private RadioGroup groupGenero, groupTipoUsuario;

    Persona persona = new Persona();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Formulario");

        RUN = (EditText) findViewById(R.id.RUN);
        nombre = (EditText) findViewById(R.id.Nombre);
        apellido = (EditText) findViewById(R.id.Apellido);
        edad = (EditText) findViewById(R.id.Edad);
        password = (EditText) findViewById(R.id.Password);
        masculino = (RadioButton) findViewById(R.id.Masculino);
        femenino = (RadioButton) findViewById(R.id.Femenino);
        administrador = (RadioButton) findViewById(R.id.Administrador);
        usuario = (RadioButton) findViewById(R.id.Usuario);
        groupGenero = (RadioGroup) findViewById(R.id.radioGenero);
        groupTipoUsuario = (RadioGroup) findViewById(R.id.radioTipoUsuario);
        registrar = (Button) findViewById(R.id.Registrar);
        modificar = (Button) findViewById(R.id.Modificar);
        eliminar = (Button) findViewById(R.id.Eliminar);
        buscar = (Button) findViewById(R.id.BuscarUser);
        ListaUsuariosActividad = (Button) findViewById(R.id.ListaUsuariosActividad);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String run = RUN.getText().toString();
                String Nombre = nombre.getText().toString();
                String Apellido = apellido.getText().toString();
                String Edad = edad.getText().toString();
                String Password = password.getText().toString();

                // Todos los campos son obligatorios
                if(run.equals("")
                        || run.equals("0")
                        || Nombre.equals("")
                        || Apellido.equals("")
                        || Edad.equals("")
                        || Edad.equals("0")
                        || Password.equals("")
                        || groupGenero.getCheckedRadioButtonId() == -1
                        || groupTipoUsuario.getCheckedRadioButtonId() == -1){
                    Toast.makeText(Formulario.this, "Por favor rellene todos los datos", Toast.LENGTH_SHORT).show();
                }
                else{
                    registrarUsuario(view);
                }
            }
        });

        ListaUsuariosActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Formulario.this, ListaUsuarios.class);
                startActivity(intent);
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String run = RUN.getText().toString();
                String Nombre = nombre.getText().toString();
                String Apellido = apellido.getText().toString();
                String Edad = edad.getText().toString();
                String Password = password.getText().toString();

                // Todos los campos son obligatorios
                if(run.equals("")
                        || run.equals("0")
                        || Nombre.equals("")
                        || Apellido.equals("")
                        || Edad.equals("")
                        || Edad.equals("0")
                        || Password.equals("")
                        || groupGenero.getCheckedRadioButtonId() == -1
                        || groupTipoUsuario.getCheckedRadioButtonId() == -1){
                    Toast.makeText(Formulario.this, "Por favor rellene todos los datos", Toast.LENGTH_SHORT).show();
                }
                else{
                    modificarUsuario(view);
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String run = RUN.getText().toString();

                // Todos los campos son obligatorios
                if(run.equals("") || run.equals("0")){
                    Toast.makeText(Formulario.this, "Por favor ingrese el RUN", Toast.LENGTH_SHORT).show();
                }else if(run.equals("10100100")){
                    Toast.makeText(Formulario.this, "El RUN ingresado es el Super Usuario, por lo tanto no puedes eliminarlo.", Toast.LENGTH_SHORT).show();
                } else{
                    eliminarUsuario(view);
                }
            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String run = RUN.getText().toString();

                // Todos los campos son obligatorios
                if(run.equals("") || run.equals("0")){
                    Toast.makeText(Formulario.this, "Por favor ingrese el RUN", Toast.LENGTH_SHORT).show();
                }
                else{
                    buscarUsuario(view);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cerrarsesion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.CerrarSesion:
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(this, "Cerraste sesión", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void radioGenero(View view){

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.Masculino:
                if (checked)
                    persona.setGenero(masculino.getText().toString());
                break;
            case R.id.Femenino:
                if (checked)
                    persona.setGenero(femenino.getText().toString());
                break;
        }

    }

    public void radioTipoUsuario(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.Administrador:
                if (checked)
                    persona.setTipoUsuario(administrador.getText().toString());
                break;
            case R.id.Usuario:
                if (checked)
                    persona.setTipoUsuario(usuario.getText().toString());
                break;
            }
    }

    private void registrarUsuario(View view){
        try{
            DbHelper admin = new DbHelper(this, "dbUsuarios.db", null,1 );
            SQLiteDatabase BaseDeDatos = admin.getReadableDatabase();

            persona.setRUN(Integer.parseInt(RUN.getText().toString()));
            persona.setNombre(nombre.getText().toString());
            persona.setApellido(apellido.getText().toString());
            persona.setEdad(Integer.parseInt(edad.getText().toString()));
            persona.setPassword(password.getText().toString());

            ContentValues registro = new ContentValues();

            registro.put("run", persona.getRUN());
            registro.put("nombre", persona.getNombre());
            registro.put("apellido", persona.getApellido());
            registro.put("edad", persona.getEdad());
            registro.put("password", persona.getPassword());
            registro.put("genero", persona.getGenero());
            registro.put("tipo_usuario", persona.getTipoUsuario());

            BaseDeDatos.insert("usuarios", null, registro);

            BaseDeDatos.close();

            limpiarFormulario();

            Toast.makeText(this,"Usuario agregado", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ListaUsuarios.class));
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void buscarUsuario(View view){

        try{
            DbHelper admin = new DbHelper(this, "dbUsuarios.db",null, 1);
            SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

            persona.setRUN(Integer.parseInt(RUN.getText().toString()));

            Cursor registro = BaseDeDatos.rawQuery("SELECT nombre, apellido, edad, password, genero, tipo_usuario FROM usuarios WHERE run = " + persona.getRUN(), null);


            if(registro.moveToFirst()){
                nombre.setText(registro.getString(0));
                apellido.setText(registro.getString(1));
                edad.setText(registro.getString(2));
                password.setText(registro.getString(3));

                if(registro.getString(4).equals("Masculino")){
                    masculino.setChecked(true);
                    femenino.setChecked(false);
                    persona.setGenero(masculino.getText().toString());
                }else if (registro.getString(4).equals("Femenino")){
                    femenino.setChecked(true);
                    masculino.setChecked(false);
                    persona.setGenero(femenino.getText().toString());
                }

                if(registro.getString(5).equals("Administrador")){
                    administrador.setChecked(true);
                    usuario.setChecked(false);
                    persona.setTipoUsuario(administrador.getText().toString());
                }else if(registro.getString(5).equals("Usuario")){
                    usuario.setChecked(true);
                    administrador.setChecked(false);
                    persona.setTipoUsuario(usuario.getText().toString());
                }
            }else{
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }

            BaseDeDatos.close();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void modificarUsuario(View view){
        try{
            DbHelper admin = new DbHelper(this, "dbUsuarios.db",null, 1);
            SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

            String runUpdate = RUN.getText().toString();
            Cursor buscarRUN = BaseDeDatos.rawQuery("SELECT run FROM usuarios WHERE run = " + runUpdate, null);

            if(buscarRUN.moveToFirst()){
                persona.setNombre(nombre.getText().toString());
                persona.setApellido(apellido.getText().toString());
                persona.setEdad(Integer.parseInt(edad.getText().toString()));
                persona.setPassword(password.getText().toString());

                ContentValues registro = new ContentValues();

                registro.put("nombre", persona.getNombre());
                registro.put("apellido", persona.getApellido());
                registro.put("edad", persona.getEdad());
                registro.put("password", persona.getPassword());
                registro.put("genero", persona.getGenero());
                registro.put("tipo_usuario", persona.getTipoUsuario());

                BaseDeDatos.update("usuarios", registro, "run="+ runUpdate, null);

                BaseDeDatos.close();

                limpiarFormulario();

                Toast.makeText(this,"Usuario modificado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListaUsuarios.class));
            }else{
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarUsuario(View view){
        try{
            DbHelper admin = new DbHelper(this, "dbUsuarios.db",null, 1);
            SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

            String runDelete = RUN.getText().toString();

            Cursor buscarRUN = BaseDeDatos.rawQuery("SELECT run FROM usuarios WHERE run = " + runDelete, null);

            if(buscarRUN.moveToFirst()){
                BaseDeDatos.delete("usuarios", "run="+ runDelete, null);
                limpiarFormulario();
                BaseDeDatos.close();

                Toast.makeText(this,"Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListaUsuarios.class));
            }else{
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void limpiarFormulario(){
        RUN.setText("");
        nombre.setText("");
        apellido.setText("");
        edad.setText("");
        password.setText("");
        groupGenero.clearCheck();
        groupTipoUsuario.clearCheck();

        persona.setRUN(0);
        persona.setNombre("");
        persona.setApellido("");
        persona.setEdad(0);
        persona.setPassword("");
        persona.setGenero("");
        persona.setTipoUsuario("");
    }
}