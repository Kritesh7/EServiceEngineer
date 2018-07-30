package in.co.cfcs.eserviceengineer.broadcastReciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.LoginActivity;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.activity_engineer.ManageComplaint;

public class CustomNotifyAsync extends AsyncTask<String, Integer, String> {


    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerSyncData";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerSyncData";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    int flag;
    String jsonValue;
    String status = "", notifyStatus;
    Context context;
    String EngineerID;
    String AuthCode;

    public CustomNotifyAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {

        EngineerID = Config_Engg.getSharedPreferences(context, "pref_Engg", "EngineerID", "");
        AuthCode = Config_Engg.getSharedPreferences(context, "pref_Engg", "AuthCode", "");
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
        request.addProperty("EngineerID", EngineerID);
        request.addProperty("AuthCode", AuthCode);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            androidHttpTransport.call(SOAP_ACTION1, envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            if (result != null) {
                jsonValue = result.getProperty(0).toString();
                JSONArray jsonArray = new JSONArray(jsonValue);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                if (jsonObject.has("NotificationStatus")) {
                    notifyStatus = jsonObject.getString("NotificationStatus");
                    Log.e("notifyStatus is ", notifyStatus);
                    if (notifyStatus.compareTo("0") == 0) {
                        flag = 1;
                    } else {
                        flag = 2;
                    }
                } else {
                    // jsonValue;
                    flag = 3;
                }
            } else {
                flag = 4;
                // no responce
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception is ", e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (flag == 2) {
            showNotification(context);
        }
    }

    private void showNotification(Context context) {

        String complain = null, authcode = "";
        authcode = Config_Engg.getSharedPreferences(context, "pref_Engg", "AuthCode", "");

        PendingIntent contentIntent;
        if (authcode.compareTo("") == 0) {
            contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), 0);
        } else {
            contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, ManageComplaint.class), 0);
        }

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (notifyStatus.compareTo("1") == 0) {
            complain = notifyStatus + " New Complaint !";
        } else {
            complain = notifyStatus + " New Complaints !";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)

                .setSmallIcon(R.drawable.notifiy_icon)
                .setSound(alarmSound)
                .setContentTitle("New Complaints!")
                .setContentText(complain);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setSound(alarmSound);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
