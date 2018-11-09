package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.TipoMaquina;

/**
 * Created by Ricardo on 17/02/2017.
 */

public interface TipoMaquinaService {

    @GET("tipoMaquina/mostrarTodos")
    Call<List<TipoMaquina>> mostrarTodos();

    @POST("tipoMaquina/inserir")
    Call<Boolean> inserir(@Body TipoMaquina tipoMaquina);

    @POST("nitipoMaquinavel/atualizar")
    Call<Boolean> atualizar(@Body TipoMaquina tipoMaquina);

    @GET("tipoMaquina/deletar/{id}")
    Call<Boolean> deletar(@Path("id") int id);

}
