package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Empresa;
import ricardo.com.atager.modelo.Nivel;

/**
 * Created by Ricardo on 17/02/2017.
 */

public interface NivelService {

    @GET("nivel/mostrarTodos")
    Call<List<Nivel>> mostrarTodos();

    @POST("nivel/inserir")
    Call<Boolean> inserir(@Body Nivel nivel);

    @POST("nivel/atualizar")
    Call<Boolean> atualizar(@Body Nivel nivel);

    @GET("nivel/deletar/{id}")
    Call<Boolean> deletar(@Path("id") int id);

}
