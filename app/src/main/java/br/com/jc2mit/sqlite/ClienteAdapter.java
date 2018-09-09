package br.com.jc2mit.sqlite;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.jc2mit.sqlite.model.Cliente;

public class ClienteAdapter extends ArrayAdapter<Cliente> {

    private Context context;
    private List<Cliente> clientes;

    public ClienteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Cliente> objects) {
        super(context, resource, objects);
        this.context = context;
        this.clientes = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_clientes, parent, false);

        TextView txtCliCodigo = (TextView) rowView.findViewById(R.id.txtCliCodigo);
        TextView txtCliNome = (TextView) rowView.findViewById(R.id.txtCliNome);
        TextView txtCliTelefone = (TextView) rowView.findViewById(R.id.txtCliTelefone);
        TextView txtCliEmail = (TextView) rowView.findViewById(R.id.txtCliEmail);

        txtCliCodigo.setText(String.format("cli_cod: %s", clientes.get(position).getCodigo()));
        txtCliNome.setText(String.format("cli_nm: %s", clientes.get(position).getNome()));
        txtCliTelefone.setText(String.format("cli_tel: %s", clientes.get(position).getTelefone()));
        txtCliEmail.setText(String.format("cli_email: %s", clientes.get(position).getEmail()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ClienteActivity.class);
                intent.putExtra("cli_cod", clientes.get(position).getCodigo());
                intent.putExtra("cli_nm", clientes.get(position).getNome());
                intent.putExtra("cli_tel", clientes.get(position).getTelefone());
                intent.putExtra("cli_email", clientes.get(position).getEmail());
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}
