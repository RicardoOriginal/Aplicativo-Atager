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
import ricardo.com.atager.modelo.Divisao;
import ricardo.com.atager.service.DivisaoService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.views.DivisaoEditar;

/**
 * Created by Ricardo on 13/02/2017.
 */
public class DivisaoAdapter extends RecyclerView.Adapter<DivisaoAdapter.Holder>{

    private List<Divisao> divisaoList;
    private Context telachamadora;

    public DivisaoAdapter(Context context, List<Divisao> divisaos){
        this.divisaoList = divisaos;
        this.telachamadora = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.divisao_cartao, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Divisao divisaoAtual = divisaoList.get(position);
        holder.tvNome.setText(divisaoAtual.getDivisao_nome());
        holder.tvUnidadeNome.setText(divisaoAtual.getUnidade_nome());
        holder.tvSupervisorNome.setText(divisaoAtual.getColaborador_nome());
    }

    @Override
    public int getItemCount() {
        return divisaoList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvNome, tvUnidadeNome, tvSupervisorNome, tvEditar, tvExcluir;
        public Context context;

        public Holder(View itemView) {
            super(itemView);

            tvNome = (TextView) itemView.findViewById(R.id.tvNome);
            tvUnidadeNome = (TextView) itemView.findViewById(R.id.tvUnidadeNome);
            tvSupervisorNome = (TextView) itemView.findViewById(R.id.tvSupervisorNome);
            tvEditar = (TextView) itemView.findViewById(R.id.tvEditar);
            tvExcluir = (TextView) itemView.findViewById(R.id.tvExcluir);

            tvEditar.setOnClickListener(this);
            tvExcluir.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            final Divisao divisao = divisaoList.get(getAdapterPosition());
            if (v == tvEditar){
                Bundle pocateDados = new Bundle();
                pocateDados.putInt("id", divisao.getDivisao_id());
                pocateDados.putString("nome", divisao.getDivisao_nome());
                pocateDados.putInt("unidade_id", divisao.getUnidade_id());
                pocateDados.putInt("supervisor_id", divisao.getColaborador_id());

                Intent intentMudancaTela = new Intent(context, DivisaoEditar.class);
                intentMudancaTela.putExtras(pocateDados);
                context.startActivity(intentMudancaTela);

            }else if(v == tvExcluir){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Publicas publicas = new Publicas();
                DivisaoService divisaoService = publicas.retrofit().create(DivisaoService.class);
                final Call<Boolean> call = divisaoService.deletar(divisao.getDivisao_id());

                builder.setTitle("Atager")
                        .setIcon(R.mipmap.atarger9)
                        .setMessage("Deseja realmente excluir a divisao " + divisao.getDivisao_nome() + "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                call.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.body().equals(true)) {
                                            divisaoList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), divisaoList.size());
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
