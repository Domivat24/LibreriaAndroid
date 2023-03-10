package dam.curso2022.u2aev1.u6aev1listado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Locale;

public class AcercaDeActivity extends AppCompatActivity {
    WebView cosmere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se define el idioma según las preferencias locales
        SharedPreferences preferenciasCompartidas = getSharedPreferences("PreferenciasCompartidas", MODE_PRIVATE);
        String codigoIdioma = preferenciasCompartidas.getString("codigo_idioma", "es");
        setAppLocale(codigoIdioma);


        setContentView(R.layout.activity_acerca_de);
        cosmere = findViewById(R.id.web_Cosmere);
        final WebSettings settings = cosmere.getSettings();
        settings.setJavaScriptEnabled(true);
        cosmere.setWebViewClient(new WebViewClient());
        cosmere.loadUrl((String) getText(R.string.pag_web_cosmere));
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