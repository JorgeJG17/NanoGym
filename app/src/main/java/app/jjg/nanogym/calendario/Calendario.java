package app.jjg.nanogym.calendario;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import androidx.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import app.jjg.nanogym.R;
import app.jjg.nanogym.database.CaledarTL;
import app.jjg.nanogym.database.HistorialTL;
import app.jjg.nanogym.database.Modelo;
import app.jjg.nanogym.ventanasentrenar.ventana_entrenar;
import app.jjg.nanogym.ventanashistorial.ventanaHistorial;

public class Calendario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendario);
        revisarEntrenamientosCaducados();
        calendar();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    /*TODO:
            -PONER UN MENSAJE SEMANAL CON LO DÍAS ENTRENADO Y PROGRAMADOS DE LA SEMANA
            -Mirar si podemos hacer un diseño de los botone mas chulos estilos pixel
     */
    private void calendar(){
        // 1. Conectar con el XML
        MaterialCalendarView calendarView = findViewById(R.id.calendarView);

        // 2. Configurar el comportamiento inicial
        calendarView.setSelectedDate(CalendarDay.today()); // Selecciona el día de hoy al abrir
        // 1. Creamos una lista de días (por ahora a mano para probar)
        ArrayList<CalendarDay> diasEntrenados = new ArrayList<>();
        ArrayList<CalendarDay> diasFaltados = new ArrayList<>();
        ArrayList<CalendarDay> diasPendientes = new ArrayList<>();
        List<CaledarTL> c =  consultaBD();

        //diasEntrenados.add(CalendarDay.from(2026, 1, 20)); // 20 de Enero //Esta son fechas de PRUEBAA
        //diasEntrenados.add(CalendarDay.from(2026, 1, 22)); // 22 de Enero
        if(c!=null && !c.isEmpty()) {

            for (CaledarTL datos : c) {

                String date = datos.getDate();
                String color;
                //DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fecha = LocalDate.parse(date);

                //diasEntrenados.add(CalendarDay.from(fecha));

                switch (Integer.parseInt(datos.getestado())) {

                    case FALTADO:
                        //calendarView.addDecorator(new CalendarioDecorator(Color.parseColor("#F44336"), diasEntrenados));
                        diasFaltados.add(CalendarDay.from(fecha));
                        break;
                    case ENTRENADO:
                        //calendarView.addDecorator(new CalendarioDecorator(Color.parseColor("#4CAF50"), diasEntrenados));
                        diasEntrenados.add(CalendarDay.from(fecha));
                        break;
                    case PENDIENTE:
                        diasPendientes.add(CalendarDay.from(fecha));
                        //calendarView.addDecorator(new CalendarioDecorator(Color.parseColor("#E0E0E0"), diasEntrenados));
                        break;
                }

            }
        }

        if(diasFaltados!=null && !diasFaltados.isEmpty()){
            calendarView.addDecorator(new CalendarioDecorator(Calendario.this,R.drawable.circulo_rojo,diasFaltados));
        }

        if(diasEntrenados!=null && !diasEntrenados.isEmpty()){
            calendarView.addDecorator(new CalendarioDecorator(Calendario.this,R.drawable.circulo_verde,diasEntrenados));
        }

        if(diasPendientes!=null && !diasPendientes.isEmpty()){
            calendarView.addDecorator(new CalendarioDecorator(Calendario.this,R.drawable.circulo_gris,diasPendientes));
        }

        // 2. Le pasamos la lista al calendario con el color morado de tu app
        //calendarView.addDecorator(new CalendarioDecorator(Color.parseColor("#4CAF50"), diasEntrenados));

        // 3. Escuchar los clics del usuario
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // Esto sacará un mensaje con la fecha que toques
                f_seleccionada = date.getDate();//String.format("%02d/%02d/%04d", date.getDay(), date.getMonth() + 1,date.getYear());
                //Toast.makeText(MainActivity.this, "Día seleccionado: " + fechaSeleccionada, Toast.LENGTH_SHORT).show();

               if(diasEntrenados.contains(date) || diasFaltados.contains(date) || diasPendientes.contains(date)){
                   contiene = true;
               }
               else contiene = false;
            }
        });
    }

    //Consulta a la DB para sacar el historial, devuelve una lista de objetos HistorialTL
    private List<CaledarTL> consultaBD(){

        Modelo obj = new Modelo();
        Cursor resultados = obj.SeleccionarCalendario(Calendario.this); //Llamamos al SeleccionarCalendario para hacer la consulta en la db

        List<CaledarTL> c = new ArrayList<>(); //Lista donde vamos almacenar cada objeto HistorialTl es decir cada fila de nuestra futura tabla

        if (resultados != null && resultados.moveToFirst()) {
            do {
                // Obtén repes,peso,date del cursor del historial del ejercicio
                String date = resultados.getString(0);
                int estado = resultados.getInt(1);


                CaledarTL calendar_res = new CaledarTL();
                calendar_res.setDate(date); //Guardo la fecha
                calendar_res.setestado(Integer.toString(estado)); //Guardo estado

                // Crea un objeto Caledar y le añadimos el Caledario de ese ejercicio
                c.add(calendar_res);

            } while (resultados.moveToNext());  // Continúa hasta el siguiente resultado en el cursor
        }

        // Cierra el cursor después de usarlo
        if (resultados != null) {
            resultados.close();
        }

        return c; //devolvemos la lista
    }

    //bt annadir entrenamiento
    public void onEntrenamiento(View view){
        LocalDate fecha = LocalDate.now();

        if(f_seleccionada.isBefore(fecha)){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fecha no válida");
            builder.setMessage("No puedes añadir entrenamientos en el pasado.\n\nPor favor, selecciona el día de hoy o una fecha futura.");

            builder.setPositiveButton("Entendido", null);
            builder.show();
        } else if (contiene){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Día ocupado");
            builder.setMessage("Esta fecha ya contiene un entrenamiento.");
            builder.setPositiveButton("Entendido", null);
            builder.show();
        }else{

            Modelo obj = new Modelo();
            int resultados = obj.InsertarCalendario(Calendario.this,f_seleccionada);
            recreate(); //recargar la pantalla
        }

    }

    //bt annadir entrenamiento
    public void onEntrenar(View view){
        LocalDate fecha = LocalDate.now();

        if(!contiene){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Día sin entrenamiento");
            builder.setMessage("Esta fecha no contiene un entrenamiento.");
            builder.setPositiveButton("Entendido", null);
            builder.show();
        } else if (!f_seleccionada.isEqual(fecha)){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Solo hoy es posible");
            builder.setMessage("No puedes registrar sesiones en fechas pasadas ni futuras.\n\n" +
                    "El pasado ya está escrito y el futuro está por llegar. ¡Concéntrate en el entrenamiento de hoy!");
            builder.setPositiveButton("Entendido", null);
            builder.show();
        }else{

            Modelo obj = new Modelo();
            int resultados = obj.MarcarCalendario(Calendario.this,ENTRENADO,fecha);
            recreate(); //recargar la pantalla
        }

    }

    //bt borrar entrenamiento
    public void onBorrarEnt(View view){

        LocalDate fecha = LocalDate.now();
        if(!contiene){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No contiene un entrenamiento");
            builder.setMessage("No puedes borrar entrenamientos que no existen.\n\n");
            builder.setPositiveButton("Aceptar el reto", null);
            builder.show();
        } else if(f_seleccionada.isBefore(fecha) || f_seleccionada.isEqual(fecha)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Compromiso NanoGym");
            builder.setMessage("No puedes borrar entrenamientos pasados ni el de hoy.\n\n" +
                    "La planificación es una obligación. Si no entrenas, el sistema lo marcará como fallido. ¡No busques excusas!");
            builder.setPositiveButton("Aceptar el reto", null);
            builder.show();
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmación");
            builder.setMessage("¿Estas seguro de borrar este día de entrenamiento: " + f_seleccionada + "?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Modelo obj = new Modelo();
                    int resultados = obj.BorrarCalendario(Calendario.this, f_seleccionada);

                    //Si se ha eliminado correctamente devolvera 1
                    if (resultados == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Calendario.this);
                        builder.setTitle("Entrenamiento Borrado");
                        builder.setMessage("El día de entrenamiento se ha borrado correctamente");
                        builder.setPositiveButton("Entendido", null);
                        builder.show();

                        tiempoEspera.removeCallbacks(runnable); // Cancelamos la ejecución anterior (si hay alguna programada)
                        runnable = () -> { //En este lambda no hace falta poner el metodo run porque esta interfaz solo tiene ese metodo entcoes entiende que ese el que esta poniendo
                            recreate(); //recargar la pantalla
                        };
                        tiempoEspera.postDelayed(runnable, 1500);// Programamos la tarea para que se ejecute dentro de 1500 ms (1.5 segundo)

                    } else {
                        new AlertDialog.Builder(Calendario.this)
                                .setTitle("Error")
                                .setMessage("¡Ups! Algo salió mal. Por favor, infórmaselo al desarrollador. Recuerda que esta es una versión Beta.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() { //Si dice que no pues desactivamos el modo borrar
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
    }

    public void revisarEntrenamientosCaducados() {
        Modelo obj = new Modelo();
        LocalDate fecha = LocalDate.now();
        int resultados = obj.MarcarCalendario(Calendario.this,FALTADO,fecha); //Llamamos al SeleccionarCalendario para hacer la consulta en la db

        if (resultados == 1) {


        } else {
            new AlertDialog.Builder(Calendario.this)
                    .setTitle("Error")
                    .setMessage("¡Ups! Algo salió mal. Por favor, infórmaselo al desarrollador. Recuerda que esta es una versión Beta.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
    //CAMPOS DE CLASE
    private static final int FALTADO = 0;
    private static final int ENTRENADO = 1;
    private static final int PENDIENTE = 2;
    private Handler tiempoEspera = new Handler(); //Lo usamos para hacer el tiempo de espera de 1500 segundos y llamara al run de Runnable
    private Runnable runnable; //para utilizar su metodo run así parar ese trozo hasta que yo diga

    private LocalDate f_seleccionada = LocalDate.now(); //por defecto la fecha es actial la de hoy
    private boolean contiene;

}