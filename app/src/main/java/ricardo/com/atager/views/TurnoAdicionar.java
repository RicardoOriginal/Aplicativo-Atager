package ricardo.com.atager.views;

import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.adapters.ColaboradorSpinnerAdapter;
import ricardo.com.atager.adapters.FrenteSpinnerAdapter;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.modelo.Frente;
import ricardo.com.atager.modelo.Turno;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.FrenteService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.TurnoService;

/**
 * Created by Ricardo on 06/02/2017.
 */

public class TurnoAdicionar extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edtTemaDds, edtProducao, edtChuva, edtObservacao;
    private Button btnData;
    private int ano, mes, dia;
    private Publicas publicas;
    private int idColaborador, idFrete, idTurno;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turno);
//        calendario();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.salvar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mSalvar:
                inserirTurno();
        }
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        calendario();
        carregarSpinnerFrentes();
        carregarSpinnerTurnos();
        carregarSpinnerLiders();
    }

//    public void iniciarVisoes() {
//        edtTemaDds = (TextInputEditText) findViewById(R.id.edtTemaDds);
//        edtProducao = (TextInputEditText) findViewById(R.id.edtProducao);
//        edtChuva = (TextInputEditText) findViewById(R.id.edtChuva);
//        edtObservacao = (TextInputEditText) findViewById(R.id.edtObservacao);
//        btnData = (Button) findViewById(R.id.btnData);
//        btnData.setOnClickListener(this);
//    }

    private void carregarSpinnerFrentes() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de frentes, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        publicas = new Publicas();
        FrenteService frenteService = publicas.retrofit().create(FrenteService.class);
        Call<List<Frente>> call = frenteService.mostrarTodos();
        call.enqueue(new Callback<List<Frente>>() {
            @Override
            public void onResponse(Call<List<Frente>> call, Response<List<Frente>> response) {
                if (response.isSuccessful()) {
                    final FrenteSpinnerAdapter frenteSpinnerAdapter = new FrenteSpinnerAdapter(TurnoAdicionar.this, response.body());
                    Spinner spnFrente = (Spinner) findViewById(R.id.spinnerFrente);
                    spnFrente.setAdapter(frenteSpinnerAdapter);
                    dialog.dismiss();
                    spnFrente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Frente frente = frenteSpinnerAdapter.getItem(position);
                            idFrete = frente.getFrente_id();
                            publicas.hideSoftKeyboard(TurnoAdicionar.this);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapter) {
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(TurnoAdicionar.this, "Erro no servidor de frentes", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Frente>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TurnoAdicionar.this, "Servidor desligado", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void carregarSpinnerTurnos(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Turnos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spnTurno = (Spinner) findViewById(R.id.spinnerTurno);
        spnTurno.setAdapter(adapter);
        spnTurno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idTurno = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
    }

    private void carregarSpinnerLiders() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Carregando lista de liders, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        publicas = new Publicas();
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Call<List<Colaborador>> call = colaboradorService.mostrarTodos();
        call.enqueue(new Callback<List<Colaborador>>() {
            @Override
            public void onResponse(Call<List<Colaborador>> call, Response<List<Colaborador>> response) {
                if (response.isSuccessful()) {
                    final ColaboradorSpinnerAdapter colaboradorSpinnerAdapter = new ColaboradorSpinnerAdapter(TurnoAdicionar.this, response.body());
                    Spinner spnColaborador = (Spinner) findViewById(R.id.spinnerLider);
                    spnColaborador.setAdapter(colaboradorSpinnerAdapter);
                    dialog.dismiss();
                    spnColaborador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Colaborador colaborador = colaboradorSpinnerAdapter.getItem(position);
                            idColaborador = colaborador.getColaborador_id();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapter) {
                        }
                    });
                } else {
                    dialog.dismiss();
                    Toast.makeText(TurnoAdicionar.this, "Erro no servidor, carregamento de liders", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Colaborador>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TurnoAdicionar.this, "Erro no servidor, carregamento de liders", Toast.LENGTH_LONG).show();
            }
        });
    }

    public Turno novo() {
        Turno turno = new Turno();

        edtTemaDds = (TextInputEditText) findViewById(R.id.edtTemaDds);
        edtProducao = (TextInputEditText) findViewById(R.id.edtProducao);
        edtChuva = (TextInputEditText) findViewById(R.id.edtChuva);
        edtObservacao = (TextInputEditText) findViewById(R.id.edtObservacao);

        turno.setFrente_id(idFrete);
        turno.setData(String.format("%02d-%02d-%02d", ano, (mes+1), dia));
        turno.setTurno(idTurno);
        turno.setColaborador_id(idColaborador);
        turno.setTemaDds(edtTemaDds.getText().toString());
        turno.setProducao(Double.valueOf(edtProducao.getText().toString()));
        turno.setChuva(Double.valueOf(edtChuva.getText().toString()));
        turno.setObservacao(edtObservacao.getText().toString());
        return turno;
    }

    public void calendario(){
        btnData = (Button) findViewById(R.id.btnData);
        btnData.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        btnData.setText(dia + "/" + (mes + 1) + "/" + ano);
    }

    private void selecionarData() {
        DatePickerDialog dialogData = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ano = year;
                mes = monthOfYear;
                dia = dayOfMonth;
                btnData.setText(dia+"/"+(mes + 1)+"/"+ano);
            }
        }, ano, mes, dia);
        dialogData.show();
    }

    private void inserirTurno() {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Cadastrando turno, aguarde...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        Publicas publicas = new Publicas();
        TurnoService turnoService = publicas.retrofit().create(TurnoService.class);
        Call<Boolean> call = turnoService.inserir(novo());
        Log.d("Turno novo ", String.valueOf(novo().getTurno_id()));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TurnoAdicionar.this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    Log.d("Turno ", String.valueOf(novo().getFrente_id()));
                    dialog.dismiss();
                    finish();
                    startActivity(new Intent(TurnoAdicionar.this, TurnoLista.class));
                }else {
                    dialog.dismiss();
                    Toast.makeText(TurnoAdicionar.this,"Erro ao cadastrar novo turno!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(TurnoAdicionar.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == btnData){
            selecionarData();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        startActivity(new Intent(this, TurnoLista.class));
    }

}
