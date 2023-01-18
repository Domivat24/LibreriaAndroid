package dam.curso2022.u2aev1.u6aev1listado;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        Bitmap bitmap = BitmapFactory.decodeByteArray(libro.getPortada(), 0, libro.getPortada().length);
        portada.setImageBitmap(bitmap);
        titulo.setText(libro.getTitulo());
        sinopsis.setText(libro.getSinopsis());

        sinopsis.setMovementMethod(new ScrollingMovementMethod());
    }
}