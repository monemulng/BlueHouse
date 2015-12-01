package ifg.tcc.bluehouse;

/**
 * Created by Zeoraima on 24-10-2015.
 */

// Dispositivos que estão sendo pareados com o celular, preparar para passar estas info por Blue

public class PluggedDevice
{
    private int _id;                // ID no banco de dados
    private int typeID;             // Categoria do device
    private int address;            // Endereço dentro do master
    private int io;                 // Input / Output
    private int paired;             // Pareado com quantos dispositivos?
    private String status;          // Status do device - ON/OFF/Sensor
    private int active;             // Device ativo ou não?
    private int analog;             // Device é analógico?
    private int icon;               // Icone do device

    public PluggedDevice()
    {

    }

    public PluggedDevice(int id, int _houseDeviceID, int _address, int _io, int _paired,
                         String _status, int _active, int _analog, int _icon)
    {
        this._id = id;
        this.typeID = _houseDeviceID;
        this.address = _address;
        this.io = _io;
        this.paired = _paired;
        this.status = _status;
        this.active = _active;
        this.analog = _analog;
        this.icon = _icon;
    }

    public PluggedDevice(int _houseDeviceID, int _address, int _io, int _paired,
                         String _status, int _active, int _analog, int _icon)
    {
        this.typeID = _houseDeviceID;
        this.address = _address;
        this.io = _io;
        this.paired = _paired;
        this.status = _status;
        this.active = _active;
        this.analog = _analog;
        this.icon = _icon;
    }

    public PluggedDevice(int _houseDeviceID, int _address, int _io, int _analog)
    {
        this.typeID = _houseDeviceID;
        this.address = _address;
        this.io = _io;
        this.paired = 0;
        this.status = "";
        this.active = 0;
        this.analog = _analog;
        this.icon = 0;
    }


    public int getAnalog() {
        return analog;
    }

    public void setAnalog(int analog) {
        this.analog = analog;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getAddress() {
        return this.address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getIo() {
        return io;
    }

    public void setIo(int io) {
        this.io = io;
    }

    public int getPaired() {
        return paired;
    }

    public void setPaired(int paired) {
        this.paired = paired;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int isActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
