package br.com.jc2mit.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.jc2mit.sqlite.model.Cliente;
import br.com.jc2mit.sqlite.remote.APIUtils;
import br.com.jc2mit.sqlite.remote.ClienteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRest extends AppCompatActivity {

    Button btnListCliente, btnAddCli, btnBackCli;
    ListView listView;

    ClienteService clienteService;
    List<Cliente> list = new ArrayList<Cliente>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clirest);

        btnListCliente = (Button) findViewById(R.id.btnListCliente);
        btnAddCli = (Button) findViewById(R.id.btnAddCliente);
        btnBackCli = (Button) findViewById(R.id.btnCliRestBack);
        listView = (ListView) findViewById(R.id.listView);
        clienteService = APIUtils.getClienteService();

        btnListCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClienteList();
            }
        });

        btnAddCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteRest.this, ClienteActivity.class);
                intent.putExtra("cli_nm", "");
                startActivity(intent);
            }
        });

        btnBackCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClienteRest.this, MainActivity.class);
                intent.putExtra("cli_nm", "");
                startActivity(intent);
            }
        });

    }

    public void getClienteList(){

        Call<List<Cliente>> call = clienteService.getClientes();
        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    listView.setAdapter(new ClienteAdapter(ClienteRest.this, R.layout.list_clientes, list));
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }

}
