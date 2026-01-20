package app.jjg.nanogym.pesaje;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import app.jjg.nanogym.R;
import app.jjg.nanogym.database.Modelo;
import app.jjg.nanogym.database.PesajeTL;

public class ventanaPesaje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_pesaje);

        pintarPesaje(); //Pintamos la tabla del historial del ejercicio seleccionado

        //TASK 15
        if(contador != 1){ //Para que cuando llegue solo 1 registro pues que no pinte el grafico

            pintarGrafico(); //Pintamos el grafico del historial obtenido
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Metodo para pintar la tabla del Pesjae  Fecha / Peso
    private void pintarPesaje(){

        List<PesajeTL> h = consultaBD(); //Llamamos a la consulta que nos devolvera una lista donde almacena objetos de tipo HistorialTL
        tableLayout = findViewById(R.id.tableLayout); //Creamos nuestra tabla
        int contadorArray = h.size()-1; //Un contador para añadir los datos al contrario a como nos llega, recordar que llegan en orden del mas nuevo al mas antiguo

        for (PesajeTL datos : h) {

            TableRow tablaPesaje = new TableRow(this);
            //***************************************************************************************************************************************

            //Voy a rellenar la arraylist de los puntos del grafico del peso y la array de las fechas
            //puntos.add(new Entry((contador), Float.parseFloat(datos.getPeso())));
            peso[contadorArray] = datos.getPeso();
            fechas[contadorArray] = datos.getDate();
            contadorArray--;

            // Crear las celdas para cada fila
            EditText textViewDate = new EditText(this);
            textViewDate.setText(datos.getDate());  // Mostrar el nombre de la rutina (Las demas explicaciones de los siguiente metodos llamado estan en Peso y RM)
            textViewDate.setBackgroundResource(R.drawable.border_tabla);
            textViewDate.setPadding(8, 8, 8, 8);
            textViewDate.setEnabled(false);       // No editable
            textViewDate.setFocusable(false);     // No se enfoca
            textViewDate.setClickable(false);     // No se puede clickar
            textViewDate.setSingleLine(true);

            //***************************************************************************************************************************************

            EditText textViewPeso = new EditText(this);
            textViewPeso.setText(datos.getPeso());  // Mostrar las repeticiones (Las demas explicaciones de los siguiente metodos llamado estan en Peso y RM)
            textViewPeso.setBackgroundResource(R.drawable.border_tabla);
            textViewPeso.setPadding(8, 8, 8, 8);
            textViewPeso.setSingleLine(true);
            textViewPeso.setEnabled(false);       // No editable
            textViewPeso.setFocusable(false);     // No se enfoca
            textViewPeso.setClickable(false);     // No se puede clickar
            textViewPeso.setGravity(Gravity.CENTER);

            // Agregar las celdas a la fila
            tablaPesaje.addView(textViewDate);
            tablaPesaje.addView(textViewPeso);

            // Agregar la fila a la tabla
            tableLayout.addView(tablaPesaje);

            contador++;

            if(contador == 7){ //Para que solo muestre los 5 ultimos cambios
                break;
            }
        }

    }

    //Consulta a la DB para sacar el pesaje, devuelve una lista de objetos PesajeTL
    private List<PesajeTL> consultaBD(){

        Modelo obj = new Modelo();
        Cursor resultados = obj.SeleccionarPesaje(ventanaPesaje.this); //Llamamos al SeleccionarHistorial para hacer la consulta en la db

        List<PesajeTL> h = new ArrayList<>(); //Lista donde vamos almacenar cada objeto HistorialTl es decir cada fila de nuestra futura tabla

        if (resultados != null && resultados.moveToFirst()) {
            do {
                int peso = resultados.getInt(1);
                String date = resultados.getString(2);

                PesajeTL hisPesa = new PesajeTL();
                hisPesa.setPeso(Integer.toString(peso)); //Guardo peso
                hisPesa.setDate(date); //Guardo la fecha

                // Crea un objeto Historial y le añadimos el historial de ese ejercicio
                h.add(hisPesa);

            } while (resultados.moveToNext());  // Continúa hasta el siguiente resultado en el cursor
        }

        // Cierra el cursor después de usarlo
        if (resultados != null) {
            resultados.close();
        }

        return h; //devolvemos la lista
    }


    //Metodo que usamos para pintar el grafico, estas clases que usamos vienen de la libreria MPAndroidChart de github
    private void pintarGrafico(){

        //Este for lo usamos para almacenar la array en los puntos correctamente desde 0 al X pero con el orden que pusimos en la array

        for(int i = 0; i<contador; i++){

            puntos.add(new Entry(i, Float.parseFloat(peso[i])));
        }

        //Este objeto con su metodo para formatear las fechas en un valor para los puntos 0,1,2,3,4,5 que hemos puesto antes en el eje x
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < fechas.length) {
                    return fechas[index];
                } else {
                    return "";
                }
            }
        };

        grafico = findViewById(R.id.lineChart); // Conectar nuestra variable con el grafico del xml

        XAxis xAxis = grafico.getXAxis();
        xAxis.setGranularity(1f);  // Para que no muestre valores intermedios
        xAxis.setValueFormatter(formatter);
        xAxis.setTextColor(Color.WHITE); //Color eje x
        xAxis.setAvoidFirstLastClipping(true); //para que tenga espacion en eje x y se vean bien las fechas de los extremos


        YAxis leftAxis = grafico.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE); //Color eje y

        YAxis rightAxis = grafico.getAxisRight();
        rightAxis.setEnabled(false); //ocultar el de la derecha

        //Crear los datos del gráfico
        /*ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 1)); // X=0, Y=1
        entries.add(new Entry(1, 3)); // X=1, Y=3
        entries.add(new Entry(2, 2)); // etc.
        entries.add(new Entry(3, 5));
        entries.add(new Entry(4, 3));*/

        //Crear la linea de los puntos
        LineDataSet dataSet = new LineDataSet(puntos, "Peso (kg)");

        //Personalizar la linea del grafico
        dataSet.setLineWidth(2f); // Grosor de la línea
        dataSet.setCircleRadius(5f); // Tamaño de los puntos
        dataSet.setDrawValues(false); // No mostrar los números sobre los puntos

        // Color de línea y puntos
        dataSet.setColor(Color.parseColor("#ffffff")); //linea
        dataSet.setCircleColor(Color.parseColor("#e22929")); //puntos

        //Objeto que necesita nuestro grafico para mostrar lineas y puntos
        LineData lineData = new LineData(dataSet); //Añadimos los datos al objeto
        grafico.setData(lineData); //Añadimos el objeto a nuestro grafico

        //Descripcion del grafico
        Description desc = new Description();
        desc.setText(""); // Vacío = no mostrar nada
        grafico.setDescription(desc);

        //Dibujar el grafico
        grafico.invalidate();
    }

    public void onPesaje(View view){
        // 1. Inflar el diseño personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_contacto, null);

        EditText etPeso = dialogView.findViewById(R.id.etPeso);
        EditText etFecha = dialogView.findViewById(R.id.etFecha);
        CheckBox cbNoFecha = dialogView.findViewById(R.id.cbNoFecha);
        // Configurar el CheckBox para deshabilitar el campo de fecha
        cbNoFecha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etFecha.setEnabled(false); // Bloquea el campo
                etFecha.setText("");       // Opcional: limpia lo que haya escrito
            } else {
                etFecha.setEnabled(true);  // Lo vuelve a habilitar
            }
        });

        // 2. Construir el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registro de Peso");
        builder.setView(dialogView);

        // Botón de Aceptar
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String peso = etPeso.getText().toString();
            String fecha = cbNoFecha.isChecked() ? "Sin fecha" : etFecha.getText().toString();

            LocalDate fecha_act = LocalDate.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fecha_act.format(formato);

            // Aquí puedes procesar los datos (guardar en BD o mostrar en consola)
            //Toast.makeText(this, "Guardado: " + peso + "kg el " + fecha, Toast.LENGTH_SHORT).show();
            PesajeTL hisPesa = new PesajeTL();
            hisPesa.setPeso(peso); //Guardo peso
            if(fecha.equals("Sin fecha")){
                hisPesa.setDate(fechaFormateada); //Guardo la fecha
            } else {
                hisPesa.setDate(fecha); //Guardo la fecha
            }

            consultaBDInsertar(hisPesa);

            tiempoEspera.removeCallbacks(runnable); // Cancelamos la ejecución anterior (si hay alguna programada)
            runnable = () -> { //En este lambda no hace falta poner el metodo run porque esta interfaz solo tiene ese metodo entcoes entiende que ese el que esta poniendo
                recreate(); //recargar la pantalla
            };
            tiempoEspera.postDelayed(runnable, 1500);// Programamos la tarea para que se ejecute dentro de 1500 ms (1.5 segundo)
        });

        // Botón de Cancelar
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        // 3. Mostrar el diálogo
        builder.create().show();

    }


    //Consulta a la DB para sacar el pesaje, devuelve una lista de objetos PesajeTL
    private void consultaBDInsertar(PesajeTL dto){
        Modelo obj = new Modelo();
        int resultados = obj.InsertarPesaje(ventanaPesaje.this,dto); //Llamamos al SeleccionarHistorial para hacer la consulta en la db
    }


    //TODO tenmos que hacer el registrar un pesaje nuevo
    private TableLayout tableLayout;
    private LineChart grafico; //variable para nuestro grafico
    private ArrayList<Entry> puntos = new ArrayList<>(); //puntos de nuestra grafica
    private String[] fechas = new String[5]; //array para guardar todos las fechas
    private String[] peso = new String[5]; //array para guaradar todos los pesos
    private int contador = 0; //Contador de cuanto historial ha sacado de ese ejercicio, max 5
    private Handler tiempoEspera = new Handler(); //Lo usamos para hacer el tiempo de espera de 1500 segundos y llamara al run de Runnable
    private Runnable runnable; //para utilizar su metodo run así parar ese trozo hasta que yo diga
}