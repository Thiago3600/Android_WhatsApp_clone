package com.example.whatsapp_clone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.activity.LoginActivity;
import com.example.whatsapp_clone.fragment.ContatoFragment;
import com.example.whatsapp_clone.fragment.ConversaFragment;
import com.example.whatsapp_clone.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Whatsapp");
        setSupportActionBar(toolbar);

        /*SmartTabLayout - Abas*/
        FragmentPagerItemAdapter pagerItemAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Conversa", ConversaFragment.class)
                .add("Contato", ContatoFragment.class)
                .create()

        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerItemAdapter);

        SmartTabLayout smartTabLayout = findViewById(R.id.viewPagerTab);
        smartTabLayout.setViewPager(viewPager);

    }

    public void deslogarUsuario(){
        try {
            auth = ConfiguracaoFirebase.getFirebaseAuth();
            auth.signOut();
            Toast.makeText(MainActivity.this, "Usuario deslogado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void abrirTelaConfig(){
        startActivity(new Intent(this, ConfiguracoesActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuConfig:
                abrirTelaConfig();

                break;
            case R.id.menuSair:
                deslogarUsuario();
                abrirTelaLogin();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void abrirTelaLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}