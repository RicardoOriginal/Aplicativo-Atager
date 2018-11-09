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
import ricardo.com.atager.modelo.Empresa;
import ricardo.com.atager.service.EmpresaService;
import ricardo.com.atager.service.Publicas;
import ricardo.com.atager.views.EmpresaEditar;

/**
 * Created by Ricardo on 17/02/2017.
 */

public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.Holder> {

    private List<Empresa> empresaList;
    private Context telachamadora;

    public EmpresaAdapter(Context context, List<Empresa> empresas) {
        this.empresaList = empresas;
        this.telachamadora = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.empresa_cartao, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Empresa empresaAtual = empresaList.get(position);
        holder.tvNome.setText(empresaAtual.getEmpresa_nome());
        holder.tvAdministradorNome.setText(empresaAtual.getColaborador_nome());
    }

    @Override
    public int getItemCount() {
        return empresaList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvNome, tvAdministradorNome, tvEditar, tvExcluir;
        public Context context;

        public Holder(View itemView) {

            super(itemView);
            tvNome = (TextView) itemView.findViewById(R.id.tvEmpresaNome);
            tvAdministradorNome = (TextView) itemView.findViewById(R.id.tvAdministradorNome);
            tvEditar = (TextView) itemView.findViewById(R.id.tvEditar);
            tvExcluir = (TextView) itemView.findViewById(R.id.tvExcluir);

            tvEditar.setOnClickListener(this);
            tvExcluir.setOnClickListener(this);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            final Empresa empresa = empresaList.get(getAdapterPosition());
            if (v == tvEditar) {
                Bundle pacoteDados = new Bundle();
                pacoteDados.putInt("id", empresa.getEmpresa_id());
                pacoteDados.putString("nome", empresa.getEmpresa_nome());
                pacoteDados.putInt("administrador_id", empresa.getColaborador_id());

                Intent intentMudancaTela = new Intent(context, EmpresaEditar.class);
                intentMudancaTela.putExtras(pacoteDados);
                context.startActivity(intentMudancaTela);

            } else if (v == tvExcluir) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Publicas publicas = new Publicas();
                EmpresaService empresaService = publicas.retrofit().create(EmpresaService.class);
                final Call<Boolean> call = empresaService.deletar(empresa.getEmpresa_id());

                builder.setTitle("Atager")
                        .setIcon(R.mipmap.atarger9)
                        .setMessage("Deseja realmente excluir a empresa " + empresa.getEmpresa_nome() + "?")
                        .setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int id) {
                                call.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if (response.body().equals(true)) {
                                            empresaList.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), empresaList.size());
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
