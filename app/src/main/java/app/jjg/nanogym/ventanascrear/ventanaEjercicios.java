package app.jjg.nanogym.ventanascrear;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import app.jjg.nanogym.MainActivity;
import app.jjg.nanogym.R;
import app.jjg.nanogym.database.EjerciciosTL;
import app.jjg.nanogym.database.Modelo;

public class ventanaEjercicios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_ejercicios);

        //Recuperar datos enviados desde la otra pantalla
        Intent intent = getIntent();
        idRutina = intent.getIntExtra("idRutina", -1); //idrutina
        sdias = intent.getIntExtra("sdias", -1); // Los dias que tiene la rutina en total

        nactDia = findViewById(R.id.ndia);
        nactDia.setText(Integer.toString(actDia)); //Pintamos el dia 1
        iniciar(); //llamamos al metodo iniciar

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void iniciar(){
        tableLayout = findViewById(R.id.tableLayout); //Creamos nuestra tabla
        New_fila_Tabla();//cargamos una fila al inciar
    }

    public void new_fila_click(View view){ //Boton de una fila más
        New_fila_Tabla(); ////cargamos una fila más
    }

    public void crearEjer(){ //Metodo que cogera todos los ejercicios de la tabla y se los enviara uno a uno al metodo para insertar en la db

        int n_filas_t = tableLayout.getChildCount(); //Este metodo de vuelve el numero de filas totales, contando la principal que es la de los titulos

        //Lo pongo a 1 para saltarme los titulos
        for(int i = 1; i< n_filas_t; i++){

            TableRow tablaEjerc = (TableRow) tableLayout.getChildAt(i);

            EditText arrayCampos[] = new EditText[4];
            arrayCampos[0] = (EditText) tablaEjerc.getChildAt(0);
            arrayCampos[1] = (EditText) tablaEjerc.getChildAt(1);
            arrayCampos[2] = (EditText) tablaEjerc.getChildAt(2);
            arrayCampos[3] = (EditText) tablaEjerc.getChildAt(3);

            String valoresCampos[] = new String[4];
            valoresCampos[0] = arrayCampos[0].getText().toString();
            valoresCampos[1] = arrayCampos[1].getText().toString();
            valoresCampos[2] = arrayCampos[2].getText().toString();
            valoresCampos[3] = arrayCampos[3].getText().toString();


            insertDB(valoresCampos); //enviamos los datos en un array para insertarlos en la db
        }


    }

    public void insertDB(String valores[]){ //metodo para insertar los ejercicios en la db

        Modelo obj = new Modelo(); //Nos conectamos a la bd
        EjerciciosTL eje = new EjerciciosTL(); //Un objeto de tipo Ejercicios que le envairemos a la consulta de la db para guardar los datos en el y recuperarlos alli

        eje.setIdRutina(Integer.toString(idRutina)); //idRutina sacado de la bd
        eje.setDia(Integer.toString(actDia)); //actdia metemos el dia actual al que se le esta rellenado los ejercicios
        eje.setNombre(valores[0]);
        eje.setSeries(valores[1]);
        eje.setRepes(valores[2]);
        eje.setPeso(valores[3]);

        int resInsert = obj.InsertaEjercicios(ventanaEjercicios.this,eje); //Insertar ejercicios


        //Si se ha insertado correctamente devolvera 1
        if(resInsert == 1){

            contador++;//el contador que llevara las que se han insertado correctamente
            if(contador == tableLayout.getChildCount()) { //Cuando se hayan insertados todas

                Toast.makeText(ventanaEjercicios.this, "Ok", Toast.LENGTH_SHORT).show();

                //Borramos la tabla menos la cebecera
                int childCount = tableLayout.getChildCount();
                if (childCount > 1) {
                    tableLayout.removeViews(1, childCount - 1);
                }

                contador = 1; //Reiniciamos el contador al incio de nuevo
                New_fila_Tabla();//Volvemos a llamar para que pinte una fila


            }
            //Intent intent = new Intent(this, ventanaEjercicios.class);
            //startActivity(intent);
        } else{
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("¡Ups! Algo salió mal. Por favor, infórmaselo al desarrollador. Recuerda que esta es una versión Alpha.")
                    .setPositiveButton("OK", null)
                    .show();
            //Toast.makeText(ventanaEjercicios.this, "¡Ups! Algo salió mal. Por favor, infórmaselo al desarrollador. Recuerda que esta es una versión Alfa.", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo del bt Finalizar dia
    public void finDiaClick(View view){

        crearEjer(); //Crear ejercicio y insertarlo en db
        CambiarDia(); //Cambianmos de dia

        if(actDia != -10){ //si dia es diferente a a -10 entoces continuamos

            nactDia.setText(Integer.toString(actDia)); //pintamos el nueco da

        }else{ // si es -10 significa que ya no hay mas días

            //Una vez es el ultimo día y se pulsa finalizar, se llama a la ventana principal
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    //Metodo para que cambie de dia y si ya a completado todos los dias ponemos el valor en negativo para controlar que ya ha rellenado la rutina entera
    private void CambiarDia(){
        if(actDia < sdias){
            actDia++;
        }
        else{
            actDia = -10;
        }
    }


    private void New_fila_Tabla() { //Metodo para pintar una nueva fila vacía
        TableRow tablaEjerc= new TableRow(this);

        EditText textViewNombre = new EditText(this);
        textViewNombre.setText("");
        textViewNombre.setBackgroundResource(R.drawable.border_tabla);
        textViewNombre.setPadding(8, 8, 8, 8);
        textViewNombre.setSingleLine(true);

        EditText textViewSeries = new EditText(this);
        textViewSeries.setText("");
        textViewSeries.setBackgroundResource(R.drawable.border_tabla);
        textViewSeries.setPadding(8, 8, 8, 8);
        textViewSeries.setSingleLine(true);
        textViewSeries.setGravity(Gravity.CENTER);

        EditText textViewRepes = new EditText(this);
        textViewRepes.setText("");
        textViewRepes.setBackgroundResource(R.drawable.border_tabla);
        textViewRepes.setPadding(8, 8, 8, 8);
        textViewRepes.setSingleLine(true);
        textViewRepes.setGravity(Gravity.CENTER);

        EditText textViewPeso = new EditText(this);
        textViewPeso.setText("");
        textViewPeso.setBackgroundResource(R.drawable.border_tabla); //Le metemos los colores bordes, especificados en border_tabla.xml
        textViewPeso.setPadding(8, 8, 8, 8); //La cantidad de px de la caja
        textViewPeso.setSingleLine(true);  //Limita el texto por si llega largo que la celda no se modifique
        textViewPeso.setGravity(Gravity.CENTER);

        // Agregar las celdas a la fila
        tablaEjerc.addView(textViewNombre); //0
        tablaEjerc.addView(textViewSeries); //1
        tablaEjerc.addView(textViewRepes); //2
        tablaEjerc.addView(textViewPeso);  //3

        // Agregar la fila a la tabla
        tableLayout.addView(tablaEjerc);
    }


    //CAMPOS DE CLASE
    private int idRutina; //id rutina generado en la pantalla anterior
    private int sdias; //dias totales que tiene la rutina, viene de la pantalla anterior
    private int actDia = 1; //dia actual de la rutina que esta rellenado, donde esta metiendo los ejercicios actuales
    private TextView nactDia;

    private TableLayout tableLayout; //nuestra tabla
    private int contador = 1; //contador para los ejercicios insertados
}