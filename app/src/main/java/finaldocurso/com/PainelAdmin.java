package finaldocurso.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PainelAdmin extends AppCompatActivity {

    Button btn_criar_conta, btn_deposito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_admin);

        btn_criar_conta = findViewById(R.id.btn_criar_conta);
        btn_deposito = findViewById(R.id.btn_deposito);

        btn_criar_conta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PainelAdmin.this, Cadastro.class);
                startActivity(intent);
            }
        });

        btn_deposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PainelAdmin.this, Deposito.class);
                startActivity(intent);
            }
        });

    }
}