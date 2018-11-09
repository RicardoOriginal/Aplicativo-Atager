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
import ricardo.com.atager.adapters.FrenteAdapter;
import ricardo.com.atager.modelo.Frente;
import ricardo.com.atager.service.FrenteService;
import ricardo.com.atager.service.Publicas;

/**
 * Created by Ricardo on 12/02/2017.
 */

public class FrenteLista extends AppCompatActivity{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FrenteAdapter frenteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reliclador);
        initViews();
    }

    public void onResume(){
        carregarFrentes();
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.frente, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mAdicionar:
                startActivity(new Intent(this, FrenteAdicionar.class));
                finish();
                break;
            case R.id.mTurnos:
                startActivity(new Intent(this, TurnoLista.class));
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

    public void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregarFrentes();
            }
        });
    }

    private void carregarFrentes(){
        swipeRefreshLayout.setRefreshing(true);
        Publicas publicas = new Publicas();
        FrenteService frenteService = publicas.retrofit().create(FrenteService.class);
        Call<List<Frente>> call = frenteService.mostrarTodos();
        call.enqueue(new Callback<List<Frente>>() {
            @Override
            public void onResponse(Call<List<Frente>> call, Response<List<Frente>> response) {
                if (response.isSuccessful()){

                    recyclerView = (RecyclerView) findViewById(R.id.recicladorCartao);
                    recyclerView.setAdapter(frenteAdapter);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);

                    frenteAdapter = new FrenteAdapter(FrenteLista.this, response.body());
                    recyclerView.setAdapter(frenteAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                }else {
                    Toast.makeText(FrenteLista.this, "Erro ao listar as frentes", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Frente>> call, Throwable t) {
                Toast.makeText(FrenteLista.this, "Servidor desligado", Toast.LENGTH_LONG).show();
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
