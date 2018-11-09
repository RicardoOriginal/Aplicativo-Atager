package ricardo.com.atager.service;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ricardo.com.atager.R;
import ricardo.com.atager.views.ColaboradorAdicionar;
import ricardo.com.atager.views.DivisaoLista;
import ricardo.com.atager.views.EmpresaLista;
import ricardo.com.atager.views.FrenteLista;
import ricardo.com.atager.views.TurnoLista;
import ricardo.com.atager.views.UnidadeLista;

/**
 * Created by Ricardo on 27/09/2016.
 */

public class Publicas extends AppCompatActivity {

    public static final String ID = "idColaborador",
            NOME = "nomeColaborador",
            MATRICULA = "matricula",
            SENHA = "senha",
            ATIVO = "ativo",
            NIVEL_ID = "nivel_id",
            CARGO = "nivel";
//    public static final String URL_REDE = "http://192.168.1.7:8080/WebService/webresources/";
    public static final String URL_REDE = "http://192.168.0.100:8080/WebService/webresources/";
//    public static final String URL_NET = "http://201.40.15.2:8080/WebService/webresources/";
    public static final String URL_NET = "http://atager.ddns.net:8080/WebService/webresources/";
    public static final String TAG = "Ricardo";
    public static final String TAG_ERRO = "Ricardo erro";
    public static String URL = null;

    public Retrofit retrofit(){
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View currentFocus = activity.getCurrentFocus();
            if (currentFocus != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
    }
}
