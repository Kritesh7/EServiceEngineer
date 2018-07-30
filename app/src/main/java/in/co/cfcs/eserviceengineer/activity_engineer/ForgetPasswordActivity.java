package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.LoginActivity;
import in.co.cfcs.eserviceengineer.R;

public class ForgetPasswordActivity extends Activity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerForgetPassword";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerForgetPassword";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    Button btn_back, btn_forgetpassword;
    EditText txt_user_name;
    ProgressDialog progressDialog;
    RelativeLayout maincontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        btn_back = findViewById(R.id.btn_back);
        btn_forgetpassword = findViewById(R.id.btn_forgetpassword);
        txt_user_name = findViewById(R.id.txt_user_name);
        maincontainer = findViewById(R.id.maincontainer);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });


        btn_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = txt_user_name.getText().toString();
                if ((userName.compareTo("") == 0) || (userName == null)) {
                    Config_Engg.alertBox("Enter Your User Name", ForgetPasswordActivity.this);
                    txt_user_name.requestFocus();
                } else {
                    btn_forgetpassword.setClickable(false);

                    Config_Engg.isOnline(ForgetPasswordActivity.this);
                    if (Config_Engg.internetStatus == true) {

                        new ForgotPasswordAsync().execute();

                    } else {
                        Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ForgetPasswordActivity.this);
                    }
                }

            }
        });

    }

    private class ForgotPasswordAsync extends AsyncTask<String, String, String> {

        int flag;
        String jsonValue, msgstatus;
        TextView txt_incorrect;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ForgetPasswordActivity.this, "", "Please wait...", true, false, null);
        }

        @Override
        protected String doInBackground(String... strings) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("UserName", txt_user_name.getText().toString());
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
                    if (jsonObject.has("MsgNotification")) {
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (msgstatus.compareTo("success") == 0) {
                            flag = 1;
                        } else {
                            flag = 2;
                        }
                    } else {
                        flag = 4;
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
                Config_Engg.toastShow("Password Reset", ForgetPasswordActivity.this);
                txt_user_name.setText("");
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (flag == 2) {

                    Toast.makeText(getApplicationContext(), msgstatus, Toast.LENGTH_LONG).show();

                } else if (flag == 5) {
                    ScanckBar();
                    btn_forgetpassword.setEnabled(false);
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
            btn_forgetpassword.setClickable(true);
        }
    }

    private void ScanckBar() {

        Snackbar snackbar = Snackbar
                .make(maincontainer, "Connectivity issues", Snackbar.LENGTH_LONG)
                .setDuration(60000)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btn_forgetpassword.setEnabled(true);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
