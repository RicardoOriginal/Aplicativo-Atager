package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Unidade;

/**
 * Created by Ricardo on 17/02/2017.
 */

public interface UnidadeService {

    @GET("unidade/mostrarTodos")
    Call<List<Unidade>> mostrarTodos();

    @POST("unidade/inserir")
    Call<Boolean> inserir(@Body Unidade unidade);

    @POST("unidade/atualizar")
    Call<Boolean> atualizar(@Body Unidade unidade);

    @GET("unidade/deletar/{id}")
    Call<Boolean> deletar(@Path("id")int id);

}
