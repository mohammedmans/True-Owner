package Actions.SendingSMS;

import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;


/**
 * Created by maged on 6/23/2017.
 */

public class sendingSMS {

//There is propability of unauthorized usage , please check your email for more d etails.
    public Boolean SendSMS(String phone,String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, message, null, null);
        return true;
    }

}
