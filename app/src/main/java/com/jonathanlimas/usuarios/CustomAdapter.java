package com.jonathanlimas.usuarios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<Persona> list;

    public CustomAdapter(Context context, List<Persona> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView imageUser;
        TextView run, nombre, apellido, edad, genero;

        Persona persona = list.get(i);

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_usuarios, null);
        }
            imageUser = view.findViewById(R.id.imageViewUser);
            run = view.findViewById(R.id.run_User);
            nombre = view.findViewById(R.id.nombreUser);
            apellido = view.findViewById(R.id.apellidoUser);
            edad = view.findViewById(R.id.edad_User);
            genero = view.findViewById(R.id.genero_User);

            if(persona.getTipoUsuario().equals("Administrador")){
                imageUser.setImageResource(R.drawable.admin);
            }else{
                imageUser.setImageResource(R.drawable.user);
            }

            run.setText(String.valueOf(persona.getRUN()));
            nombre.setText(persona.getNombre());
            apellido.setText(persona.getApellido());
            edad.setText(String.valueOf(persona.getEdad()) + " años");
            genero.setText(persona.getGenero());

        return view;
    }
}
