package ricardo.com.atager.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.modelo.Turno;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.TurnoService;


/**
 * Created by Ricardo on 10/10/2016.
 */
public class TurnoEditar extends AppCompatActivity{

    private int id, idColaborador, idFrente;
    private TextInputEditText edtFrenteNome, edtData, edtTurno, edtColaboradorNome, edtTemaDds, edtProducao, edtChuva, edtObservacao;
    private Date date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turno_editar);
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
                editarColaborador();
        }
        return true;
    }

    public void iniciarVisoes() {
        edtFrenteNome = (TextInputEditText) findViewById(R.id.edtFrenteNome);
        edtData = (TextInputEditText) findViewById(R.id.edtData);
        edtTurno = (TextInputEditText) findViewById(R.id.edtTurno);
        edtColaboradorNome = (TextInputEditText) findViewById(R.id.edtColaboradorNome);
        edtTemaDds = (TextInputEditText) findViewById(R.id.edtTemaDds);
        edtProducao = (TextInputEditText) findViewById(R.id.edtProducao);
        edtChuva = (TextInputEditText) findViewById(R.id.edtChuva);
        edtObservacao = (TextInputEditText) findViewById(R.id.edtObservacao);
        preencherCampos();
    }

    public void preencherCampos() {
        Intent receberDados = getIntent();
        Bundle bundle = receberDados.getExtras();
        Publicas publicas = new Publicas();

        id = bundle.getInt("id");
        idFrente = bundle.getInt("frente_id");

        edtFrenteNome.setText(String.valueOf(bundle.getString("frente_nome")));
        publicas.disableEditText(edtFrenteNome);

        edtData.setText(bundle.getString("data"));
        publicas.disableEditText(edtData);

        edtTurno.setText(String.valueOf(bundle.getInt("turno")));
        publicas.disableEditText(edtTurno);

        idColaborador = bundle.getInt("colaborador_id");
        edtColaboradorNome.setText(bundle.getString("colaborador_nome"));
        publicas.disableEditText(edtColaboradorNome);

        edtTemaDds.setText(bundle.getString("tema_dds"));
        edtProducao.setText(String.valueOf(bundle.getDouble("producao")));
        edtChuva.setText(String.valueOf(bundle.getDouble("chuva")));
        edtObservacao.setText((bundle.getString("observacao")));
    }


    public Turno atualizarTurno(){
        Intent receberDados = getIntent();
        Bundle bundle = receberDados.getExtras();

        Turno turno = new Turno();
        turno.setTurno_id(id);
        turno.setFrente_id(idFrente);

        String dataRecebida = bundle.getString("data");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();
        try {
            date = df.parse(dataRecebida);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");

        turno.setData(df1.format(date));
        turno.setTurno(Integer.parseInt(edtTurno.getText().toString()));
        turno.setColaborador_id(idColaborador);
        turno.setTemaDds(edtTemaDds.getText().toString());
        turno.setProducao(Double.valueOf(edtProducao.getText().toString()));
        turno.setChuva(Double.valueOf(edtChuva.getText().toString()));
        turno.setObservacao(edtObservacao.getText().toString());
        return turno;
    }

    public void editarColaborador() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Fazendo auterações solicitadas, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        TurnoService turnoService = publicas.retrofit().create(TurnoService.class);
        Call<Boolean> call = turnoService.atualizar(atualizarTurno());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    dialog.dismiss();
                    finish();
                    Toast.makeText(TurnoEditar.this, atualizarTurno().getTurno_id()+" registro de turno atualizado com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(TurnoEditar.this, "Informação inconsistente, por favor verifique!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TurnoEditar.this,"Servidor desligado", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, TurnoLista.class));
    }
}