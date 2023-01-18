package dam.curso2022.u2aev1.u6aev1listado;
import java.io.Serializable;

public class Libro implements Serializable {
    private String titulo, sinopsis;
    //Mi ieda inicial era pasar el byte[] con la imagen, pero como este no se puede serializar 
    // y las soluciones que he encontrado a esto eran algo engorrosas y obtusas, he decidio almacenar
    // la portada en el array de byte y ya en el adapter, lo paso a byte[] y lo meto en la ImageView; lo mismo en LibroDetalle
    private byte[] portada;

    public Libro(String titulo, String sinopsis, byte[] portada) {
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

    public byte[] getPortada() {
        return portada;
    }

    public void setPortada(byte[] portada) {
        this.portada = portada;
    }
}
