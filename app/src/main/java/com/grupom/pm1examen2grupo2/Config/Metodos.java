package com.grupom.pm1examen2grupo2.Config;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import androidx.appcompat.app.AlertDialog;

import java.io.ByteArrayOutputStream;
public class Metodos {

    public byte[] setBitmaptobyte(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();
        return ArrayFoto;

    }
}
