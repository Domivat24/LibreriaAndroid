package dam.curso2022.u2aev1.u6aev1listado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

public class LibroDetalle extends AppCompatActivity {
    private TextView titulo, sinopsis;
    private ImageView portada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_detalle);
        titulo = findViewById(R.id.tvTitulo);
        sinopsis = findViewById(R.id.tvSinopsis);
        portada = findViewById(R.id.portada);

        Libro libro = (Libro) getIntent().getSerializableExtra("libro");
        portada.setImageResource(libro.getPortada());
        titulo.setText(libro.getTitulo());
        sinopsis.setText(libro.getSinopsis());

        sinopsis.setMovementMethod(new ScrollingMovementMethod());
    }
}