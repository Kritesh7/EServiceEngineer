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

public class ChangePassword extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerChangePassword";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerChangePassword";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    Button btn_submit;
    EditText txt_old_paasword, txt_new_password, txt_confirm_password;

    ProgressDialog progressDialog;
    LinearLayout maincontainer;

    TextView tv_old_pass, tv_new_pass, tv_confirm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tv_old_pass = findViewById(R.id.tv_old_pass);
        tv_new_pass = findViewById(R.id.tv_new_pass);
        tv_confirm_pass = findViewById(R.id.tv_confirm_pass);

        SimpleSpanBuilder ssbOldPass = new SimpleSpanBuilder();
        ssbOldPass.appendWithSpace("Old Password");
        ssbOldPass.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_old_pass.setText(ssbOldPass.build());


        SimpleSpanBuilder ssbNewPass = new SimpleSpanBuilder();
        ssbNewPass.appendWithSpace("New Password");
        ssbNewPass.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_new_pass.setText(ssbNewPass.build());

        SimpleSpanBuilder ssbConfirm = new SimpleSpanBuilder();
        ssbConfirm.appendWithSpace("Confirm Password");
        ssbConfirm.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_confirm_pass.setText(ssbConfirm.build());

        txt_old_paasword = findViewById(R.id.txt_old_password);
        txt_new_password = findViewById(R.id.txt_new_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        btn_submit = findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                String saveoldpass = Config_Engg.getSharedPreferences(ChangePassword.this, "pref_Engg", "Pass", "");
                String oldpassword = txt_old_paasword.getText().toString().trim();
                String newpassword = txt_new_password.getText().toString().trim();
                String Repassword = txt_confirm_password.getText().toString().trim();

                Config_Engg.isOnline(ChangePassword.this);
                if (Config_Engg.internetStatus == true) {

                    if (oldpassword.compareTo("") != 0 && newpassword.compareTo("") != 0 && Repassword.compareTo("") != 0) {
                        if (oldpassword.compareTo(saveoldpass) != 0) {
                            Config_Engg.alertBox("Old Password does not match!", ChangePassword.this);
                            txt_old_paasword.setText("");
                            txt_old_paasword.requestFocus();
                        } else {
                            if (newpassword.compareTo(Repassword) != 0) {
                                Config_Engg.alertBox("New Password and Confirm Password does not match!", ChangePassword.this);
                                txt_confirm_password.setText("");
                                txt_confirm_password.requestFocus();
                            } else {
                                btn_submit.setClickable(false);
                                new ChangePassAsync().execute();
                            }
                        }
                    } else {
                        if (oldpassword.compareTo("") == 0) {
                            Config_Engg.alertBox("Please enter Old Password", ChangePassword.this);
                            txt_old_paasword.requestFocus();
                        } else if (newpassword.compareTo("") == 0) {
                            Config_Engg.alertBox("Please enter New Password", ChangePassword.this);
                            txt_new_password.requestFocus();
                        } else if (Repassword.compareTo("") == 0) {
                            Config_Engg.alertBox("Please enter Confirm Password", ChangePassword.this);
                            txt_confirm_password.requestFocus();
                        } else if (oldpassword.compareTo("") == 0 && newpassword.compareTo("") == 0)
                            Config_Engg.alertBox("Please enter Old Password and New Password", ChangePassword.this);
                        else if (newpassword.compareTo("") == 0 && Repassword.compareTo("") == 0)
                            Config_Engg.alertBox("Please enter New Password and Confirm Password", ChangePassword.this);
                        else if (oldpassword.compareTo("") == 0)
                            Config_Engg.alertBox("Please enter Old Password", ChangePassword.this);
                        else if (newpassword.compareTo("") == 0)
                            Config_Engg.alertBox("Please enter New Password", ChangePassword.this);
                        else
                            Config_Engg.alertBox("Please enter Confirm Password", ChangePassword.this);
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ChangePassword.this);
                }

            }
        });

    }

    private class ChangePassAsync extends AsyncTask<String, Integer, String> {

        int flag = 0;
        String jsonValue;
        String status = "";
        String msgstatus;
        String LoginStatus;
        String invalid = "LoginFailed";
        String valid = "success";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ChangePassword.this, "", "Please wait...", true, false, null);
        }

        @Override
        protected String doInBackground(String... strings) {
            String EngineerID = Config_Engg.getSharedPreferences(ChangePassword.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ChangePassword.this, "pref_Engg", "AuthCode", "");
            String oldpassword = txt_old_paasword.getText().toString().trim();
            String newpassword = txt_new_password.getText().toString().trim();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("OldPassword", oldpassword);
            request.addProperty("NewPassword", newpassword);
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
                        } else if (LoginStatus.equals(valid)) {
                            flag = 2;
                        } else {
                            flag = 1;
                        }
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
                Config_Engg.toastShow(msgstatus, ChangePassword.this);
                progressDialog.dismiss();
            } else {
                if (flag == 2) {
                    Config_Engg.toastShow("Password Changed Successfully", ChangePassword.this);
                    txt_old_paasword.setText("");
                    txt_new_password.setText("");
                    txt_confirm_password.setText("");

                    Config_Engg.logout(ChangePassword.this);
                    finish();
                    Config_Engg.putSharedPreferences(ChangePassword.this, "checklogin", "status", "2");

                } else if (flag == 3) {

                    Config_Engg.toastShow("No Response", ChangePassword.this);

                } else if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, ChangePassword.this);
                    Config_Engg.logout(ChangePassword.this);
                    Config_Engg.putSharedPreferences(ChangePassword.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {
                    ScanckBar();
                    btn_submit.setEnabled(false);
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
            btn_submit.setClickable(true);
            // finish();
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
                intent = new Intent(ChangePassword.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(ChangePassword.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(ChangePassword.this, DashboardActivity.class);
                startActivity(intent);
                finish();

                return (true);
            case R.id.profile:
                intent = new Intent(ChangePassword.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(ChangePassword.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(ChangePassword.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(ChangePassword.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(ChangePassword.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(ChangePassword.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChangePassword.this, DashboardActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
