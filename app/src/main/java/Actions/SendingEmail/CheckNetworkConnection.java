package Actions.SendingEmail;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by maged on 6/23/2017.
 */

public class CheckNetworkConnection {
    private boolean connect;
    public  boolean checkNetConnection(Context context){
        connect=false;
        ConnectivityManager check = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (int i = 0; i<info.length; i++){
            if (info[i].getState() == NetworkInfo.State.CONNECTED){
                connect=true;
            }
        }
        return connect;
    }

}
