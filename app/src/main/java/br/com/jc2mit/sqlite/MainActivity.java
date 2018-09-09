package br.com.jc2mit.sqlite;

import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.jc2mit.sqlite.model.Cliente;
import br.com.jc2mit.sqlite.remote.APIUtils;
import br.com.jc2mit.sqlite.remote.ClienteService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText editCodigo, editNome, editTelefone, editEmail;
    Button btnLimpar, btnSalvar, btnExcluir, btnClienteRest;
    ListView listViewClientes;

    BancoDeDados db = new BancoDeDados(this);

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCodigo = (EditText) findViewById(R.id.editCodigo);
        editNome = (EditText) findViewById(R.id.editNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        editEmail = (EditText) findViewById(R.id.editEmail);

        btnLimpar = (Button) findViewById(R.id.btnLimpar);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        listViewClientes = (ListView) findViewById(R.id.listViewClientes);

        btnClienteRest = (Button) findViewById(R.id.btnClienteRest);

        imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);

        listarClientes();

        listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String conteudo = (String) listViewClientes.getItemAtPosition(i);

                //Toast.makeText(MainActivity.this, "Select: " + conteudo, Toast.LENGTH_LONG).show();
                String codigo = conteudo.substring(0, conteudo.indexOf("@"));

                Cliente cliente = db.selecionaCliente(codigo);

                editCodigo.setText(cliente.getCodigo());
                editNome.setText(cliente.getNome());
                editTelefone.setText(cliente.getTelefone());
                editEmail.setText(cliente.getEmail());

            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limparCampos();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigo = editCodigo.getText().toString();
                String nome = editNome.getText().toString();
                String telefone = editTelefone.getText().toString();
                String email = editEmail.getText().toString();

                if (nome.isEmpty()) {
                    editNome.setError("Este campo é obrigatório");
                } else if (codigo.isEmpty()) {
                    db.addCliente(new Cliente(nome,telefone,email));
                    Toast.makeText(MainActivity.this, "Cliente adicionado com sucesso", Toast.LENGTH_LONG).show();
                    limparCampos();
                    listarClientes();
                    escondeTeclado();
                } else {
                    db.atualizaCliente(new Cliente(codigo,nome,telefone,email));
                    Toast.makeText(MainActivity.this, "Cliente atualizado com sucesso", Toast.LENGTH_LONG).show();
                    limparCampos();
                    listarClientes();
                    escondeTeclado();
                }

            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String codigo = editCodigo.getText().toString();

                if (codigo.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Nenhum cliente foi selecionado", Toast.LENGTH_LONG).show();
                } else {
                    Cliente cliente = new Cliente();
                    cliente.setCodigo(codigo);
                    db.apagarCliente(cliente);
                    Toast.makeText(MainActivity.this, "Cliente excluido com sucesso", Toast.LENGTH_LONG).show();
                    limparCampos();
                    listarClientes();
                }

            }
        });

        btnClienteRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ClienteRest.class);
                intent.putExtra("cli_nm","");
                startActivity(intent);
            }
        });

        /* TESTE DO CRUD */

        /* insert ok
        db.addCliente(new Cliente("JULIO CESAR MION DE MORAES","15981037142","miondemoraes@gmail.com"));
        db.addCliente(new Cliente("LUCINEIDE DA SILVA SANTOS DE MORAES","15981670017","neidedemoraes@gmail.com"));
        db.addCliente(new Cliente("BRUNO CESAR DE MORAES","15981686846","brunodemoraes@gmail.com"));
        db.addCliente(new Cliente("LUCCA VINICIUS DE MORAES","15997758502","luccademoraes@gmail.com"));
        db.addCliente(new Cliente("MARIA EDUARDA DE MORAES","1532825093","mariademoraes@gmail.com"));
        Toast.makeText(MainActivity.this, "Salvo com sucesso", Toast.LENGTH_LONG).show();
        */


        /* delete ok
        Cliente cliente = new Cliente();
        cliente.setCodigo(1);
        db.apagarCliente(cliente);
        cliente.setCodigo(2);
        db.apagarCliente(cliente);
        cliente.setCodigo(3);
        db.apagarCliente(cliente);
        cliente.setCodigo(4);
        db.apagarCliente(cliente);
        cliente.setCodigo(5);
        db.apagarCliente(cliente);
        Toast.makeText(MainActivity.this, "Apagado com sucesso", Toast.LENGTH_LONG).show();
        */


        /* select ok
        Cliente cliente = db.selecionaCliente(1);
        Log.d("Cliente Selecionado", "Código: " + cliente.getCodigo() +
                                                " Nome: " + cliente.getNome() +
                                                " Telefone: " + cliente.getTelefone() +
                                                " Email: " + cliente.getEmail());
        */

        /* update ok
        Cliente cliente = new Cliente();
        cliente.setCodigo(1);
        cliente.setNome("Julio de Moraes");
        cliente.setTelefone("1532825093");
        cliente.setEmail("julio@indusparquet.com.br");

        db.atualizaCliente(cliente);

        Toast.makeText(MainActivity.this, "Atualizado com sucesso", Toast.LENGTH_LONG).show();
        */

    }

    void limparCampos() {
        editCodigo.setText("");
        editNome.setText("");
        editTelefone.setText("");
        editEmail.setText("");

        editNome.requestFocus();
    }

    void escondeTeclado() {
        imm.hideSoftInputFromWindow(editNome.getWindowToken(),0);
    }

    public void listarClientes(){

        List<Cliente> clientes = db.listaTodosClientes();

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);

        listViewClientes.setAdapter(adapter);

        for (Cliente c : clientes) {
            Log.d("Lista", "\nCódigo: " + c.getCodigo() + " Nome: " + c.getNome());
            arrayList.add(c.getCodigo() + "@" + c.getNome());
            adapter.notifyDataSetChanged();
        }

    }

}
