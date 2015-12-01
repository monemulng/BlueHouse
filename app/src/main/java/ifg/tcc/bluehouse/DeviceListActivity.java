package ifg.tcc.bluehouse;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceListActivity extends ListActivity {

    private SQLiteDatabase db;
    private CursorAdapter dataSource;

    Button btnLoadDeviceList;
    ListView deviceList;
    DBHandler dbHandler;

    int count = 0, dv1, dv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_device_list);

        deviceList = (ListView) findViewById(R.id.list);

        ArrayAdapter<String> deviceArrayAdapter
                = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        // Cria instância da classe DBHandler, que cuida do banco de dados
        dbHandler = new DBHandler(this);
        Cursor devices = dbHandler.getAllPluggedDevices();

        while (devices.moveToNext()) {
            String deviceName = devices.getString(devices.getColumnIndexOrThrow("TYPE_ID"));
            String address = devices.getString(devices.getColumnIndexOrThrow("ADDRESS"));
            String io = devices.getString(devices.getColumnIndexOrThrow("IO"));
            String paired = devices.getString(devices.getColumnIndexOrThrow("PAIRED"));
            String active = devices.getString(devices.getColumnIndexOrThrow("ACTIVE"));
            String analog = devices.getString(devices.getColumnIndexOrThrow("ANALOG"));
            String icon = devices.getString(devices.getColumnIndexOrThrow("ICON"));
            deviceArrayAdapter.add(address+" - Tipo: "+deviceName+" => IO: "+io+" . Pareado: "+paired+" .\nAtivo:"+active+" . Analog: "+analog+" . Icone:"+icon);
        }
        devices.close();

        setListAdapter(deviceArrayAdapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        // Cria o intent para enviar os dados para o Painel de Controle

        super.onListItemClick(l, v, position, id);

//      Intent i = new Intent(DeviceListActivity.this, MainActivity.class);
//      startActivity(i);
        if (count == 0) {
            dv1 = position+1;
            count++;
        } else {
            dv2 = position+1;
            count = 0;
            Intent returnIntent = new Intent();
            returnIntent.putExtra("dv1",String.valueOf(dv1));
            returnIntent.putExtra("dv2", String.valueOf(dv2));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    // Economizando no Toast
    private void msgPop(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

}