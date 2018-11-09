package ricardo.com.atager.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.adapters.ColaboradorSpinnerAdapter;
import ricardo.com.atager.adapters.DivisaoSpinnerAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Divisao;
import ricardo.com.atager.modelo.Frente;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.DivisaoService;
import ricardo.com.atager.service.FrenteService;
import ricardo.com.atager.service.Publicas;

/**
 * Created by Ricardo on 12/02/2017.
 */

public class FrenteAdicionar extends AppCompatActivity{

    private TextInputEditText edtNome;
    private int idDivisao, idEncarregado;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frente);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                inserirFrente();
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        carregarSpinnerDivisoes();
        carregarSpinnerEncarregados();
    }

    private void carregarSpinnerDivisoes() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de divisões, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        DivisaoService divisaoService = publicas.retrofit().create(DivisaoService.class);
        Call<List<Divisao>> call = divisaoService.mostrarTodos();
        call.enqueue(new Callback<List<Divisao>>() {
            @Override
            public void onResponse(Call<List<Divisao>> call, Response<List<Divisao>> response) {
                if (response.isSuccessful()) {
                    final DivisaoSpinnerAdapter divisaoSpinnerAdapter = new DivisaoSpinnerAdapter(FrenteAdicionar.this, response.body());
                    Spinner spnDivisao = (Spinner) findViewById(R.id.spinnerDivisao);
                    spnDivisao.setAdapter(divisaoSpinnerAdapter);
                    dialog.dismiss();
                    spnDivisao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Divisao divisao = divisaoSpinnerAdapter.getItem(position);
                            idDivisao = divisao.getDivisao_id();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapter) {
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(FrenteAdicionar.this, "Erro no servidor", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Divisao>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(FrenteAdicionar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarSpinnerEncarregados(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de encarregados, aguarde...");
        Publicas publicas = new Publicas();
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<List<Colaborador>> call = colaboradorService.mostrarTodos();
        call.enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful()) {
                    final ColaboradorSpinnerAdapter colaboradorSpinnerAdapter = new ColaboradorSpinnerAdapter(FrenteAdicionar.this, response.body());
                    Spinner spnEncarregado = (Spinner) findViewById(R.id.spinnerEncarregado);
                    spnEncarregado.setAdapter(colaboradorSpinnerAdapter);
                    dialog.dismiss();
                    spnEncarregado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Colaborador colaborador = colaboradorSpinnerAdapter.getItem(position);
                            idEncarregado = colaborador.getColaborador_id();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapter) {
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(FrenteAdicionar.this, "Erro ao carregar encarregados", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(FrenteAdicionar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Frente novaFrente(){
        Frente frente = new Frente();
        edtNome = (TextInputEditText) findViewById(R.id.edtFrenteNome);
        frente.setFrente_nome(edtNome.getText().toString());
        frente.setDivisao_id(idDivisao);
        frente.setColaborador_id(idEncarregado);
        return frente;
    }

    private void inserirFrente(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Inserindo frente, aguarde...");
        Publicas publicas = new Publicas();
        final FrenteService frenteService = publicas.retrofit().create(FrenteService.class);
        Call<Boolean> call = frenteService.inserir(novaFrente());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()){
                    dialog.dismiss();
                    Toast.makeText(FrenteAdicionar.this, "Frente "+novaFrente().getFrente_nome()+" adicionada com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(FrenteAdicionar.this, FrenteLista.class));
                }else {
                    dialog.dismiss();
                    Log.d("ERRO", response.message());
                    Toast.makeText(FrenteAdicionar.this,"Informações inconsistentes, por favor verifique!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(FrenteAdicionar.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, FrenteLista.class));
    }
}
