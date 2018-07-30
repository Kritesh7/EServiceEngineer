package in.co.cfcs.eserviceengineer.broadcastReciever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;


public class CustomerBroadcastReciever extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {


        Config_Engg.isOnline(context);
        if (Config_Engg.internetStatus == true) {

            new CustomNotifyAsync(context).execute();
        } else {
            // Config_Customer.toastShow("No Internet Connection", context);
        }

    }

    public static void startAlarm(Context context) {
        Intent i = new Intent(context, CustomerBroadcastReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 12345, i, 0);
        // We want the alarm to go off 3 seconds from now.
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 3 * 1000;//start 3 seconds after first register.
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 60000, sender);//5 min interval

        Log.e(" Zonal Service Started", "11");

    }

    public static void enableBroadcastReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, CustomerBroadcastReciever.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        //  Toast.makeText(context,  "Enable broadcst receiver", Toast.LENGTH_SHORT).show();
    }


    public static void cancelAlarm(Context context) {
        //	Log.e("Success", "cfcs 1");
        Intent intent = new Intent(context, CustomerBroadcastReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12345, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.e("Success", " 111");
    }


    public static void disableBroadcastReceiver(Context context) {
        ComponentName receiver = new ComponentName(context, CustomerBroadcastReciever.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        // Toast.makeText(context, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();
    }
}

