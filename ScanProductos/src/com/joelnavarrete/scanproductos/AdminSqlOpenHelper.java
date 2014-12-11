package com.joelnavarrete.scanproductos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSqlOpenHelper extends SQLiteOpenHelper   {
	
	
	 private static final int DATABASE_VERSION = 1;
	 private static final String DATABASE_NAME = "commments.db";
	
    public AdminSqlOpenHelper(Context context) 
    {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	
		// TODO Auto-generated constructor stub
	}

	@Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Pedidos(CodigoProducto integer primary key, Cantidad integer, Detalle text)");
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int VersionAnterior, int VersionNueva) {
		// TODO Auto-generated method stub
		
	}

}
