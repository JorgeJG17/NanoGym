package app.jjg.nanogym.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import app.jjg.nanogym.R;
import app.jjg.nanogym.database.Modelo;

public class ventanaEventos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_eventos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void onEvento(View view){

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("📅 Eventos en Septiembre");
        builder.setMessage("¡En septiembre podrás participar en retos y desafíos especiales dentro de la app!\n" +
                "Estamos trabajando para ofrecerte la mejor experiencia posible. Aunque septiembre es la fecha estimada,\n" +
                "podrían surgir algunos retrasos.\n\n" +
                "Mantente atento a las actualizaciones.\n¡Gracias por tu paciencia!");
        builder.setPositiveButton("OK", null);
        builder.show();*/
        int id;
        Intent intent = new Intent(this, tipo_Eventos.class);

        if(view.getId() == R.id.evento1){
            System.out.println("Evento1");
            id = ConsultaBD(1);

            intent.putExtra("idEvento", id);

            //Le enviamos el id de la rutina a la siguiente pantalla
            startActivity(intent);
        }
        else if(view.getId() == R.id.evento2){
            System.out.println("Evento2");
            id = ConsultaBD(2);

            intent.putExtra("idEvento", id);

            //Le enviamos el id de la rutina a la siguiente pantalla
            startActivity(intent);
        }
        else if(view.getId() == R.id.evento3){
            System.out.println("Evento3");
            id = ConsultaBD(3);

            intent.putExtra("idEvento", id);

            //Le enviamos el id de la rutina a la siguiente pantalla
            startActivity(intent);
        }

    }

    public int ConsultaBD(int valor){
        Modelo obj = new Modelo();
        int id = -1;

        switch(valor){

            case 1:
                id = obj.SeleccionaridEvento(ventanaEventos.this,codigo_even_1);
                break;
            case 2:
                id = obj.SeleccionaridEvento(ventanaEventos.this,codigo_even_2);
                break;
            case 3:
                id = obj.SeleccionaridEvento(ventanaEventos.this,codigo_even_3);
                break;
        }

        return id;
    }

    //CAMPOS DE CLASE
    private static final String codigo_even_1 ="ELORG1";
    private static final String codigo_even_2 ="";
    private static final String codigo_even_3 ="";
}