package app.jjg.nanogym;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ventanaAjuste extends AppCompatActivity {
    //Ventana creada por la TASK 7
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ventana_ajuste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Bt Antes de Empezar
    public void onButtonClick2(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Bienvenido/a a NanoGym!");
        builder.setMessage("Estás usando una versión Alpha, lo que significa que todavía estamos en fase temprana de desarrollo. Algunas funciones podrían no estar completas o presentar errores.\n\nTu ayuda es muy valiosa: si notas algún problema o tienes sugerencias, por favor compártelas con el desarrollador. ¡Tu feedback nos ayuda a mejorar!\n" +
                        "\n" + "¡Gracias por ser parte del inicio de este proyecto!");
        builder.setPositiveButton("OK", null);
        builder.show();
    }


    //Bt Version
    public void onVersion(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡Bienvenido/a a la Versión 1.1Alpha!");
        // Texto resumido para el AlertDialog
        String mensaje = "• Check para ejercicios libres sin peso\n" +
                "• Corrección de historial con un solo registro\n" +
                "• Inserción automática en historial al crear ejercicio\n" +
                "• Primera letra de los nombres ahora en mayúscula\n" +
                "• Teclado numérico en campos de peso/series/repes\n" +
                "• Nueva pantalla principal: Rutinas\n" +
                "• Botón de ajustes añadido\n" +
                "• Confirmación al borrar rutina\n" +
                "• Botón para borrar ejercicios en creación\n" +
                "• Nueva sección: Eventos (¡Próximamente!)";

        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.show();

    }
}