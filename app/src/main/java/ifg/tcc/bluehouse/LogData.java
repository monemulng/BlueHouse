package ifg.tcc.bluehouse;

/**
 * Created by Zeoraima on 10-11-2015.
 */
public class LogData
{
    private int _id;              // ID no banco de dados
    private String data;             // Texto com os dados

    public LogData ()
    {

    }

    public LogData(int _id, String _data)
    {
        this._id = _id;
        this.data = _data;
    }

    public LogData(String _data)
    {
        this.data = _data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
