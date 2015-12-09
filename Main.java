package com.vanprzz.textformat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Main extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lista;
    ArrayAdapter<Propiedades> adaptador;
    private List<String> listaRutasArchivos;
    private String dirRaiz,rutaDirA;
    private TextView cActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cActual = (TextView) findViewById(R.id.rutaActual);
        dirRaiz = Environment.getExternalStorageDirectory().getPath();

        //Instancia del ListView
        lista = (ListView) findViewById(R.id.listView);
        //Instancia del adaptador con la fuente de datos
        adaptador = new EAdapter(this, Lista(dirRaiz));
        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(this);
        registerForContextMenu(lista);


    }


    public List<Propiedades> Lista(String rutaDirectorio) {

        rutaDirA="";

        //Se obtiene ruta arriba para poder volver con el elemento de la lista
        String rutaDirectorios = rutaDirectorio;
        String[] rutaArray = rutaDirectorios.split("/");
        for (int i = 2; i < rutaArray.length; i++) {
            rutaDirA =rutaDirA+"/"+rutaArray[i-1];
        }

        //El arreglo Arhivos es para los elementos de la lista
        //y el arreglo listaRutasArchivos es para poder controlar el directorio
        List Archivos = new ArrayList<Propiedades>();
        listaRutasArchivos = new ArrayList<String>();


        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;
        if (rutaDirectorio!=dirRaiz && rutaDirectorio.contains("/mnt/sdcard/")) {
            Archivos.add(new Propiedades("../", rutaDirA, rutaDirA, R.drawable.files_icon24));
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        // Rutas de todos los archivos y carpetas del directorio
        for (File archivo : listaArchivos) {
            listaRutasArchivos.add(archivo.getPath());
        }

        //Se ordenan por nombre
        Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

        //Se agrega cada elemento a la vista
        for (int i = x; i < listaRutasArchivos.size(); i++) {
            File archivo = new File(listaRutasArchivos.get(i));
            long fm = archivo.lastModified();
            Date date = new Date(fm);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            String dia = Integer.toString(calendar.get(Calendar.DATE));
            String mes = Integer.toString(calendar.get(Calendar.MONTH));
            String annio = Integer.toString(calendar.get(Calendar.YEAR));


            if (archivo.isFile()) {

                if (archivo.getName().contains(".txt")) {
                    Archivos.add(new Propiedades(archivo.getName(), dia + "/" + mes + "/" + annio, archivo.getPath(), R.drawable.txt_icon48));
                } else if (archivo.getName().contains(".doc") || archivo.getName().contains(".docx")) {
                    Archivos.add(new Propiedades(archivo.getName(), dia + "/" + mes + "/" + annio, archivo.getPath(), R.drawable.doc_icon48x48));
                } else {
                    Archivos.add(new Propiedades(archivo.getName(), dia + "/" + mes + "/" + annio, archivo.getPath(), R.drawable.files_icon48));
                }
            } else if (!archivo.getName().contains(".")) {
                Archivos.add(new Propiedades(archivo.getName(), "", archivo.getPath(), R.drawable.folder_icon48));
            }
        }
        // Si no hay ningun archivo en el directorio se crea un elemento para inicializar el editor
        if (listaArchivos.length < 1) {
            Archivos.add(new Propiedades("No file", "Void", rutaDirA, R.drawable.files_icon48));
            listaRutasArchivos.add(rutaDirectorio);
        }
        //se muestra la ruta al usuario
        cActual.setText(rutaDirectorio);
        return Archivos;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id ==R.id.action_new){
            Intent edicion = new Intent(this, Edit.class);
            startActivity(edicion);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Propiedades ElementoActual = (Propiedades)adaptador.getItem(position);
        String Nombre = ElementoActual.getRuta();
        File file = new File(Nombre);
        Intent edicion = new Intent(this, Edit.class);

        if(file.isFile()){
            edicion.putExtra("Titulo",ElementoActual.getNombre());
            edicion.putExtra("Ruta",Nombre);

            startActivity(edicion);
        }else if(Nombre.contains("../")){
            adaptador = new EAdapter(this, Lista(Nombre));
            lista.setAdapter(adaptador);
        }else if(ElementoActual.getHora().contains("Void")){
            startActivity(edicion);
        }else{
            adaptador = new EAdapter(this, Lista(Nombre));
            lista.setAdapter(adaptador);}

    }


    //Se reescibe el codigo de la tecla back para no finalizar la app cuando se encuentre en un sub direcorio
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean regreso=true;
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if(!cActual.getText().equals(dirRaiz)){
                adaptador = new EAdapter(this, Lista(rutaDirA));
                lista.setAdapter(adaptador);
                regreso =false;
            }else {
                finish();
                regreso= super.onKeyDown(keyCode, event);
            }
        }

        return regreso;
    }

}
