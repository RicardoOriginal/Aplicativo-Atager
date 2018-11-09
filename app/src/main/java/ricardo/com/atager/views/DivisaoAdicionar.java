package ricardo.com.atager.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
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
import ricardo.com.atager.adapters.UnidadeSpinnerAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Divisao;
import ricardo.com.atager.modelo.Unidade;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.DivisaoService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.UnidadeService;

/**
 * Created by Ricardo on 13/02/2017.
 */

public class DivisaoAdicionar extends AppCompatActivity{

    private TextInputEditText edtNome;
    private int unidadeId, supervisorId;
    private Spinner spnUnidade, spnSupervisor;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divisao);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                adicionarDivisao();
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        carregarSpinnerUnidade();
        carregarSpinnerSupervisor();
    }

    private Divisao novaDivisao(){
        Divisao divisao = new Divisao();
        edtNome = (TextInputEditText) findViewById(R.id.edtDivisaoNome);
        divisao.setDivisao_nome(edtNome.getText().toString());
        divisao.setUnidade_id(unidadeId);
        divisao.setColaborador_id(supervisorId);
        return divisao;
    }

    private void carregarSpinnerUnidade(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de unidades, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        UnidadeService unidadeService = publicas.retrofit().create(UnidadeService.class);
        Call<List<Unidade>> call = unidadeService.mostrarTodos();
        call.enqueue(new Callback<List<Unidade>>() {
            @Override
            public void onResponse(Call<List<Unidade>> call, Response<List<Unidade>> response) {
                if (response.isSuccessful()){
                    spnUnidade = (Spinner) findViewById(R.id.spinnerUnidade);
                    final UnidadeSpinnerAdapter unidadeSpinnerAdapter = new UnidadeSpinnerAdapter(DivisaoAdicionar.this, response.body());
                    spnUnidade.setAdapter(unidadeSpinnerAdapter);
                    dialog.dismiss();
                    spnUnidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Unidade unidade = unidadeSpinnerAdapter.getItem(position);
                            unidadeId = unidade.getUnidade_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            dialog.dismiss();
                            Toast.makeText(DivisaoAdicionar.this, "ainda nao selecionou nada", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Unidade>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DivisaoAdicionar.this, "Erro ao carregar divisões", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarSpinnerSupervisor(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de supervisores, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        final ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<List<Colaborador>> call = colaboradorService.mostrarTodos();
        call.enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful()){
                    spnSupervisor = (Spinner) findViewById(R.id.spinnerSupervisor);
                    final ColaboradorSpinnerAdapter colaboradorSpinnerAdapter = new ColaboradorSpinnerAdapter(DivisaoAdicionar.this, response.body());
                    spnSupervisor.setAdapter(colaboradorSpinnerAdapter);
                    dialog.dismiss();
                    spnSupervisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Colaborador colaborador = colaboradorSpinnerAdapter.getItem(position);
                            supervisorId = colaborador.getColaborador_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            dialog.dismiss();
                            Toast.makeText(DivisaoAdicionar.this, "Erro no carregamento de supervisores", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DivisaoAdicionar.this, "Erro no servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void adicionarDivisao() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Inserindo nova divisao, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        DivisaoService divisaoService = publicas.retrofit().create(DivisaoService.class);
        Call<Boolean> call = divisaoService.inserir(novaDivisao());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()){
                    dialog.dismiss();
                    Toast.makeText(DivisaoAdicionar.this, "Divisão "+novaDivisao().getDivisao_nome()+" adicionada com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(DivisaoAdicionar.this, DivisaoLista.class));
                }else {
                    dialog.dismiss();
                    Toast.makeText(DivisaoAdicionar.this, "Informações inconsistentes, por favor verifique!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DivisaoAdicionar.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, DivisaoLista.class));
    }
}
