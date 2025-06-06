package app.jjg.nanogym.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLite extends SQLiteOpenHelper {

    final String TBL_USR = "CREATE TABLE tlrutinas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT UNIQUE, dias INTEGER)"; //tabla de la rutinas
    final String TBL_EJE = "CREATE TABLE tlejercicios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, dia INTEGER, idrutinas INTEGER, series INTEGER, repes INTEGER, peso INTEGER)"; //tablas de los ejercicios
    final String TBL_HIS = "CREATE TABLE tlhistorial (id INTEGER PRIMARY KEY AUTOINCREMENT, idejercicio INTEGER, repes INTEGER, peso INTEGER, date TEXT)";

    public ConexionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override //Metodo para crear las tablas, llamara al onCreate cuando sea la primera vez que instala la app
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBL_USR); //Version 1
        db.execSQL(TBL_EJE); //Version 1
        db.execSQL(TBL_HIS); //Version 2 para dispisitivos nuevos
    }

    @Override //Llamara a onUpgrade si detesta que la version nueva es mayor que la version de la db del dipositivo
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion < 2){ //Actualizar la db con la version 2
            db.execSQL(TBL_HIS);
        }
    }
}