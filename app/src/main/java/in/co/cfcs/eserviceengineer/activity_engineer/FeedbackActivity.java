package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.utils.SimpleSpanBuilder;

public class FeedbackActivity extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppFeedBack";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppFeedBack";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    EditText txt_subject, txt_message;
    Button btn_submit_feedback;

    String subject, message;

    LinearLayout maincontainer;

    TextView tv_subject, tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tv_subject = findViewById(R.id.tv_subject);
        tv_message = findViewById(R.id.tv_message);

        SimpleSpanBuilder ssbSubject = new SimpleSpanBuilder();
        ssbSubject.appendWithSpace("Subject");
        ssbSubject.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_subject.setText(ssbSubject.build());

        SimpleSpanBuilder ssbMessage = new SimpleSpanBuilder();
        ssbMessage.appendWithSpace("Message");
        ssbMessage.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_message.setText(ssbMessage.build());

        txt_subject = findViewById(R.id.txt_subject);
        txt_message = findViewById(R.id.txt_message);
        btn_submit_feedback = findViewById(R.id.btn_submit_feedback);
        maincontainer = findViewById(R.id.maincontainer);

        btn_submit_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config_Engg.isOnline(FeedbackActivity.this);
                if (Config_Engg.internetStatus == true) {

                    if (txt_subject.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Subject ",
                                FeedbackActivity.this);
                        txt_subject.requestFocus();
                        //focusOnView();
                    } else if (txt_message.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Message ",
                                FeedbackActivity.this);
                        txt_message.requestFocus();
                    } else {

                        subject = txt_subject.getText().toString();
                        message = txt_message.getText().toString();
                        new FeedbackActivityAsy().execute();
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", FeedbackActivity.this);
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_password:
                Intent intent;
                intent = new Intent(FeedbackActivity.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(FeedbackActivity.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(FeedbackActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();

                return (true);
            case R.id.profile:
                intent = new Intent(FeedbackActivity.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(FeedbackActivity.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(FeedbackActivity.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_machines:
                intent = new Intent(FeedbackActivity.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(FeedbackActivity.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(FeedbackActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FeedbackActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private class FeedbackActivityAsy extends AsyncTask<String, String, String> {

        String jsonValue, status, id;
        int flag;
        String msgstatus;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FeedbackActivity.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(FeedbackActivity.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(FeedbackActivity.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            request.addProperty("EngineerID", EngineerID);
            request.addProperty("Subject", subject);
            request.addProperty("Message", message);
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
                    if (jsonObject.has("status")) {
                        LoginStatus = jsonObject.getString("status");
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {

                            flag = 4;
                        } else {

                            flag = 1;
                        }
                    } else {
                        msgstatus = jsonObject.getString("MsgNotification");
                        flag = 2;
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, FeedbackActivity.this);
                Intent i = new Intent(FeedbackActivity.this, DashboardActivity.class);
                startActivity(i);
            } else if (flag == 2) {
                Config_Engg.toastShow(msgstatus, FeedbackActivity.this);
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", FeedbackActivity.this);
            } else if (flag == 4) {
                Config_Engg.toastShow(msgstatus, FeedbackActivity.this);
                Config_Engg.logout(FeedbackActivity.this);
                Config_Engg.putSharedPreferences(FeedbackActivity.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                ScanckBar();
                btn_submit_feedback.setEnabled(false);
                progressDialog.dismiss();
            }
            btn_submit_feedback.setClickable(true);
            progressDialog.dismiss();
        }
    }

    private void ScanckBar() {

        Snackbar snackbar = Snackbar
                .make(maincontainer, "Connectivity issues", Snackbar.LENGTH_LONG)
                .setDuration(60000)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btn_submit_feedback.setEnabled(true);
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }

}
