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

public class Deposito extends AppCompatActivity {

    Button depositar;
    EditText dep_conta, valor;
    ProgressDialog loadingBar;
    String pai = "Users";
    Float  val,soma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposito);

        depositar = findViewById(R.id.depositar);
        dep_conta = findViewById(R.id.dep_conta);
        valor = findViewById(R.id.valor);
        loadingBar = new ProgressDialog(this);

        depositar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FazerDeposito();

            }
        });
    }

    private void FazerDeposito()
    {
        String numero_conta = dep_conta.getText().toString();
        String saldo = valor.getText().toString();

        if (TextUtils.isEmpty(numero_conta)) {
            Toast.makeText(Deposito.this, "Digite numero da conta", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(saldo)) {
            Toast.makeText(Deposito.this, "Digite o valor a depositar", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Depositando "+saldo+"na conta "+numero_conta);
            loadingBar.setMessage("Aguarde por favor!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Depositar(numero_conta, saldo);

        }
    }

    private void Depositar(String numero_conta, String saldo)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                if (datasnapshot.child(pai).child(numero_conta).exists())
                {
                    Users userData = datasnapshot.child(pai).child(numero_conta).getValue(Users.class);

                    val = Float.parseFloat(userData.getSaldo().toString());
                    soma = Float.parseFloat(saldo)+val;

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("saldo", soma);

                    RootRef.child("Users").child(numero_conta).updateChildren(userdataMap).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Deposito.this, "Deposito Feito", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Deposito.this, PainelAdmin.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Deposito.this, "Problemas com a internet", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(Deposito.this, "Esta para qual quer fazer deposito" +
                            "não existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}