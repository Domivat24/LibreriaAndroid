package dam.curso2022.u2aev1.u6aev1listado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Libro> listLibros;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        listLibros = new ArrayList<>();
        llenarLibros();
        AdaptadorLibros adapter = new AdaptadorLibros(listLibros);
        recycler.setAdapter(adapter);
        adapter.setOnClickListener(view -> {
            Intent intent = new Intent(this,LibroDetalle.class);
            intent.putExtra("libro", listLibros.get(recycler.getChildAdapterPosition(view)));
            startActivity(intent);

        });

    }

    private void llenarLibros() {
        listLibros.add(new Libro("Mistborn 1: El Imperio Final", "Durante mil años, han caído las cenizas y nada florece. Durante mil años, los Skaa han sido esclavizados y viven sumidos en un miedo inevitable. Durante mil años, el Lord Legislador reina con un poder absoluto gracias al terror, a sus poderes y a su inmortalidad. Le ayudan «obligadores» e «inquisidores», junto a la poderosa magia de la alomancia. Pero los nobles a menudo han tenido trato sexual con jóvenes skaa y, aunque la ley lo prohíbe, algunos de sus bastardos han sobrevivido y heredado los poderes alománticos: son los «nacidos de la bruma» (mistborns). Ahora, Kelsier, el «superviviente», el único que ha logrado huir de los Pozos de Hathsin, ha encontrado a Vin, una pobre chica skaa con mucha suerte… Tal vez los dos, unidos a la rebelión que los skaa intentan desde hace mil años, logren cambiar el mundo y la atroz dominación del Lord Legislador.",
                R.drawable.mistborn1));
        listLibros.add(new Libro("Mistborn 2: El Pozo de la Ascensión", "Durante mil años, han caído las cenizas y nada florece. Durante mil años, los skaa han sido esclavizados y han vivido sumidos en un miedo inevitable. Durante mil años, el Lord Legislador ha reinado con un poder absoluto gracias al terror y la divina invencibilidad que le otorga la poderosa magia de la alomancia. Pero vencer y matar al Lord Legislador fue la parte sencilla. El verdadero desafío será sobrevivir a las consecuencias de su caída. Tomar el poder tal vez resultó fácil, pero ¿qué ocurre después, cómo se usa? En ese mundo de aventura épica, la estrategia política y religiosa debe lidiar con los siempre misteriosos poderes de la alomancia.",
                R.drawable.mistborn2));
        listLibros.add(new Libro("Mistborn 3: El Héroe de las Eras", "Durante mil años los skaa han vivido esclavizados y sumidos en el miedo al Lord Legislador, que ha reinado con un poder absoluto gracias al terror y a la poderosa magia de la alomancia. Kelsier, el Superviviente, el único que ha logrado huir de los Pozos de Hathsin, encuentra a Vin, una pobre chica skaa con mucha suerte. Los dos se unen a la rebelión que los skaa intentan desde hace un milenio y vencen al Lord Legislador. Pero acabar con el Lord Legislador es la parte sencilla. El verdadero desafío consistirá en sobrevivir a las consecuencias de su caída. En El Héroe de las Eras se comprende el porqué de la niebla y las cenizas, las tenebrosas acciones del Lord Legislador y la naturaleza del Pozo de la Ascensión. Vin y el Rey Elend buscan en los últimos escondites de recursos del Lord Legislador y descubren el peligro que acecha a la humanidad. ¿Conseguirán detenerlo a tiempo?",
                R.drawable.mistborn3));
        listLibros.add(new Libro("Mistborn: Historia Secreta", "Nacidos de la Bruma: Historia secreta es una historia complementaria de la trilogía original. Como tal, contiene enormes spoilers de los libros El Imperio Final, el Pozo de la Ascensión y el Héroe de las Eras. También contiene spoilers muy pequeños del libro Brazales de Duelo. Nacidos de la Bruma: Historia secreta se basa en los personajes, los eventos y el desarrollo establecido en el mundo de la trilogía original. Leerlo sin ese trasfondo será un proceso confuso en el mejor de los casos. En resumen, este no es el lugar para comenzar su viaje a Nacidos de la Bruma. (Aunque si has leído la trilogía, pero ha pasado un tiempo, estarás bien, siempre que recuerdes a los personajes y la trama general de los libros). Decir algo más aquí corre el riesgo de revelar demasiado. Incluso el conocimiento de la existencia de esta historia es, de alguna manera, un spoiler. Siempre hay otro secreto.",
                R.drawable.mistbornh));
        listLibros.add(new Libro("Mistborn 4: Aleación de Ley", "Han pasado ya trescientos años desde los acontecimientos narrados en la primera trilogía de la saga y Scadrial se encuentra ahora cerca de la modernidad: ferrocarriles, canales, iluminación eléctrica y los primeros rascacielos invaden el planeta. Aunque la ciencia y la tecnología están alcanzando nuevos retos, la antigua magia de la alomancia continúa desempeñando un papel fundamental. En una zona conocida como los Áridos existen herramientas cruciales para aquellos hombres y mujeres que intentan establecer el orden y la justicia. Uno de estos hombres es Lord Waxillium Ladrian, experto en metales y en el uso de la alomancia y la feruquimia.\n" +
                "\n" +
                "Después de vivir veinte años en los Áridos, Wax se ha visto obligado, por una tragedia familiar, a volver a la metrópolis de Elendel. Sin embargo, y a su pesar, deberá guardar las armas y asumir las obligaciones que exige el hecho de estar rodeado de la clase noble. O al menos eso cree, ya que aún no sabe que las mansiones y las elegantes calles arboladas de la ciudad pueden ser incluso más peligrosas que las llanuras de los Áridos. Un skyline metálico de bruma, de ceniza y vapor conquista el cielo amenazando a todos aquellos que viven y luchan debajo de él.",
                R.drawable.mistborn4));
        listLibros.add(new Libro("Mistborn 5: Sombras de Identidad", "La sociedad de Nacidos de la Bruma ha evolucionado en una fusión de magia y tecnología en la que la economía se expande, la democracia se enfrenta a la corrupción y la religión se convierte en una potencia cultural cada vez más influyente, con cuatro fes distintas enfrentadas por la captación de conversos.\n" +
                "\n" +
                "Esta sociedad tan animada y optimista, aunque todavía tambaleante, se enfrenta ahora a su primera amenaza de terrorismo, crímenes cuyo objetivo es fomentar el descontento de la clase trabajadora y avivar las llamas de los conflictos religiosos. Wax y Wayne, con la asistencia de la adorable y brillante Marasi, deberán dar al traste con la conspiración antes de que las revueltas civiles frenen por completo el progreso de Scadrial.",
                R.drawable.mistborn5));
        listLibros.add(new Libro("Mistborn 6: Brazales de Duelo", "Brandon Sanderson regresa con la sexta entrega de Nacidos de la Bruma (Mistborn) Brazales de Duelo: legendarios brazales que portaba el lord Legislador hace siglos, hasta que la Guerrero de la Ascensión se los arrebató, precipitando su muerte. Dicen de ellos que contienen un poder increíble, aunque, como todo el mundo sabe, hace tiempo que se perdieron entre las brumas del tiempo. Solo que alguien acaba de encontrarlos. La cuenca de Elendel es un polvorín. El descontento de los trabajadores solo es la punta del iceberg; las diferencias son cada vez más irreconciliables entre la capital y las demás ciudades de la cuenca, ciudades que Elendel asegura gobernar mientras sus habitantes denuncian la opresión a la que se sienten sometidos. En medio de todo esto, llega a oídos de Waxillium Ladrian el rumor de que un académico kandra podría haber localizado los legendarios Brazales de Duelo, un arma capaz de sembrar la destrucción y dar al traste con el actual equilibrio de poder imperante en la cuenca.",
                R.drawable.mistborn6));
        listLibros.add(new Libro("Mistborn 7: El Metal Perdido", "Regresa a la segunda era de Scadrial, el mundo de Nacidos de la Bruma del escritor bestseller #1 del New York Times, Brandon Sanderson, que empezó con Aleación de Ley, y que ahora llega a su demoledora conclusión en El Metal Perdido.\n" +
                "\n" +
                "Durante años, el agente del orden fronterizo convertido en senador de la gran ciudad, Waxillium Ladrian, ha dado caza a la sombría organización conocida como el Grupo entre cuyos líderes se encuentran su tío y su hermana, después de que empezaran a secuestrar a personas con el poder de la Alomancia corriendo por sus venas. Cuando la detective Marasi Colms y su compañero Wayne encuentran armas almacenadas destinadas a la ciudad portuaria de Bilming, se abre una nueva vía de investigación. El conflicto entre Elendel y las ciudades exteriores no hace sino favorecer al Grupo, y sus tentáculos ahora llegan hasta el Senado de Elendel, cuya corrupción Wax y Steris creyeron haber expuesto. Ahora la ciudad de Bilming se encuentra todavía más involucrada.\n" +
                "\n" +
                "Después de que Wax descubre un nuevo tipo de material explosivo capaz de provocar una destrucción sin precedentes, y tras darse cuenta de que el Grupo ya debe tenerlo en su poder, un kandra inmortal al servicio del dios de Scadrial revela que el poder de Armonía se encuentra obstruido en Bilming. Ello implica que la ciudad ha caído bajo la influencia de otro dios: Trell, adorado por el Grupo. Y Trell no es el único factor en juego del vasto Cosmere. Marasi es reclutada por individuos de otro mundo con extrañas habilidades, que esgrimen que su misión es proteger Scadrial… a cualquier precio.\n" +
                "\n" +
                "Mañana por la noche, la visión de Armonía sobre posibilidades futuras llegan a su abrupto fin, y después de eso solo hay oscuridad. Es una carrera contra el tiempo, y Wax ha de elegir si dejar de lado su sinuosa relación con Dios para convertirse una vez más en la Espada de Armonía, algo para lo que el dios le ha criado desde pequeño. Si no aparece alguien para ser el héroe que Scadrial necesita, el planeta, junto con sus millones de habitantes, sucumbirá a una repentina y funesta ruina.",
                R.drawable.mistborn7));
        listLibros.add(new Libro("El Archivo de las Tormentas 1: El Camino de los Reyes", "Anhelo los días previos a la Última Desolación.\n" +
                "\n" +
                "Los días en que los Heraldos nos abandonaron y los Caballeros Radiantes se giraron en nuestra contra. Un tiempo en que aún había magia en el mundo y honor en el corazón de los hombres.\n" +
                "\n" +
                "El mundo fue nuestro, pero lo perdimos. Probablemente no hay nada más estimulante para las almas de los hombres que la victoria.\n" +
                "\n" +
                "¿O tal vez fue la victoria una ilusión durante todo ese tiempo? ¿Comprendieron nuestros enemigos que cuanto más duramente luchaban, más resistíamos nosotros? Quizá vieron que el fuego y el martillo tan solo producían mejores espadas. Pero ignoraron el acero durante el tiempo suficiente para oxidarse.\n" +
                "\n" +
                "Hay cuatro personas a las que observamos. La primera es el médico, quien dejó de curar para convertirse en soldado durante la guerra más brutal de nuestro tiempo. La segunda es el asesino, un homicida que llora siempre que mata. La tercera es la mentirosa, una joven que viste un manto de erudita sobre un corazón de ladrona. Por último está el alto príncipe, un guerrero que mira al pasado mientras languidece su sed de guerra.\n" +
                "\n" +
                "El mundo puede cambiar. La potenciación y el uso de las esquirlas pueden aparecer de nuevo, la magia de los días pasados puede volver a ser nuestra. Esas cuatro personas son la clave.",
                R.drawable.archivo1));
        listLibros.add(new Libro("El Archivo de las Tormentas 2: Palabras Radiantes", "Los Caballeros Radiantes deben volver a alzarse.\n" +
                "\n" +
                "Los antiguos juramentos por fin se han pronunciado. Los hombres buscan lo que se perdió. Temo que la búsqueda los destruya.\n" +
                "\n" +
                "Es la naturaleza de la magia. Un alma rota tiene grietas donde puede colarse algo más. Las potencias, los poderes de la creación misma, pueden abrazar un alma rota, pero también pueden ampliar sus fisuras.\n" +
                "\n" +
                "El Corredor del Viento está perdido en una tierra quebrada, en equilibro entre la venganza y el honor. La Tejedora de Luz, lentamente consumida por su pasado, busca la mentira en la que debe convertirse. El Forjador de Vínculos, nacido en la sangre y la muerte, se esfuerza ahora por reconstruir lo que fue destruido. La Exploradora, a caballo entre los destinos de dos pueblos, se ve obligada a elegir entre una muerte lenta y una terrible traición a todo en lo que cree.\n" +
                "\n" +
                "Ya es hora de despertarlos, pues acecha la eterna tormenta.\n" +
                "\n" +
                "Y el asesino ha llegado.",
                R.drawable.archivo2));
        listLibros.add(new Libro("El Archivo de las Tormentas 3: Juramentada", "La humanidad se enfrenta a una nueva Desolación con el regreso de los Portadores del Vacío, un enemigo tan grande en número como en sed de venganza. La victoria fugaz de los ejércitos alezi de Dalinar Kholin ha tenido consecuencias: el enemigo parshendi ha convocado la violenta tormenta eterna, que arrasa el mundo y hace que los hasta ahora pacíficos parshmenios descubran con horror que llevan un milenio esclavizados por los humanos. Al mismo tiempo, en una desesperada huida para alertar a su familia de la amenaza, Kaladin se pregunta si la repentina ira de los parshmenios está justificada.\n" +
                "\n" +
                "Entretanto, en la torre de la ciudad de Urithiru, a salvo de la tormenta, Shallan Davar investiga las maravillas de la antigua fortaleza de los Caballeros Radiantes y desentierra oscuros secretos que acechan en las profundidades. Dalinar descubre entonces que su sagrada misión de unificar su tierra natal de Alezkar era corta de miras. A menos que todas las naciones sean capaces de unirse y dejar de lado el pasado sangriento de Dalinar, ni siquiera la restauración de los Caballeros Radiantes conseguirá impedir el fin de la civilización.",
                R.drawable.archivo3));
        listLibros.add(new Libro("El Archivo de las Tormentas 4: El Ritmo de la Guerra", "Hay secretos que hemos guardado mucho tiempo. Vigilantes. Insomnes. Eternos. Y pronto dejarán de ser nuestros.\n" +
                "\n" +
                "La Una que es Tres busca, sin saberlo, el alma capturada. El spren aprisionado, olvidado hace mucho tiempo. ¿Puede liberar su propia alma a tiempo de hallar el conocimiento que condena a todos los pueblos de Roshar?\n" +
                "\n" +
                "El Soldado Caído acaricia y ama la lanza, incluso mientras el arma hiende su propia carne. Camina siempre hacia delante, siempre hacia la oscuridad, sin luz. No puede llevar consigo a nadie, salvo aquello que él mismo puede avivar.\n" +
                "\n" +
                "La Hermana Derrumbada comprende sus errores y piensa que ella misma es un error. Parece muy alejada de sus antepasados, pero no comprende que son quienes la llevan a hombros. Hacia la victoria, y hacia ese silencio, el más importante de todos.\n" +
                "\n" +
                "Y la Madre de Máquinas, la más crucial de todos ellos, danza con mentirosos en un gran baile. Debe desenmascararlos, alcanzar sus verdades ocultas y entregarlas al mundo. Tiene que reconocer que las peores mentiras son las que se cuenta a sí misma.\n" +
                "\n" +
                "Si lo hace, nuestros secretos por fin se convertirán en verdades.",
                R.drawable.archivo4));
    }
}