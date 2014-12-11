package com.joelnavarrete.scanproductos;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class ActivityListado extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listado_activity);
	}
	
	private void CargarListadoProductos()
	{
		AdminSqlOpenHelper adminsql = new AdminSqlOpenHelper(getBaseContext());
		SQLiteDatabase db = adminsql.getWritableDatabase();
	}

}
