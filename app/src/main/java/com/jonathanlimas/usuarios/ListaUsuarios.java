package com.jonathanlimas.usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuarios extends AppCompatActivity {

    ListView listaViewUsuarios;
    List<Persona> listPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Lista de usuarios");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        listaViewUsuarios = (ListView) findViewById(R.id.listaViewUsuarios);

        CustomAdapter adapter = new CustomAdapter(this, getData());

        listaViewUsuarios.setAdapter(adapter);

        listaViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Persona persona = listPersonas.get(i);
                Toast.makeText(getBaseContext(), persona.getNombre() + " " + persona.getApellido() + " - " + persona.getTipoUsuario(), Toast.LENGTH_SHORT).show();
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

    private List<Persona> getData() {
        DbHelper admin = new DbHelper(this, "dbUsuarios.db", null,1 );
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        Cursor cursor = BaseDeDatos.rawQuery("SELECT * FROM usuarios", null);
        Persona persona = null;

        if(cursor.moveToFirst()){
            listPersonas = new ArrayList<Persona>();
            do{
                persona = new Persona();
                persona.setRUN(cursor.getInt(0));
                persona.setNombre(cursor.getString(1));
                persona.setApellido(cursor.getString(2));
                persona.setEdad(cursor.getInt(3));
                persona.setPassword(cursor.getString(4));
                persona.setGenero(cursor.getString(5));
                persona.setTipoUsuario(cursor.getString(6));

                listPersonas.add(persona);
            }while (cursor.moveToNext());
        }

        BaseDeDatos.close();
        return listPersonas;
    }
}