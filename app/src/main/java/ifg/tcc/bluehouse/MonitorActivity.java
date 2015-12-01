package ifg.tcc.bluehouse;

import android.app.Activity;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;

public class MonitorActivity extends Activity implements View.OnClickListener
{

    Button btnVoltar;
    TextView txtTemp,txtUmid;
    Switch swtLuz1;
    DBHandler dbHandler;
    PluggedDevice luz;
    PluggedDevice sensor;
    String[] dados;
    String tempLabel = "Temperatura: ";
    String umidLabel = "Umidade: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtUmid = (TextView) findViewById(R.id.txtUmid);
        swtLuz1 = (Switch) findViewById(R.id.swtLuz1);

        dbHandler = new DBHandler(this);
        Cursor c = dbHandler.getAllPluggedDevices();

        // Cria instância da classe DBHandler, que cuida do banco de dados
        dbHandler = new DBHandler(this);

        Cursor devices = dbHandler.getAllPluggedDevices();

        while (c.moveToNext()) {
            if (c.getInt(1)==2) {
                swtLuz1.setVisibility(View.VISIBLE);
                if ("1".equals(c.getString(5))) {
                    swtLuz1.setChecked(true);
                } else {
                    swtLuz1.setChecked(false);
                }
            }
             else if (c.getInt(1)==7) {
                txtTemp.setVisibility(View.VISIBLE);
                txtUmid.setVisibility(View.VISIBLE);
                dados = c.getString(5).split("%");
                txtUmid.setText(umidLabel+dados[0]+" %");
                txtTemp.setText(tempLabel+dados[1]+" ºC");
            }
        }

    }

    @Override
    public void onClick(View v) {


    }

    // Economizando no Toast
    private void msgPop(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}

