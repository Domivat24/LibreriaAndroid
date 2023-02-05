package dam.curso2022.u2aev1.u6aev1listado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdaptadorLibros.ItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog pd;
    ArrayList<Libro> listLibros;
    RecyclerView recycler;
    AdaptadorLibros adapter;
    public static LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        listLibros = new ArrayList<>();

        //prueba Firebase
        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, String> datos = new HashMap<>();
        datos.put("nombre", "Juan");
        datos.put("apellido", "Pérez");
        databaseReference.child("usuarios").child("123").setValue(datos);

        //Copiada de almacenar las imágenes en cache
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        adapter = new AdaptadorLibros(listLibros);
        recycler.setAdapter(adapter);

        // Lo registramos para que soporte menús contextuales
        registerForContextMenu(recycler);

        adapter.setClickListener(this);
        //Recojo los datos del de los libros del json
        new JsonCall().execute("https://raw.githubusercontent.com/Domivat24/LibreriaAndroid/master/app/src/main/java/dam/curso2022/u2aev1/u6aev1listado/books.json");


    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, LibroDetalle.class);
        //envio el titulo y la sinopsis en el extra del intent
        intent.putExtra("titulo", adapter.getItem(position).getTitulo());
        intent.putExtra("sinopsis", adapter.getItem(position).getSinopsis());

        //Si defino el identificador del cache, no se sobrescribe al almacenar otro Bitmap sobre este, por lo que
        // ha hecho falta definir una variable id en libro para almacenar en un string único por libro
        String portada = "portada" + adapter.getItem(position).getId();
        intent.putExtra("portada", portada);
        addBitmapToMemoryCache(portada, adapter.getItem(position).getPortada());

        Toast.makeText(this, "Has hecho clic en " + adapter.getItem(position).getTitulo() + " de la fila " + position, Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(getApplicationContext(), "Has hecho clic en Añadir a favoritos " + item.getGroupId(), Toast.LENGTH_LONG).show();

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Control de apertura/cierre del NavigationDrawer
        /*
         if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
         */


        // Control de opciones de la action bar
        int id = item.getItemId();
        if (id == R.id.configuracion_actionbar) {
            Toast.makeText(getApplicationContext(), "Entrando a configuración", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, ConfiguracionActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.acercade_actionbar) {
            Toast.makeText(getApplicationContext(), "Entrando a Acerca de...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, AcercaDeActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //añade el bitmap a un cache, similar al intent, lo recoge según el valor key
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    //recoge el bitmap según el String de busqueda key
    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private void llenarLibros(String jsonstream) {

        byte[] decodedBytes;
        try {
            JSONArray json = new JSONArray(jsonstream);

            for (int i = 0; i < json.length(); i++) {
                decodedBytes = Base64.getDecoder().decode(json.getJSONObject(i).getString("imagen"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                listLibros.add(new Libro(json.getJSONObject(i).getString("titulo"), json.getJSONObject(i).getString("sinopsis"), bitmap));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //clase asyncrona que se encarga de recoger los datos del json y mostrar un progress Dialog
//mientras estos recursos son cargados, inhabilitando al usuario de hacer cualquier otra cosa durante
    private class JsonCall extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Cargando recursos");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    //respuesta en el log
                    Log.d("Response:", "> " + line);
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            //rellenamos el adapter con los datos cargados
            llenarLibros(result);
            // y notificamos al adapter de que han habido cambios
            adapter.notifyDataSetChanged();
        }
    }
}