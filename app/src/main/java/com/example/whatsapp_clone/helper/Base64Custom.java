package com.example.whatsapp_clone.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarEmailBase64(String email){
        return Base64.encodeToString(email.getBytes(), Base64.DEFAULT).replaceAll(("\\n|\\r"), "");
    }
    public static String decodificarEmailBase64(String key){
        return String.valueOf(Base64.decode(key.getBytes(), Base64.DEFAULT));
    }

}
