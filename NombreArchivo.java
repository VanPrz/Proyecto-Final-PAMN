package com.vanprzz.textformat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class NombreArchivo extends Edit implements OnClickListener, AdapterView.OnItemSelectedListener{
    ArrayAdapter<CharSequence> adapter;
    String ext,contenido;
    EditText Name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_in);

        View buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(this);
        View buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(this);
        Name  = (EditText)findViewById(R.id.NomA);

        contenido = getIntent().getStringExtra("CONTENIDO");



    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonCancel){
            finish();
        }else{
            String Nombre =Name.getText().toString();
            finishActivity(107);
            Intent rutaSave = new Intent (this, RutaGuarda.class);
            rutaSave.putExtra("Nombre",Nombre);
            rutaSave.putExtra("CONTENIDO", contenido);

            Intent Resultado = new Intent (this, Edit.class);
            Resultado.putExtra("Nombre",Nombre);

            startActivity(rutaSave);
            finish();

        }

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }



}
