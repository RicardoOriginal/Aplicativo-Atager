package ricardo.com.atager.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ricardo.com.atager.modelo.Colaborador;

/**
 * Created by Ricardo on 19/09/2016.
 */

public interface ColaboradorService {

    @GET("colaborador/mostrarTodos")
    Call<List<Colaborador>> mostrarTodos();

    @GET("colaborador/mostrarTodosDesc")
    Call<List<Colaborador>> mostrarTodosDesc();

    @GET("colaborador/mostrarNomes")
    Call<List<Colaborador>> mostrarNomes();

    @POST("colaborador/inserir")
    Call<Boolean> inserir( @Body Colaborador colaborador);

    @POST("colaborador/atualizar")
    Call<Boolean> atualizar( @Body Colaborador colaborador);

    @GET("colaborador/deletar/{id}")
    Call<Boolean> deletar( @Path("id") int id);

    @GET("colaborador/autenticar/{matricula}/{senha}")
    Call<Colaborador> autenticar(@Path("matricula") int matricula, @Path("senha") String senha);
}
