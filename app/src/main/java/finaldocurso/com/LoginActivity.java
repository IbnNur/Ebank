package finaldocurso.com;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import finaldocurso.com.Modelo.Users;
import finaldocurso.com.Prevalent.Prevalent;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    EditText numero_conta2, senha2;
    CheckBox lembrar;
    Button login_btn;
    TextView admin_nao, admin;
    String pai = "Users";
    String nomeS, sal;

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_Ebank);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn = findViewById(R.id.login_btn);
        lembrar = findViewById(R.id.lembrar);
        senha2 = findViewById(R.id.senha);
        admin_nao = findViewById(R.id.admin_nao);
        numero_conta2 = findViewById(R.id.numero_conta);
        admin = findViewById(R.id.admin);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcessarConta();

            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login_btn.setText("Login de Administrador");
                admin.setVisibility(View.INVISIBLE);
                pai = "Admins";
            }
        });

        String userAccountKey = Paper.book().read(Prevalent.userAccountKey);
        String userPasswordtKey = Paper.book().read(Prevalent.userPasswordKey);

        if(userAccountKey != "" && userPasswordtKey != "")
        {
            if(!TextUtils.isEmpty(userAccountKey) && !TextUtils.isEmpty(userPasswordtKey))
            {
                AllowAcess(userAccountKey, userPasswordtKey);

                loadingBar.setTitle("Iniciando");
                loadingBar.setMessage("Aguarde por favor!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void AllowAcess(final String numero_conta, final String senha)
    {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(pai).child(numero_conta).exists())
                {
                    Users userData = dataSnapshot.child(pai).child(numero_conta).getValue(Users.class);

                    if(userData.getNumero_conta().equals(numero_conta))
                    {
                        if(userData.getSenha().equals(senha))
                        {
                            nomeS = userData.getNome();
                            sal = userData.getSaldo();
                            Toast.makeText(LoginActivity.this, "Bem vindo a sua conta"
                                    +" sr/a "+userData.getNome(), Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(LoginActivity.this, PaginaInicial.class);
                            intent.putExtra("Nome",nomeS);
                            intent.putExtra("Saldo",sal);

                            startActivity(intent);
                        }

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Esse número de conta não existe " +
                            "contacte o seu banco", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void AcessarConta()
    {
        String numero_conta = numero_conta2.getText().toString();
        String senha = senha2.getText().toString();

        if (TextUtils.isEmpty(numero_conta)) {
            Toast.makeText(LoginActivity.this, "Digite numero da conta", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(senha)) {
            Toast.makeText(LoginActivity.this, "Digite a senha", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Acessar a conta");
            loadingBar.setMessage("Aguarde por favor!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Permitir(numero_conta, senha);

        }
    }

    private void Permitir(String numero_conta, String senha)
    {
        if(lembrar.isChecked())
        {
            Paper.book().write(Prevalent.userAccountKey, numero_conta);
            Paper.book().write(Prevalent.userPasswordKey, senha);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(pai).child(numero_conta).exists())
                {
                    Users userData = dataSnapshot.child(pai).child(numero_conta).getValue(Users.class);

                    if(userData.getNumero_conta().equals(numero_conta))
                    {
                        if(userData.getSenha().equals(senha))
                        {

                            if(pai.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Bem vindo a sua conta"
                                        +" Administrador:  "+userData.getNome(), Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, PainelAdmin.class);
                                startActivity(intent);
                            }
                            else if(pai.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "Bem vindo a sua conta"
                                        +" sr/a "+userData.getNome(), Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, PaginaInicial.class);
                                startActivity(intent);
                            }
                        }

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Esse número de conta não existe " +
                            "contacte o seu banco", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}

