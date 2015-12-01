package ifg.tcc.bluehouse;

/**
 * Created by Zeoraima on 24-10-2015.
 */

// Categoria dos dispositivos, deve ser populada automaticamente por nós
public class HouseDevice
{
    private int _id;
    private String name;
    private String function;
    private int function_id;
    private int type;
    private int io;

    public HouseDevice() {

    }

    public HouseDevice(int id, String _name, String _function, int _function_id, int _type,
                       int _io)
    {
        this._id = id;
        this.name = _name;
        this.function = _function;
        this.function_id = _function_id;
        this.type = _type;
        this.io = _io;
    }

    public HouseDevice(String _name, String _function, int _function_id, int _type,
                       int _io)
    {
        this.name = _name;
        this.function = _function;
        this.function_id = _function_id;
        this.type = _type;
        this.io = _io;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getFunction_id() {
        return function_id;
    }

    public void setFunction_id(int function_id) {
        this.function_id = function_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIo() {
        return io;
    }

    public void setIo(int io) {
        this.io = io;
    }

}
