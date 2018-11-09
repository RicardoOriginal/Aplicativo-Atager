package ricardo.com.atager.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.adapters.TurnoAdapter;
import ricardo.com.atager.modelo.Turno;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.TurnoService;

import static ricardo.com.atager.service.Publicas.TAG;

/**
 * Created by Ricardo on 09/10/2016.
 */

public class TurnoLista extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reliclador);
    }

    public void onResume(){
        super.onResume();
        initViews();
        carregarTurnos();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.turno, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mAdicionar:
                startActivity(new Intent(this, TurnoAdicionar.class));
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
                carregarTurnos();
            }
        });
    }

    private void carregarTurnos() {
        swipeRefreshLayout.setRefreshing(true);
        Publicas publicas = new Publicas();
        TurnoService turnoService = publicas.retrofit().create(TurnoService.class);
        Call<List<Turno>> call = turnoService.mostrarTodos();
        call.enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(Call<List<Turno>> call, Response<List<Turno>> response) {
                if (response.isSuccessful()) {

                    recyclerView = (RecyclerView) findViewById(R.id.recicladorCartao);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);

                    TurnoAdapter turnoAdapter = new TurnoAdapter(TurnoLista.this, response.body());
                    recyclerView.setAdapter(turnoAdapter);
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    Toast.makeText(TurnoLista.this, "Erro no servidor", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Turno>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                Log.d(TAG, call.toString());
                Toast.makeText(TurnoLista.this, "Servidor desligado", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
