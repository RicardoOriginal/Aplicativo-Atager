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
import ricardo.com.atager.adapters.MaquinaAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Maquina;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.MaquinaService;
import ricardo.com.atager.service.Publicas;

/**
 * Created by Ricardo on 09/10/2016.
 */

public class MaquinaLista extends AppCompatActivity{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reliclador);
    }

    @Override
    protected void onResume(){
        super.onResume();
        iniciarVisoes();
        carregarMaquinas();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.maquina, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mAdicionar:
                startActivity(new Intent(this, MaquinaAdicionar.class));
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
            case R.id.mColaboradores:
                startActivity(new Intent(this, ColaboradorLista.class));
                finish();
                break;
        }
        return true;
    }

    private void iniciarVisoes() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregarMaquinas();
            }
        });
    }

    private void carregarMaquinas() {
        swipeRefreshLayout.setRefreshing(true);
        Publicas publicas = new Publicas();
        MaquinaService maquinaService = publicas.retrofit().create(MaquinaService.class);
        Call<List<Maquina>> call = maquinaService.mostrarTodos();
        call.enqueue(new Callback<List<Maquina>>() {
            @Override
            public void onResponse(Call<List<Maquina>> call, Response<List<Maquina>> response) {
                if (response.isSuccessful()) {
                    MaquinaAdapter maquinaAdapter = new MaquinaAdapter(MaquinaLista.this, response.body());
                    recyclerView = (RecyclerView) findViewById(R.id.recicladorCartao);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(maquinaAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    Toast.makeText(MaquinaLista.this, "Erro no servidor", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Maquina>> call, Throwable t) {
                Toast.makeText(MaquinaLista.this, "Servidor desligado", Toast.LENGTH_LONG).show();
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
