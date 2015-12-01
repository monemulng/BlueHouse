package ifg.tcc.bluehouse;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener
{
    // Variaveis da activity
    TextView labelDebug, txtView2;
    EditText cmdBox;
    Button btnFindMaster, btnSendCommand, btnDisconnect,
            btnFindDevices, btnPairComponents, btnExit,
            btnPopulate, btnControl;
    Switch swtConfig;

    // Variaveis da comunicação Bluetooth
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread receiverThread;
    byte[] readBuffer;
    int readBufferPosition;
    public int statusFlag;
    volatile boolean stopThread;

    // Variáveis do banco de dados
    private SQLiteDatabase db;
    private CursorAdapter dataSource;
    DBHandler dbHandler;

    String play = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendCommand = (Button)findViewById(R.id.btnSendCommand);
        btnFindMaster = (Button)findViewById(R.id.btnFindMaster);
        btnFindDevices = (Button)findViewById(R.id.btnFindDevices);
        btnPairComponents = (Button)findViewById(R.id.btnPairComponents);
        btnDisconnect = (Button)findViewById(R.id.btnDisconnect);
        btnExit = (Button)findViewById(R.id.btnExit);
        btnPopulate = (Button)findViewById(R.id.btnPopulate);
        btnControl = (Button)findViewById(R.id.btnControl);

        swtConfig = (Switch)findViewById(R.id.swtConfig);

        labelDebug = (TextView)findViewById(R.id.labelDebug);

        // APAGAR
        txtView2 = (TextView) findViewById(R.id.textView2);

        cmdBox = (EditText)findViewById(R.id.cmdBox);

        // Grava funções do listener nos botões
        btnSendCommand.setOnClickListener(this);
        btnFindMaster.setOnClickListener(this);
        btnFindDevices.setOnClickListener(this);
        btnPairComponents.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPopulate.setOnClickListener(this);
        btnControl.setOnClickListener(this);


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            msgPop("Não encontrei Bluetooth neste dispositivo.");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        // Cria instância da classe DBHandler, que cuida do banco de dados
        dbHandler = new DBHandler(this);

        // Listener do Switch do modo de configuração
        swtConfig.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    try {
                        sendCMD("100;m;1;");
                        msgPop("Modo Configuração Ativado!");
                    } catch (IOException e) {
                        e.printStackTrace();
                        msgPop("Não possível ativar modo configuração!");
                    }
                } else
                {
                    try {
                        sendCMD("100;m;0;");
                        msgPop("Modo Configuração Desligado!");
                    } catch (IOException e) {
                        e.printStackTrace();
                        msgPop("Não possível desativar modo configuração!");
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // Botão Envia Comando Digitado
            case R.id.btnSendCommand:
                try
                {
                    sendData();
                }
                catch (IOException ex)
                {
                    msgPop("Não consegui enviar o comando..");
                }
                break;

            // Botão Encontra MASTER
            case R.id.btnFindMaster:
                try
                {
                    findBT();
                    openBT();
                }
                catch (IOException ex)
                {
                    msgPop("Não consegui ligar a comunicação..");
                }
                break;

            // Botão Encontra Componentes
            case R.id.btnFindDevices:
                try {
                    statusFlag = 0;
                    sendCMD("100;s;0;");
                    msgPop("Encontrando componentes...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            // Botão Pareador de Componentes
            case R.id.btnPairComponents:
                // Vamos para outra activity tentar parear

                Intent D = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivityForResult(D, 1);

                break;


            // Botão para entrar na Activity que monitora os componentes
            case R.id.btnControl:
                try {
                    statusFlag = 1;
                    sendCMD("100;s;1;");
                    msgPop("Recebendo status...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // */
                //dbHandler.clearDatabase();
                // Activity ainda não gerada

                Intent Monitor = new Intent(MainActivity.this, MonitorActivity.class);
                startActivity(Monitor);

                break;

            // Botão Fecha Conexão
            case R.id.btnDisconnect:
                Intent log = new Intent(MainActivity.this, LogActivity.class);
                startActivity(log);
                /*
                try
                {
                    closeBT();
                    msgPop("Desligando Bluetooth..");
                }
                catch (IOException ex)
                {
                    msgPop("Não consegui desconectar..");
                }*/
                break;

            case R.id.btnExit:
                /*
                try {
                    MainActivity.this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                System.exit(0);
                 */
                dbHandler.clearDatabase();


                break;

            case R.id.btnPopulate:
                if (dbHandler.populateDatabase()) msgPop("Concluido");
                else msgPop("Falha na população");
                break;
        }
    }

    // Verifica se BLUETOOTH está ligado e encontra endereço do MASTER
    // Por enquanto MASTER está direcionado manualmente para o nosso BLUETOOTH
    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            msgPop("Não encontrei Bluetooth neste dispositivo.");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06"))
                {
                    mmDevice = device;
                    msgPop("Encontramos o Master.");
                    break;
                }
            }
        }
    }

    // Abre conexão com o MASTER, liga In e Output, e liga Thread de Espera de sinais
    // FUNÇÃO MAIS IMPORTANTE AQUI
    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        msgPop("COMUNICAÇÃO ABERTA !!!");
    }

    // HANDLER - Recebe e codifica os sinais, preparar para descartá-los
    // FUNÇÃO MAIS SENSÍVEL AQUI
    void beginListenForData()
    {
        final Handler handler = new Handler();
        // código em ASCII para fim de linha '/n' = 10, estudar a melhor opção
        final byte delimiter = 35; // Sinal de hashtag para termino de msgs

        stopThread = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        final DBHandler dbHandler = new DBHandler(this);
        receiverThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    // O APP age aqui quando a mensagem é recebida
                                    // data é o código que será trabalhado
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {

                                        /* *******************************************************************************************
                                             TRABALHAR COM OS DADOS --- PARTE ONDE VAMOS TRABALHAR POR AGORA

                                             senha (;) String(active_pipes) (;) String(i)(??) (;) nomes[i] (;) identificacoes[i] (;) funcoes[i] (!)
                                        ****************************************************************************************** */
                                            //labelDebug.setText(data+"\n"+labelDebug.getText());

                                            dbHandler.addLogData(data);

                                            // Instala os Devices
                                            if (statusFlag == 0) {
                                                // Retira o lixo da mensagem
                                                String[] msgs = data.split("!");
                                                String[] device = msgs[0].split(";");


                                                // Tira a senha
                                                if ("100".equals(device[0])) {
                                                    boolean ok = dbHandler.addDevice(device[2], device[3], device[4], device[5]);
                                                    labelDebug.setText(device[0]);
                                                    if (ok) msgPop("Novo device instalado!");
                                                } else {
                                                    boolean ok = dbHandler.addDevice(device[0], device[1], device[2], device[3]);
                                                    if (ok) msgPop("Novo device instalado!");
                                                }
                                            }
                                            // Recebe os Status dos Devices Instalados
                                            else if (statusFlag ==1)
                                            {
                                                // Cria Log
                                                dbHandler.addLogData(data);
                                                String[] msgs = data.split("!");
                                                String[] device = msgs[0].split(";");
                                                // Grava no banco de dados os novos status
                                                if ("100".equals(device[0]))
                                                {
                                                    if (device.length==4) {
                                                        boolean ok = dbHandler.updateDevice(device[2], device[3]);
                                                    }
                                                }
                                                else
                                                {
                                                    if (device.length==2) {
                                                        boolean ok = dbHandler.updateDevice(device[0], device[1]);
                                                    }
                                                }
                                            }

                                            /* *******************************************************************************************
                                                  FIM DA PARTE DOS DADOS DO BLUETOOTH  -- CRIAR MÉTODOS PRA RESUMIR DEPOIS
                                            ****************************************************************************************** */

                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        receiverThread.start();
    }

    void sendData() throws IOException
    {
        // Envia comandos/dados, envia somente comandos direto da caixa de texto
        String msg = cmdBox.getText().toString();
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        msgPop("Mensagem enviada.");
    }

    public void sendCMD(String command) throws IOException
    {
        // Método que envia comandos para o Master
        command += "\n";
        mmOutputStream.write(command.getBytes());
        // msgPop("Mensagem enviada.");
    }

    // Fechar conexão, sockets e para o Thread, não tem volta, por enquanto..
    void closeBT() throws IOException
    {
        stopThread = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        msgPop("Bluetooth Desconectado.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String dv1 = data.getStringExtra("dv1");
                String dv2 = data.getStringExtra("dv2");
                try {
                    sendCMD("100;g;1;"+dv1+";"+dv2+";");
                    msgPop("mandei : 100;g;1;"+dv1+";"+dv2+";");
                } catch (IOException e) {
                    e.printStackTrace();
                    msgPop("PAREAMENTO ERRADO!");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    // Economizando no Toast
    private void msgPop(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

}