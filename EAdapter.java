package com.vanprzz.textformat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by van on 30/11/15.
 */

public class EAdapter extends ArrayAdapter<Propiedades> {

    public EAdapter(Context context, List<Propiedades> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.image_list_item,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView titulo = (TextView)listItemView.findViewById(R.id.text1);
        TextView subtitulo = (TextView)listItemView.findViewById(R.id.text2);
        ImageView Imagen = (ImageView)listItemView.findViewById(R.id.category);


        //Obteniendo instancia de la Propiedades en la posición actual
        Propiedades item = getItem(position);

        titulo.setText(item.getNombre());
        subtitulo.setText(item.getHora());
        Imagen.setImageResource(item.getImagen());

        //Regresar el arreglo de la lista
        return listItemView;

    }
}
