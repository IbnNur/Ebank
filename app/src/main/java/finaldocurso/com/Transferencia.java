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

public class Transferencia extends AppCompatActivity {

    EditText transferir_conta, valor;
    Button transferir_btn;
    String pai = "Users";
    Float val, val1, soma, soma1;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);

        transferir_conta = findViewById(R.id.transferir_conta);
        transferir_btn = findViewById(R.id.transferir_btn);
        valor = findViewById(R.id.valor);
        loadingBar = new ProgressDialog(this);

        transferir_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FazerTransferencia();
            }
        });

    }
    String userAccountKey = Paper.book().read(Prevalent.userAccountKey);

    private void FazerTransferencia() {
        String numero_conta = transferir_conta.getText().toString();
        String saldo = valor.getText().toString();

        if (TextUtils.isEmpty(numero_conta)) {
            Toast.makeText(Transferencia.this, "Digite numero da conta", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(saldo)) {
            Toast.makeText(Transferencia.this, "Digite o valor a depositar", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Transferindo da conta" + userAccountKey + "para a conta " + numero_conta);
            loadingBar.setMessage("Aguarde por favor!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            TransferirValor(numero_conta, saldo);
        }
    }

    private void TransferirValor(String numero_conta, String saldo)
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
                        Toast.makeText(Transferencia.this, "Valor que esta na conta e' menor",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Transferencia.this, Transferencia.class);
                        startActivity(intent);
                    }
                    else
                    {
                        soma = val - Float.parseFloat(saldo);

                    }

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("saldo",soma);
                    RootRef.child("Users").child(userAccountKey).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(Transferencia.this, "Ficou com"
                                        +saldo+" MZN", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(Transferencia.this, "Problemas com a internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                else
                {
                    Toast.makeText(Transferencia.this, "Esta conta para qual quer fazer transferencia" +
                            " não existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                if(datasnapshot.child(pai).child(numero_conta).exists())
                {
                    Users userU = datasnapshot.child(pai).child(numero_conta).getValue(Users.class);

                    val1 = Float.parseFloat(userU.getSaldo().toString());
                    soma1 = val1 + Float.parseFloat(saldo);

                }
                HashMap<String, Object> userdataP = new HashMap<>();
                userdataP.put("saldo",soma1);


                RootRef.child("Users").child(numero_conta).updateChildren(userdataP).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Transferencia.this, "Levantou "
                                            +saldo+" MZN", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(Transferencia.this, PaginaInicial.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    loadingBar.dismiss();
                                    Toast.makeText(Transferencia.this, "Problemas com a internet", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
