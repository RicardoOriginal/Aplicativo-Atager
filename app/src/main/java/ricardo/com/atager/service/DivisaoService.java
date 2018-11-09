package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Divisao;

/**
 * Created by Ricardo on 13/02/2017.
 */

public interface DivisaoService {

    @GET("divisao/mostrarTodos")
    Call<List<Divisao>> mostrarTodos();

    @POST("divisao/inserir")
    Call<Boolean> inserir(@Body Divisao divisao);

    @GET("divisao/deletar/{id}")
    Call<Boolean> deletar(@Path("id")int id);

    @POST("divisao/atualizar")
    Call<Boolean> atualizar(@Body Divisao divisao);
}
