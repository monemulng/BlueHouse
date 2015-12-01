package ifg.tcc.bluehouse;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends Activity implements View.OnClickListener {

    Button btnLoadLog;
    ListView logList;
    TextView textView22;

    private SQLiteDatabase db;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        btnLoadLog = (Button) findViewById(R.id.btnLoadLog);
        logList = (ListView) findViewById(R.id.logList);
        textView22 = (TextView) findViewById(R.id.textView22);

        // Cria instância da classe DBHandler, que cuida do banco de dados
        dbHandler = new DBHandler(this);

        btnLoadLog.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Botão carrega log
            case R.id.btnLoadLog:
                //executa consulta geral de todos os registros cadastrados no banco de dados
                ArrayList list = new ArrayList();

                Cursor c = dbHandler.getAllPluggedDevices();
                //Cursor c = dbHandler.getLogData();

                String strmaster = "";
                int n = 1;
                if (c.getCount()>0) {
                    while (c.moveToNext()) {
                        String strGo1 = String.valueOf(n)+": " + c.getString(2)+ " " + c.getString(1) + c.getString(7) + "XX> " + c.getString(5) + c.getString(3) + "\n";
                        //String strGo1 = String.valueOf(n)+": " + c.getString(1) + "\n";
                        strmaster = strmaster + strGo1;
                        n++;
                    }
                    textView22.setText(strmaster);
                } else textView22.setText("Vazio");
                c.close();
/*
                String query = "SELECT * FROM logData";
                db = dbHandler.getWritableDatabase();
                Cursor logs = db.rawQuery(query, null);
                db.close();
                if (logs.getCount() == 0) {
                    msgPop("Nenhum registro encontrado");
                    break;
                }
                if (logs.getCount() > 0) {

                    logs.moveToFirst();
                    while (logs.moveToNext()) {
                        list.add(logs.getString(1));
                    }
                    final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
                    logList.setAdapter(adapter);
                } else {
                    msgPop("Nenhum registro encontrado");
                }
                */

        }
    }

    // Economizando no Toast
    private void msgPop(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

}