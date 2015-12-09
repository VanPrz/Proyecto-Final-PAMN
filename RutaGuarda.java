package com.vanprzz.textformat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RutaGuarda extends NombreArchivo implements AdapterView.OnItemClickListener {

    ListView lista;
    ArrayAdapter<Propiedades> adaptador;
    private List<String> listaRutasArchivos;
    private String dirRaiz, Name, contenido, rutaDirA,rutaDirectorios;
    private TextView cActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_save);

        View buttonCancel = findViewById(R.id.buttonRCancel);
        buttonCancel.setOnClickListener(this);
        View buttonOk = findViewById(R.id.buttonROk);
        buttonOk.setOnClickListener(this);


        Name = getIntent().getStringExtra("Nombre");
        contenido = getIntent().getStringExtra("CONTENIDO");


        cActual = (TextView) findViewById(R.id.Ruta);
        dirRaiz = Environment.getExternalStorageDirectory().getPath();

        //Instancia del ListView
        lista = (ListView) findViewById(R.id.listView2);
        //Inicializar el adaptador con la fuente de datos
        adaptador = new EAdapter(this, Lista(dirRaiz));
        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);
        lista.setOnItemClickListener(this);

    }

    public List<Propiedades> Lista(String rutaDirectorio) {

        //Dividir la cadena en Nombre y Hora
        rutaDirA = "";

        rutaDirectorios = rutaDirectorio;
        String[] rutaArray = rutaDirectorios.split("/");
        for (int i = 2; i < rutaArray.length; i++) {

            rutaDirA = rutaDirA + "/" + rutaArray[i - 1];
        }

        List Archivos = new ArrayList<Propiedades>();
        listaRutasArchivos = new ArrayList<String>();


        File directorioActual = new File(rutaDirectorio);
        File[] listaArchivos = directorioActual.listFiles();

        int x = 0;
        if (rutaDirectorio != dirRaiz && rutaDirectorio.contains("/mnt/sdcard/")) {
            Archivos.add(new Propiedades("../", rutaDirA, rutaDirA, R.drawable.files_icon24));
            listaRutasArchivos.add(directorioActual.getParent());
            x = 1;
        }

        // Almacenamos las rutas de todos los archivos y carpetas del directorio
        for (File archivo : listaArchivos) {
            listaRutasArchivos.add(archivo.getPath());
        }

        //Collections.sort(listaRutasArchivos, String.CASE_INSENSITIVE_ORDER);

        for (int i = x; i < listaRutasArchivos.size(); i++) {
            File archivo = new File(listaRutasArchivos.get(i));
            long ms = archivo.lastModified();
            Date d = new Date(ms);
            Calendar c = new GregorianCalendar();
            c.setTime(d);
            String dia = Integer.toString(c.get(Calendar.DATE));
            String mes = Integer.toString(c.get(Calendar.MONTH));
            String annio = Integer.toString(c.get(Calendar.YEAR));
            //String hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
            //String minuto = Integer.toString(c.get(Calendar.MINUTE));
            //String segundo = Integer.toString(c.get(Calendar.SECOND));

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
// Si no hay ningun archivo en el directorio lo indicamos
        if (listaArchivos.length < 1) {
            Archivos.add(new Propiedades("Save here", "Void", rutaDirA, R.drawable.files_icon48));
            listaRutasArchivos.add(rutaDirectorio);
        }

        cActual.setText(rutaDirectorio);
        return Archivos;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Propiedades ElementoActual = (Propiedades) adaptador.getItem(position);
        String Nombre = ElementoActual.getRuta();
        File file = new File(Nombre);
        Intent edicion = new Intent(this, Edit.class);

        if (Nombre.contains("../")) {
            adaptador = new EAdapter(this, Lista(Nombre));
            lista.setAdapter(adaptador);
        } else if (ElementoActual.getHora().contains("Void")) {
            startActivity(edicion);
        } else {
            adaptador = new EAdapter(this, Lista(Nombre));
            lista.setAdapter(adaptador);
        }
    }

    @Override
    public void onClick(View v) {
        //Control de aciones para botones
        if (v.getId() == R.id.buttonRCancel) {
            finish();
        } else if (v.getId() == R.id.buttonROk) {
            //Se guarda


            int aux = 0;

            //Se valida que no se encuente el arhivo

            File dir = new File(rutaDirectorios);
            File[] fichero = dir.listFiles();
            for(int i=0; i<fichero.length;i++){

                if(fichero[i].getName().contains(Name)){
                    aux=1;
                }
            }

            if (aux==0) {
                try {
                    File file = new File(cActual.getText().toString() + "/", Name);
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(fos);
                    pw.write(contenido);
                    pw.flush();
                    pw.close();
                    Toast.makeText(this, "Se Guardo: " + Name,
                            Toast.LENGTH_SHORT).show();

                } catch (IOException ioe) {
                    Toast.makeText(this, "Error" + ioe,
                            Toast.LENGTH_SHORT).show();
                }
            }else if(aux == 1){
                Toast.makeText(this, "El arhivo ya existe",
                        Toast.LENGTH_LONG).show();
            }

            finish();
        }


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
