package ricardo.com.atager.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.modelo.Colaborador;
import ricardo.com.atager.service.ColaboradorService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.views.ColaboradorEditar;

import static ricardo.com.atager.service.Publicas.ATIVO;
import static ricardo.com.atager.service.Publicas.ID;
import static ricardo.com.atager.service.Publicas.MATRICULA;
import static ricardo.com.atager.service.Publicas.NIVEL_ID;
import static ricardo.com.atager.service.Publicas.NOME;
import static ricardo.com.atager.service.Publicas.SENHA;

public class ColaboradorAdapter extends RecyclerView.Adapter<ColaboradorAdapter.Holder> {

    private List<Colaborador> listaColaborador;
    private Context telachamadora;

    public ColaboradorAdapter(Context context, List<Colaborador> adapListColaborador) {
        this.listaColaborador = adapListColaborador;
        this.telachamadora = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.colaborador_cartao, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Colaborador currentColaborador = listaColaborador.get(position);
        holder.matricula.setText(Integer.toString(currentColaborador.getMatricula()));
        holder.nome.setText(currentColaborador.getColaborador_nome());
        holder.senha.setText(currentColaborador.getSenha());
        if (currentColaborador.isAtivo()) {
            holder.ativo.setText("Ativo");
        } else {
            holder.ativo.setText("Inativo");
        }
        holder.cargo.setText(currentColaborador.getCargo());
    }

    @Override
    public int getItemCount() {
        return listaColaborador.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView matricula, nome, senha, ativo, cargo, editar, excluir;

        public Context context;

        public Holder(View itemView) {
            super(itemView);

            matricula = (TextView) itemView.findViewById(R.id.tvMatricula);
            nome = (TextView) itemView.findViewById(R.id.tvNome);
            senha = (TextView) itemView.findViewById(R.id.tvSenha);
            ativo = (TextView) itemView.findViewById(R.id.tvAtivo);
            cargo = (TextView) itemView.findViewById(R.id.tvCargo);
            editar = (TextView) itemView.findViewById(R.id.tvEditar);
            excluir = (TextView) itemView.findViewById(R.id.tvExcluir);

            editar.setOnClickListener(this);
            excluir.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {

            final Colaborador colaborador = listaColaborador.get(getAdapterPosition());
            Bundle enviarDados = new Bundle();

            if (v == editar) {
                enviarDados.putInt(ID, colaborador.getColaborador_id());
                enviarDados.putInt(MATRICULA, colaborador.getMatricula());
                enviarDados.putString(NOME, colaborador.getColaborador_nome());
                enviarDados.putString(SENHA, colaborador.getSenha());
                enviarDados.putBoolean(ATIVO, colaborador.isAtivo());
                enviarDados.putInt(NIVEL_ID, colaborador.getNivel_id());

                Intent intentMudancaTela = new Intent(context, ColaboradorEditar.class);
                intentMudancaTela.putExtras(enviarDados);
                context.startActivity(intentMudancaTela);

            } else if (v == excluir) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Publicas publicas = new Publicas();
                ColaboradorService colaboradorService = publicas.retrofit().create(ColaboradorService.class);
                final Call<Boolean> call = colaboradorService.deletar(colaborador.getColaborador_id());

                builder.setTitle("Atager")
                        .setIcon(R.mipmap.atarger9)
                        .setMessage("Deseja realmente excluir o colaborador " + colaborador.getColaborador_nome() + "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                call.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.body().equals(true)) {
                                            listaColaborador.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), listaColaborador.size());
                                        } else {
                                            Toast.makeText(telachamadora, "Não pode ser feito, verifique o pedido.", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        Toast.makeText(telachamadora, "Servidor desligado", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        }
    }
}