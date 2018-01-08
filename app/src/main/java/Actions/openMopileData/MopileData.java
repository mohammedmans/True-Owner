package Actions.openMopileData;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by maged on 6/23/2017.
 */

public class MopileData {

    //-----------------------------------------------------------
    //to call it need

    /*
          try {
            setMobileDataEnabled(getApplicationContext(),true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    */
    //-----------------------------------------------------------
    public void setMobileDataEnabled(Context context, boolean enabled) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
        Method setMobileDataEnabledMethod = null;
        try {
            setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        setMobileDataEnabledMethod.setAccessible(true);

        try {
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
