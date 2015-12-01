package ifg.tcc.bluehouse;

/**
 * Created by Zeoraima on 24-10-2015.
 */
public class DeviceScheme
{
    private int _id;                 // ID no banco de dados
    private int input;             // Chave de Plugged Device
    private int output;             // Chave de Plugged Device

    public DeviceScheme ()
    {

    }

    public DeviceScheme(int _id, int _input, int _output)
    {
        this._id = _id;
        this.input = _input;
        this.output = _output;
    }

    public DeviceScheme(int _input, int _output)
    {
        this.input = _input;
        this.output = _output;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
}
