package ricardo.com.atager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.views.TurnoLista;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;
import static ricardo.com.atager.service.Publicas.ATIVO;
import static ricardo.com.atager.service.Publicas.CARGO;
import static ricardo.com.atager.service.Publicas.ID;
import static ricardo.com.atager.service.Publicas.MATRICULA;
import static ricardo.com.atager.service.Publicas.NOME;
import static ricardo.com.atager.service.Publicas.SENHA;
import static ricardo.com.atager.service.Publicas.TAG;
import static ricardo.com.atager.service.Publicas.URL;
import static ricardo.com.atager.service.Publicas.URL_NET;
import static ricardo.com.atager.service.Publicas.URL_REDE;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView tvConexão;
    private Button btnEntrar;
    private TextInputEditText edtMatricula, edtSenha;
    private int matricula;
    private String senha;
    private ConnectivityManager conectivtyManager;

    private Timer timerAtual = new Timer();
    private TimerTask task;
    private final Handler handler = new Handler();
    Publicas publicas = new Publicas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ativaTimer();
        conexao();
    }

    private void ativaTimer() {
        task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        conexao();
                    }
                });
            }
        };
        timerAtual.schedule(task, 10000, 10000);
    }

    public void iniciarVisoes() {
        tvConexão = (TextView) findViewById(R.id.tvConexão);
        edtMatricula = (TextInputEditText) findViewById(R.id.edtMatricula);
        edtSenha = (TextInputEditText) findViewById(R.id.edtSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(this);
    }

    public Boolean conexao() {
        iniciarVisoes();
        conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {

            if (conectivtyManager.getActiveNetworkInfo().getType() == TYPE_WIFI) {
                URL = URL_REDE;
                Log.d("Ricardo: ", "Usando URL REDE");
            } else if (conectivtyManager.getActiveNetworkInfo().getType() == TYPE_MOBILE) {
                URL = URL_NET;
                Log.d("Ricardo: ", "Usando URL Net");
            }
            btnEntrar.setEnabled(true);
            tvConexão.setVisibility(View.INVISIBLE);
            return true;
        } else {
            btnEntrar.setEnabled(false);
            tvConexão.setVisibility(View.VISIBLE);
            tvConexão.setTextColor(getResources().getColor(R.color.vermelho));
            tvConexão.setText("Sem conexao com a internet!");
            return false;
        }
    }

    public void autenticarAcesso(int matricula, String senha) {
        final ProgressDialog dialog = ProgressDialog.show(this, "Atager", "Verificando matricula e senha, aguarde...");
        ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
        Log.d(TAG, String.valueOf(publicas.retrofit().baseUrl()));
        Call<Colaborador> call = colaboradorService.autenticar(matricula, senha);
        call.enqueue(new Callback<Colaborador>() {
            @Override
            public void onResponse(Call<Colaborador> call, Response<Colaborador> response) {
                if (response.isSuccessful()) {
                    if (response.body().getColaborador_id() != 0) {

                        Bundle enviarDados = new Bundle();
                        enviarDados.putInt(ID, response.body().getColaborador_id());
                        enviarDados.putInt(MATRICULA, response.body().getMatricula());
                        enviarDados.putString(NOME, response.body().getColaborador_nome());
                        enviarDados.putString(SENHA, response.body().getSenha());
                        enviarDados.putBoolean(ATIVO, response.body().isAtivo());
                        enviarDados.putString(CARGO, response.body().getCargo());

                        Intent intent = new Intent(Login.this, TurnoLista.class);
                        timerAtual.cancel();
                        dialog.dismiss();
                        finish();
                        startActivity(intent.putExtras(enviarDados));

                    } else {
                        dialog.dismiss();

                        tvConexão.setVisibility(View.VISIBLE);
                        tvConexão.setTextColor(getResources().getColor(R.color.vermelho));
                        tvConexão.setText("Matricula ou senha incorretos");
                    }
                }else {
                    Toast.makeText(Login.this, "Erro ao acessar tente novamente", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Colaborador> call, Throwable t) {
                dialog.dismiss();
                Log.d(TAG, t.getMessage()+" Sem acesso ao servidor");
                Toast.makeText(Login.this, "Sem acesso ao servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnEntrar) {
            if (edtMatricula.getText().length() == 0) {
                edtMatricula.setError("Campo vazio");
                edtMatricula.requestFocus();
            } else if (edtMatricula.getText().length() != 6) {
                edtMatricula.setError("Matricula invalida");
                edtMatricula.requestFocus();
            } else if (edtSenha.getText().length() == 0) {
                edtSenha.setError("Campo vazio");
                edtSenha.requestFocus();
            } else if (edtSenha.getText().length() < 5) {
                edtSenha.setError("senha invalida");
                edtSenha.requestFocus();
            } else {
                matricula = Integer.parseInt(edtMatricula.getText().toString());
                senha = edtSenha.getText().toString();
                autenticarAcesso(matricula, senha);
            }
        }
    }
}
