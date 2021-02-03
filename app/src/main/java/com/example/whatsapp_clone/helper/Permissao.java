package com.example.whatsapp_clone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {


    public static boolean validarPermissao(String[] permissoes, Activity activity, int requestCode){

        List<String> listaPermissoes = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= 23){
            for (String permissao : permissoes){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao){
                    listaPermissoes.add(permissao);
                }
            }
            if (listaPermissoes.isEmpty()){
                return true;
            }
            String[] arrayPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(arrayPermissoes);

            ActivityCompat.requestPermissions(activity, arrayPermissoes, requestCode);
        }
        return true;
    }

}
