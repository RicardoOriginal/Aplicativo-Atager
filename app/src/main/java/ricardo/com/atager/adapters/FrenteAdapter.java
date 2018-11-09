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
import ricardo.com.atager.modelo.Frente;
import ricardo.com.atager.service.FrenteService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.views.FrenteEditar;

/**
 * Created by Ricardo on 12/02/2017.
 */

public class FrenteAdapter extends RecyclerView.Adapter<FrenteAdapter.Holder> {

    private List<Frente> frenteList;
    private Context telachamadora;

    public FrenteAdapter(Context contexto, List<Frente> frentes) {
        this.frenteList = frentes;
        this.telachamadora = contexto;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.frente_cartao, parent, false);
        return new Holder(row);
    }

    public void onBindViewHolder(FrenteAdapter.Holder holder, int position) {
        Frente frenteAtual = frenteList.get(position);
        holder.tvNome.setText(frenteAtual.getFrente_nome());
        holder.tvDivisao.setText(frenteAtual.getDivisao_nome());
        holder.tvEncarregado.setText(frenteAtual.getColaborador_nome());
    }

    @Override
    public int getItemCount() {
        return frenteList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvNome, tvDivisao, tvEncarregado, tvEditar, tvExcluir;
        public Context context;

        public Holder(View itemView) {
            super(itemView);

            tvNome = (TextView) itemView.findViewById(R.id.tvNomeFrente);
            tvDivisao = (TextView) itemView.findViewById(R.id.tvNomeDivisao);
            tvEncarregado = (TextView) itemView.findViewById(R.id.tvNomeEncarregado);

            tvEditar = (TextView) itemView.findViewById(R.id.tvEditar);
            tvExcluir = (TextView) itemView.findViewById(R.id.tvExcluir);

            tvEditar.setOnClickListener(this);
            tvExcluir.setOnClickListener(this);
            this.context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            final Frente frente = frenteList.get(getAdapterPosition());
            Bundle pacoteDados = new Bundle();

            if (v == tvEditar){
                pacoteDados.putInt("id", frente.getFrente_id());
                pacoteDados.putString("nome", frente.getFrente_nome());
                pacoteDados.putInt("divisao_id", frente.getDivisao_id());
                pacoteDados.putInt("encarregado_id", frente.getColaborador_id());

                Intent intentMudancaTela = new Intent(context, FrenteEditar.class);
                intentMudancaTela.putExtras(pacoteDados);
                context.startActivity(intentMudancaTela);

            }else if(v == tvExcluir){

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Publicas publicas = new Publicas();
                FrenteService frenteService = publicas.retrofit().create(FrenteService.class);
                final Call<Boolean> call = frenteService.deletar(frente.getFrente_id());

                builder.setTitle("Atager")
                        .setIcon(R.mipmap.atarger9)
                        .setMessage("Deseja realmente excluir a frente " + frente.getFrente_nome() + "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                call.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.body().equals(true)) {
                                            frenteList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), frenteList.size());
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
