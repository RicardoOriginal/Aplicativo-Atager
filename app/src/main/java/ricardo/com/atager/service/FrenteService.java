package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Frente;

/**
 * Created by Ricardo on 19/09/2016.
 */

public interface FrenteService {

    @GET("frente/mostrarTodos")
    Call<List<Frente>> mostrarTodos();

    @POST("frente/inserir")
    Call<Boolean> inserir(@Body Frente frente);

    @POST("frente/atualizar")
    Call<Boolean> atualizar(@Body Frente frente);

    @GET("frente/deletar/{id}")
    Call<Boolean> deletar(@Path("id") int id);

}
