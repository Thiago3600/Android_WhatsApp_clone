package com.example.whatsapp_clone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.model.Usuario;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContatos extends RecyclerView.Adapter<AdapterContatos.MyViewHolder> {

    private ArrayList<Usuario> listaContatos;
    private Context context;

    public AdapterContatos(ArrayList<Usuario> listaContatos, Context context) {
        this.listaContatos = listaContatos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contato_adapter, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Usuario usuario = listaContatos.get(position);

        holder.textViewNomeContato.setText(usuario.getNome());
        holder.textViewEmailContato.setText(usuario.getEmail());
        if (usuario.getFoto() != null){
            Uri url = Uri.parse(usuario.getFoto());
            Glide.with(context).load(url).into(holder.imgContato);
        }else{
            holder.imgContato.setImageResource(R.drawable.padrao);
        }

    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imgContato;
        private TextView textViewNomeContato;
        private TextView textViewEmailContato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgContato = itemView.findViewById(R.id.circleImageViewPerfilContato);
            textViewNomeContato = itemView.findViewById(R.id.textViewNomeContato);
            textViewEmailContato = itemView.findViewById(R.id.textViewEmailContato);

        }
    }

}
