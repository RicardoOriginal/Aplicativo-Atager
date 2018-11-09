package ricardo.com.atager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ricardo.com.atager.modelo.Empresa;

public class EmpresaSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context context;
    private List<Empresa> empresaList;

    public EmpresaSpinnerAdapter(Context context, List<Empresa> empresas) {
        this.empresaList = empresas;
        this.context = context;
    }

    public int getCount() {
        return empresaList.size();
    }

    @Override
    public Empresa getItem(int position) {
        return empresaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setText(empresaList.get(position).getEmpresa_nome());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(context);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setTextColor(Color.parseColor("#000000"));
        txt.setText(empresaList.get(i).getEmpresa_nome());
        return txt;
    }
}