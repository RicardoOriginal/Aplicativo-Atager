package ricardo.com.atager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ricardo.com.atager.modelo.Unidade;

public class UnidadeSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<Unidade> unidades;

    public UnidadeSpinnerAdapter(Context context, List<Unidade> unidadeList) {
        this.unidades = unidadeList;
        this.context = context;
    }

    public int getCount() {
        return unidades.size();
    }

    public Unidade getItem(int idDivisao) {
        return unidades.get(idDivisao);
    }

    public long getItemId(int posicao) {
        return (long)posicao;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setText(unidades.get(position).getUnidade_nome());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setText(unidades.get(i).getUnidade_nome());
        return txt;
    }
}