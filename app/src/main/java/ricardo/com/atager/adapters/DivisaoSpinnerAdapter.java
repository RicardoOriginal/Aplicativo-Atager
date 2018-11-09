package ricardo.com.atager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ricardo.com.atager.modelo.Divisao;

public class DivisaoSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<Divisao> divisaoList;

    public DivisaoSpinnerAdapter(Context context, List<Divisao> divisaoList) {
        this.divisaoList = divisaoList;
        this.context = context;
    }

    public int getCount() {
        return divisaoList.size();
    }

    public Divisao getItem(int idDivisao) {
        return divisaoList.get(idDivisao);
    }

    public long getItemId(int posicao) {
        return (long)posicao;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setText(divisaoList.get(position).getDivisao_nome());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setText(divisaoList.get(i).getDivisao_nome());
        return txt;
    }
}