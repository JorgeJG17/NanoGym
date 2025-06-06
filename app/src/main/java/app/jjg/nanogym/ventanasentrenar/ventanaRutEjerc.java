package app.jjg.nanogym.ventanasentrenar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
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

import app.jjg.nanogym.R;
import app.jjg.nanogym.database.EjerciciosTL;
import app.jjg.nanogym.database.Modelo;

public class ventanaRutEjerc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_rut_ejerc);

        Intent intent = getIntent();
        idRutina = intent.getIntExtra("idRutina", -1); //recuperadmos el idRutina
        dia = intent.getIntExtra("dia", -1); //Recuperamos el dia de la pantalla anterior

        nactDia = findViewById(R.id.textdias);
        nactDia.setText("Día: " + dia);//Pintamos el dia seleccionado

        CargarTabla(idRutina, dia); //Le enviamos al metodo idRutina y dia para que carge en una tabla los ejercicios y datos correspondientes

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void CargarTabla(int id, int d){ //En este metodo cargamos los datos necesarios en la tabla

        Modelo obj = new Modelo();
        Cursor resultados = obj.SeleccionarEjercicos(ventanaRutEjerc.this, id,d); //Recuperamos los ejercicios para la rutina x y el dia y
        tableLayout = findViewById(R.id.tableLayout); //Creamos nuestra tabla

        if (resultados != null && resultados.moveToFirst()) { //Vemos que los resultados esten rellenos y empezamos con el primero, sin no hay primero deveulve null
            do { //Realizamos un bucle para eecribir la tabla completa segun los resultados de la db
                int idEjercicio = resultados.getInt(0);
                String nombreEje = resultados.getString(1);
                int seriesRutina = resultados.getInt(2);
                int repesRutina = resultados.getInt(3);
                int pesoRutina = resultados.getInt(4);

                TableRow tablaEjerc= new TableRow(this);
                //***************************************************************************************************************************************

                // Crear las celdas para cada fila
                EditText textViewNombre = new EditText(this);
                textViewNombre.setText(nombreEje);  // Mostrar el nombre de la rutina (Las demas explicaciones de los siguiente metodos llamado estan en Peso y RM)
                textViewNombre.setBackgroundResource(R.drawable.border_tabla);
                textViewNombre.setPadding(8, 8, 8, 8);
                textViewNombre.setSingleLine(true);
                textViewNombre.setId(idEjercicio);

                //***************************************************************************************************************************************

                EditText textViewSeries = new EditText(this);
                textViewSeries.setText(String.valueOf(seriesRutina));  // Mostrar las series (Las demas explicaciones de los siguiente metodos llamado estan en Peso y RM)
                textViewSeries.setBackgroundResource(R.drawable.border_tabla);
                textViewSeries.setPadding(8, 8, 8, 8);
                textViewSeries.setSingleLine(true);
                textViewSeries.setGravity(Gravity.CENTER);
                //textViewSeries.setId(idEjercicio);

                textViewSeries.addTextChangedListener(new TextWatcher() { //Le asignamos como escuha un objeto de una clase que hereda de la interface TextWatche a campo Series
                                                                          //La clase es anonima por eso es así
                    public void afterTextChanged(Editable s) { //A este metodo se le llama cada vez que usuario realiza un cambio en el campo Series
                        ViewGroup row = (ViewGroup) textViewSeries.getParent(); //Recupero toda la fila del campo editado
                        CambioTexto(s,row);//Y se la enviamos a nuestro metodo para que cambie todo
                    }
                    //Para el campo Seres no veo necesario usar la interfaz runnable y la clase Handler como esta en repes y peso

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {} //Override obligatorio de la interfaz
                    public void onTextChanged(CharSequence s, int start, int before, int count) {} //Override obligatorio de la interfaz
                });

                //***************************************************************************************************************************************

                EditText textViewRepes = new EditText(this);
                textViewRepes.setText(String.valueOf(repesRutina));  // Mostrar las repeticiones (Las demas explicaciones de los siguiente metodos llamado estan en Peso y RM)
                textViewRepes.setBackgroundResource(R.drawable.border_tabla);
                textViewRepes.setPadding(8, 8, 8, 8);
                textViewRepes.setSingleLine(true);
                textViewRepes.setGravity(Gravity.CENTER);
                //textViewRepes.setId(idEjercicio);

                textViewRepes.addTextChangedListener(new TextWatcher() { //Lo mismo explicado en el campo Series pero con el campo Repes

                    public void afterTextChanged(Editable s) {
                        tiempoEspera.removeCallbacks(runnable); // Cancelamos la ejecución anterior (si hay alguna programada)
                        runnable = () -> { //En este lambda no hace falta poner el metodo run porque esta interfaz solo tiene ese metodo entcoes entiende que ese el que esta poniendo
                            ViewGroup row = (ViewGroup) textViewRepes.getParent();
                            CambioTexto(s, row);
                        };
                        tiempoEspera.postDelayed(runnable, 2000);// Programamos la tarea para que se ejecute dentro de 2000 ms (2 segundos)
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {} //Override obligatorio de la interfaz
                    public void onTextChanged(CharSequence s, int start, int before, int count) {} //Override obligatorio de la interfaz
                });

                //***************************************************************************************************************************************

                EditText textViewPeso = new EditText(this);
                textViewPeso.setText(String.valueOf(pesoRutina));  // Mostrar el peso
                textViewPeso.setBackgroundResource(R.drawable.border_tabla); //Le metemos los colores bordes, especificados en border_tabla.xml
                textViewPeso.setPadding(8, 8, 8, 8); //La cantidad de px de la caja
                textViewPeso.setSingleLine(true);  //Limita el texto por si llega largo que la celda no se modifique
                textViewPeso.setGravity(Gravity.CENTER); //Centrado en la celda
                //textViewPeso.setId(idEjercicio);ç

                textViewPeso.addTextChangedListener(new TextWatcher() { //Lo mismo explicado en el campo Series pero con el campo Peso

                    public void afterTextChanged(Editable s) {
                        tiempoEspera.removeCallbacks(runnable); // Cancelamos la ejecución anterior (si hay alguna programada)
                        runnable = () -> { //En este lambda no hace falta poner el metodo run porque esta interfaz solo tiene ese metodo entcoes entiende que ese el que esta poniendo
                            ViewGroup row = (ViewGroup) textViewPeso.getParent();
                            CambioTexto(s, row);
                        };
                        tiempoEspera.postDelayed(runnable, 2000);// Programamos la tarea para que se ejecute dentro de 2000 ms (2 segundos)
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {} //Override obligatorio de la interfaz
                    public void onTextChanged(CharSequence s, int start, int before, int count) {} //Override obligatorio de la interfaz
                });

                //***************************************************************************************************************************************

                //Mostrar la repeticion max
                EditText textViewRM = new EditText(this);
                textViewRM.setText(CalculadorDeRM(pesoRutina, repesRutina)); // Mostrar el RM
                textViewRM.setBackgroundResource(R.drawable.border_tablarmm); //Le metemos los colores bordes, especificados en border_tablarmm.xml
                textViewRM.setPadding(8, 8, 8, 8); //La cantidad de px de la caja
                textViewRM.setSingleLine(true); //Limita el texto por si llega largo que la celda no se modifique
                textViewRM.setEnabled(false);       // No editable
                textViewRM.setFocusable(false);     // No se enfoca
                textViewRM.setClickable(false);     // No se puede clickar
                textViewRM.setTextColor(Color.BLACK); //Color del texto
                textViewRM.setGravity(Gravity.CENTER); //Centrado en la celda



                // Agregar las celdas a la fila
                tablaEjerc.addView(textViewNombre);
                tablaEjerc.addView(textViewSeries);
                tablaEjerc.addView(textViewRepes);
                tablaEjerc.addView(textViewPeso);
                tablaEjerc.addView(textViewRM);

                // Agregar la fila a la tabla

                tableLayout.addView(tablaEjerc);
            } while (resultados.moveToNext()); //Pasamos al siguiente resultado, devuelve true si hay mas filas, si no devuelve false y bucle termina
        }

        //Cuando salimos del bucle verificamos que hayamos entrado viendo que resultado no era null y cerramos el cursor
        if (resultados != null) {
            resultados.close();
        }
    }

    private void ActualizarDatos( EjerciciosTL eje) {

        Modelo obj = new Modelo(); //Nos conectamos a la bd

        int resultados = obj.ActualizarDatosTabla(ventanaRutEjerc.this,eje);

        //Si se ha actualizado correctamente devolvera 1
        if(resultados == 1){
            Toast.makeText(ventanaRutEjerc.this, "Ok", Toast.LENGTH_SHORT).show();
            recreate(); //recargar la pantalla
        } else{
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("¡Ups! Algo salió mal. Por favor, infórmaselo al desarrollador. Recuerda que esta es una versión Alpha.")
                    .setPositiveButton("OK", null)
                    .show();
            //Toast.makeText(ventanaRutEjerc.this, "¡Ups! Algo salió mal. Por favor, infórmaselo al desarrollador. Recuerda que esta es una versión Alfa.", Toast.LENGTH_SHORT).show();
        }

    }

    private void CambioTexto(Editable s, ViewGroup row){ //Metodo para realizar las modificaciones de la fila modificada

        if(s.toString().trim().length() > 0) { //Entrara cuando la casilla cambiada no este vacia


            // Cuando cambia, obtener fila y actualizar DB
            //ViewGroup row = (ViewGroup) textViewSeries.getParent();
            EditText idEditText = (EditText) row.getChildAt(0);// Recuperamos la columna del nombre que tiene el dni
            int id = idEditText.getId(); //Guardamos el id de ese ejercicio que ha cambiado en algunas de sus columnas
            EditText col2 = (EditText) row.getChildAt(1); //columna 2 Series
            EditText col3 = (EditText) row.getChildAt(2); //columna 3 Repes
            EditText col4 = (EditText) row.getChildAt(3); //columna 4 Peso

            EjerciciosTL eje = new EjerciciosTL(); //Creo un objeto EjerciciosTL para guardar los datos de este ejercicio y enviarlo a al db

            eje.setId(Integer.toString(id)); //id del ejerciio
            eje.setIdRutina(Integer.toString(idRutina)); //idRutina que habiamos rellenado antes
            //eje.setDia(Integer.toString(actDia)); //Teniendo el id del ejercicicio y el idRutina, el dia no es necesario (el idRutina tampoco en realidad pero prefiero asegurar)
            //eje.setNombre(col2.getText().toString().trim()); //Ahora mismo el nombre nunca cambia, lo dejo comentado por si en futuro cambia el nombre del ejercicio
            eje.setSeries(col2.getText().toString().trim()); //valor de series
            eje.setRepes(col3.getText().toString().trim()); //valor de repes
            eje.setPeso(col4.getText().toString().trim()); // valor de peso

            // Llamar a tu método de actualización en la base de datos
            ActualizarDatos(eje); //Cuando ya tenemos nuestro objeto EjerciciosTl relleno de toda la fila que ha sido modficada se lo enviamos a la db para actualizar
        }
    }

    private  String CalculadorDeRM(int p,int r){ //Metodo para calcular la repe max

        double rm = p/(1.0278-(0.0278*r)); //Formula de la repr max siendo p peso y r repeticiones

        String rmFormateado = String.format("%.1f", rm); //Solo quiero que se vea un decimal

        return rmFormateado;
    }


    private TableLayout tableLayout; //nuestra tabla
    private int idRutina; //idRutina
    private int dia; //dia de entrenamiento
    private TextView nactDia;

    private Handler tiempoEspera = new Handler(); //Lo usamos para hacer el tiempo de espera de 1500 segundos y llamara al run de Runnable
    private Runnable runnable; //para utilizar su metodo run así parar ese trozo hasta que yo diga
}