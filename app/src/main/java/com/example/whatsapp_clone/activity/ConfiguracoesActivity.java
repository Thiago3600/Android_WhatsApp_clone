package com.example.whatsapp_clone.activity;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.helper.ConfiguracaoFirebase;
import com.example.whatsapp_clone.helper.Permissao;
import com.example.whatsapp_clone.helper.UsuarioFirebase;
import com.example.whatsapp_clone.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private FloatingActionButton fabPhoto;
    private LinearLayout linearLayout;
    protected static CircleImageView circleImageViewPerfil;
    public TextInputEditText inputEditTextNomeUsuario;
    private FirebaseUser firebaseUser;
    private ImageView imageViewEdit;
    public Usuario usuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        fabPhoto = findViewById(R.id.fabPhoto);
        circleImageViewPerfil = findViewById(R.id.perfilImage);
        inputEditTextNomeUsuario = findViewById(R.id.TextInputEditTextNomeUsuario);
        imageViewEdit = findViewById(R.id.imageViewEditConfig);
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Ajustes");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Permissao.validarPermissao(permissoesNecessarias, this, 1);
        firebaseUser = UsuarioFirebase.getUsuarioAtual();
        Uri url = firebaseUser.getPhotoUrl();
        if (url != null){
            Glide.with(ConfiguracoesActivity.this)
                    .load(url)
                    .into(circleImageViewPerfil);
        }else{
            circleImageViewPerfil.setImageResource(R.drawable.padrao);
        }
        inputEditTextNomeUsuario.setText(firebaseUser.getDisplayName());
    }

    public void selecionarFoto(View view){
        startActivity(new Intent(this, GalleryConfigActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissoesNegadas : grantResults){
            if (permissoesNegadas == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }


    public static void setImagePerfil(Bitmap bitmap){
        if (bitmap != null){
            circleImageViewPerfil.setImageBitmap(bitmap);
        }
    }
    public static CircleImageView getImagePerfil(){
        return circleImageViewPerfil;
    }




    @Override
    protected void onResume() {
        super.onResume();
    }

    public void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes");
        builder.setMessage("Para poder alterar sua foto de perfil e preciso aceitar essas permissoes, vai aceitar?");
        builder.setCancelable(false);
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recreate();
            }
        });
        builder.create().show();

    }

    public void alterarNome(View view) {


        String nome = inputEditTextNomeUsuario.getText().toString();
        String idUser = UsuarioFirebase.getIdCurrentUser();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();


        if (!nome.isEmpty()){
            if (UsuarioFirebase.atualizaNomeUsuario(nome)){

                usuarioLogado.setNome(nome);
                usuarioLogado.atualizar();
                //database.child("Usuario").child(idUser).child("nome").setValue(nome);
                Toast.makeText(this, "Nome alterado", Toast.LENGTH_SHORT).show();
            }


        }

    }
}