package dam.curso2022.u2aev1.u6aev1listado;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionActivity extends AppCompatActivity {
    private Spinner spIdiomas;
    private String[] arrayIdiomas;
    private ArrayAdapter<String> adaptadorIdiomas;
    private Switch swNotificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        getSupportActionBar().setTitle(getResources().getString(R.string.configMenu));

        spIdiomas = (Spinner) findViewById(R.id.sp_idioma);
        arrayIdiomas = getResources().getStringArray(R.array.array_idiomas);

        adaptadorIdiomas = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayIdiomas);
        spIdiomas.setAdapter(adaptadorIdiomas);
    }
}