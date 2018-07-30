package in.co.cfcs.eserviceengineer;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.activity_engineer.DashboardActivity;
import in.co.cfcs.eserviceengineer.activity_engineer.ForgetPasswordActivity;
import in.co.cfcs.eserviceengineer.broadcastReciever.AutoNofity;

public class LoginActivity extends Activity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerLogin";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerLogin";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    EditText txt_user_name, txt_user_pass;
    Button btn_submit;
    TextView forgotPassword;
    RelativeLayout maincontainer;

    String userName, userPass, AuthCode, ClientName, ClientVersion;

    ProgressDialog progressDialog;

    Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize widgets
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_user_pass = findViewById(R.id.txt_user_pass);
        btn_submit = findViewById(R.id.btn_submit);
        forgotPassword = findViewById(R.id.forgotPassword);
        maincontainer = findViewById(R.id.maincontainer);

        String Username = Config_Engg.getSharedPreferences(LoginActivity.this,"pref_Engg","UserName","");
        txt_user_name.setText(Username);
        userName = txt_user_name.getText().toString().trim();
        userPass = txt_user_pass.getText().toString().trim();

        if(userName.compareTo("") != 0){
            txt_user_pass.requestFocus();
        }else {
            txt_user_name.requestFocus();
        }

        //Click Listener
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = txt_user_name.getText().toString().trim();
                userPass = txt_user_pass.getText().toString().trim();

                if (userName.compareTo("") != 0 && userPass.compareTo("") != 0) {

                    Config_Engg.isOnline(LoginActivity.this);
                    if (Config_Engg.internetStatus == true) {
                        btn_submit.setClickable(false);
                        new LoginTask().execute();
                    } else {
                        Config_Engg.toastShow("No Internet Connection", LoginActivity.this);
                    }
                } else {
                    if (userName.compareTo("") == 0 && userPass.compareTo("") == 0) {
                        Config_Engg.alertBox("Please enter Username and Password", LoginActivity.this);

                    } else {
                        if (userName.compareTo("") == 0) {
                            Config_Engg.alertBox("Please enter Username", LoginActivity.this);
                            txt_user_name.requestFocus();
                        } else {
                            Config_Engg.alertBox("Please enter Password", LoginActivity.this);
                            txt_user_name.requestFocus();
                        }
                    }
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public class LoginTask extends AsyncTask<String, String, String> {

        String jsonValue, status, EngineerID, EngineerName, AuthCode;
        int flag;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, "", "Please wait...", true, false, null);
        }

        @Override
        protected String doInBackground(String... params) {
            long randomNumber = (long) ((Math.random() * 9000000) + 1000000);
            AuthCode = String.valueOf(randomNumber);

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("UserName", userName);
            request.addProperty("Password", userPass);
            request.addProperty("AuthCode", AuthCode);

            SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelop.setOutputSoapObject(request);
            envelop.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION1, envelop);
                SoapObject result = (SoapObject) envelop.bodyIn;
                if (result != null) {
                    jsonValue = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(jsonValue);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.has("MsgNotification")) {
                        status = jsonObject.getString("MsgNotification");
                        flag = 1;
                    } else {
                        flag = 2;
                        EngineerID = jsonObject.getString("EngineerID").toString();
                        EngineerName = jsonObject.getString("EngineerName").toString();

                        //Log.e("AppInfo",newAppVersion + AppUrl + AppFileName);
                        Config_Engg.putSharedPreferences(LoginActivity.this, "pref_Engg", "UserName", userName);
                        Config_Engg.putSharedPreferences(LoginActivity.this, "pref_Engg", "Pass", userPass);
                        Config_Engg.putSharedPreferences(LoginActivity.this, "pref_Engg", "EngineerID", EngineerID);
                        Config_Engg.putSharedPreferences(LoginActivity.this, "pref_Engg", "EngineerName", EngineerName);
                        Config_Engg.putSharedPreferences(LoginActivity.this, "pref_Engg", "AuthCode", AuthCode);
                        Config_Engg.putSharedPreferences(LoginActivity.this, "pref_Engg", "PendingFeedback", "1");

                        // Config_Engg.getSharedPreferenceRemove(LoginActivity.this,"pref_Engg","Password");
                    }
                } else {
                    flag = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = 5;

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (flag == 1) {
                Config_Engg.toastShow(status, LoginActivity.this);
                progressDialog.dismiss();
            } else {
                if (flag == 2) {
                    login();
                } else if(flag == 5) {
                    ScanckBar();
                    btn_submit.setEnabled(false);
                    progressDialog.dismiss();
                }else {
                    Config_Engg.toastShow("No Response", LoginActivity.this);
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
            txt_user_name.setText("");
            txt_user_pass.setText("");
            btn_submit.setClickable(true);
        }

    }

    private void ScanckBar() {

        Snackbar snackbar = Snackbar
                .make(maincontainer, "Connectivity issues", Snackbar.LENGTH_LONG)
                .setDuration(60000)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        btn_submit.setEnabled(true);

                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        snackbar.show();

    }

    private void login() {

        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        Config_Engg.toastShow("Login Success", LoginActivity.this);

        startService(new Intent(getBaseContext(), AutoNofity.class));

        finish();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
