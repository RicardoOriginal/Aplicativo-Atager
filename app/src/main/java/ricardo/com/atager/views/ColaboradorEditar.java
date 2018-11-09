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

import static ricardo.com.atager.service.Publicas.ATIVO;
import static ricardo.com.atager.service.Publicas.ID;
import static ricardo.com.atager.service.Publicas.MATRICULA;
import static ricardo.com.atager.service.Publicas.NIVEL_ID;
import static ricardo.com.atager.service.Publicas.NOME;
import static ricardo.com.atager.service.Publicas.SENHA;
import static ricardo.com.atager.service.Publicas.TAG;


/**
 * Created by Ricardo on 10/10/2016.
 */
public class ColaboradorEditar extends AppCompatActivity{

    private int id, cargoId;
    private TextInputEditText edtMatricula, edtNome, edtSenha;
    private Spinner spnCargo;
    private Switch ativo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colaborador);
        preencherCampos();
    }

    @Override
    public void onResume(){
        super.onResume();
        carregarSpinnerCargo();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                editarColaborador();
        }
        return true;
    }

    public void preencherCampos() {
        Intent receberDados = getIntent();
        Bundle bundle = receberDados.getExtras();

        edtMatricula = (TextInputEditText) findViewById(R.id.edtMatricula);
        edtNome = (TextInputEditText) findViewById(R.id.edtNome);
        edtSenha = (TextInputEditText) findViewById(R.id.edtSenha);
        ativo = (Switch) findViewById(R.id.swAtivo);

        id = bundle.getInt(ID);
        edtMatricula.setText(String.valueOf(bundle.getInt(MATRICULA)));
        edtNome.setText(bundle.getString(NOME));
        edtSenha.setText(bundle.getString(SENHA));
        ativo.setChecked(bundle.getBoolean(ATIVO));
    }

    public void carregarSpinnerCargo(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de cargos, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        NivelService nivelService = publicas.retrofit().create(NivelService.class);
        Call<List<Nivel>> call = nivelService.mostrarTodos();
        call.enqueue(new Callback<List<Nivel>>() {
            @Override
            public void onResponse(Call<List<Nivel>> call, Response<List<Nivel>> response) {
                if (response.isSuccessful()) {
                    final NivelSpinnerAdapter nivelSpinnerAdapter = new NivelSpinnerAdapter(ColaboradorEditar.this, response.body());
                    spnCargo = (Spinner) findViewById(R.id.spinnerCargo);
                    spnCargo.setAdapter(nivelSpinnerAdapter);
                    Intent receberdados = getIntent();
                    Bundle bundle = receberdados.getExtras();
                    int posicao = 0;
                    for ( Nivel nivel : response.body()){
                        if(nivel.getNivel_id() == bundle.getInt(NIVEL_ID)){
                            break;
                        }
                        posicao++;
                    }
                    spnCargo.setSelection(posicao);
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
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Nivel>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public Colaborador atualizarColaborador() {
        Colaborador colaborador = new Colaborador();
        colaborador.setColaborador_id(id);
        colaborador.setMatricula(Integer.parseInt(edtMatricula.getText().toString()));
        colaborador.setColaborador_nome(edtNome.getText().toString());
        colaborador.setSenha(edtSenha.getText().toString());
        colaborador.setAtivo(ativo.isChecked());
        colaborador.setNivel_id(cargoId);
        return colaborador;
    }

    public void editarColaborador() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Fazendo auterações solicitadas, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        final ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<Boolean> call = colaboradorService.atualizar(atualizarColaborador());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.body()) {
                    dialog.dismiss();
                    finish();
                    Toast.makeText(ColaboradorEditar.this, atualizarColaborador().getColaborador_nome() + " Atualizado com sucesso!", Toast.LENGTH_LONG).show();

                } else {
                    dialog.dismiss();
                    Toast.makeText(ColaboradorEditar.this, "Informação inconsistente, por favor verifique!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, "ERRO AO SALVAR" + t.getMessage());
                Toast.makeText(ColaboradorEditar.this,"Servidor desligado", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ColaboradorLista.class));
    }
}