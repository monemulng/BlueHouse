package ifg.tcc.bluehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "blueHouse.db";

    private static final String TABLE_HOUSE_DEVICE = "houseDevice";
    private static final String TABLE_PLUGGED_DEVICE = "pluggedDevice";
    private static final String TABLE_DEVICE_SCHEME = "deviceScheme";
    private static final String TABLE_LOG_DATA = "logData";

    // Tabela Device
    private static final String HD_COL_1 = "ID";
    private static final String HD_COL_2 = "NAME";
    private static final String HD_COL_3 = "FUNCTION";
    private static final String HD_COL_4 = "FUNC_ID";
    private static final String HD_COL_5 = "TYPE";
    private static final String HD_COL_6 = "IO";

    // Tabela Plugged Device
    private static final String PD_COL_1 = "_ID";
    private static final String PD_COL_2 = "TYPE_ID";
    private static final String PD_COL_3 = "ADDRESS";
    private static final String PD_COL_4 = "IO";
    private static final String PD_COL_5 = "PAIRED";
    private static final String PD_COL_6 = "STATUS";
    private static final String PD_COL_7 = "ACTIVE";
    private static final String PD_COL_8 = "ANALOG";
    private static final String PD_COL_9 = "ICON";

    // Tabela Pareamento
    private static final String DS_COL_1 = "ID";
    private static final String DS_COL_2 = "INPUT";
    private static final String DS_COL_3 = "OUTPUT";

    // Tabela LOG
    private static final String LD_COL_1 = "ID";
    private static final String LD_COL_2 = "DATA";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Criar as tabelas de dados, uma a uma

        String CREATE_TABLE_HOUSEDEVICE = "CREATE TABLE " + TABLE_HOUSE_DEVICE + " (" +
                HD_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HD_COL_2 + " TEXT," +
                HD_COL_3 + " TEXT," +
                HD_COL_4 + " INTEGER UNIQUE NOT NULL," +
                HD_COL_5 + " INTEGER NOT NULL," +
                HD_COL_6 + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_TABLE_HOUSEDEVICE);

        String CREATE_TABLE_PLUGGEDDEVICE = "CREATE TABLE " + TABLE_PLUGGED_DEVICE + " (" +
                PD_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PD_COL_2 + " INTEGER," +
                PD_COL_3 + " INTEGER UNIQUE NOT NULL," +
                PD_COL_4 + " INTEGER NOT NULL," +
                PD_COL_5 + " INTEGER NOT NULL," +
                PD_COL_6 + " TEXT," +
                PD_COL_7 + " INTEGER NOT NULL," +
                PD_COL_8 + " INTEGER NOT NULL," +
                PD_COL_9 + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_PLUGGEDDEVICE);

        String CREATE_TABLE_DEVICESCHEME = "CREATE TABLE " + TABLE_DEVICE_SCHEME + " (" +
                DS_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DS_COL_2 + " INTEGER NOT NULL," +
                DS_COL_3 + " INTEGER NOT NULL" + ")";
        db.execSQL(CREATE_TABLE_DEVICESCHEME);

        String CREATE_TABLE_LOG_DATA = "CREATE TABLE " + TABLE_LOG_DATA + " (" +
                LD_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LD_COL_2 + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_LOG_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Reseta as tabelas, perde os dados por enquanto
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE_DEVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLUGGED_DEVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICE_SCHEME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG_DATA);
        onCreate(db);
    }

    public boolean addHouseDevice(String name, String func, String func_id, String type, String io)
    {
        // Adiciona um HouseDevice - Tabela padrão dos nossos devices - INTERNA
        SQLiteDatabase db = this.getWritableDatabase();

        // Conteudo do ContentValues neste caso deve ser string para entrar na query do insert
        ContentValues values = new ContentValues();
        values.put(HD_COL_2, name);
        values.put(HD_COL_3, func);
        values.put(HD_COL_4, func_id);
        values.put(HD_COL_5, type);
        values.put(HD_COL_6, io);

        long result = db.insert(TABLE_HOUSE_DEVICE, null, values);
        db.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    public boolean addPluggedDevice(PluggedDevice pluggedDevice)
    {
        // Cria um PluggedDevice, populado quando o master envia os dados [comando Search]
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PD_COL_2, String.valueOf(pluggedDevice.getTypeID()));
        values.put(PD_COL_3, String.valueOf(pluggedDevice.getAddress()));
        values.put(PD_COL_4, String.valueOf(pluggedDevice.getIo()));
        values.put(PD_COL_5, String.valueOf(pluggedDevice.getPaired()));
        values.put(PD_COL_6, String.valueOf(pluggedDevice.getStatus()));
        values.put(PD_COL_7, String.valueOf(pluggedDevice.isActive()));
        values.put(PD_COL_8, String.valueOf(pluggedDevice.getAnalog()));
        values.put(PD_COL_9, String.valueOf(pluggedDevice.getIcon()));

        long result = db.insert(TABLE_PLUGGED_DEVICE, null, values);
        db.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    public boolean addDevice(String address, String name, String analog, String io)
    {
        // Cria um PluggedDevice, populado quando o master envia os dados [comando Search]

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PD_COL_2, name);
        values.put(PD_COL_3, address);
        values.put(PD_COL_4, io);
        values.put(PD_COL_5, "0");
        values.put(PD_COL_6, "0");
        values.put(PD_COL_7, "0");
        values.put(PD_COL_8, analog);
        values.put(PD_COL_9, "0");

        long result = db.insert(TABLE_PLUGGED_DEVICE, null, values);
        db.close();

        if (result != -1)
            return true;
        else
            return false;
    }

    public boolean addDeviceScheme(DeviceScheme deviceScheme)
    {
        // Inclui um DeviceScheme - Um pareamento entre Devices, usa dois endereços de device
        ContentValues values = new ContentValues();
        values.put("input", String.valueOf(deviceScheme.getInput()));
        values.put("output", String.valueOf(deviceScheme.getOutput()));
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(TABLE_DEVICE_SCHEME, null, values);
        db.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    public boolean addLogData(String logData)
    {
        // Inclui um DeviceScheme - Um pareamento entre Devices, usa dois endereços de device
        ContentValues values = new ContentValues();
        values.put(LD_COL_2, String.valueOf(logData));
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(TABLE_LOG_DATA, null, values);
        db.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    public Cursor getAllHouseDevice()
    {
        // Pega toda a tabela de HouseDevice
        String query = "SELECT * FROM " + TABLE_HOUSE_DEVICE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getHouseDevice(int function_id)
    {
        // Pega um HouseDevice da Tablela Interna pelo código de tipo
        String query = "SELECT * FROM " + TABLE_HOUSE_DEVICE + " WHERE " + HD_COL_4 + " = \"" + function_id + "\"";;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAllPluggedDevices()
    {
        // Pega toda a tabela de HouseDevice
        String query = "SELECT * FROM " + TABLE_PLUGGED_DEVICE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public PluggedDevice getPluggedDevice(int address)
    {
        // Pega um PluggedDevice baseado no endereço
        String query = "SELECT * FROM " + TABLE_PLUGGED_DEVICE + " WHERE " + "address" + " = \"" + address + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        db.close();
        PluggedDevice pluggedDevice = new PluggedDevice();

        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            pluggedDevice.setId(Integer.parseInt(cursor.getString(0)));
            pluggedDevice.setTypeID(Integer.parseInt(cursor.getString(1)));
            pluggedDevice.setAddress(Integer.parseInt(cursor.getString(2)));
            pluggedDevice.setIo(Integer.parseInt(cursor.getString(3)));
            pluggedDevice.setPaired(Integer.parseInt(cursor.getString(4)));
            pluggedDevice.setStatus(cursor.getString(5));
            pluggedDevice.setActive(Integer.parseInt(cursor.getString(6)));
            pluggedDevice.setAnalog(Integer.parseInt(cursor.getString(7)));
            pluggedDevice.setIcon(Integer.parseInt(cursor.getString(8)));
        }
        else
        {
            pluggedDevice = null;
        }
        cursor.close();
        return pluggedDevice;
    }

    public Cursor getLogData()
    {
        // Pega todos os Logs, para explorar
        String query = "SELECT * FROM " + TABLE_LOG_DATA;

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(query, null);
    }

    public DeviceScheme getDeviceScheme(int address)
    {
        // Pega um PAREAMENTO de um device, seja como input ou output
        String query = "SELECT * FROM " + TABLE_DEVICE_SCHEME + " WHERE " + "input" + " = \"" + address + "\""+" OR "+"output"+ " = \"" + address + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        DeviceScheme deviceScheme = new DeviceScheme();

        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            deviceScheme.setId(Integer.parseInt(cursor.getString(0)));
            deviceScheme.setInput(Integer.parseInt(cursor.getString(1)));
            deviceScheme.setOutput(Integer.parseInt(cursor.getString(2)));
        }
        else
        {
            deviceScheme = null;
        }
        db.close();
        cursor.close();

        return deviceScheme;
    }

    public boolean updateDevice(String address, String status) {
        // Cria um PluggedDevice, populado quando o master envia os dados [comando Search]

        //PluggedDevice pluggedDevice;
        //pluggedDevice = getPluggedDevice(address);

        ContentValues values = new ContentValues();
        /*
        values.put(PD_COL_2, String.valueOf(pluggedDevice.getTypeID()));
        values.put(PD_COL_3, String.valueOf(pluggedDevice.getAddress()));
        values.put(PD_COL_4, String.valueOf(pluggedDevice.getIo()));
        values.put(PD_COL_5, String.valueOf(pluggedDevice.getPaired()));
        */
        values.put(PD_COL_6, status);
        /*
        values.put(PD_COL_7, String.valueOf(pluggedDevice.isActive()));
        values.put(PD_COL_8, String.valueOf(pluggedDevice.getAnalog()));
        values.put(PD_COL_9, String.valueOf(pluggedDevice.getIcon()));
        // */

        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.update(TABLE_PLUGGED_DEVICE, values, "ADDRESS=" + address, null);
        db.close();
        if (result != -1)
            return true;
        else
            return false;
    }

    public void clearDatabase()
    {
        // Apaga os dados internos das tabelas
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HOUSE_DEVICE);
        db.execSQL("DELETE FROM " + TABLE_PLUGGED_DEVICE);
        db.execSQL("DELETE FROM " + TABLE_DEVICE_SCHEME);
        db.execSQL("DELETE FROM " + TABLE_LOG_DATA);
        db.close();
    }

    public boolean populateDatabase()
    {
        /* Cadastro de DEVICES  --- ultimos dois campos são: é digital? é acionador?
          Verifica se já está populada, se já estiver não precisa popular  */

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_HOUSE_DEVICE;

        Cursor cursor = db.rawQuery(query, null);

        db.close();

        if(cursor != null)
        {
            cursor.close();
            HouseDevice[] preDevice = new HouseDevice[10];
            preDevice[0] = new HouseDevice("Interruptor",
                    "Interruptor multiuso", 1, 1, 1);
            preDevice[1] = new HouseDevice("Lâmpada Relé",
                    "Lampada liga-desliga", 2, 1, 2);
            preDevice[2] = new HouseDevice("Dimmer",
                    "Controle de potencia", 3, 0, 1);
            preDevice[3] = new HouseDevice("Lampada Dimerizada",
                    "Lampada com controle de iluminacao", 4, 0, 0);
            preDevice[4] = new HouseDevice("Sensor de Humidade",
                    "Sensor de humidade padrao", 5, 0, 0);
            preDevice[5] = new HouseDevice("Sensor de Temperatura",
                    "Sensor de temperatura padrao", 6, 0, 0);
            preDevice[6] = new HouseDevice("Sensor Analogico Generico",
                    "Sensor analogico padrao-configurar", 7, 2, 1);
            preDevice[7] = new HouseDevice("Acionador Analogico Generico",
                    "Acionador analogico generico", 8, 0, 1);
            preDevice[8] = new HouseDevice("Sensor Digital Generico",
                    "Sensor digital generico", 9, 1, 0);
            preDevice[9] = new HouseDevice("Acionador Digital Generico(Rele)",
                    "Acionador digital generico(Rele)", 10, 1, 1);

            // Adiciona o vetor na tabela de dados
            for (HouseDevice device: preDevice)
            {
                addHouseDevice(device.getName(),
                        device.getFunction(),
                        String.valueOf(device.getFunction_id()),
                        String.valueOf(device.getType()),
                        String.valueOf(device.getIo()));
            }
        }
        return true;
    }


}