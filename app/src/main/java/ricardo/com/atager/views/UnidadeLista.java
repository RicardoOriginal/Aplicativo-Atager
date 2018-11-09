package ricardo.com.atager.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.adapters.UnidadeAdapter;
import ricardo.com.atager.modelo.Unidade;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.UnidadeService;

/**
 * Created by Ricardo on 19/02/2017.
 */

public class UnidadeLista extends AppCompatActivity{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UnidadeAdapter unidadeAdapter;
    private Publicas publicas;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reliclador);
        iniciarVisoes();
    }

    public void onResume(){
        carregarUnidades();
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.unidade, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mAdicionar:
                startActivity(new Intent(this, UnidadeAdicionar.class));
                finish();
                break;
            case R.id.mTurnos:
                startActivity(new Intent(this, TurnoLista.class));
                finish();
                break;
            case R.id.mFrentes:
                startActivity(new Intent(this, FrenteLista.class));
                finish();
                break;
            case R.id.mDivisoes:
                startActivity(new Intent(this, DivisaoLista.class));
                finish();
                break;
            case R.id.mEmpresas:
                startActivity(new Intent(this, EmpresaLista.class));
                finish();
                break;
            case R.id.mColaboradores:
                startActivity(new Intent(this, ColaboradorLista.class));
                finish();
                break;
            case R.id.mMaquinas:
                startActivity(new Intent(this, MaquinaLista.class));
                finish();
                break;
        }
        return true;
    }

    private void iniciarVisoes() {
        recyclerView = (RecyclerView) findViewById(R.id.recicladorCartao);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregarUnidades();
            }
        });
    }

    private void carregarUnidades() {
        swipeRefreshLayout.setRefreshing(true);
        publicas = new Publicas();
        UnidadeService unidadeService = publicas.retrofit().create(UnidadeService.class);
        Call<List<Unidade>> call = unidadeService.mostrarTodos();
        call.enqueue(new Callback<List<Unidade>>() {
            @Override
            public void onResponse(Call<List<Unidade>> call, Response<List<Unidade>> response) {
                if (response.isSuccessful()){
                    unidadeAdapter = new UnidadeAdapter(response.body(), UnidadeLista.this);
                    recyclerView.setAdapter(unidadeAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                }else {
                    Toast.makeText(UnidadeLista.this, "Erro ao listar unidades", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Unidade>> call, Throwable t) {
                Toast.makeText(UnidadeLista.this, "Servidor desligado", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, TurnoLista.class));
    }
}
