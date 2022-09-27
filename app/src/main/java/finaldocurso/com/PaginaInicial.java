package finaldocurso.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import finaldocurso.com.Modelo.Users;
import finaldocurso.com.Prevalent.Prevalent;
import io.paperdb.Paper;

public class PaginaInicial extends AppCompatActivity {

    TextView transferir, levantar, saldo_actual, nome_user;
    ImageView sair;
    String nome, saldo, user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial);
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        transferir = findViewById(R.id.transferir);
        levantar = findViewById(R.id.levantar);
        saldo_actual = findViewById(R.id.saldo_actual);
        nome_user = findViewById(R.id.nome_user);
        sair = findViewById(R.id.sair);
        Intent intent = new Intent(getIntent());


        saldo_actual.setText(intent.getStringExtra("Saldo")+" MZN");
        nome_user.setText(intent.getStringExtra("Nome"));
        Paper.init(this);

        transferir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PaginaInicial.this, Transferencia.class);
                startActivity(intent);
            }
        });

        levantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PaginaInicial.this, Levantamento.class);
                startActivity(intent);
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();

                Intent intent = new Intent(PaginaInicial.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}