package dam.curso2022.u2aev1.u6aev1listado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ProgressDialog pd;
    ArrayList<Libro> listLibros;
    RecyclerView recycler;
    AdaptadorLibros adapter;
    public static LruCache<String, Bitmap> mMemoryCache;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences preferenciasCompartidas;
    String codigoIdioma, keyFirebase;
    FirebaseDatabase db;
    ArrayList<String> favoritos = new ArrayList<String>();
    ArrayList<String> wishlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuración de idioma
        preferenciasCompartidas = getSharedPreferences("PreferenciasCompartidas", MODE_PRIVATE);
        codigoIdioma = preferenciasCompartidas.getString("codigo_idioma", "es");
        keyFirebase = preferenciasCompartidas.getString("keyFirebase", "");
        setAppLocale(codigoIdioma);

        //Iniciamos firebase
        FirebaseApp.initializeApp(getApplicationContext());
        db = FirebaseDatabase.getInstance();

        //recojo de firebase los libros favoritos y deseados


        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        listLibros = new ArrayList<>();

        // Configuramos el NavigationDrawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_abrir, R.string.nav_cerrar);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Mostramos siempre el icono del Navigation Drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Añadimos la escucha para el NavigationDrawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Manejo de firebase, si en las preferencias locales no se ha definido el id del usuario generado por firebase, lo generamos
        if (keyFirebase.equals("")) {
            FirebaseApp.initializeApp(getApplicationContext());
            DatabaseReference databaseReference = db.getReference();
            Map<String, String> datos = new HashMap<>();
            Map<String, String> favoritos = new HashMap<>();

            SharedPreferences.Editor editorPreferencias = preferenciasCompartidas.edit();
            //Guardo el identificador en una variable, pues cada instancia de push parece generar una clave diferente
            String key = databaseReference.push().getKey();
            editorPreferencias.putString("keyFirebase", key);

            editorPreferencias.commit();

            //defino de nuevo la variable id con la llave recién generada, pues sinó los cambios no tendrían efecto hasta destruir la activity
            keyFirebase = preferenciasCompartidas.getString("keyFirebase", "");
            datos.put("idioma", "es");
            datos.put("notificaciones", "false");


            databaseReference.child("usuarios").child(key).setValue(datos);
        } else {
            DatabaseReference refUsuario = db.getReference("usuarios");
            refUsuario.orderByKey().equalTo(keyFirebase).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //Si se encuentran valores, se añaden a la lista local, ya que sino se definirá el arraylist como nulo y dará problemas
                                // de ejecución al llamarlo
                                HashMap<String, String> favoritosDB;
                                //try y catch teoricamente innecesario, pero los dejo por si acaso porque la consulta me ha devuelto tanto una lista como Map,
                                // y aunque no haga nada pues al poner una condición deberíoa devolver siempre map, me parece útil tenerlo en cuenta:
                                //https://stackoverflow.com/a/66738126
                                try {
                                    favoritosDB = ((HashMap<String, String>) snapshot.child(keyFirebase).child("favoritos").getValue());
                                    if (favoritosDB != null) {
                                        favoritos = new ArrayList<>(favoritosDB.values());
                                    }
                                } catch (ClassCastException e) {
                                    favoritos = (ArrayList<String>) snapshot.child(keyFirebase).child("favoritos").getValue();
                                }
                                try {
                                    HashMap<String, String> wishlistDB = ((HashMap<String, String>) snapshot.child(keyFirebase).child("wishlist").getValue());
                                    if (wishlistDB != null) {
                                        wishlist = new ArrayList<>(wishlistDB.values());
                                    }
                                } catch (ClassCastException e) {
                                    wishlist = (ArrayList<String>) snapshot.child(keyFirebase).child("wishlist").getValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

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


        //Recojo los datos del de los libros del json
        new JsonCall().execute("https://raw.githubusercontent.com/Domivat24/LibreriaAndroid/master/app/src/main/res/values/books/books.json");

        //item click listener
        adapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(getApplicationContext(), LibroDetalle.class);
            //envio el titulo y la sinopsis en el extra del intent
            intent.putExtra("titulo", adapter.getItem(position).getTitulo());
            intent.putExtra("sinopsis", adapter.getItem(position).getSinopsis());

            //Si defino el identificador del cache, no se sobrescribe al almacenar otro Bitmap sobre este, por lo que
            // ha hecho falta definir una variable id en libro para almacenar en un string único cada libro almacenado en caché
            int portada = adapter.getItem(position).getId();
            intent.putExtra("portada", portada);
            addBitmapToMemoryCache(String.valueOf(portada), adapter.getItem(position).getPortada());

            startActivity(intent);

        });

        //Creación del menú contextual, definirá un menú u otro en función de si el usuario tiene los libros en favoritos/deseados
        adapter.setOnCreateContextMenu((menu, view, menuInfo, position, menuItemClickListener) -> {
            //Compruebo si el libro está en la lista de favoritos
            //He de sepearar en dos ifs pues si hago el contains sin comprobar que es nulo, salta error

            if (favoritos.contains(String.valueOf(position))) {
                menu.add((int) position, 123, 0, R.string.ctx_Quitar_Favoritos).setOnMenuItemClickListener(menuItemClickListener);//groupId, itemId, order, title
            } else {
                menu.add(position, 121, 0, R.string.ctx_anadir_favoritos).setOnMenuItemClickListener(menuItemClickListener);//groupId, itemId, order, title
            }

            //Compruebo si el libro está en la lista de deseados

            if (wishlist.contains(String.valueOf(position))) {
                menu.add(position, 124, 0, R.string.ctx_Quitar_Wishlist).setOnMenuItemClickListener(menuItemClickListener);//groupId, itemId, order, title
            } else {
                menu.add(position, 122, 0, R.string.ctx_anadir_wishlist).setOnMenuItemClickListener(menuItemClickListener);//groupId, itemId, order, title
            }
        });

        adapter.setOnContextMenuItemClickListener((menuItem, position) -> {
            int id = menuItem.getItemId();
            //position == menuItem.getGroupId(), ya que es la posicion del getAdapterPosition
            switch (id) {
                //añadir a favoritos
                case 121:
                    DatabaseReference refUsuario = db.getReference("usuarios");
                    refUsuario.orderByKey().equalTo(keyFirebase).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Map<String, Object> favorito = new HashMap<>();
                                        favorito.put(String.valueOf(position), String.valueOf(position));
                                        refUsuario.child(keyFirebase).child("favoritos").updateChildren(favorito);
                                        favoritos.add(String.valueOf(position));
                                        Toast.makeText(getApplicationContext(), getText(R.string.toast_Favorito).toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                    break;
                //añadir a wishlist
                case 122:
                    refUsuario = db.getReference("usuarios");
                    refUsuario.orderByKey().equalTo(keyFirebase).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Map<String, Object> deseado = new HashMap<>();
                                        deseado.put(String.valueOf(position), String.valueOf(position));
                                        refUsuario.child(keyFirebase).child("wishlist").updateChildren(deseado);
                                        wishlist.add(String.valueOf(position));
                                        Toast.makeText(getApplicationContext(), getText(R.string.toast_Wishlist).toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                    break;
                //quitar de favoritos
                case 123:
                    refUsuario = db.getReference("usuarios");
                    refUsuario.orderByKey().equalTo(keyFirebase).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        refUsuario.child(keyFirebase).child("favoritos").child(String.valueOf(position)).removeValue();
                                        favoritos.remove(String.valueOf(position));
                                        Toast.makeText(getApplicationContext(), getText(R.string.toast_Quitar_Favorito).toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                    break;
                //quitar de wishlist
                case 124:
                    refUsuario = db.getReference("usuarios");
                    refUsuario.orderByKey().equalTo(keyFirebase).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        refUsuario.child(keyFirebase).child("wishlist").child(String.valueOf(position)).removeValue();
                                        wishlist.remove(String.valueOf(position));
                                        Toast.makeText(getApplicationContext(), getText(R.string.toast_Quitar_Wishlist).toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                    break;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_Cosmere: {
                Toast.makeText(this, getText(R.string.toast_Guia), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, GuiaLectura.class));
                break;
            }
            case R.id.nav_inicio: {
                // hacer algo
                Toast.makeText(getApplicationContext(), getText(R.string.toast_Inicio), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_favoritos: {
                // TODO muestra lista de los libros en favoritos
                Toast.makeText(getApplicationContext(), getText(R.string.toast_Favoritos_View), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_wishlist: {
                // TODO muestra lista de los libros en deseados
                Toast.makeText(getApplicationContext(), getText(R.string.toast_Wishlist_View), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_cerrar_sesion: {
                // hacer algo
                Toast.makeText(getApplicationContext(), getText(R.string.toast_CerrarSesion), Toast.LENGTH_SHORT).show();
                break;
            }
        }
        // Cerramos el NavigationDrawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //guardar en Firebase

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Control de apertura/cierre del NavigationDrawer
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Control de opciones de la action bar
        int id = item.getItemId();
        if (id == R.id.configuracion_actionbar) {
            Toast.makeText(getApplicationContext(), getText(R.string.toast_Config), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, ConfiguracionActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.acercade_actionbar) {
            Toast.makeText(getApplicationContext(), getText(R.string.toast_AcercaDe), Toast.LENGTH_LONG).show();
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

            //Si la interfaz debe estar en inglés, meto los tíutulos hardcodeados del string.xml, pues de base, estos
            // no se traducían bien con la traducción automática (esperable). Además,  meter el translator en el adapter me parecía
            // que iba a ser demasiada carga a la api y demasiado engorroso de hacer para lo que iba a aportar
            if (codigoIdioma.equals("en")) {
                for (int i = 0; i < json.length(); i++) {
                    decodedBytes = Base64.getDecoder().decode(json.getJSONObject(i).getString("imagen"));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    listLibros.add(new Libro(getResources().getStringArray(R.array.title_Books_en)[i], json.getJSONObject(i).getString("sinopsis"), bitmap));
                }
            } else {
                for (int i = 0; i < json.length(); i++) {
                    decodedBytes = Base64.getDecoder().decode(json.getJSONObject(i).getString("imagen"));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                    listLibros.add(new Libro(json.getJSONObject(i).getString("titulo"), json.getJSONObject(i).getString("sinopsis"), bitmap));
                }
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
            pd.setMessage(getText(R.string.loadingData));
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

    // Manejo al intentar salir de la app
    public void onBackPressed() {
        // Crear el objeto constructor de alerta AlertDialog.Builder
        AlertDialog.Builder constructorAlerta = new AlertDialog.Builder(MainActivity.this);

        // Establecer título y mensaje
        constructorAlerta.
                setTitle(getText(R.string.txt_salir_app_titulo)).
                setMessage(getText(R.string.txt_salir_app_mensaje));

        // Hacer que no se pueda dejar el diálogo sin pulsar una opción
        constructorAlerta.setCancelable(false);

        // Establecer botón de respuesta positiva
        constructorAlerta.setPositiveButton(getText(R.string.txt_si), (dialog, which) -> {
            finish();
        });

        // Establecer botón de respuesta negativa
        constructorAlerta.setNegativeButton(getText(R.string.txt_no), (dialog, which) -> {
            dialog.cancel();
        });

        // Crear y mostrar diálogo de alerta
        AlertDialog dialogoAlertaSalir = constructorAlerta.create();
        dialogoAlertaSalir.show();
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