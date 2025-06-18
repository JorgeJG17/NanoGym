package app.jjg.nanogym.ventanasentrenar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import app.jjg.nanogym.R;
import app.jjg.nanogym.database.Modelo;
import app.jjg.nanogym.database.RutinasTL;
import app.jjg.nanogym.ventanaAjuste;
import app.jjg.nanogym.ventanascrear.ventanaRutinas;

import java.util.ArrayList;
import java.util.List;

public class ventana_entrenar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //Que no deje el modo oscuro, asi manetenmos los colores de nuestra app //TASK 7 POR CAMBIAR DE PANTALLA PRINCIPAL
        setContentView(R.layout.activity_ventana_entrenar);
        PintarBT();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void PintarBT(){
        layoutBotones = findViewById(R.id.layoutBotones);

        // Realiza la consulta a la base de datos y recupera los nombres
        List<RutinasTL> rutinas = consultaDB();

        // Crea un botón para cada nombre recuperado
        for (RutinasTL datos : rutinas) {
            Button boton = new Button(this);
            boton.setText(datos.getNombre());
            boton.setTag(Integer.parseInt(datos.getId()));

            // Establecer listener para cuando se haga clic en el botón
            boton.setOnClickListener(v -> {

                if(btborrar == false) {

                    Intent intent = new Intent(this, ventana_dias.class);
                    intent.putExtra("idRutina", (int) v.getTag()); //Le enviamos el id de la rutina a la siguiente pantalla
                    startActivity(intent);
                    // Aquí puedes utilizar el ID de la rutina
                    //Toast.makeText(MiActividad.this, "ID de la rutina: " + rutina.getId(), Toast.LENGTH_SHORT).show();
                } else{

                    //TASK 8: Poner un aviso de confirmacion antes de borrar la rutina
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Confirmación");
                    builder.setMessage("¿Estas seguro de borrar esta rutina: "+ boton.getText() +"?");
                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Modelo obj = new Modelo();
                            int resultados = obj.EliminarRutina(ventana_entrenar.this, (int) v.getTag());

                            //Si se ha eliminado correctamente devolvera 1
                            if(resultados == 1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ventana_entrenar.this);
                                builder.setTitle("Modo Borrar Desactivado");
                                builder.setMessage("La rutina se ha eliminado correctamente. El modo Borrar ha sido desactivado.");
                                builder.setPositiveButton("OK", null);
                                builder.show();

                                tiempoEspera.removeCallbacks(runnable); // Cancelamos la ejecución anterior (si hay alguna programada)
                                runnable = () -> { //En este lambda no hace falta poner el metodo run porque esta interfaz solo tiene ese metodo entcoes entiende que ese el que esta poniendo
                                    recreate(); //recargar la pantalla
                                };
                                tiempoEspera.postDelayed(runnable, 1500);// Programamos la tarea para que se ejecute dentro de 1500 ms (1.5 segundo)

                            } else{
                                new AlertDialog.Builder(ventana_entrenar.this)
                                        .setTitle("Error")
                                        .setMessage("¡Ups! Algo salió mal. Por favor, infórmaselo al desarrollador. Recuerda que esta es una versión Alpha.")
                                        .setPositiveButton("OK", null)
                                        .show();
                            }
                        }
                    });
                    builder.setNegativeButton("No",new DialogInterface.OnClickListener() { //Si dice que no pues desactivamos el modo borrar
                        public void onClick(DialogInterface dialog, int which) {
                            btborrar = false;
                        }
                    });
                    builder.show();
                }
            });

            layoutBotones.addView(boton);
        }
    }

    private List<RutinasTL> consultaDB(){

        List<RutinasTL> rutinas = new ArrayList<>();

        Modelo obj = new Modelo(); //Nos conectamos a la bd

        Cursor resultados = obj.SeleccionarRutinas(ventana_entrenar.this);

        if (resultados != null && resultados.moveToFirst()) {
            do {
                // Obtén el nombre y el ID de la rutina desde el cursor
                int idRutina = resultados.getInt(0);
                String nombreRutina = resultados.getString(1);


                RutinasTL rutinact = new RutinasTL();
                rutinact.setNombre(nombreRutina);
                rutinact.setId(Integer.toString(idRutina));
                // Crea un objeto Rutina y lo agrega a la lista
                rutinas.add(rutinact);

            } while (resultados.moveToNext());  // Continúa hasta el siguiente resultado en el cursor
        }

        // Cierra el cursor después de usarlo
        if (resultados != null) {
            resultados.close();
        }

        return rutinas;
    }

    //Boton de borrar rutina
    public void onButtonClick(View view){
        if(btborrar == false) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Modo Borrar Activado");
            builder.setMessage("La siguiente rutina que pulses a continuación será eliminada junto a su días y ejercicios.\n"+
                    "\n" + "Si no desea borrar ninguna rutina, puedes volver a pulsar este botón para desactivar este modo borrar.");
            builder.setPositiveButton("OK", null);
            builder.show();

            btborrar = true;
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Modo Borrar Desactivado");
            builder.setMessage("El modo Borrar ha sido desactivado.");
            builder.setPositiveButton("OK", null);
            builder.show();
            btborrar = false;
        }
    }

    //Boton de  crear rutinas //TASK 7 hemos pasado la pantalla principal a la secundaria
    public void onButtonClick3(View view){

        Intent intent = new Intent(this, ventanaRutinas.class);
        startActivity(intent);
    }

    //TASK 15 metodo del nuevo bt de ajuste
    public void onAjuste(View view){

        Intent intent = new Intent(this, ventanaAjuste.class);
        startActivity(intent);
    }

    public void onEventos(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📅 Eventos próximamente");
        builder.setMessage("¡Muy pronto podrás participar en retos y desafíos especiales dentro de la app!\n\nMantente atento a las actualizaciones.\n¡Gracias por tu paciencia!");
        builder.setPositiveButton("OK", null);
        builder.show();

    }



    //Campos de clase
    private LinearLayout layoutBotones;  // El contenedor donde se agregarán los botones
    private boolean btborrar = false;

    private Handler tiempoEspera = new Handler(); //Lo usamos para hacer el tiempo de espera de 1500 segundos y llamara al run de Runnable
    private Runnable runnable; //para utilizar su metodo run así parar ese trozo hasta que yo diga
}