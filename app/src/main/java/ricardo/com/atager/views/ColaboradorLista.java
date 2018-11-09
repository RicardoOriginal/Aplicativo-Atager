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
import ricardo.com.atager.adapters.ColaboradorAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.Publicas;

/**
 * Created by Ricardo on 09/10/2016.
 */

public class ColaboradorLista extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ColaboradorAdapter colaboradorAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Publicas publicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reliclador);
        initViews();
    }

    @Override
    protected void onResume(){
        super.onResume();
        carregarColaboradores();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.colaborador, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mAdicionar:
                startActivity(new Intent(this, ColaboradorAdicionar.class));
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
            case R.id.mUnidades:
                startActivity(new Intent(this, UnidadeLista.class));
                finish();
                break;
            case R.id.mEmpresas:
                startActivity(new Intent(this, EmpresaLista.class));
                finish();
                break;
            case R.id.mMaquinas:
                startActivity(new Intent(this, MaquinaLista.class));
                finish();
                break;
        }
        return true;
    }

    private void initViews() {
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
                carregarColaboradores();
            }
        });
    }

    private void carregarColaboradores() {
        swipeRefreshLayout.setRefreshing(true);
        publicas = new Publicas();
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<List<Colaborador>> call = colaboradorService.mostrarTodos();
        call.enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful()) {
                    colaboradorAdapter = new ColaboradorAdapter(ColaboradorLista.this, response.body());
                    recyclerView.setAdapter(colaboradorAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    Toast.makeText(ColaboradorLista.this, "Erro no servidor", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                Toast.makeText(ColaboradorLista.this, "Servidor desligado", Toast.LENGTH_LONG).show();
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
