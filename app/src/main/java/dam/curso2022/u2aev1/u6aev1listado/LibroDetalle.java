package dam.curso2022.u2aev1.u6aev1listado;

import static android.content.ContentValues.TAG;
import static dam.curso2022.u2aev1.u6aev1listado.MainActivity.getBitmapFromMemCache;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Locale;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.google.gson.JsonParser;

import org.json.JSONObject;

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
        //Recojo el idioma
        SharedPreferences preferenciasCompartidas = getSharedPreferences("PreferenciasCompartidas", MODE_PRIVATE);
        String codigoIdioma = preferenciasCompartidas.getString("codigo_idioma", "es");
        //Y lo traduzco si hiciera falta
        if (codigoIdioma.equals("en")) {
            try {
                new TranslatorText().Post(getIntent().getStringExtra("sinopsis"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            titulo.setText(getIntent().getStringExtra("titulo"));
            sinopsis.setText(getIntent().getStringExtra("sinopsis"));
        }

        //recojo los datos del intent y del cache


        Bitmap bitmap = getBitmapFromMemCache(String.valueOf(getIntent().getIntExtra("portada", 0)));
        portada.setImageBitmap(bitmap);

        sinopsis.setMovementMethod(new ScrollingMovementMethod());
    }

    //Clase que se encarga de traducir las cadenas del libro utilizando la api de microsoft azure translator
    public class TranslatorText {
        //La llave de la API de Azure la almaceno en el archivo local.properties del proyecto
        private final String key = BuildConfig.API_KEY_AZURE;
        public String endpoint = "https://api.cognitive.microsofttranslator.com";
        public String route = "/translate?api-version=3.0&from=es&to=en";
        public String url = endpoint.concat(route);

        // location, also known as region.
        // required if you're using a multi-service or regional (not global) resource. It can be found in the Azure portal on the Keys and Endpoint page.
        private final String location = "westeurope";

        // Instantiates the OkHttpClient.
        OkHttpClient client = new OkHttpClient();

        // This function performs a POST request.
        public void Post(String cadenaSinopsis) throws IOException {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,
                    //si quisiera traducir tambi??n el t??tulo: "[{\"Text\": \"" + cadenaTitulo + "\"},"+
                    "[{\"Text\": \"" + cadenaSinopsis + "\"}]");
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Ocp-Apim-Subscription-Key", key)
                    // location required if you're using a multi-service or regional (not global) resource.
                    .addHeader("Ocp-Apim-Subscription-Region", location)
                    .addHeader("Content-type", "application/json")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseBody responseBody = response.body();
                        String res = responseBody.string();
                        Log.d(TAG, "onResponse: " + res);
                        JsonArray translations = (JsonArray) JsonParser.parseString(res);
                        sinopsis.setText(translations.get(0).getAsJsonObject().get("translations").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString());
                        //si quisiera traducir m??s valores, como el t??tulo:
                        //titulo.setText(translations.get(1).getAsJsonObject().get("translations").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString());
                        //en cambio, lo defino seg??n el id pasado por el extra y el valor de resources
                        titulo.setText((getResources().getStringArray(R.array.title_Books_en)[getIntent().getIntExtra("portada", 0)]));
                    }
                }
            });

        }
    }
}

