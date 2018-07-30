package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.regex.Pattern;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.LoginActivity;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.utils.SimpleSpanBuilder;

public class ProfileUpdate extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerMyProfileDetail";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerMyProfileDetail";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerMyProfileUpdate";
    private static String METHOD_NAME2 = "AppEngineerMyProfileUpdate";

    TextView txt_eng_name, txt_region, txt_designation, txt_mail;
    EditText txt_mobile, txt_phone_no, txt_state, txt_city, txt_user_name, txt_address, txt_pin_code;
    Button btn_update;

    LinearLayout maincontainer;

    TextView tv_mobile_no, tv_state, tv_city, tv_user_name, tv_address, tv_pin_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tv_mobile_no = findViewById(R.id.tv_mobile_no);
        tv_state = findViewById(R.id.tv_state);
        tv_city = findViewById(R.id.tv_city);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_address = findViewById(R.id.tv_address);
        tv_pin_code = findViewById(R.id.tv_pin_code);

        SimpleSpanBuilder ssbMobile = new SimpleSpanBuilder();
        ssbMobile.appendWithSpace("Mobile No");
        ssbMobile.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_mobile_no.setText(ssbMobile.build());

        SimpleSpanBuilder ssbState = new SimpleSpanBuilder();
        ssbState.appendWithSpace("State");
        ssbState.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_state.setText(ssbState.build());

        SimpleSpanBuilder ssbCity = new SimpleSpanBuilder();
        ssbCity.appendWithSpace("City");
        ssbCity.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_city.setText(ssbCity.build());

        SimpleSpanBuilder ssbUserName = new SimpleSpanBuilder();
        ssbUserName.appendWithSpace("User Name");
        ssbUserName.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_user_name.setText(ssbUserName.build());

        SimpleSpanBuilder ssbAddress = new SimpleSpanBuilder();
        ssbAddress.appendWithSpace("Address");
        ssbAddress.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_address.setText(ssbAddress.build());

        SimpleSpanBuilder ssbPinCode = new SimpleSpanBuilder();
        ssbPinCode.appendWithSpace("Pin Code");
        ssbPinCode.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_pin_code.setText(ssbPinCode.build());

        txt_eng_name = findViewById(R.id.txt_eng_name);
        txt_region = findViewById(R.id.txt_region);
        txt_designation = findViewById(R.id.txt_designation);
        txt_mail = findViewById(R.id.txt_mail);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_phone_no = findViewById(R.id.txt_phone_no);
        txt_state = findViewById(R.id.txt_state);
        txt_city = findViewById(R.id.txt_city);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_address = findViewById(R.id.txt_address);
        txt_pin_code = findViewById(R.id.txt_pin_code);
        btn_update = findViewById(R.id.btn_update);

        maincontainer = findViewById(R.id.maincontainer);

        Config_Engg.isOnline(ProfileUpdate.this);
        if (Config_Engg.internetStatus == true) {

            new ProfileAsy().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ProfileUpdate.this);
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "", mobileNo = "", state = "", city = "", userName = "", pinCode = "";
                address = txt_address.getText().toString().trim();
                state = txt_state.getText().toString().trim();
                city = txt_city.getText().toString().trim();
                userName = txt_user_name.getText().toString().trim();
                pinCode = txt_pin_code.getText().toString().trim();
                mobileNo = String.valueOf(isValidMobile(txt_mobile.getText().toString().trim()));
                if ((address.compareTo("") != 0) && (state.compareTo("") != 0) && (city.compareTo("") != 0) && (userName.compareTo("") != 0) && (pinCode.compareTo("") != 0) && (mobileNo.compareTo("") != 0) && isValidMobile(txt_mobile.getText().toString().trim())) {
                    btn_update.setClickable(false);

                    Config_Engg.isOnline(ProfileUpdate.this);
                    if (Config_Engg.internetStatus == true) {

                        new ProfileUpdateAsy().execute();

                    } else {
                        Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ProfileUpdate.this);
                    }
                } else {
                    if (TextUtils.isEmpty(address)) {
                        Config_Engg.alertBox("Please Enter Address", ProfileUpdate.this);
                        txt_address.requestFocus();
//                        focusOnView();

                    } else if (TextUtils.isEmpty(state)) {

                        Config_Engg.alertBox("Please Enter State", ProfileUpdate.this);
                        txt_state.requestFocus();
                    } else if (TextUtils.isEmpty(city)) {
                        Config_Engg.alertBox("Please Enter City", ProfileUpdate.this);
                        txt_city.requestFocus();
                    } else if (TextUtils.isEmpty(pinCode)) {
                        Config_Engg.alertBox("Please Enter Pin Code", ProfileUpdate.this);
                        txt_pin_code.requestFocus();
                    } else if (TextUtils.isEmpty(mobileNo)) {
                        Config_Engg.alertBox("Please Enter Mobile No", ProfileUpdate.this);
                        txt_mobile.requestFocus();
                    }
                }
            }
        });

    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() != 10) {
                // if(phone.length() != 10) {
                check = false;
                Config_Engg.toastShow("Not Valid Number", ProfileUpdate.this);
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private class ProfileAsy extends AsyncTask<String, String, String> {

        int flag;
        String jsonValue;
        String msgstatus;
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ProfileUpdate.this, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {


            String EngineerID = Config_Engg.getSharedPreferences(
                    ProfileUpdate.this, "pref_Engg", "EngineerID", "");

            String AuthCode = Config_Engg.getSharedPreferences(ProfileUpdate.this,
                    "pref_Engg", "AuthCode", "");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
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
                        flag = 2;
                    }
                } else {
                    flag = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = 5;
            }

            return msgstatus;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, ProfileUpdate.this);
            } else {
                if (flag == 2) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonValue);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String EngineerName = jsonObject.getString("EngineerName").toString();
                        txt_eng_name.setText(EngineerName);
                        String ZoneName = jsonObject.getString("ZoneName").toString();
                        txt_region.setText(ZoneName);
                        String Post = jsonObject.getString("Post").toString();
                        txt_designation.setText(Post);
                        String EmailID = jsonObject.getString("EmailID").toString();
                        txt_mail.setText(EmailID);
                        String MobileNo = jsonObject.getString("MobileNo").toString();
                        txt_mobile.setText(MobileNo);
                        String PhoneNo = jsonObject.getString("PhoneNo").toString();
                        txt_phone_no.setText(PhoneNo);
                        String State = jsonObject.getString("State").toString();
                        txt_state.setText(State);
                        String City = jsonObject.getString("City").toString();
                        txt_city.setText(City);
                        String UserName = jsonObject.getString("UserName").toString();
                        txt_user_name.setText(UserName);
                        String Address = jsonObject.getString("Address").toString();
                        txt_address.setText(Address);
                        String ZipCode = jsonObject.getString("ZipCode").toString();
                        txt_pin_code.setText(ZipCode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (flag == 3) {
                    Config_Engg.toastShow("No Response", ProfileUpdate.this);
                } else if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, ProfileUpdate.this);
                    Intent i = new Intent(ProfileUpdate.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                } else if (flag == 5) {

                    ScanckBar();
                    btn_update.setEnabled(false);
                    progressDialog.dismiss();
                }
            }
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

                        Config_Engg.isOnline(ProfileUpdate.this);
                        if (Config_Engg.internetStatus == true) {

                            new ProfileAsy().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ProfileUpdate.this);
                        }
                        btn_update.setEnabled(true);

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
                intent = new Intent(ProfileUpdate.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(ProfileUpdate.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(ProfileUpdate.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(ProfileUpdate.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(ProfileUpdate.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(ProfileUpdate.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(ProfileUpdate.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(ProfileUpdate.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(ProfileUpdate.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileUpdate.this, DashboardActivity.class);
        startActivity(i);
    }

    private class ProfileUpdateAsy extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        int flag = 0;
        String jsonValue;
        String status = "";
        String msgstatus;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ProfileUpdate.this, "Lodaing", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(ProfileUpdate.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ProfileUpdate.this, "pref_Engg", "AuthCode", "");
            String Address = txt_address.getText().toString().trim();
            String Mobile = txt_mobile.getText().toString().trim();
            String PhoneNo = txt_phone_no.getText().toString().trim();
            String State = txt_state.getText().toString().trim();
            String City = txt_city.getText().toString().trim();
            String Username = txt_user_name.getText().toString().trim();
            String PinCode = txt_pin_code.getText().toString().trim();
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("Address", Address);
            request.addProperty("City", City);
            request.addProperty("State", State);
            request.addProperty("ZipCode", PinCode);
            request.addProperty("PhoneNo", PhoneNo);
            request.addProperty("MobileNo", Mobile);
            request.addProperty("UserName", Username);
            request.addProperty("AuthCode", AuthCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION2, envelope);
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, ProfileUpdate.this);
                Intent i = new Intent(ProfileUpdate.this, DashboardActivity.class);
                startActivity(i);
                finish();
            } else {
                if (flag == 2) {
                    Config_Engg.toastShow(msgstatus, ProfileUpdate.this);

                } else {
                    if (flag == 3) {
                        Config_Engg.toastShow("No Response", ProfileUpdate.this);
                    } else {
                        if (flag == 4) {

                            Config_Engg.toastShow(msgstatus, ProfileUpdate.this);
                            Intent i = new Intent(ProfileUpdate.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else if (flag == 5) {
                            ScanckBar();
                            btn_update.setEnabled(false);

                        }


                    }
                }
            }
            progressDialog.dismiss();
            btn_update.setClickable(true);
            // finish();
        }

    }
}
