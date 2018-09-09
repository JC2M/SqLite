package br.com.jc2mit.sqlite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.jc2mit.sqlite.model.Cliente;
import br.com.jc2mit.sqlite.remote.APIUtils;
import br.com.jc2mit.sqlite.remote.ClienteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteActivity extends AppCompatActivity {

    ClienteService clienteService;
    EditText edtCliCodigo;
    EditText edtCliNome;
    EditText edtCliTelefone;
    EditText edtCliEmail;
    Button btnCliBack;
    Button btnCliSalvar;
    Button btnCliDeletar;
    TextView txtCliCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        txtCliCodigo = (TextView) findViewById(R.id.txtCliCodigo);
        edtCliCodigo = (EditText) findViewById(R.id.edtCliCodigo);
        edtCliNome = (EditText) findViewById(R.id.edtCliNome);
        edtCliTelefone = (EditText) findViewById(R.id.edtCliTelefone);
        edtCliEmail = (EditText) findViewById(R.id.edtCliEmail);
        btnCliBack = (Button) findViewById(R.id.btnCliBack);
        btnCliSalvar = (Button) findViewById(R.id.btnCliSalvar);
        btnCliDeletar = (Button) findViewById(R.id.btnCliDeletar);

        clienteService = APIUtils.getClienteService();

        Bundle extras = getIntent().getExtras();
        final String cliCodigo = extras.getString("cli_cod");
        String cliNome = extras.getString("cli_nm");
        String cliTelefone = extras.getString("cli_tel");
        String cliEmail = extras.getString("cli_email");

        edtCliCodigo.setText(cliCodigo);
        edtCliNome.setText(cliNome);
        edtCliTelefone.setText(cliTelefone);
        edtCliEmail.setText(cliEmail);

        if (cliCodigo != null && cliCodigo.trim().length() > 0) {

            edtCliCodigo.setFocusable(false);

        } else {

            txtCliCodigo.setVisibility(View.INVISIBLE);
            edtCliCodigo.setVisibility(View.INVISIBLE);
            btnCliDeletar.setVisibility(View.INVISIBLE);

        }



        btnCliSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cliente cli = new Cliente();
                cli.setNome(edtCliNome.getText().toString());
                cli.setTelefone(edtCliTelefone.getText().toString());
                cli.setEmail(edtCliEmail.getText().toString());

                if (cliCodigo != null && cliCodigo.trim().length() > 0) {

                    updateCliente(cliCodigo, cli);

                } else {

                    addCliente(cli);

                }

            }
        });

        btnCliBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteActivity.this, ClienteRest.class);
                startActivity(intent);
            }
        });

        btnCliDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCliente(cliCodigo);
                Intent intent = new Intent(ClienteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public  void addCliente(Cliente c) {

        Call<Cliente> call = clienteService.addCliente(c);
        call.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClienteActivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage());
            }
        });

    }

    public  void updateCliente(String cod, Cliente c) {

        Call<Cliente> call = clienteService.updateCliente(cod, c);
        call.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClienteActivity.this, "Usuário alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage());
            }
        });

    }

    public  void deleteCliente(String cod) {

        Call<Cliente> call = clienteService.deleteCliente(cod);
        call.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ClienteActivity.this, "Usuário deletado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.d("ERROR: ", t.getMessage());
            }
        });

    }

}
