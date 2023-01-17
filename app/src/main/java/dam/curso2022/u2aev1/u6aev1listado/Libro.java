package dam.curso2022.u2aev1.u6aev1listado;

import java.io.Serializable;

public class Libro implements Serializable {
    private String titulo, sinopsis;
    private int portada;

    public Libro(String titulo, String sinopsis, int portada) {
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.portada = portada;
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

    public int getPortada() {
        return portada;
    }

    public void setPortada(int portada) {
        this.portada = portada;
    }
}
