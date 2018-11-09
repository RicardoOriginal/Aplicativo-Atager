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
public class FrenteEditar extends AppCompatActivity{

    private TextInputEditText edtNome;
    private Spinner spnDivisao, spnEncarregado;
    private int idfrente, idDivisao, idEncarregado;

    protected void onCreate(Bundle savedInstanceState){
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
                editarFrente();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        preencherCampos();
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
                    final DivisaoSpinnerAdapter divisaoSpinnerAdapter = new DivisaoSpinnerAdapter(FrenteEditar.this, response.body());
                    spnDivisao = (Spinner) findViewById(R.id.spinnerDivisao);
                    spnDivisao.setAdapter(divisaoSpinnerAdapter);

                    Intent receberdados = getIntent();
                    Bundle bundle = receberdados.getExtras();
                    int posicao = 0;
                    for (Divisao divisao : response.body()) {
                        if (divisao.getDivisao_id() == bundle.getInt("divisao_id")) {
                            break;
                        }
                        posicao++;
                    }
                    spnDivisao.setSelection(posicao);
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
                    Toast.makeText(FrenteEditar.this, "Erro ao carregar divisões", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Divisao>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(FrenteEditar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void carregarSpinnerEncarregados(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de encarregados, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<List<Colaborador>> call = colaboradorService.mostrarTodos();
        call.enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful()) {
                    final ColaboradorSpinnerAdapter colaboradorSpinnerAdapter = new ColaboradorSpinnerAdapter(FrenteEditar.this, response.body());
                    spnEncarregado = (Spinner) findViewById(R.id.spinnerEncarregado);
                    spnEncarregado.setAdapter(colaboradorSpinnerAdapter);

                    Intent receberdados = getIntent();
                    Bundle bundle = receberdados.getExtras();
                    int posicao = 0;
                    for (Colaborador colaborador : response.body()) {
                        if (colaborador.getColaborador_id() == bundle.getInt("encarregado_id")) {
                            break;
                        }
                        posicao++;
                    }
                    spnEncarregado.setSelection(posicao);
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
                    Toast.makeText(FrenteEditar.this, "Erro ao carregar lista de colaboradores", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(FrenteEditar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void preencherCampos() {
        Intent receberPacote = getIntent();
        Bundle bundle = receberPacote.getExtras();
        idfrente = bundle.getInt("id");
        edtNome = (TextInputEditText) findViewById(R.id.edtFrenteNome);
        edtNome.setText(bundle.getString("nome"));
    }

    private Frente atualizarFrente(){
        Frente frente = new Frente();
        frente.setFrente_id(idfrente);
        frente.setFrente_nome(edtNome.getText().toString());
        frente.setDivisao_id(idDivisao);
        frente.setColaborador_id(idEncarregado);
        return frente;
    }

    private void editarFrente(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Fazendo auterações solicitadas, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        FrenteService frenteService = publicas.retrofit().create(FrenteService.class);
        Call<Boolean> call = frenteService.atualizar(atualizarFrente());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()){
                    dialog.dismiss();
                    finish();
                    Toast.makeText(FrenteEditar.this, atualizarFrente().getFrente_nome() + "atualizado com sucesso!", Toast.LENGTH_LONG).show();
                }else {
                    dialog.dismiss();
                    Toast.makeText(FrenteEditar.this, "Informação inconsistente, por favor verifique!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(FrenteEditar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, FrenteLista.class));
    }
}
