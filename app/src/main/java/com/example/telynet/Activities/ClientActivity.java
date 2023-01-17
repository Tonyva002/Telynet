package com.example.telynet.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.telynet.Helpes.UtilsHelper;
import com.example.telynet.Models.Client;
import com.example.telynet.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ClientActivity extends AppCompatActivity {

    private ImageView imgPhoto;
    private TextView name, code, phone, email, favorite;
    private MaterialButton call;
    private Client client;
    private final int PHONE_CALL_CODE = 100;

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

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = client.getTelefono();
                if (phoneNumber != null){
                    //Comprobar version de android que estamos corriendo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                    }else {
                        olderVersions(phoneNumber);
                    }
                }
            }
            private void olderVersions(String phoneNumber){
                Intent intenCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (checkPermission(Manifest.permission.CALL_PHONE)){
                    startActivity(intenCall);
                }else {
                    Toast.makeText(ClientActivity.this, "You declined the access", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PHONE_CALL_CODE:
                String permission = permissions[0];
                int result = grantResults[0];
                if (permission.equals(Manifest.permission.CALL_PHONE)){
                    //Comprobar si acepto o denego la peticion de permiso
                    if (result == PackageManager.PERMISSION_GRANTED){
                        //Concedio el permiso
                        String phoneNumber = client.getTelefono();
                        Intent intenCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            return;
                        }
                        startActivity(intenCall);
                    }else {
                        //Nego el permiso
                        Toast.makeText(this, "You declined access", Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean checkPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
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
        call = findViewById(R.id.btnCall);

    }

    // Metodo para configurar la toolbar
    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.detail_title_message);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }
}