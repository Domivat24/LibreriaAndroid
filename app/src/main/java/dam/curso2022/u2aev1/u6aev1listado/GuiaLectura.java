package dam.curso2022.u2aev1.u6aev1listado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.ortiz.touchview.TouchImageView;

import java.util.Locale;

public class GuiaLectura extends AppCompatActivity {
    TouchImageView tvGuia;
    TextView tvLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se define el idioma según las preferencias locales
        SharedPreferences preferenciasCompartidas = getSharedPreferences("PreferenciasCompartidas", MODE_PRIVATE);
        String codigoIdioma = preferenciasCompartidas.getString("codigo_idioma", "es");
        setAppLocale(codigoIdioma);

        setContentView(R.layout.activity_guia_lectura);
        tvGuia = findViewById(R.id.tv_Guia);
        //defino la guía en función del idioma
        if (codigoIdioma.equals("en")) {
            tvGuia.setImageResource(R.drawable.guia_en);
        } else {
            tvGuia.setImageResource(R.drawable.guia_es);
        }
        tvLink = findViewById(R.id.tvLink);
        //hacemos que el enlace del TextView se lance en el navegador
        tvLink.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setAppLocale(String localeCode) {
        Locale myLocale = new Locale(localeCode);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        Locale.setDefault(myLocale);
        conf.setLayoutDirection(myLocale);
        res.updateConfiguration(conf, dm);
    }
}