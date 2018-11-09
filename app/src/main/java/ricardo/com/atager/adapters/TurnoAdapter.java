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
import ricardo.com.atager.modelo.Turno;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.TurnoService;
import ricardo.com.atager.views.TurnoEditar;


public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.Holder> {

    private List<Turno> lista;
    private Context telachamadora;

    public TurnoAdapter(Context context, List<Turno> adapListTurno) {
        lista = adapListTurno;
        telachamadora = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.turno_cartao, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Turno currentTurno = lista.get(position);

        holder.frente_nome.setText((currentTurno.getFrente_nome()));
        holder.data.setText(currentTurno.getData());
        holder.turno.setText(Integer.toString(currentTurno.getTurno())+"º");
        holder.colaborador_nome.setText(currentTurno.getColaborador_nome());
        holder.data_hora_atualizacao.setText(currentTurno.getData_hora_atualizacao());
        holder.tema_dds.setText(currentTurno.getTemaDds());
        holder.producao.setText(String.valueOf(currentTurno.getProducao())+" tn");
        holder.chuva.setText(String.valueOf(currentTurno.getChuva())+" mm");
        holder.observacao.setText(currentTurno.getObservacao());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView frente_nome, data, turno, colaborador_nome,
                data_hora_atualizacao, tema_dds, producao, chuva, observacao, editar, excluir;

        private final Context context;

        public Holder(View itemView) {
            super(itemView);

            frente_nome = (TextView) itemView.findViewById(R.id.tvFrenteNome);
            data = (TextView) itemView.findViewById(R.id.tvdata);
            turno = (TextView) itemView.findViewById(R.id.tvTurno);
            colaborador_nome = (TextView) itemView.findViewById(R.id.tvColaboradorNome);
            data_hora_atualizacao = (TextView) itemView.findViewById(R.id.tvDataHoraAtualizacao);
            tema_dds = (TextView) itemView.findViewById(R.id.tvTemaDds);
            producao = (TextView) itemView.findViewById(R.id.tvProducao);
            chuva = (TextView) itemView.findViewById(R.id.tvChuva);
            observacao = (TextView) itemView.findViewById(R.id.tvObservacao);

            editar = (TextView) itemView.findViewById(R.id.tvEditar);
            excluir = (TextView) itemView.findViewById(R.id.tvExcluir);

            editar.setOnClickListener(this);
            excluir.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {

            final Turno turno = lista.get(getAdapterPosition());
            Bundle enviarDados = new Bundle();

            if (v == editar) {
                enviarDados.putInt("id", turno.getTurno_id());
                enviarDados.putInt("frente_id", turno.getFrente_id());
                enviarDados.putString("frente_nome", turno.getFrente_nome());
                enviarDados.putString("data", turno.getData());
                enviarDados.putInt("turno", turno.getTurno());
                enviarDados.putInt("colaborador_id", turno.getColaborador_id());
                enviarDados.putString("colaborador_nome", turno.getColaborador_nome());
                enviarDados.putString("tema_dds", turno.getTemaDds());
                enviarDados.putDouble("producao", turno.getProducao());
                enviarDados.putDouble("chuva", turno.getChuva());
                enviarDados.putString("observacao", turno.getObservacao());

                Intent intentMudancaTela = new Intent(context, TurnoEditar.class);
                intentMudancaTela.putExtras(enviarDados);
                context.startActivity(intentMudancaTela);

            } else if (v == excluir) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Publicas publicas = new Publicas();
                TurnoService turnoService = publicas.retrofit().create(TurnoService.class);
                final Call<Boolean> call = turnoService.deletar(turno.getTurno_id());

                builder.setTitle("Atager")
                        .setIcon(R.mipmap.atarger9)
                        .setMessage("Deseja realmente excluir o turno " + turno.getTurno() + "º da frente " + turno.getFrente_nome() + "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                call.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.body().equals(true)) {
                                            lista.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), lista.size());
                                        } else {
                                            Toast.makeText(telachamadora, "Não pode ser feito", Toast.LENGTH_LONG).show();
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