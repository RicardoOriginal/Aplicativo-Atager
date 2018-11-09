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
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Empresa;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.EmpresaService;
import ricardo.com.atager.service.Publicas;

/**
 * Created by Ricardo on 13/02/2017.
 */

public class EmpresaAdicionar extends AppCompatActivity {

    private TextInputEditText edtNome;
    private int administradorId;
    private Spinner spnAdministrador;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empresa);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                adicionarEmpresa();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarSpinnerAdministradores();
    }

    public void carregarSpinnerAdministradores() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de administradores, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<List<Colaborador>> listCall = colaboradorService.mostrarTodos();
        listCall.enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful()) {
                    spnAdministrador = (Spinner) findViewById(R.id.spinnerAdministrador);
                    final ColaboradorSpinnerAdapter colaboradorSpinnerAdapter = new ColaboradorSpinnerAdapter(EmpresaAdicionar.this, response.body());
                    spnAdministrador.setAdapter(colaboradorSpinnerAdapter);
                    dialog.dismiss();
                    spnAdministrador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Colaborador administrador = colaboradorSpinnerAdapter.getItem(position);
                            administradorId = administrador.getColaborador_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            dialog.dismiss();
                            Toast.makeText(EmpresaAdicionar.this, "ainda nao selecionou nada", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EmpresaAdicionar.this, "Erro ao carregar Administradore", Toast.LENGTH_LONG).show();
            }
        });

    }

    private Empresa nova() {
        Empresa empresa = new Empresa();
        edtNome = (TextInputEditText) findViewById(R.id.edtNomeEmpresa);
        empresa.setEmpresa_nome(edtNome.getText().toString());
        empresa.setColaborador_id(administradorId);
        return empresa;
    }

    private void adicionarEmpresa() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Inserindo nova divisao, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        EmpresaService empresaService = publicas.retrofit().create(EmpresaService.class);
        Call<Boolean> call = empresaService.inserir(nova());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    dialog.dismiss();
                    Toast.makeText(EmpresaAdicionar.this, "Empresa " + nova().getEmpresa_nome() + " adicionada com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(EmpresaAdicionar.this, EmpresaLista.class));
                } else {
                    dialog.dismiss();
                    Toast.makeText(EmpresaAdicionar.this, "Informações inconsistentes, por favor verifique!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EmpresaAdicionar.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, EmpresaLista.class));
    }
}
