package com.grupom.pm1examen2grupo2.Config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.grupom.pm1examen2grupo2.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Usuario> implements View.OnClickListener
{
    private List<Usuario> dataset;
    private Context mContext;

    public static class ViewHolder{
        TextView id, numero, nombre, latitud, longitud;
        ImageView foto, img;
    }

    public CustomAdapter(List<Usuario> data, Context context) {
        super(context, R.layout.mylist, data);

        this.dataset = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        Object object = getItem(position);

        Usuario datModel = (Usuario) object;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Usuario datosModel = getItem(position);
        ViewHolder viewHolder = null;
        final View Result;

        if(convertView == null){
            viewHolder = new ViewHolder();


            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.mylist, parent, false);

            viewHolder.numero = convertView.findViewById(R.id.txtNumero);
            viewHolder.nombre = convertView.findViewById(R.id.txtNombreContacto);
            viewHolder.foto = convertView.findViewById(R.id.image);
            viewHolder.img = convertView.findViewById(R.id.imgCk);
            Result = convertView;
            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) convertView.getTag();
            convertView.setTag(viewHolder);
        }

        viewHolder.numero.setText("Tel: "+datosModel.getTelefono());
        viewHolder.nombre.setText("Nombre: "+datosModel.getNombre());
        if (datosModel.getImagen().length()>0){
            Log.i("LOG",""+datosModel.getImagen().length());
            viewHolder.foto.setImageBitmap(ConvertBase64toImage(datosModel.imagen));
        }
        if (datosModel.isSeleccionado()){
            viewHolder.img.setImageResource(R.drawable.ck);
        }else{
            viewHolder.img.setImageResource(R.drawable.ico);

        }


        return convertView;
    }

    private Bitmap ConvertBase64toImage(String Base64String)
    {
        //String base64Image  = Base64String.split(",")[1];
        String base64Image  = Base64String;
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public void update(List<Usuario> list){
        this.dataset = list;

        notifyDataSetChanged();
    }
}
