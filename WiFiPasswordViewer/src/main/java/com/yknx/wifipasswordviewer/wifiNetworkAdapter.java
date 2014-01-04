package com.yknx.wifipasswordviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yknx on 4/01/14.
 */
public class wifiNetworkAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<wifiNetwork> data;
    public wifiNetworkAdapter(Context context, ArrayList<wifiNetwork> data){
        super(context,R.layout.listview_wifi_item,data);
        this.context = context;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.listview_wifi_item, null);



        // Recogemos el TextView para mostrar el nombre y establecemos el
        // nombre.
        TextView nombre = (TextView) item.findViewById(R.id.networkName);
        nombre.setText(data.get(position).getName());
        TextView clave = (TextView) item.findViewById(R.id.networkPass);
        clave.setText(data.get(position).getKey());
        TextView security = (TextView) item.findViewById(R.id.networkSecurity);
        switch (data.get(position).getType()){

            case wep:
                security.setText("WEP");
                break;
            case wpa:
                security.setText("WPA");
                break;
            case open:
                security.setText("Open");
                break;
        }



        // Recogemos el TextView para mostrar el número de celda y lo
        // establecemos.

        // Devolvemos la vista para que se muestre en el ListView.
        return item;
    }
}
