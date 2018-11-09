package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Turno;

/**
 * Created by Ricardo on 19/09/2016.
 */

public interface TurnoService {

    @GET("turno/mostrarTodos")
    Call<List<Turno>> mostrarTodos();

    @POST("turno/inserir")
    Call<Boolean> inserir(@Body Turno turno);

    @POST("turno/atualizar")
    Call<Boolean> atualizar(@Body Turno turno);

    @GET("turno/deletar/{id}")
    Call<Boolean> deletar(@Path("id")int id);

}
