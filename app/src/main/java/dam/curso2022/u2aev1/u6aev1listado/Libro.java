package dam.curso2022.u2aev1.u6aev1listado;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Libro implements Serializable {
    private String titulo, sinopsis;
    private static int identificadorGlobal = 0;
    private int id;
    //Me he pegado demasiado, demasiado, para intentar pasar el Bitmap, ya sea como ImageView, Bitmap o Bitmap[], de una activity a otra
    // pero de primeras las dos primeras opciones no se pueden serializar. En cambio, el Bitmap[] si se pasaba, pero pesaba demasiado y petaba :)
    //Intentando buscar soluciones de coger, de alguna forma, la ruta/ id del imageView del recycler, pues al estar esta asociada con las imagenes,
    // tal vez era posible coger la ruta de alguna forma para pasarla a la otra activity, pero no he llegado a descubrir si esto era posible.
    //Finalmente, he utilizado la opción de almacenar las imágenes en caché, que había descartado en una primera instancia por no entender 
    // como implementarlo, siendo más sencillo de lo que me imaginaba. Al utilizar esta opción, no necesito meter la clase en el Extra, así que
    //puedo simplemente definir la portada como Bitmap y manejarlo en las clases que corresponda
    private Bitmap portada;

    public int getId() {
        return id;
    }

    public Libro(String titulo, String sinopsis, Bitmap portada) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.portada = portada;
        id = identificadorGlobal;
        identificadorGlobal++;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public Libro() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Bitmap getPortada() {
        return portada;
    }

    public void setPortada(Bitmap portada) {
        this.portada = portada;
    }
}
