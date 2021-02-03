package com.example.whatsapp_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText inputEditTextEmail, inputEditTextSenha;
    private Button buttonLogar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEditTextEmail = findViewById(R.id.TextInputEditTextLoginEmail);
        inputEditTextSenha = findViewById(R.id.TextInputEditTextLoginSenha);
        buttonLogar = findViewById(R.id.buttonLogin);
        //auth = ConfiguracaoFirebase.getFirebaseAuth();
        //auth.signOut();

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        if (auth.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    public String getString(TextInputEditText text){
        return text.getText().toString();
    }
    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }
    public void autenticacaoUsuario(View view){
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        String email = getString(inputEditTextEmail);
        String senha = getString(inputEditTextSenha);

        if (validarCamposLogin(email , senha)){
            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                        abrirTelaPrincipal();
                    }else{
                        String excecao = "";
                        try {
                            throw task.getException();
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            excecao = "Senha invalida";
                        }catch (FirebaseAuthInvalidUserException e){
                            excecao = "Digite um email valido";
                        }catch (Exception e) {
                            excecao = "Erro ao cadastrar o usuario: "+e.getMessage();
                            e.printStackTrace();
                        }
                        Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean validarCamposLogin(String email, String senha){
        if (!email.isEmpty()){
            if (!senha.isEmpty()){
                return true;
            }else{
                Toast.makeText(LoginActivity.this, "Preencha o campo senha", Toast.LENGTH_SHORT);
            }
        }else{
            Toast.makeText(LoginActivity.this, "Preencha o campo email", Toast.LENGTH_SHORT);
        }
        return false;
    }



    public void novoUsuario(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
        finish();
    }
}