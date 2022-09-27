package finaldocurso.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import finaldocurso.com.Modelo.Users;
import finaldocurso.com.Prevalent.Prevalent;
import io.paperdb.Paper;

public class Levantamento extends AppCompatActivity {

    Button levantar_btn;
    EditText valor;
    ProgressDialog loadingBar;
    String pai = "Users";
    Float val, soma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levantamento);

        levantar_btn = findViewById(R.id.levantar_btn);
        valor = findViewById(R.id.valor);
        loadingBar = new ProgressDialog(this);

        levantar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FazerLevantamento();
            }
        });
    }
    String userAccountKey = Paper.book().read(Prevalent.userAccountKey);

    private void FazerLevantamento()
    {
        String numero_conta = userAccountKey;
        String saldo = valor.getText().toString();

       if (TextUtils.isEmpty(saldo)) {
            Toast.makeText(Levantamento.this, "Digite o valor", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Depositando "+saldo+"na conta "+numero_conta);
            loadingBar.setMessage("Aguarde por favor!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Levantar(saldo);

        }
    }

    private void Levantar(String saldo)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                if (datasnapshot.child(pai).child(userAccountKey).exists())
                {
                    Users userData = datasnapshot.child(pai).child(userAccountKey).getValue(Users.class);

                    val = Float.parseFloat(userData.getSaldo().toString());

                    if(val < Float.parseFloat(saldo))
                    {
                        Toast.makeText(Levantamento.this, "Valor que esta na conta e' menor",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Levantamento.this, Levantamento.class);
                        startActivity(intent);
                    }
                    else
                    {
                        soma = val - Float.parseFloat(saldo);
                    }


                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("saldo",soma);

                    RootRef.child("Users").child(userAccountKey).updateChildren(userdataMap).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Levantamento.this, "Levantou "
                                                +saldo+" MZN", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Levantamento.this, PaginaInicial.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Levantamento.this, "Problemas com a internet", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(Levantamento.this, "Esta para qual quer fazer deposito" +
                            "não existe", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}