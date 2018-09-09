package br.com.jc2mit.sqlite.remote;

import java.util.List;

import br.com.jc2mit.sqlite.model.Cliente;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClienteService {

    @GET("clientes/")
    Call<List<Cliente>> getClientes();

    @POST("add/")
    Call<Cliente> addCliente(@Body Cliente cliente);

    @PUT("update/{codigo}")
    Call<Cliente> updateCliente(@Path("codigo") String codigo, @Body Cliente cliente);

    @DELETE("delete/{codigo}")
    Call<Cliente> deleteCliente(@Path("codigo") String codigo);

}
