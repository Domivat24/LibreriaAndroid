package dam.curso2022.u2aev1.u6aev1listado;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfiguracionActivity extends AppCompatActivity {
    private Spinner spIdiomas;
    private String[] arrayIdiomas;
    private ArrayAdapter<String> adaptadorIdiomas;
    private Switch swNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se define el idioma según las preferencias locales
        SharedPreferences preferenciasCompartidas = getSharedPreferences("PreferenciasCompartidas", MODE_PRIVATE);
        String codigoIdioma = preferenciasCompartidas.getString("codigo_idioma", "es");
        setAppLocale(codigoIdioma);

        getSupportActionBar().setTitle(getResources().getString(R.string.configMenu));
        setContentView(R.layout.activity_configuracion);

        //Spinner de idiomas
        spIdiomas = findViewById(R.id.sp_idioma);
        arrayIdiomas = getResources().getStringArray(R.array.array_idiomas);
        swNotificaciones = findViewById(R.id.sw_notificaciones);

        adaptadorIdiomas = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayIdiomas);
        spIdiomas.setAdapter(adaptadorIdiomas);
        //Listener del cambio de idioma
        spIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences preferencias = getSharedPreferences("PreferenciasCompartidas", MODE_PRIVATE);

                int idiomaAnterior = preferencias.getInt("idioma", 0);
                int idiomaActual = spIdiomas.getSelectedItemPosition();

                if (idiomaAnterior != idiomaActual) {
                    AlertDialog.Builder constructorDialogo = new AlertDialog.Builder(ConfiguracionActivity.this);
                    constructorDialogo.setMessage(getText(R.string.aviso_cambio_idioma_mensaje));
                    constructorDialogo.setPositiveButton(getText(R.string.aviso_cambio_idioma_aceptar), (dialog, which) -> {
                        dialog.cancel();
                    });
                    AlertDialog dialogoAvisoCambioIdioma = constructorDialogo.create();
                    dialogoAvisoCambioIdioma.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // Definimos los valores del usuario recogidos en las preferencias
        int idioma = preferenciasCompartidas.getInt("idioma", 0);
        spIdiomas.setSelection(idioma);
        // Notificaciones
        boolean notificaciones = preferenciasCompartidas.getBoolean("notificaciones", false);
        swNotificaciones = findViewById(R.id.sw_notificaciones);
        swNotificaciones.setChecked(notificaciones);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Guardamos las opciones seleccionadas por la persona usuaria en las SharedPreferences
        SharedPreferences preferencias = getSharedPreferences("PreferenciasCompartidas", MODE_PRIVATE);

        // Creación de un objeto Editor para escribir en las preferencias
        SharedPreferences.Editor editorPreferencias = preferencias.edit();

        //guardamos la posición del idioma en el spinner
        int idioma = spIdiomas.getSelectedItemPosition();
        editorPreferencias.putInt("idioma", idioma);

        //switch con la selección de los idiomas, se deja con switch en caso de que hayan más en el futuro
        switch (idioma) {
            case 1:
                editorPreferencias.putString("codigo_idioma", "en");
                break;
            case 0:
                editorPreferencias.putString("codigo_idioma", "es");
                break;
        }
        //guardo el valor de notificaciones
        editorPreferencias.putBoolean("notificaciones", swNotificaciones.isChecked());

        // Una vez hechos los cambios, guardamos los datos
        editorPreferencias.apply();

        //implementar valores en Firebase
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference refUsuario = db.getReference("usuarios");
        String keyFirebase = preferencias.getString("keyFirebase", "");
        refUsuario.orderByKey().equalTo(keyFirebase).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Map<String, Object> datos = new HashMap<>();
                            datos.put("notificaciones", swNotificaciones.isChecked());
                            datos.put("idioma", preferencias.getString("codigo_idioma", "es"));
                            refUsuario.child(keyFirebase).updateChildren(datos);
                            Toast.makeText(getApplicationContext(), getText(R.string.toast_Favorito).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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