package com.example.whatsapp_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsapp_clone.helper.Base64Custom;
import com.example.whatsapp_clone.helper.ConfiguracaoFirebase;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.helper.UsuarioFirebase;
import com.example.whatsapp_clone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText inputEditTextNome, inputEditTextEmail, inputEditTextSenha;
    private Button buttonCadastrar;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inputEditTextNome = findViewById(R.id.TextInputEditTextCadastroNome);
        inputEditTextEmail = findViewById(R.id.TextInputEditTextEmail);
        inputEditTextSenha = findViewById(R.id.TextInputEditTextLoginSenha);
        buttonCadastrar = findViewById(R.id.buttonCadastro);
    }



    public boolean validarCampos(String nome, String email, String senha){
        if (!nome.isEmpty()){
            if (!email.isEmpty()){
                if (!senha.isEmpty()){
                    if (senha.length() < 6){
                        Toast.makeText(CadastroActivity.this, "Digite uma senha com 6 digitos", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }else{
                    Toast.makeText(CadastroActivity.this, "Digite uma senha", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(CadastroActivity.this, "Digite um email", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroActivity.this, "Digite um nome", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void novoUsuarioFirebase(Usuario usuario){
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        database = ConfiguracaoFirebase.getFirebaseDatabase();

        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    try {
                        usuario.salvar();
                        UsuarioFirebase.atualizaNomeUsuario(usuario.getNome());
                        Toast.makeText(CadastroActivity.this, "Cadastro criado com sucesso", Toast.LENGTH_SHORT).show();
                        abrirTelaPrincipal();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                       excecao = "Senha invalida";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email invalido";
                    }catch (FirebaseAuthUserCollisionException e ){
                        excecao = "Email ja cadastrado";
                    }catch(Exception e) {
                        excecao = "Erro ao cadastrar o usuario: "+e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void cadastrarNovoUsuario(View view) {
        String nome = inputEditTextNome.getText().toString();
        String email = inputEditTextEmail.getText().toString();
        String senha = inputEditTextSenha.getText().toString();
        if (validarCampos(nome, email, senha)){
            usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            novoUsuarioFirebase(usuario);
        }
    }
}