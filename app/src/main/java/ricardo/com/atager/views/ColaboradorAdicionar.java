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
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.adapters.NivelSpinnerAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Nivel;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.NivelService;
import ricardo.com.atager.service.Publicas;

/**
 * Created by Ricardo on 10/10/2016.
 */
public class ColaboradorAdicionar extends AppCompatActivity{

    private TextInputEditText matricula, nome, senha;
    private Spinner spnCargo;
    private int cargoId;
    private Switch ativo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colaborador);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                adicinarColaborador();
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        carregarSpinnerCargos();
    }

    public void carregarSpinnerCargos(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de cargos, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        NivelService nivelService = publicas.retrofit().create(NivelService.class);
        Call<List<Nivel>> listCall = nivelService.mostrarTodos();
        listCall.enqueue(new Callback<List<Nivel>>() {
            @Override
            public void onResponse(Call<List<Nivel>> call, Response<List<Nivel>> response) {
                if (response.isSuccessful()){
                    final NivelSpinnerAdapter nivelSpinnerAdapter = new NivelSpinnerAdapter(ColaboradorAdicionar.this, response.body());
                    spnCargo = (Spinner) findViewById(R.id.spinnerCargo);
                    spnCargo.setAdapter(nivelSpinnerAdapter);
                    dialog.dismiss();
                    spnCargo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Nivel nivel = nivelSpinnerAdapter.getItem(position);
                            cargoId = nivel.getNivel_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            dialog.dismiss();
                            Toast.makeText(ColaboradorAdicionar.this, "ainda nao selecionou nada", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<Nivel>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ColaboradorAdicionar.this, "Erro ao carregar cargos", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Colaborador criarColaborador() {
        matricula = (TextInputEditText) findViewById(R.id.edtMatricula);
        nome = (TextInputEditText) findViewById(R.id.edtNome);
        senha = (TextInputEditText) findViewById(R.id.edtSenha);
        ativo = (Switch) findViewById(R.id.swAtivo);

        Colaborador colaborador = new Colaborador();
        colaborador.setMatricula(Integer.parseInt(matricula.getText().toString()));
        colaborador.setColaborador_nome(nome.getText().toString());
        colaborador.setSenha(senha.getText().toString());
        colaborador.setAtivo(ativo.isChecked());
        colaborador.setNivel_id(cargoId);
        return colaborador;
    }

    public void adicinarColaborador(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Casdastrando colaborador, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<Boolean> call = colaboradorService.inserir(criarColaborador());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Toast.makeText(ColaboradorAdicionar.this, criarColaborador().getColaborador_nome() + " inserido com sucesso!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
                startActivity(new Intent(ColaboradorAdicionar.this, ColaboradorLista.class));
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ColaboradorAdicionar.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, ColaboradorLista.class));
    }
}