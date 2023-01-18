package dam.curso2022.u2aev1.u6aev1listado;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pd;
    ArrayList<Libro> listLibros;
    RecyclerView recycler;
    AdaptadorLibros adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        listLibros = new ArrayList<>();

        adapter = new AdaptadorLibros(listLibros);
        recycler.setAdapter(adapter);
        adapter.setOnClickListener(view -> {
            Intent intent = new Intent(this, LibroDetalle.class);
            intent.putExtra("libro", listLibros.get(recycler.getChildAdapterPosition(view)));
            startActivity(intent);
        });
        new JsonCall().execute("https://raw.githubusercontent.com/Domivat24/LibreriaAndroid/master/app/src/main/java/dam/curso2022/u2aev1/u6aev1listado/books.json");


    }

    private void llenarLibros(String jsonstream) {

        byte[] decodedBytes;
        try {
            JSONArray json = new JSONArray(jsonstream);

            for (int i = 0; i < json.length(); i++) {
                decodedBytes = Base64.getDecoder().decode(json.getJSONObject(i).getString("imagen"));

                listLibros.add(new Libro(json.getJSONObject(i).getString("titulo"), json.getJSONObject(i).getString("sinopsis"), decodedBytes));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
                    Log.d("Response:", "> " + line); //respuesta en el log
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
            llenarLibros(result);
            adapter.notifyDataSetChanged();
        }
    }
}