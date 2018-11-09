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
import ricardo.com.atager.adapters.TipoMaquinaSpinnerAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Maquina;
import ricardo.com.atager.modelo.Nivel;
import ricardo.com.atager.modelo.TipoMaquina;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.MaquinaService;
import ricardo.com.atager.service.NivelService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.TipoMaquinaService;

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
public class MaquinaEditar extends AppCompatActivity{

    private int maquinaId, tipoMaquinaId;
    private TextInputEditText edtFrota, edtModelo, edtHorasTrabalhadas;
    private Switch ativo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maquina);
    }

    @Override
    public void onResume(){
        super.onResume();
        preencherCampos();
        carregarSpinnerTipoMaquina();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                editarMaquina();
        }
        return true;
    }

    public void carregarSpinnerTipoMaquina(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de tipos, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        TipoMaquinaService tipoMaquinaService = publicas.retrofit().create(TipoMaquinaService.class);
        Call<List<TipoMaquina>> call = tipoMaquinaService.mostrarTodos();
        call.enqueue(new Callback<List<TipoMaquina>>() {
            @Override
            public void onResponse(Call<List<TipoMaquina>> call, Response<List<TipoMaquina>> response) {
                if (response.isSuccessful()) {
                    final TipoMaquinaSpinnerAdapter tipoMaquinaSpinnerAdapter = new TipoMaquinaSpinnerAdapter(MaquinaEditar.this, response.body());
                    Spinner spnTipoMaquina = (Spinner) findViewById(R.id.spinnerTipo);
                    spnTipoMaquina.setAdapter(tipoMaquinaSpinnerAdapter);
                    Intent receberdados = getIntent();
                    Bundle bundle = receberdados.getExtras();
                    int posicao = 0;
                    for ( TipoMaquina tipoMaquina : response.body()){
                        if(tipoMaquina.getTipoMaquina_id() == bundle.getInt("tipo_maquina_id")){
                            break;
                        }
                        posicao++;
                    }
                    spnTipoMaquina.setSelection(posicao);
                    dialog.dismiss();
                    spnTipoMaquina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TipoMaquina tipoMaquina = tipoMaquinaSpinnerAdapter.getItem(position);
                            tipoMaquinaId = tipoMaquina.getTipoMaquina_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<TipoMaquina>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    public void preencherCampos() {
        Intent receberDados = getIntent();
        Bundle bundle = receberDados.getExtras();

        edtFrota = (TextInputEditText) findViewById(R.id.edtFrota);
        edtModelo = (TextInputEditText) findViewById(R.id.edtModelo);
        edtHorasTrabalhadas = (TextInputEditText) findViewById(R.id.edtHorasTrabalhadas);
        ativo = (Switch) findViewById(R.id.swAtivo);

        maquinaId = bundle.getInt("maquinaId");
        edtFrota.setText(String.valueOf(bundle.getInt("frota")));
        edtModelo.setText(bundle.getString("modelo"));
        edtHorasTrabalhadas.setText(String.valueOf(bundle.getDouble("horas_trabalhadas")));
        ativo.setChecked(bundle.getBoolean(ATIVO));
    }

    public void editarMaquina() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Fazendo auterações solicitadas, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        final MaquinaService maquinaService = publicas.retrofit().create(MaquinaService.class);
        Call<Boolean> call = maquinaService.atualizar(atualizarMaquina());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.body()) {
                    dialog.dismiss();
                    finish();
                    Toast.makeText(MaquinaEditar.this, atualizarMaquina().getModelo() + " Atualizado com sucesso!", Toast.LENGTH_LONG).show();

                } else {
                    dialog.dismiss();
                    Toast.makeText(MaquinaEditar.this, "Informação inconsistente, por favor verifique!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Log.i(TAG, "ERRO AO SALVAR" + t.getMessage());
                Toast.makeText(MaquinaEditar.this,"Servidor desligado", Toast.LENGTH_LONG).show();

            }
        });
    }

    public Maquina atualizarMaquina() {
        Maquina maquina = new Maquina();
        maquina.setMaquina_id(maquinaId);
        maquina.setFrota(Integer.parseInt(edtFrota.getText().toString()));
        maquina.setTipoMaquina_id(tipoMaquinaId);
        maquina.setModelo(edtModelo.getText().toString());
        maquina.setHoras_trabalhadas(Double.valueOf(edtHorasTrabalhadas.getText().toString()));
        maquina.setAtivo(ativo.isChecked());
        return maquina;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MaquinaLista.class));
    }
}