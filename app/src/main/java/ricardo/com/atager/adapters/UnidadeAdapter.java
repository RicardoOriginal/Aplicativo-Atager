package ricardo.com.atager.adapters;

import android.annotation.SuppressLint;
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
import ricardo.com.atager.modelo.Unidade;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.service.UnidadeService;
import ricardo.com.atager.views.UnidadeEditar;

/**
 * Created by Ricardo on 17/02/2017.
 */

public class UnidadeAdapter extends RecyclerView.Adapter<UnidadeAdapter.Holder> {

    private List<Unidade> unidadeList;
    private Context telaChamadora;

    public UnidadeAdapter(List<Unidade> unidades, Context context) {
        unidadeList = unidades;
        telaChamadora = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.unidade_cartao, parent, false);
        return new Holder(row);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Unidade unidadeAtual = unidadeList.get(position);
        holder.tvNome.setText(unidadeAtual.getUnidade_nome());
        holder.tvEmpresaId = unidadeAtual.getEmpresa_id();
        holder.tvEmpresaNome.setText(unidadeAtual.getEmpresa_nome());
        holder.tvGerenteId = unidadeAtual.getColaborador_id();
        holder.tvGerenteNome.setText(unidadeAtual.getUnidade_nome());
    }

    @Override
    public int getItemCount() {
        return unidadeList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvNome, tvEmpresaNome, tvGerenteNome, tvEditar, tvExcluir;
        public int tvEmpresaId, tvGerenteId;
        public Context context;

        public Holder(View itemView) {

            super(itemView);
            tvNome = (TextView) itemView.findViewById(R.id.tvNome);
            tvEmpresaNome = (TextView) itemView.findViewById(R.id.tvEmpresaNome);
            tvGerenteNome = (TextView) itemView.findViewById(R.id.tvGerenteNome);
            tvEditar = (TextView) itemView.findViewById(R.id.tvEditar);
            tvExcluir = (TextView) itemView.findViewById(R.id.tvExcluir);

            tvEditar.setOnClickListener(this);
            tvExcluir.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            final Unidade unidade = unidadeList.get(getAdapterPosition());
            if (v == tvEditar) {
                Bundle pacoteDados = new Bundle();
                pacoteDados.putInt("id", unidade.getUnidade_id());
                pacoteDados.putString("nome", unidade.getUnidade_nome());
                pacoteDados.putInt("empresa_id", unidade.getEmpresa_id());
                pacoteDados.putInt("gerente_id", unidade.getColaborador_id());

                Intent intentMudancaTela = new Intent(context, UnidadeEditar.class);
                intentMudancaTela.putExtras(pacoteDados);
                context.startActivity(intentMudancaTela);

            } else if (v == tvExcluir) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Publicas publicas = new Publicas();
                UnidadeService unidadeService = publicas.retrofit().create(UnidadeService.class);
                final Call<Boolean> call = unidadeService.deletar(unidade.getUnidade_id());

                builder.setTitle("Atager")
                        .setIcon(R.mipmap.atarger9)
                        .setMessage("Deseja realmente excluir a unidade " + unidade.getUnidade_nome() + "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                call.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.body().equals(true)) {
                                            unidadeList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), unidadeList.size());
                                        }else {
                                            Toast.makeText(telaChamadora, "Não pode ser feito pois a divisões cadastradas dentro da unidade", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        Toast.makeText(telaChamadora, "Servidor desligado", Toast.LENGTH_LONG).show();
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
