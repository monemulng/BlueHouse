package ifg.tcc.bluehouse;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


/**
 * Created by Zeoraima on 29-10-2015.
 */
public class DeviceCursorAdapter extends CursorAdapter {
    private LayoutInflater mInflater;

    public DeviceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void bindView(View view, Context context, Cursor c) {
        String deviceName = c.getString(c.getColumnIndexOrThrow("TYPE_ID"));
        String address = c.getString(c.getColumnIndexOrThrow("ADDRESS"));
        String io = c.getString(c.getColumnIndexOrThrow("IO"));
        String paired = c.getString(c.getColumnIndexOrThrow("PAIRED"));
        String active = c.getString(c.getColumnIndexOrThrow("ACTIVE"));
        String analog = c.getString(c.getColumnIndexOrThrow("ANALOG"));
        String icon = c.getString(c.getColumnIndexOrThrow("ICON"));

        TextView txtDeviceIcon = (TextView) view.findViewById(R.id.txtIcon);
        txtDeviceIcon.setTypeface(Typeface.createFromAsset(context.getAssets(), "fontawesome.ttf"));
        // Incluir simbolos
        // txtDeviceIcon.setText("");

        if (icon != null) {
            txtDeviceIcon.setText(icon);
        }

        TextView txtDeviceName = (TextView) view.findViewById(R.id.txtDeviceName);
        if (deviceName != null) {
            txtDeviceName.setText(deviceName);
        }

        TextView txtAddress= (TextView) view.findViewById(R.id.txtAddress);
        if (address != null) {
            txtAddress.setText(address);
        }

        TextView txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        if (io != null) {
            String information = io+", "+paired+", "+active+", "+analog;
            txtInfo.setText(information);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.devicelayout, parent, false);
    }

}
