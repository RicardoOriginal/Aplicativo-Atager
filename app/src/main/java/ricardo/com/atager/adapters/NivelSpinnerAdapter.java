package ricardo.com.atager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ricardo.com.atager.modelo.Nivel;

public class NivelSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<Nivel> nivelList;

    public NivelSpinnerAdapter(Context context, List<Nivel> nivels) {
        this.nivelList = nivels;
        this.context = context;
    }

    public int getCount() {
        return nivelList.size();
    }

    public Nivel getItem(int id) {
        return nivelList.get(id);
    }

    public long getItemId(int posicao) {
        return (long)posicao;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setText(nivelList.get(position).getCargo());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setText(nivelList.get(i).getCargo());
        return txt;
    }
}