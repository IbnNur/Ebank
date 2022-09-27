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

public class Cadastro extends AppCompatActivity {

    EditText nome1, numero_conta1, senha1, bi1, data_nasc1, local_nasc1, saldo1, tipo_conta1;
    Button criar_btn;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome1 = findViewById(R.id.nome);
        numero_conta1 = findViewById(R.id.numero_conta);
        senha1 = findViewById(R.id.senha);
        bi1 = findViewById(R.id.bi);
        data_nasc1 = findViewById(R.id.data_nasc);
        local_nasc1 = findViewById(R.id.local_nasc);
        saldo1 = findViewById(R.id.saldo);
        tipo_conta1 = findViewById(R.id.tipo_conta);
        loadingBar = new ProgressDialog(this);
        criar_btn = findViewById(R.id.criar_btn);

        criar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CriarConta();
            }
        });
    }

    private void CriarConta() {
        String nome = nome1.getText().toString();
        String numero_conta = numero_conta1.getText().toString();
        String senha = senha1.getText().toString();
        String bi = bi1.getText().toString();
        String data_nasc = data_nasc1.getText().toString();
        String local_nasc = local_nasc1.getText().toString();
        String saldo = saldo1.getText().toString();
        String tipo_conta = tipo_conta1.getText().toString();

        if(TextUtils.isEmpty(nome))
        {
            Toast.makeText(this, "Digite nome", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(numero_conta))
        {
            Toast.makeText(this, "Digite numero da conta", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(senha))
        {
            Toast.makeText(this, "Digite a senha", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(bi))
        {
            Toast.makeText(this, "Digite o número de BI", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(data_nasc))
        {
            Toast.makeText(this, "Digite a data de nascimento", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(local_nasc))
        {
            Toast.makeText(this, "Digite o local de nascimento", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(saldo))
        {
            Toast.makeText(this, "Digite o saldo que ele deposita", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(tipo_conta))
        {
            Toast.makeText(this, "Digite o tipo de conta", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Criar conta");
            loadingBar.setMessage("Aguarde por favor!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Validacao(nome, numero_conta, senha, bi, data_nasc, local_nasc, saldo, tipo_conta);
        }
    }


    private void Validacao(String nome, String numero_conta, String senha, String bi, String
            data_nasc, String local_nasc, String saldo, String tipo_conta)
    {
            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    if(!(datasnapshot.child("Users").child(numero_conta).exists()))
                    {
                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("numero_conta", numero_conta);
                        userdataMap.put("senha", senha);
                        userdataMap.put("nome", nome);
                        userdataMap.put("bi", bi);
                        userdataMap.put("data_nasc", data_nasc);
                        userdataMap.put("local_nasc", local_nasc);
                        userdataMap.put("saldo", saldo);
                        userdataMap.put("tipo_conta", tipo_conta);

                        RootRef.child("Users").child(numero_conta).updateChildren(userdataMap).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(Cadastro.this, "Conta criada", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            Intent intent = new Intent(Cadastro.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(Cadastro.this, "Problemas com a internet", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                    }
                    else if (!(datasnapshot.child("Users").child(bi).exists()))
                    {
                        Toast.makeText(Cadastro.this, "Esse numero de bi ja foi usado para uma conta",
                                Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(Cadastro.this, "Acesse a conta",
                                Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Cadastro.this, "Esse numero de conta ja foi usado",
                                Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(Cadastro.this, "Acesse a conta",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}