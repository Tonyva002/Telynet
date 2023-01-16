package com.example.telynet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.telynet.Adapters.ClientAdapter;
import com.example.telynet.Helpes.UtilsHelper;
import com.example.telynet.Models.Client;
import com.example.telynet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ClientAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    final ArrayList<Client> clients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        onInit();
        setToolbar();


        UtilsHelper.getDatabase().child("client").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<Client> clients = new ArrayList<>();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Client post = dataSnapshot.getValue(Client.class);
                    assert post != null;
                    post.setIdClient(dataSnapshot.getKey());
                    clients.add(post);
                }
                mAdapter = new ClientAdapter(HomeActivity.this, clients, R.layout.client_adapter, (client, position) -> goToClient(clients.get(position).getIdClient()));

                mLinearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


      /*  clients.add(new Client("A1", "Pedro Solano", "809-459-5623", "luis@hotmail.com", R.drawable.foto_01, true));
        clients.add(new Client("A2", "Juan Perez", "829-529-5229", "omar@hotmail.com", R.drawable.foto_02, false));
        clients.add(new Client("A3", "Pedro Lopez", "849-555-4683", "ana@hotmail.com", R.drawable.foto_04, true));
        clients.add(new Client("A4", "Carmen Fernandez", "809-339-6421", "maria@hotmail.com", R.drawable.foto_05, false));



        mAdapter = new ClientAdapter(HomeActivity.this, clients, R.layout.client_adapter, (client, position) -> goToClient(clients.get(position).getIdClient()));

        mLinearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);*/
    }

    // Metodo para referenciar las vistas del layout
    private void onInit(){
        mRecyclerView = findViewById(R.id.recyclerview);
    }


    // Metodo para configurar la toolbar
    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

    }


    // Metodo para navegar a  AddressActivity
    private void goToClient(String key){
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra("value", key);
        startActivity(intent);
    }
}

