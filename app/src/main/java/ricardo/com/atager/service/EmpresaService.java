package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Empresa;

/**
 * Created by Ricardo on 17/02/2017.
 */

public interface EmpresaService {

    @GET("empresa/mostrarTodos")
    Call<List<Empresa>> mostrarTodos();

    @POST("empresa/inserir")
    Call<Boolean> inserir(@Body Empresa empresa);

    @POST("empresa/atualizar")
    Call<Boolean> atualizar(@Body Empresa empresa);

    @GET("empresa/deletar/{id}")
    Call<Boolean> deletar(@Path("id") int id);

}
