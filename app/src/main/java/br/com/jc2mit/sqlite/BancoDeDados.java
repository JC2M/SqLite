package br.com.jc2mit.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.jc2mit.sqlite.model.Cliente;

public class BancoDeDados extends SQLiteOpenHelper {

    private static final int VERSAO_BANCO = 1;
    private static final String BANCO_CLIENTE = "db_clientes";

    private static final String TABELA_CLIENTE = "tb_clientes";
    private static final String COLUNA_CODIGO = "codigo";
    private static final String COLUNA_NOME = "nome";
    private static final String COLUNA_TELEFONE = "telefone";
    private static final String COLUNA_EMAIL = "email";

    public BancoDeDados(Context context) {

        super(context, BANCO_CLIENTE, null, VERSAO_BANCO);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABELA_CLIENTE + "( "
                            + COLUNA_CODIGO + " TEXT PRIMARY KEY, "
                            + COLUNA_NOME + " TEXT, "
                            + COLUNA_TELEFONE + " TEXT, "
                            + COLUNA_EMAIL + " TEXT) ";

        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    /*CRUD*/
    void addCliente(Cliente cliente) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUNA_CODIGO, UUID.randomUUID().toString());
        values.put(COLUNA_NOME, cliente.getNome());
        values.put(COLUNA_TELEFONE, cliente.getTelefone());
        values.put(COLUNA_EMAIL, cliente.getEmail());

        db.insert(TABELA_CLIENTE, null, values);
        db.close();

    }

    void apagarCliente(Cliente cliente) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        db.delete(TABELA_CLIENTE, COLUNA_CODIGO + " = ?", new String[] {cliente.getCodigo()});

        db.close();

    }

    Cliente selecionaCliente(String codigo) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABELA_CLIENTE, new String[] {COLUNA_CODIGO, COLUNA_NOME, COLUNA_TELEFONE, COLUNA_EMAIL},COLUNA_CODIGO + " = ? ", new String[] {codigo}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Cliente cliente1 = new Cliente(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

        return cliente1;

    }

    void atualizaCliente(Cliente cliente) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUNA_NOME, cliente.getNome());
        values.put(COLUNA_TELEFONE, cliente.getTelefone());
        values.put(COLUNA_EMAIL, cliente.getEmail());

        db.update(TABELA_CLIENTE, values, COLUNA_CODIGO + " = ? ", new String[] {cliente.getCodigo()});

        db.close();

    }

    public List<Cliente> listaTodosClientes() {

        List<Cliente> listaClientes = new ArrayList<Cliente>();

        String query = "SELECT * FROM " + TABELA_CLIENTE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {

            do {

                Cliente cliente = new Cliente();
                cliente.setCodigo(c.getString(0));
                cliente.setNome(c.getString(1));
                cliente.setTelefone(c.getString(2));
                cliente.setEmail(c.getString(3));

                listaClientes.add(cliente);

            } while (c.moveToNext());

        }

        return listaClientes;

    }

}
