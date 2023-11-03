package com.qz.gertec_pos_printer.sku210;

import java.lang.reflect.Method;

import com.topwise.cloudpos.aidl.AidlDeviceService;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class DeviceServiceManager {

    private static final String ACTION_DEVICE_SERVICE = "topwise_cloudpos_device_service";

    @SuppressLint("StaticFieldLeak")
    private static DeviceServiceManager instance;
    private AidlDeviceService mDeviceService;

    public static DeviceServiceManager getInstance() {

        Log.d("FLUTTER", "getInstance()");
        if (null == instance) {
            synchronized (DeviceServiceManager.class) {
                instance = new DeviceServiceManager();
            }
        }
        return instance;
    }


    public void getDeviceService(Context context) {
        if (mDeviceService == null) {
            mDeviceService = AidlDeviceService.Stub.asInterface(getService(context, ACTION_DEVICE_SERVICE));
        }
        Log.i("FLUTTER", "onServiceDisconnected  :  " + mDeviceService);
    }

    private static IBinder getService(Context context, String serviceName) {
        IBinder binder = null;
        try {
            ClassLoader cl = context.getClassLoader();
            Class serviceManager = cl.loadClass("android.os.ServiceManager");
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = serviceManager.getMethod("getService", paramTypes);
            Object[] params = new Object[1];
            params[0] = serviceName;
            binder = (IBinder) get.invoke(serviceManager, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return binder;
    }


    public AidlPrinter getPrintManager(Context context) {
        try {
            getDeviceService(context);
            if (mDeviceService != null) {
                return AidlPrinter.Stub.asInterface(mDeviceService.getPrinter());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

}