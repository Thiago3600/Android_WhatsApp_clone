package com.example.whatsapp_clone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.helper.ConfiguracaoFirebase;
import com.example.whatsapp_clone.helper.UsuarioFirebase;
import com.example.whatsapp_clone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class GalleryConfigActivity extends AppCompatActivity {

    private FloatingActionButton fabDel, fabGal, fabCam;
    private ConstraintLayout constraintLayout;
    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_GALERIA = 200;
    private Bitmap image;
    private StorageReference imgRef = ConfiguracaoFirebase.getStorageReference();
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_config);
        fabDel = findViewById(R.id.FabRemovePhoto);
        fabGal = findViewById(R.id.FabGaleriaPhoto);
        fabCam = findViewById(R.id.FabCameraPhoto);
        constraintLayout = findViewById(R.id.ConstraintGallery);
        fabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, REQUEST_CAMERA);
                }

            }
        });
        fabGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, REQUEST_GALERIA);

                }
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Bitmap", "RequestCode: " + requestCode);

        if (resultCode == RESULT_OK) {
            image = null;

            try {
                switch (requestCode) {
                    case REQUEST_CAMERA:
                        image = (Bitmap) data.getExtras().get("data");
                        Log.i("Bitmap", "Imagem recebida: "+image.toString());
                        break;
                    case REQUEST_GALERIA:
                        Log.i("Bitmap", "REQUEST_GALERIA = "+GalleryConfigActivity.REQUEST_GALERIA);
                        Uri imagemSelecionada = data.getData();
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                        Log.i("Bitmap", "Imagem recebida endereco: "+imagemSelecionada.getPath());
                        break;
                }
                if (image != null) {
                    Log.i("Bitmap", "Imagem recebida");
                    //img.setImageBitmap(image);
                    ConfiguracoesActivity.setImagePerfil(image);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImg = baos.toByteArray();
                    Log.i("Bitmap", "Imagem comprimida: ");

                    StorageReference storageReference = imgRef.child("img").child("perfil").child(UsuarioFirebase.getIdCurrentUser()).child("perfil.jpeg");
                    Log.i("Bitmap", "Caminho criado, usuario: "+ UsuarioFirebase.getIdCurrentUser());
                    UploadTask uploadTask = storageReference.putBytes(dadosImg);
                    Log.i("Bitmap", "Upload solicitado");
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GalleryConfigActivity.this, "Erro au fazer upload", Toast.LENGTH_SHORT).show();
                            Log.i("Bitmap", "Upload nao feito");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(GalleryConfigActivity.this, "Foto de perfil alterado", Toast.LENGTH_SHORT).show();
                            Log.i("Bitmap", "Upload feito");
                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFotoUsuario(url);

                                }
                            });

                        }
                    });
                    finish();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void atualizaFotoUsuario(Uri url) {
        if (UsuarioFirebase.atualizaFotoUsuario(url)){
            firebaseUser = UsuarioFirebase.getUsuarioAtual();
            Usuario usuario = UsuarioFirebase.getDadosUsuarioLogado();
            usuario.setFoto(url.toString());
            usuario.atualizar();
        }

    }

    private void atualizaNomeUsuario(String nome){
        UsuarioFirebase.atualizaNomeUsuario(nome);
    }


    public void sairSelecaoPhoto(View view){
        finish();
    }


}