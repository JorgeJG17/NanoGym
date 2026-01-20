package app.jjg.nanogym.database;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConexionSQLite extends SQLiteOpenHelper {

    final String TBL_USR = "CREATE TABLE tlrutinas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT UNIQUE, dias INTEGER)"; //tabla de la rutinas
    final String TBL_EJE = "CREATE TABLE tlejercicios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, dia INTEGER, idrutinas INTEGER, series INTEGER, repes INTEGER, peso INTEGER)"; //tablas de los ejercicios
    final String TBL_HIS = "CREATE TABLE tlhistorial (id INTEGER PRIMARY KEY AUTOINCREMENT, idejercicio INTEGER, repes INTEGER, peso INTEGER, date TEXT)";
    final String TBL_PESAJE = "CREATE TABLE tlpesaje (id INTEGER PRIMARY KEY AUTOINCREMENT, peso INTEGER, date TEXT)";
    //final String TBL_MET = "CREATE TABLE tleventos (id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT UNIQUE,nombre TEXT, fechaini TEXT, fechafin TEXT, dias INTEGER, hiper TEXT, perder TEXT, fuerza TEXT, salud TEXT, toni TEXT)";

    public ConexionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override //Metodo para crear las tablas, llamara al onCreate cuando sea la primera vez que instala la app
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TBL_USR); //Version 1
        db.execSQL(TBL_EJE); //Version 1
        db.execSQL(TBL_HIS); //Version 2 para dispositivos nuevos
        db.execSQL(TBL_PESAJE);
        db.execSQL("ALTER TABLE tlejercicios ADD COLUMN nom_dia TEXT"); //Un nuevo campo para la tabla
        db.execSQL("ALTER TABLE tlejercicios ADD COLUMN orden INTEGER"); //Un nuevo campo para la tabla
    }

    @Override //Llamara a onUpgrade si detesta que la version nueva es mayor que la version de la db del dipositivo
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion < 2 && newVersion >= 2){ //Actualizar la db con la version 2

            db.execSQL(TBL_HIS);
        }
        /*if (oldVersion < 8 && newVersion >= 8){ //Actualizar la db con la version 3
            //TASK 6
            EjecutarScript(db, context, "02_s.sql");
            db.execSQL("ALTER TABLE tlejercicios ADD COLUMN nom_dia TEXT"); //Un nuevo campo para la tabla
            db.execSQL("ALTER TABLE tlejercicios ADD COLUMN orden INTEGER"); //Un nuevo campo para la tabla
        }*/
        /*if(oldVersion < 4 && newVersion >= 4){
            EjecutarScript(db, context, "02_s.sql");
        }
        if(oldVersion < 5 && newVersion >= 5){
            db.execSQL("ALTER TABLE tlejercicios ADD COLUMN nom_dia TEXT");
        }*/
        /*if(oldVersion < 6 && newVersion >= 6){
            db.execSQL("ALTER TABLE tlejercicios ADD COLUMN orden INTEGER");
        }*/

        /*if(oldVersion < 7 && newVersion >= 7){ //solo arreglo para martin
            EjecutarScript(db, context, "03_arreglomartin.sql");
        }*/

        /*if (oldVersion < 8 && newVersion >= 8){
            db.execSQL(TBL_MET);
            db.execSQL("ALTER TABLE tlejercicios ADD COLUMN ideventos INTEGER"); //Un nuevo campo para la tabla
            db.execSQL("ALTER TABLE tlejercicios ADD COLUMN levento TEXT"); //Un nuevo campo para la tabla
            db.execSQL("ALTER TABLE tlejercicios ADD COLUMN tipo INTEGER"); //Un nuevo campo para la tabla

            db.execSQL("INSERT into tleventos (codigo,nombre,fechaini,fechafin,dias,hiper,perder,fuerza,salud,toni) VALUES ('ELORG1','El Origen','21/07/2025','04/08/2025','2','S','N','S','N','N')"); //TODO tenemos que crear los ejercicios de la rutina el origen Investigar si podemos hacer un script modo un tlf
            //db.execSQL("INSERT into tlejercicios (nombre,dia,idrutina,series,repes,peso,ideventos,levento) VALUES ('El Origen','21/07/2025','04/08/2025','3')");
        }*/

        if (oldVersion < 9 && newVersion >= 9){
            db.execSQL(TBL_PESAJE);
        }
    }

    private void EjecutarScript(SQLiteDatabase db, Context context, String assetFileName){
        try {
            InputStream is = context.getAssets().open(assetFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder statement = new StringBuilder();
            String line;

            db.beginTransaction();
            try {
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("--")) continue; // saltar líneas vacías o comentarios

                    statement.append(line);
                    if (line.endsWith(";")) {
                        // Ejecutar sentencia
                        String sql = statement.toString();
                        db.execSQL(sql);
                        statement.setLength(0); // reiniciar
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
                reader.close();
            }
        } catch (IOException | SQLiteException e) {
            e.printStackTrace(); // O mostrar un log
        }
    }

    //Campos de calse
    Context context;
}