package com.matrixhub.vaibhav.merabyke;

public class ModelList {
    String tv_DeviceName, tv_DeviceAddress;

    public ModelList(String tv_DeviceName, String tv_DeviceAddress)
    {
        this.tv_DeviceName = tv_DeviceName;
        this.tv_DeviceAddress = tv_DeviceAddress;
    }


    public String getTv_DeviceName()
    {
        return tv_DeviceName;

    }

    public String getTv_DeviceAddress()
    {
        return tv_DeviceAddress;

    }

    public void setTv_DeviceName
            (
                    String tv_DeviceName)
    {
        this.tv_DeviceName = tv_DeviceName;
    }

    public void setTv_DeviceAddress(String tv_DeviceAddress) {
        this.tv_DeviceAddress = tv_DeviceAddress;
    }
}
