package com.example.whatsapp_clone.model;

import com.example.whatsapp_clone.helper.Base64Custom;
import com.example.whatsapp_clone.helper.ConfiguracaoFirebase;
import com.example.whatsapp_clone.helper.UsuarioFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String foto;

    public void salvar(){
        this.setId();
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase();
        usuarioRef.child("usuarios").child(this.getId()).setValue(this);
    }

    public void atualizar(){
        String idUser = UsuarioFirebase.getIdCurrentUser();
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference userRef = database.child("usuarios").child(idUser);

        Map<String, Object> valoresUsuario = converterParaMap();
        userRef.updateChildren(valoresUsuario);
    }
    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();

        usuarioMap.put("email", this.getEmail());
        usuarioMap.put("nome", this.getNome());
        usuarioMap.put("foto", this.getFoto());

        return usuarioMap;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }
    @Exclude
    public void setSenha(String senha) {
        this.senha = senha;
    }
    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId() {
        this.id = Base64Custom.codificarEmailBase64(this.getEmail());
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
