package dam.curso2022.u2aev1.u6aev1listado;

import static dam.curso2022.u2aev1.u6aev1listado.MainActivity.getBitmapFromMemCache;

import android.graphics.Bitmap;
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
        //recojo los datos del intent y del cache
        titulo.setText(getIntent().getStringExtra("titulo"));
        sinopsis.setText(getIntent().getStringExtra("sinopsis"));
        Bitmap bitmap = getBitmapFromMemCache(getIntent().getStringExtra("portada"));
        portada.setImageBitmap(bitmap);

        sinopsis.setMovementMethod(new ScrollingMovementMethod());
    }


}