package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Maquina;

/**
 * Created by Ricardo on 19/09/2016.
 */

public interface MaquinaService {

    @GET("maquina/mostrarTodos")
    Call<List<Maquina>> mostrarTodos();

    @POST("maquina/inserir")
    Call<Boolean> inserir(@Body Maquina maquina);

    @POST("maquina/atualizar")
    Call<Boolean> atualizar(@Body Maquina maquina);

    @GET("maquina/deletar/{id}")
    Call<Boolean> deletar(@Path("id") int id);

}
