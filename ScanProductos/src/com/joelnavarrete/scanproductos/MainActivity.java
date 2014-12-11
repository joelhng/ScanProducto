package com.joelnavarrete.scanproductos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlSerializer;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	TextView CodigoProducto;
	EditText CantidadProducto;
	EditText DetalleProducto;
	
	Button GuardarXML;
	Button GuardarSQL;
	Button ConsultarListado;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConfigurarBotonEscanear();        
        ConfigurarGuardarXML();
        ConfigurarGuardarSQL();
        ConfigurarBotonConsultarListado();
    }
    
    private void ConfigurarGuardarSQL()
    {
    	GuardarSQL = (Button) findViewById(R.id.btnGuardarSQL);
    	
    	GuardarSQL.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GuardarRegistroSQL();
			}
		});
    }
    
    private void GuardarRegistroSQL()
    {
    	
    	String DetalleProd, CodProd, CantProd;
    	AdminSqlOpenHelper adminSQL = new AdminSqlOpenHelper(this);
    	SQLiteDatabase db = adminSQL.getWritableDatabase();
    	ContentValues ParametrosSQL = new ContentValues();
    	
    	try
    	{
    	CodigoProducto = (TextView) findViewById(R.id.tvCodigoProducto);
    	CantidadProducto = (EditText) findViewById(R.id.etCantidad);
    	DetalleProducto = (EditText)findViewById(R.id.etDetalle);
    	
    	CodProd = CodigoProducto.getText().toString();
    	CantProd = CantidadProducto.getText().toString();
    	DetalleProd = DetalleProducto.getText().toString();
    	
    	ParametrosSQL.put("CodigoProducto", CodProd);
    	ParametrosSQL.put("Cantidad", CantProd);
    	ParametrosSQL.put("Detalle", DetalleProd);
    	
    	db.insert("Pedidos", null, ParametrosSQL);
    	db.close();
    	
    	CodigoProducto.setText("");
    	CantidadProducto.setText("");
    	DetalleProducto.setText("");
    	
    	Toast.makeText(getBaseContext(), "Producto guardados con exito.", Toast.LENGTH_SHORT).show();
    	}
    	catch(Exception ex)
    	{
    		Toast.makeText(getBaseContext(), "Se produjo un error al guardar el producto.", Toast.LENGTH_SHORT).show();
    	}
    }
    
    private void ConfigurarGuardarXML()
    {
    	GuardarXML = (Button) findViewById(R.id.btnGuardarXML);
    	
    	GuardarXML.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try
				{				
					
				XmlSerializer ser = Xml.newSerializer();
				
				
				File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),  "ArchivoPrueba.xml");
				
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(myFile));
				
				//CREO EL ARCHIVO XML
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
				DOMImplementation implementation = builder.getDOMImplementation();
				Document document = implementation.createDocument(null, "MisProductos", null);
	            document.setXmlVersion("1.0");
				 			
				//Main Node
	            Element raiz = document.getDocumentElement();
	            
	            //Item Node
                Element itemNode = document.createElement("ITEM"); 
                
                //Key Node
                
                Element keyNode = document.createElement("Productos"); 
                Text nodeKeyValue = document.createTextNode("prod 1");
                keyNode.appendChild(nodeKeyValue);  
                
                //ASIGNO EL VALOR DEL NODO AL RAIZ
                itemNode.appendChild(keyNode);
				
				//Source source = new DOMSource(document);
				//FIN ARCHIVO XML
				
				osw.write(document.toString()); //Escribo en el archivo creado
				
	            osw.flush();
	            osw.close();
				
				
				Context context = getApplicationContext();
				CharSequence  texto = "Hola Moccen";
				Toast.makeText(context, document.toString(), Toast.LENGTH_SHORT).show();
				}
				catch(Exception ex)
				{				
					Context context = getApplicationContext();
					CharSequence  texto = "Error al guardar el xml";
					Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
				}
			}
		});
    }

    private void ConfigurarBotonEscanear()
    {    	
    	final Button BuscarProducto = (Button) findViewById(R.id.btnBuscarProducto);    	
    	
    	BuscarProducto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				AbrirScan();
			}
		});
    	
    	
    	
    }
    
    private void ConfigurarBotonConsultarListado()
    {
    	ConsultarListado = (Button) findViewById(R.id.btnConsultarListado);
    	
    	ConsultarListado.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ActivityListado.class);
				startActivity(i);
			}
		});
    }
    
    private void AbrirScan()
    {
    	try {  
            //new IntentIntegrator(QrReaderActity.this).initiateScan();  
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");  
            intent.putExtra("SCAN_MODE", "PRODUCT_MODE");  
            startActivityForResult(intent, 0);  
            
        } catch (ActivityNotFoundException exception) {
        	CharSequence text = "No se pudo ejecutar la aplicación SCAN";
        	Context context = getApplicationContext();
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();  
            
        }  
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
            	CodigoProducto = (TextView) findViewById(R.id.tvCodigoProducto);
                
               String contents = intent.getStringExtra("SCAN_RESULT");
               //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
               
               CharSequence text = contents;
	           	//Context context = getApplicationContext();
	           	
	           	CodigoProducto.setText(text);
               //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();  
            
               // Handle successful scan
                                         
            } else if (resultCode == RESULT_CANCELED) {
               // Handle cancel               
            }
       }
    }
       
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
