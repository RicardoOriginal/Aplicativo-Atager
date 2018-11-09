package ricardo.com.atager.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ricardo.com.atager.R;
import ricardo.com.atager.modelo.Maquina;
import ricardo.com.atager.service.MaquinaService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.views.ColaboradorEditar;
import ricardo.com.atager.views.MaquinaEditar;

import static ricardo.com.atager.service.Publicas.ATIVO;

public class MaquinaAdapter extends RecyclerView.Adapter<MaquinaAdapter.Holder> {

    private List<Maquina> maquinaList;
    private Context telachamadora;

    public MaquinaAdapter(Context context, List<Maquina> maquinas) {
        this.maquinaList = maquinas;
        this.telachamadora = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.maquina_cartao, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Maquina currentMaquina = maquinaList.get(position);
        holder.frota.setText(Integer.toString(currentMaquina.getFrota()));
        holder.tipo.setText(currentMaquina.getTipo());
        holder.modelo.setText(currentMaquina.getModelo());
        holder.horasTrabalhadas.setText(Double.toString(currentMaquina.getHoras_trabalhadas()));
        if (currentMaquina.isAtivo()) {
            holder.ativo.setText("Ativo");
        } else {
            holder.ativo.setText("Inativo");
        }
    }

    @Override
    public int getItemCount() {
        return maquinaList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView frota, tipo, modelo, horasTrabalhadas, ativo, editar, excluir;

        public Context context;

        Holder(View itemView) {
            super(itemView);

            frota = (TextView) itemView.findViewById(R.id.tvFrota);
            tipo = (TextView) itemView.findViewById(R.id.tvTipo);
            modelo = (TextView) itemView.findViewById(R.id.tvModelo);
            ativo = (TextView) itemView.findViewById(R.id.tvAtivo);
            horasTrabalhadas = (TextView) itemView.findViewById(R.id.tvHorasTrabalhadas);
            editar = (TextView) itemView.findViewById(R.id.tvEditar);
            excluir = (TextView) itemView.findViewById(R.id.tvExcluir);

            editar.setOnClickListener(this);
            excluir.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {

            final Maquina maquina = maquinaList.get(getAdapterPosition());
            Bundle enviarDados = new Bundle();

            if (v == editar) {
                enviarDados.putInt("maquinaId", maquina.getMaquina_id());
                enviarDados.putInt("frota", maquina.getFrota());
                enviarDados.putInt("tipo_maquina_id", maquina.getTipoMaquina_id());
                enviarDados.putString("tipo", maquina.getTipo());
                enviarDados.putString("modelo", maquina.getModelo());
                enviarDados.putDouble("horas_trabalhadas", maquina.getHoras_trabalhadas());
                enviarDados.putBoolean(ATIVO, maquina.isAtivo());

                Intent intentMudancaTela = new Intent(context, MaquinaEditar.class);
                intentMudancaTela.putExtras(enviarDados);
                context.startActivity(intentMudancaTela);

            } else if (v == excluir) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Publicas publicas = new Publicas();
                MaquinaService maquinaService = publicas.retrofit().create(MaquinaService.class);
                final Call<Boolean> call = maquinaService.deletar(maquina.getMaquina_id());

                builder.setTitle("Atager")
                        .setIcon(R.mipmap.atarger9)
                        .setMessage("Deseja realmente excluir a maquina " + maquina.getModelo() + "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                call.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.body().equals(true)) {
                                            maquinaList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), maquinaList.size());
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