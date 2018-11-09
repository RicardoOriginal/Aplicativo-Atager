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
import ricardo.com.atager.adapters.TipoMaquinaSpinnerAdapter;
import ricardo.com.atager.modelo.Maquina;
import ricardo.com.atager.modelo.TipoMaquina;
import ricardo.com.atager.service.MaquinaService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.TipoMaquinaService;

/**
 * Created by Ricardo on 10/10/2016.
 */
public class MaquinaAdicionar extends AppCompatActivity{

    private TextInputEditText edtFrota, edtModelo, edtHorasTrabalhadas;
    private Spinner spnTipo;
    private int tipoId;
    private Switch ativo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maquina);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                adicionarMaquina();
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        carregarSpinnerTipo();
    }

    public void carregarSpinnerTipo(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de tipos, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        TipoMaquinaService tipoMaquinaService = publicas.retrofit().create(TipoMaquinaService.class);
        Call<List<TipoMaquina>> listCall = tipoMaquinaService.mostrarTodos();
        listCall.enqueue(new Callback<List<TipoMaquina>>() {
            @Override
            public void onResponse(Call<List<TipoMaquina>> call, Response<List<TipoMaquina>> response) {
                if (response.isSuccessful()){
                    final TipoMaquinaSpinnerAdapter tipoMaquinaSpinnerAdapter = new TipoMaquinaSpinnerAdapter(MaquinaAdicionar.this, response.body());
                    spnTipo = (Spinner) findViewById(R.id.spinnerTipo);
                    spnTipo.setAdapter(tipoMaquinaSpinnerAdapter);
                    dialog.dismiss();
                    spnTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TipoMaquina tipoMaquina = tipoMaquinaSpinnerAdapter.getItem(position);
                            tipoId = tipoMaquina.getTipoMaquina_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            dialog.dismiss();
                            Toast.makeText(MaquinaAdicionar.this, "ainda nao selecionou nada", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<TipoMaquina>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MaquinaAdicionar.this, "Erro ao carregar cargos", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Maquina criarMaquina() {
        edtFrota = (TextInputEditText) findViewById(R.id.edtFrota);
        edtModelo = (TextInputEditText) findViewById(R.id.edtModelo);
        edtHorasTrabalhadas = (TextInputEditText) findViewById(R.id.edtHorasTrabalhadas);
        ativo = (Switch) findViewById(R.id.swAtivo);

        Maquina maquina = new Maquina();
        maquina.setFrota(Integer.parseInt(edtFrota.getText().toString()));
        maquina.setTipoMaquina_id(tipoId);
        maquina.setModelo(edtModelo.getText().toString());
        maquina.setHoras_trabalhadas(Double.parseDouble(edtHorasTrabalhadas.getText().toString()));
        maquina.setAtivo(ativo.isChecked());
        return maquina;
    }

    public void adicionarMaquina(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Casdastrando maquina, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        MaquinaService maquinaService = publicas.retrofit().create(MaquinaService.class);
        Call<Boolean> call = maquinaService.inserir(criarMaquina());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Toast.makeText(MaquinaAdicionar.this, criarMaquina().getModelo() + " inserido com sucesso!", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
                startActivity(new Intent(MaquinaAdicionar.this, MaquinaLista.class));
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MaquinaAdicionar.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, MaquinaLista.class));
    }
}