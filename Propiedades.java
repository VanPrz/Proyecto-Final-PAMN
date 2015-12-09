package com.vanprzz.textformat;

/**
 * Created by van on 30/11/15.
 * Son los atributos de cada elemeno que se muestra en la lista
 */
public class Propiedades {

    private String Nombre;
    private String Hora;
    private String Ruta;
    private int Imagen;

    public Propiedades(String nombre, String hora, String ruta, int imagen) {
        Nombre = nombre;
        Hora = hora;
        Ruta = ruta;
        Imagen = imagen;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }

    public String getRuta() {
        return Ruta;
    }

    public void setRuta(String ruta) {
        Ruta = ruta;
    }
}
