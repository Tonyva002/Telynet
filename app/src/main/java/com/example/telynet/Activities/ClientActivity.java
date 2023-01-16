package com.example.telynet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.telynet.Helpes.UtilsHelper;
import com.example.telynet.Models.Client;
import com.example.telynet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ClientActivity extends AppCompatActivity {

    private ImageView imgPhoto;
    private TextView name, code, phone, email, favorite;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        setToolbar();
        onInit();

        final String mIdClient = getIntent().getStringExtra("value");
        UtilsHelper.getDatabase().child("client").child(mIdClient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onSetClient(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void onSetClient(DataSnapshot snapshot){
        client = snapshot.getValue(Client.class);
        Glide.with(getApplicationContext())
                .load(client.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(imgPhoto);
        name.setText(client.getNombre());
        code.setText(client.getCodigo());
        phone.setText(client.getTelefono());
        email.setText(client.getEmail());
        favorite.setText(client.getVisitado());
    }

    // Metodo para referenciar las vistas del layout
    private void onInit(){
        imgPhoto = findViewById(R.id.imgPhoto);
        name = findViewById(R.id.tvName);
        code = findViewById(R.id.tvCode);
        phone = findViewById(R.id.tvPhone2);
        email = findViewById(R.id.tvEmail2);
        favorite = findViewById(R.id.tvFavorite2);

    }

    // Metodo para configurar la toolbar
    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.detail_title_message);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }
}