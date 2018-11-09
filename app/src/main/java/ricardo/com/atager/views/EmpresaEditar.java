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
 * Created by Ricardo on 19/02/2017.
 */
public class EmpresaEditar extends AppCompatActivity {

    private TextInputEditText edtNomeEmpresa;
    private int idEmpresa, idAdministrador;
    private Spinner spnAdministrador;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.empresa);
        iniciarVisoes();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                editarEmpresa();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarSpinnerAdministradores();
    }

    private void iniciarVisoes() {
        edtNomeEmpresa = (TextInputEditText) findViewById(R.id.edtNomeEmpresa);
        preencherCampos();
    }

    private void preencherCampos() {
        Intent receberDados = getIntent();
        Bundle bundle = receberDados.getExtras();
        idEmpresa = bundle.getInt("id");
        edtNomeEmpresa.setText(bundle.getString("nome"));
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
                    final ColaboradorSpinnerAdapter colaboradorSpinnerAdapter = new ColaboradorSpinnerAdapter(EmpresaEditar.this, response.body());
                    spnAdministrador.setAdapter(colaboradorSpinnerAdapter);

                    Intent receberdados = getIntent();
                    Bundle bundle = receberdados.getExtras();
                    int posicao = 0;
                    for (Colaborador colaborador : response.body()) {
                        if (colaborador.getColaborador_id() == bundle.getInt("administrador_id")) {
                            break;
                        }
                        posicao++;
                    }
                    spnAdministrador.setSelection(posicao);
                    dialog.dismiss();
                    spnAdministrador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Colaborador administrador = colaboradorSpinnerAdapter.getItem(position);
                            idAdministrador = administrador.getColaborador_id();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            dialog.dismiss();
                            Toast.makeText(EmpresaEditar.this, "ainda nao selecionou nada", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EmpresaEditar.this, "Erro ao carregar Administradore", Toast.LENGTH_LONG).show();
            }
        });

    }

    private Empresa atualizar() {
        Empresa empresa = new Empresa();
        empresa.setEmpresa_id(idEmpresa);
        empresa.setEmpresa_nome(edtNomeEmpresa.getText().toString());
        empresa.setColaborador_id(idAdministrador);
        return empresa;
    }

    private void editarEmpresa() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Fazendo atualização solicitadas, aguarde");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        EmpresaService empresaService = publicas.retrofit().create(EmpresaService.class);
        Call<Boolean> call = empresaService.atualizar(atualizar());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    dialog.dismiss();
                    finish();
                    Toast.makeText(EmpresaEditar.this, "Empresa " + atualizar().getEmpresa_nome() + " atualizada com sucesso", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(EmpresaEditar.this, "Informações inconsistentes, por favor verifique", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EmpresaEditar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, EmpresaLista.class));
    }
}
