package com.vanprzz.textformat;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Edit extends AppCompatActivity {

    SharedPreferences Datos;
    EditText Text;
    String Nombre,Ruta,Name,ext,title;
    int x;
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar edittoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(edittoolbar);
        ab = getSupportActionBar();
        ab.hide();
        ab.show();
        Datos = getSharedPreferences("preferencias",0);

        title ="New";

        ab.setTitle(title);

        x = 0;


        Text = (EditText) findViewById(R.id.Text);

        Nombre = getIntent().getStringExtra("Titulo");
        Ruta = getIntent().getStringExtra("Ruta");
        if(Nombre != null){
            ab.setTitle(Nombre);
            recuperar(Ruta);
            x = 1;
        }



    }

    private void recuperar(String Ruta) {

        File file = new File(Ruta);
        try {
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader archivo = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();
            String todo = "";
            while (linea != null) {
                todo = todo + linea + "\n";
                linea = br.readLine();
            }
            br.close();
            archivo.close();
            Text.setText(todo);

        } catch (IOException e) {
        }

    }

    public void grabar(String Nombre,String Ruta,int x) {
        String Contenido = Text.getText().toString();
        switch (x){
            case 0:
                Intent SaveIn = new Intent(this,NombreArchivo.class);
                SaveIn.putExtra("CONTENIDO", Text.getText().toString());
                startActivity(SaveIn);

                 Name = getIntent().getStringExtra("Nombre");
                 ext = getIntent().getStringExtra("Ext");



                break;
            case 1:
                try {
                    File file = new File(Ruta);
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(fos);
                    pw.write(Contenido);
                    pw.flush();
                    pw.close();
                    Toast.makeText(this, "I was saved" + Nombre,
                            Toast.LENGTH_SHORT).show();
                    title = ab.getTitle().toString();

                } catch (IOException ioe) {
                    Toast.makeText(this, "Error" + ioe,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if(Name != null){
            title =Name+ext;
            ab.setTitle(title);
        }




    }
    @Override
    protected void onPause(){
        super.onPause();
        //Extrae el contenido de los campos

        //Define un editor para misDatos
        SharedPreferences.Editor Editor=Datos.edit();
        //Escribe los datos
        Editor.putString("Nombre", Nombre);
        Editor.putString("Contenido", Text.getText().toString());
        //salvar
        Editor.commit();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_Save) {
            grabar(Nombre,Ruta,x);

        }

        return super.onOptionsItemSelected(item);
    }

}
