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
import ricardo.com.atager.adapters.EmpresaSpinnerAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Empresa;
import ricardo.com.atager.modelo.Unidade;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.EmpresaService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.UnidadeService;

/**
 * Created by Ricardo on 19/02/2017.
 */
public class UnidadeEditar extends AppCompatActivity{

    private int idUnidade, idEmpresa, idGerente;
    private TextInputEditText edtNome;
    private Spinner spnEmpresa, spnGerente;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.unidade);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                editarUnidade();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        preencherCampos();
        carregarSpinnerEmpresas();
        carregarSpinnerGerente();
    }

    private void carregarSpinnerEmpresas() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de empresas, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        final EmpresaService empresaService = publicas.retrofit().create(EmpresaService.class);
        Call<List<Empresa>> call = empresaService.mostrarTodos();
        call.enqueue(new Callback<List<Empresa>>() {
            @Override
            public void onResponse(Call<List<Empresa>> call, Response<List<Empresa>> response) {
                if (response.isSuccessful()) {
                    spnEmpresa = (Spinner) findViewById(R.id.spinnerEmpresa);
                    final EmpresaSpinnerAdapter empresaSpinnerAdapter = new EmpresaSpinnerAdapter(UnidadeEditar.this, response.body());
                    spnEmpresa.setAdapter(empresaSpinnerAdapter);

                    Intent receberdados = getIntent();
                    Bundle bundle = receberdados.getExtras();
                    int posicao = 0;
                    for (Empresa empresa : response.body()) {
                        if (empresa.getEmpresa_id() == bundle.getInt("empresa_id")) {
                            break;
                        }
                        posicao++;
                    }
                    spnEmpresa.setSelection(posicao);
                    dialog.dismiss();
                    spnEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Empresa empresa = empresaSpinnerAdapter.getItem(position);
                            idEmpresa = empresa.getEmpresa_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Toast.makeText(UnidadeEditar.this, "ainda nao selecionou nada", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    dialog.dismiss();
                    Toast.makeText(UnidadeEditar.this, "Erro ao carregar empresas", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Empresa>> call, Throwable t) {
                Toast.makeText(UnidadeEditar.this, "Erro no servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarSpinnerGerente() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de gerentes, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<List<Colaborador>> call = colaboradorService.mostrarTodos();
        call.enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful()) {
                    final ColaboradorSpinnerAdapter colaboradorSpinnerAdapter = new ColaboradorSpinnerAdapter(UnidadeEditar.this, response.body());
                    spnGerente = (Spinner) findViewById(R.id.spinnerGerente);
                    spnGerente.setAdapter(colaboradorSpinnerAdapter);

                    Intent receberdados = getIntent();
                    Bundle bundle = receberdados.getExtras();
                    int posicao = 0;
                    for (Colaborador colaborador : response.body()) {
                        if (colaborador.getColaborador_id() == bundle.getInt("gerente_id")) {
                            break;
                        }
                        posicao++;
                    }
                    spnGerente.setSelection(posicao);
                    dialog.dismiss();
                    spnGerente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Colaborador gerente = colaboradorSpinnerAdapter.getItem(position);
                            idGerente = gerente.getColaborador_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.d("NADA FOI SELECIONADO", String.valueOf(parent.getFirstVisiblePosition()));
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(UnidadeEditar.this, "Erro ao carregar gerentes", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                Toast.makeText(UnidadeEditar.this, "Erro no servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void preencherCampos(){
        Intent receberDados = getIntent();
        Bundle bundle = receberDados.getExtras();
        idUnidade = bundle.getInt("id");
        edtNome = (TextInputEditText) findViewById(R.id.edtNome);
        edtNome.setText(bundle.getString("nome"));
    }

    private Unidade atualizarUnidade(){
        Unidade unidade = new Unidade();
        unidade.setUnidade_id(idUnidade);
        unidade.setUnidade_nome(edtNome.getText().toString());
        unidade.setEmpresa_id(idEmpresa);
        unidade.setColaborador_id(idGerente);
        return unidade;
    }

    private void editarUnidade() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Fazendo atualização solicitadas, aguarde");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        UnidadeService unidadeService = publicas.retrofit().create(UnidadeService.class);
        Call<Boolean> call = unidadeService.atualizar(atualizarUnidade());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()){
                    dialog.dismiss();
                    finish();
                    Toast.makeText(UnidadeEditar.this, "Unidade "+atualizarUnidade().getUnidade_nome()+" atualizada com sucesso", Toast.LENGTH_LONG).show();
                }else {
                    dialog.dismiss();
                    Toast.makeText(UnidadeEditar.this, "Informações inconsistentes, por favor verifique", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UnidadeEditar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, UnidadeLista.class));
    }
}
