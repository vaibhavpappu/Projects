package com.matrixhub.vaibhav.merabyke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<ModelList> {
    private final Context context;
    private ArrayList<ModelList> data = null;

    public DeviceListAdapter(Context context, ArrayList<ModelList> dataList) {
        super(context, R.layout.device_list);
        this.data = dataList;
        this.context = context;
    }//constructor

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.device_list, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_DeviceName = (TextView) convertView.findViewById(R.id.tvDeviceName);
            viewHolder.tv_DeviceAddress =(TextView)convertView.findViewById(R.id.tvDeviceAddress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //you can use data.get(position) too
        final ModelList myDataItem = getItem(position);

        viewHolder.tv_DeviceName.setText(myDataItem.getTv_DeviceName());
        viewHolder.tv_DeviceAddress.setText(myDataItem.getTv_DeviceAddress());

        return convertView;
    }

    public class ViewHolder {
        TextView tv_DeviceName;
        TextView tv_DeviceAddress;
    }
}
