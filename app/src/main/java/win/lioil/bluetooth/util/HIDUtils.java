package win.lioil.bluetooth.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Copyright@NIO Since 2014
 * CreateTime  : 2023/6/29 14:10
 * Author      : rambo.liu
 * Description :
 */
public class HIDUtils {

    private String TAG = "HIDUtils";
    private static HIDUtils instance;
    private final Context context;
    private BluetoothAdapter mBtAdapter;
    private BluetoothProfile mBluetoothProfile;

    public static HIDUtils getInstance(Context context){
        if(null == instance){
            instance = new HIDUtils(context);
        }
        return instance;
    }

    private HIDUtils(Context context) {
        this.context = context;
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            mBtAdapter.getProfileProxy(context,
                    mListener, 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 配对

     */
    public void pair(BluetoothDevice device) {
        Log.i(TAG, "pair device:"+device);
        Method createBondMethod;
        try {
            createBondMethod = BluetoothDevice.class.getMethod("createBond");
            createBondMethod.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 连接设备
     */
    public void connect(final BluetoothDevice device) {
        Log.i(TAG, "connect device:"+device);
        try {
            //得到BluetoothInputDevice然后反射connect连接设备
            Method method = mBluetoothProfile.getClass().getMethod("connect",
                    new Class[] { BluetoothDevice.class });
            method.invoke(mBluetoothProfile, device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     */
    public void disConnect(BluetoothDevice device) {
        Log.i(TAG, "disConnect device:"+device);
        try {
            if (device != null) {
                Method method = mBluetoothProfile.getClass().getMethod("disconnect",
                        new Class[] { BluetoothDevice.class });
                method.invoke(mBluetoothProfile, device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BluetoothProfile.ServiceListener mListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.i(TAG, "-------mConnectListener onServiceConnected  profile =  " + profile);
            //BluetoothProfile proxy这个已经是BluetoothInputDevice类型了
            try {
                if (profile == 4) {
                    mBluetoothProfile = proxy;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            Log.i(TAG, "mConnectListener onServiceConnected");
        }
    };
}
