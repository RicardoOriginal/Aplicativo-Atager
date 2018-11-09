package ricardo.com.atager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ricardo.com.atager.modelo.TipoMaquina;

public class TipoMaquinaSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context classeChamadora;
    private List<TipoMaquina> tipoMaquinaList;

    public TipoMaquinaSpinnerAdapter(Context context, List<TipoMaquina> tipoMaquinas) {
        this.tipoMaquinaList = tipoMaquinas;
        this.classeChamadora = context;
    }

    public int getCount() {
        return tipoMaquinaList.size();
    }

    public TipoMaquina getItem(int idTipoMaquina) {
        return tipoMaquinaList.get(idTipoMaquina);
    }

    public long getItemId(int posicao) {
        return (long)posicao;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(classeChamadora);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setText(tipoMaquinaList.get(position).getTipo());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(classeChamadora);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setText(tipoMaquinaList.get(i).getTipo());
        return txt;
    }
}