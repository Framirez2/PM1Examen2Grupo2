package com.grupom.pm1examen2grupo2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grupom.pm1examen2grupo2.Config.RestApiMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ActualizarContactos extends AppCompatActivity {

    private CaptureBitmapView captureBitmapView;
    Button btnAtras, btnActualizar;
    FloatingActionButton btnTomarfoto;
    EditText txtNombre,txtTelefono,txtLat,txtLon;
    ImageView Foto;
    ListarContactos lc = new ListarContactos();

    Bitmap imagen, imgC;

    static final int RESULT_GALLERY_IMG = 200;
    static final int TAKE_PIC_REQUEST = 101;
    public static String latitud = "";
    public static String longitud = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contactos);

        String id = getIntent().getStringExtra("id");
        String nombre =getIntent().getStringExtra("nombre");
        String telefono =getIntent().getStringExtra("telefono");
        Double latitud =Double.valueOf(getIntent().getStringExtra("latitud").toString());
        Double longitud =Double.valueOf(getIntent().getStringExtra("longitud").toString());
        imgC = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("imagen"), 0, getIntent().getByteArrayExtra("imagen").length);

        btnAtras = (Button) findViewById(R.id.btnAtrasAU);
        btnActualizar = (Button)findViewById(R.id.btnActualizarAU);
        txtNombre = (EditText) findViewById(R.id.txtNombreAU);
        txtTelefono = (EditText) findViewById(R.id.txtTelefonoAU);
        txtLat = (EditText) findViewById(R.id.txtLatAU);
        txtLon = (EditText) findViewById(R.id.txtLonAU);
        Foto = findViewById(R.id.imgAct);

        txtNombre.setText(nombre);
        txtTelefono.setText(telefono);
        txtLat.setText(String.valueOf(latitud));
        txtLon.setText(String.valueOf(longitud));
        if(imgC != null){
            //mostrarFoto(imgString);
            Foto.setImageBitmap(imgC);
        }else{
            Foto.setImageResource(R.drawable.contacto);
        }

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ListarContactos.class);
                startActivity(intent);
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtNombre.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" , Toast.LENGTH_LONG).show();
                }else if (txtTelefono.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
                }else if (txtLat.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Active el GPS para obtener la latitud" ,Toast.LENGTH_LONG).show();
                }else if (txtLon.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Active el GPS para obtener la longitud" ,Toast.LENGTH_LONG).show();
                }
                else {
                    actualizarUsuario(id);
                }
            }
        });


    }

    public void mostrarFoto(String Base64String) {
        try {
            //String base64String = "data:image/png;base64,"+foto;
            //String base64Image = base64String.split(",")[1];
            String base64Image  = Base64String;
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Foto.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;
        //obtener la iamgen por el almacenamiento interno
        if(resultCode==RESULT_OK && requestCode==RESULT_GALLERY_IMG)
        {

            imageUri = data.getData();
            Foto.setImageURI(imageUri);
            try {
                imagen= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Error al seleccionar imagen", Toast.LENGTH_SHORT).show();
            }
        }
        //obtener la iamgen por la camara
        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            Foto.setImageBitmap(imagen);
        }

    }


    private String Sing() {

        try {
            Bitmap sign = captureBitmapView.getBitmap();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            sign.compress(Bitmap.CompressFormat.JPEG, 70, ba);
            byte[] arrayFoto = ba.toByteArray();
            String encode = Base64.encodeToString(arrayFoto, Base64.DEFAULT);

            return encode;
        }catch (Exception ex)
        {
            ex.toString();
        }
        return "";
    }

    //--------------------------------metodo actualizar imagen-------------------------------------------------
    private void actualizarUsuario(String id) {
        //String url = "https://transportweb2.online/APIExamen/listasinglecontacto.php?nombre=";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();
        //obtiene la foto tomada o seleccionada, luego verifica en un if si la variable
        // fotoString2 no este vacia ya que en caso que este vacia significa que no se actualiza la foto
        /*String fotoString2 = GetStringImage(imagen);
        if (fotoString2.equals("")||fotoString2.isEmpty()||fotoString2.equals(null)){
            fotoString2 = fotoString;
        }*/

        //setear los parametros mediante put
        parametros.put("id", id);
        parametros.put("nombre", txtNombre.getText().toString());
        parametros.put("telefono", txtTelefono.getText().toString());
        parametros.put("latitud", txtLat.getText().toString());
        parametros.put("longitud", txtLon.getText().toString());
        parametros.put("imagen", String.valueOf(Sing()));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.apiCreateContact,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "String Response " + response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }





    //-----------------------------LATITUD Y LONGITUD----------------------------------------
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActualizarContactos.Localizacion Local = new ActualizarContactos.Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            //SE VA A LA CONFIGURACION DEL SISTEMA PARA QUE ACTIVE EL GPS UNA VEZ QUE INICIA LA APLICACION
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);


    }


    public class Localizacion implements LocationListener {
        ActualizarContactos mainActivity;

        public void setMainActivity(ActualizarContactos mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();


            MainActivity.setLatitud(loc.getLatitude()+"");
            MainActivity.setLongitud(loc.getLongitude()+"");
            txtLat.setText(loc.getLatitude()+"");
            txtLon.setText(loc.getLongitude()+"");
            this.mainActivity.setLocation(loc);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ///

    public static void setLatitud(String latitud) {
        ActualizarContactos.latitud = latitud;
    }

    public static void setLongitud(String longitud) {
        ActualizarContactos.longitud = longitud;
    }
}