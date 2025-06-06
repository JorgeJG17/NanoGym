package app.jjg.nanogym;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import app.jjg.nanogym.ventanascrear.ventanaRutinas;
import app.jjg.nanogym.ventanasentrenar.ventana_entrenar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //Que no deje el modo oscuro, asi manetenmos los colores de nuestra app
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Boton de  crear rutinas
    public void onButtonClick3(View view){

       Intent intent = new Intent(this, ventanaRutinas.class);
        startActivity(intent);
    }

    //Boton de entrenar
    public void onButtonClick4(View view){

        Intent intent = new Intent(this, ventana_entrenar.class);
        startActivity(intent);
    }

    public void onButtonClick2(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Bienvenido/a a NanoGym!");
        builder.setMessage("Estás usando una versión Alpha, lo que significa que todavía estamos en fase temprana de desarrollo. Algunas funciones podrían no estar completas o presentar errores.\n\nTu ayuda es muy valiosa: si notas algún problema o tienes sugerencias, por favor compártelas con el desarrollador. ¡Tu feedback nos ayuda a mejorar!\n" +
                        "\n" + "¡Gracias por ser parte del inicio de este proyecto!");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}