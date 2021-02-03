package com.example.whatsapp_clone.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.whatsapp_clone.activity.ConfiguracoesActivity;
import com.example.whatsapp_clone.activity.GalleryConfigActivity;
import com.example.whatsapp_clone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UsuarioFirebase {


    public static @NotNull String getIdCurrentUser(){
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        return Base64Custom.codificarEmailBase64(Objects.requireNonNull(Objects.requireNonNull(auth.getCurrentUser()).getEmail()));
    }

    public static @NotNull FirebaseUser getUsuarioAtual(){
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        return Objects.requireNonNull(auth.getCurrentUser());
    }

    public static boolean atualizaNomeUsuario(String nome){
        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest upcr = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
            user.updateProfile(upcr).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar perfil");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizaFotoUsuario(Uri url){
        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest upcr = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();
            user.updateProfile(upcr).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar perfil");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseuser = getUsuarioAtual();
        Usuario usuario = new Usuario();

        usuario.setNome(firebaseuser.getDisplayName());
        usuario.setEmail(firebaseuser.getEmail());
        if (firebaseuser.getPhotoUrl() == null){
            usuario.setFoto("");
        }else{
            usuario.setFoto(firebaseuser.getPhotoUrl().toString());
        }
        return usuario;
    }


}
