package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.utils.SimpleSpanBuilder;

public class NewContact extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerCustomerContactPersonAdd";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerCustomerContactPersonAdd";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerComplaintAddInitialData";
    private static String METHOD_NAME2 = "AppEngineerComplaintAddInitialData";

    private static String SOAP_ACTION3 = "http://cfcs.co.in/AppEngineerddlSite";
    private static String METHOD_NAME3 = "AppEngineerddlSite";

    EditText txt_name, txt_designation, txt_mobile, txt_mail, txt_login_user_name, txt_other_contact, txt_country_code;

    Spinner spinner_customer_name, spinner_plant;

    Button btn_submit_contact, btn_update_contact;

    String ContactPersonId, ContactPersonName, Designation, PhoneNo, EmailID, LoginUserName, OtherContact, CountryCode;

    String SelectedPlantID = "";

    List<String> customerIDList;
    List<String> customerNameList;

    List<String> plantID;
    List<String> plantName;

    ArrayAdapter<String> spinneradapterCustomer;
    ArrayAdapter<String> spinneradapterPlant;

    String SelctedCustomerID;

    String isEditDelete = "";

    String ComapareParentCustomerID = "";
    String CompareCustomerID = "";

    LinearLayout maincontainer;

    TextView tv_customer_name, tv_plant, tv_name, tv_country_code, tv_login_user_name;

    boolean changing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_plant = findViewById(R.id.tv_plant);
        tv_name = findViewById(R.id.tv_name);
        tv_country_code = findViewById(R.id.tv_country_code);
        tv_login_user_name = findViewById(R.id.tv_login_user_name);

        SimpleSpanBuilder ssbCustomer = new SimpleSpanBuilder();
        ssbCustomer.appendWithSpace("Customer Name");
        ssbCustomer.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_customer_name.setText(ssbCustomer.build());

        SimpleSpanBuilder ssbPlant = new SimpleSpanBuilder();
        ssbPlant.appendWithSpace("Plant");
        ssbPlant.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_plant.setText(ssbPlant.build());

        SimpleSpanBuilder ssbName = new SimpleSpanBuilder();
        ssbName.appendWithSpace("Name");
        ssbName.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_name.setText(ssbName.build());

        SimpleSpanBuilder ssbCountryName = new SimpleSpanBuilder();
        ssbCountryName.appendWithSpace("Country Code + Mobile No ");
        ssbCountryName.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_country_code.setText(ssbCountryName.build());

        SimpleSpanBuilder ssbLoginUserName = new SimpleSpanBuilder();
        ssbLoginUserName.appendWithSpace("Login User Name");
        ssbLoginUserName.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_login_user_name.setText(ssbLoginUserName.build());

        spinner_customer_name = findViewById(R.id.spinner_customer_name);
        spinner_plant = findViewById(R.id.spinner_plant);
        txt_name = findViewById(R.id.txt_name);
        txt_designation = findViewById(R.id.txt_designation);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_mail = findViewById(R.id.txt_mail);
        txt_login_user_name = findViewById(R.id.txt_login_user_name);
        txt_other_contact = findViewById(R.id.txt_other_contact);
        txt_country_code = findViewById(R.id.txt_country_code);
        btn_submit_contact = findViewById(R.id.btn_submit_contact);
        btn_update_contact = findViewById(R.id.btn_update_contact);
        maincontainer = findViewById(R.id.maincontainer);

        ContactPersonId = "00000000-0000-0000-0000-000000000000";
        btn_update_contact.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ContactPersonId = getIntent().getExtras().getString("ContactPersonId");
            isEditDelete = getIntent().getExtras().getString("IsEditDelete");

        }

        if (isEditDelete.compareTo("true") == 0) {
            btn_submit_contact.setVisibility(View.GONE);
            btn_update_contact.setVisibility(View.VISIBLE);

            Config_Engg.isOnline(NewContact.this);
            if (Config_Engg.internetStatus == true) {

                new FillContactData().execute();

            } else {
                Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewContact.this);
            }

        }

        Config_Engg.isOnline(NewContact.this);
        if (Config_Engg.internetStatus == true) {

            new AddInitialData().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewContact.this);
        }

        txt_country_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (!changing && txt_country_code.getText().toString().startsWith("0")){
                    changing = true;
                    txt_country_code.setText(txt_country_code.getText().toString().replace("0", ""));
                }
                changing = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });


        btn_submit_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config_Engg.isOnline(NewContact.this);
                if (Config_Engg.internetStatus == true) {

                    int customerPos = spinner_customer_name.getSelectedItemPosition();
                    int plantPos = spinner_plant.getSelectedItemPosition();

                    if (customerPos == 0) {
                        Config_Engg.alertBox("Please Select Customer",
                                NewContact.this);
                        //focusOnView();
                    } else if (plantPos == 0) {
                        Config_Engg.alertBox("Please Select Plant",
                                NewContact.this);
                    } else if (txt_name.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Name ",
                                NewContact.this);
                        txt_name.requestFocus();
                    } else if (txt_country_code.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Country Code", NewContact.this);
                        txt_country_code.requestFocus();
                    } else if (!isValidMobile(txt_mobile.getText().toString().trim())) {
                        Config_Engg.alertBox("Please Enter Valid Mobile No",
                                NewContact.this);
                        txt_mobile.requestFocus();
                    } else if (!txt_mail.getText().toString().equalsIgnoreCase("") && !isValidMail(txt_mail.getText().toString())) {
                        Config_Engg.alertBox("Please Enter Vaild Email",
                                NewContact.this);
                        txt_mail.requestFocus();
                    }else if (txt_login_user_name.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Login User Name",
                                NewContact.this);
                        txt_login_user_name.requestFocus();
                    } else {

                        long SelectedPlant = spinner_plant.getSelectedItemId();
                        SelectedPlantID = plantID.get((int) SelectedPlant);

                        ContactPersonName = txt_name.getText().toString().trim();
                        Designation = txt_designation.getText().toString().trim();
                        PhoneNo = txt_mobile.getText().toString().trim();
                        EmailID = txt_mail.getText().toString();
                        LoginUserName = txt_login_user_name.getText().toString().trim();
                        OtherContact = txt_other_contact.getText().toString();
                        CountryCode = txt_country_code.getText().toString().trim();

                        new AddNewContactAsy().execute();
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewContact.this);
                }
            }
        });


        btn_update_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config_Engg.isOnline(NewContact.this);
                if (Config_Engg.internetStatus == true) {

                    int customerPos = spinner_customer_name.getSelectedItemPosition();
                    int plantPos = spinner_plant.getSelectedItemPosition();

                    if (customerPos == 0) {
                        Config_Engg.alertBox("Please Select Customer",
                                NewContact.this);
                        //focusOnView();
                    } else if (plantPos == 0) {
                        Config_Engg.alertBox("Please Select Plant",
                                NewContact.this);
                    } else if (txt_name.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Name ",
                                NewContact.this);
                        txt_name.requestFocus();
                    }else if (txt_country_code.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Country Code", NewContact.this);
                        txt_country_code.requestFocus();
                    } else if (!isValidMobile(txt_mobile.getText().toString().trim())) {
                        Config_Engg.alertBox("Please Enter Mobile No",
                                NewContact.this);
                        txt_mobile.requestFocus();
                    } else if (!txt_mail.getText().toString().equalsIgnoreCase("") && !isValidMail(txt_mail.getText().toString())) {
                        Config_Engg.alertBox("Please Enter Vaild Email",
                                NewContact.this);
                        txt_mail.requestFocus();
                    }else if (txt_login_user_name.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Login User Name",
                                NewContact.this);
                        txt_login_user_name.requestFocus();
                    } else {

                        long SelectedPlant = spinner_plant.getSelectedItemId();
                        SelectedPlantID = plantID.get((int) SelectedPlant);

                        ContactPersonName = txt_name.getText().toString();
                        Designation = txt_designation.getText().toString();
                        PhoneNo = txt_mobile.getText().toString();
                        EmailID = txt_mail.getText().toString();
                        LoginUserName = txt_login_user_name.getText().toString();
                        OtherContact = txt_other_contact.getText().toString();
                        CountryCode = txt_country_code.getText().toString().trim();

                        new AddNewContactAsy().execute();
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewContact.this);
                }

            }
        });


        spinner_customer_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(NewContact.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedCustomer = parent.getSelectedItemId();
                    SelctedCustomerID = customerIDList.get((int) SelectedCustomer);
                    if (SelectedCustomer != 0) {
                        new AddSitePlant().execute();

                    } else {

                        plantID = new ArrayList<String>();
                        plantID.add(0, "00000000-0000-0000-0000-000000000000");
                        plantName = new ArrayList<String>();
                        plantName.add(0, "Select");

                        ArrayAdapter<String> spinneradapterMachine = new ArrayAdapter<String>(NewContact.this,
                                android.R.layout.simple_spinner_item, plantName);
                        spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_plant.setAdapter(spinneradapterMachine);
                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewContact.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() != 10) {
                // if(phone.length() != 10) {
                check = false;
                //       Config_Engg.toastShow("Not Valid Number",RaiseComplaintActivity.this);
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if (!check) {
            //       Config_Engg.toastShow("Not Valid Email", RaiseComplaintActivity.this);
        }
        return check;
    }

    private class FillContactData extends AsyncTask<String, String, String> {

        private String SOAP_ACTION = "http://cfcs.co.in/AppEngineerCustomerContactPersonDetail";
        private String NAMESPACE = "http://cfcs.co.in/";
        private String METHOD_NAME = "AppEngineerCustomerContactPersonDetail";
        private String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

        int flag;
        String msgstatus;
        String contact_detail_value;
        ProgressDialog progressDialog;
        JSONArray contactDetail;


        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewContact.this, "Loading...", "Please wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("ContactPersonId", ContactPersonId);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    contact_detail_value = result.getProperty(0).toString();

                    // JSONObject jsonObject = new JSONObject(machine_detail_value);
                    contactDetail = new JSONArray(contact_detail_value);
                    JSONObject jsonObject = contactDetail.getJSONObject(0);
                    // machineDetail = jsonObject.toString();


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
            return contact_detail_value;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, NewContact.this);
            } else if (flag == 2) {
                try {
                    JSONArray jsonArray = new JSONArray(contact_detail_value);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    CompareCustomerID = jsonObject.getString("CustomerID").toString();

                    ComapareParentCustomerID = jsonObject.getString("ParentCustomerID").toString();

                    String ContactPersonName = jsonObject.getString("ContactPersonName").toString();
                    txt_name.setText(ContactPersonName);

                    String Email = jsonObject.getString("Email").toString();
                    txt_mail.setText(Email);

                    String Phone = jsonObject.getString("Phone").toString();
                    txt_mobile.setText(Phone);

                    String Designation = jsonObject.getString("Designation").toString();
                    txt_designation.setText(Designation);

                    String LoginUserName = jsonObject.getString("LoginUserName").toString();
                    txt_login_user_name.setText(LoginUserName);

                    String OtherContact = jsonObject.getString("OtherPhoneNo").toString();
                    txt_other_contact.setText(OtherContact);

                    String CountryCode = jsonObject.getString("CountryCode").toString();
                    txt_country_code.setText(CountryCode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewContact.this);
            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, NewContact.this);
                Config_Engg.logout(NewContact.this);
                Config_Engg.putSharedPreferences(NewContact.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {
                ScanckBar();
                btn_submit_contact.setEnabled(false);
                btn_update_contact.setEnabled(false);
                progressDialog.dismiss();
            }
            btn_submit_contact.setClickable(true);
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
                        if (isEditDelete.compareTo("true") == 0) {
                            btn_submit_contact.setVisibility(View.GONE);
                            btn_update_contact.setVisibility(View.VISIBLE);

                            Config_Engg.isOnline(NewContact.this);
                            if (Config_Engg.internetStatus == true) {

                                new FillContactData().execute();

                                btn_update_contact.setEnabled(true);

                            } else {
                                Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewContact.this);
                            }


                        }

                        btn_submit_contact.setEnabled(true);

                        Config_Engg.isOnline(NewContact.this);
                        if (Config_Engg.internetStatus == true) {

                            new AddInitialData().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewContact.this);
                        }
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }

    private class AddNewContactAsy extends AsyncTask<String, String, String> {

        String jsonValue, status, id;
        int flag;
        String msgstatus;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewContact.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("ContactPersonId", ContactPersonId);
            request.addProperty("CustomerID", SelectedPlantID);
            request.addProperty("ContactPersonName", ContactPersonName);
            request.addProperty("Designation", Designation);
            request.addProperty("EmailID", EmailID);
            request.addProperty("PhoneNo", PhoneNo);
            request.addProperty("OtherPhoneNo", OtherContact);
            request.addProperty("LoginUserName", LoginUserName);
            request.addProperty("CountryCode", CountryCode);
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
                Config_Engg.toastShow(msgstatus, NewContact.this);
                spinner_customer_name.setSelection(0);
                spinner_plant.setSelection(0);
                txt_name.setText("");
                txt_designation.setText("");
                txt_mail.setText("");
                txt_mobile.setText("");
                txt_login_user_name.setText("");
                txt_other_contact.setText("");
                txt_country_code.setText("");
                spinner_customer_name.requestFocus();

                Intent i = new Intent(NewContact.this, ManageContact.class);
                startActivity(i);
                finish();

            } else if (flag == 2) {
                Config_Engg.toastShow(msgstatus, NewContact.this);
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewContact.this);
            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, NewContact.this);
                Config_Engg.logout(NewContact.this);
                Config_Engg.putSharedPreferences(NewContact.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {
                ScanckBar();
                progressDialog.dismiss();
            }
            btn_submit_contact.setClickable(true);
            progressDialog.dismiss();
        }
    }

    private class AddInitialData extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String initialData, customerList, transactionList, engWorkStatusList;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(NewContact.this, "Loading", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String EngineerID = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "AuthCode", "");
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION2, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    initialData = result.getProperty(0).toString();
                    Object json = new JSONTokener(initialData).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(initialData);
                        JSONArray plantjsonArray = object.getJSONArray("CustomerEngineerWise");
                        customerList = plantjsonArray.toString();
                        if (initialData.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(initialData);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            flag = 2;
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(initialData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (jsonObject.has("status")) {

                            LoginStatus = jsonObject.getString("status");
                            msgstatus = jsonObject.getString("MsgNotification");
                            if (LoginStatus.equals(invalid)) {

                                flag = 4;
                            } else {

                                flag = 1;
                            }
                        }

                    }


                } else {
                    JSONArray jsonArray = new JSONArray(initialData);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    msgstatus = jsonObject.getString("MsgNotification");
                    flag = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = 5;
            }
            return null;
        }


        @Override
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, NewContact.this);
            } else if (flag == 2) {
                try {
//
                    JSONArray jsonArray2 = new JSONArray(customerList);
                    customerIDList = new ArrayList<String>();
                    customerIDList.add(0, "00000000-0000-0000-0000-000000000000");
                    customerNameList = new ArrayList<String>();
                    customerNameList.add(0, "Select");


                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String CustomerID = jsonObject2.getString("CustomerID");
                        String CustomerName = jsonObject2.getString("CustomerName");


                        customerIDList.add(i + 1, CustomerID);
                        //siteNameList.add(SiteName);
                        customerNameList.add(i + 1, CustomerName);
                    }

                    spinneradapterCustomer = new ArrayAdapter<String>(NewContact.this, android.R.layout.simple_spinner_item, customerNameList);
                    spinneradapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_customer_name.setAdapter(spinneradapterCustomer);


                    int index = -1;
                    for (int i = 0; i < customerIDList.size(); i++) {
                        if (customerIDList.get(i).equals(ComapareParentCustomerID)) {
                            index = i;
                            break;
                        }
                    }

                    if (index > 0) {

                        String plantString = customerNameList.get((int) index);
                        if (!plantString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterCustomer.getPosition(plantString);
                            spinner_customer_name.setSelection(spinnerpos);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewContact.this);
//                fillListDialog.dismiss();
                finish();
            } else {
                if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, NewContact.this);
                    Config_Engg.logout(NewContact.this);
                    Config_Engg.putSharedPreferences(NewContact.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {

                    ScanckBar();
                    btn_submit_contact.setEnabled(false);
                    btn_submit_contact.setEnabled(false);
                    progressDialog.dismiss();
                }

            }
            progressDialog.dismiss();
        }
    }

    private class AddSitePlant extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String machine_detail, machine_list;
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;
        int count = 0;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(NewContact.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            String EngineerID = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewContact.this, "pref_Engg", "AuthCode", "");
            String CustomerID = SelctedCustomerID;

            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);
            request.addProperty("CustomerID", CustomerID);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION3, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    machine_detail = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(machine_detail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    machine_list = jsonArray.toString();
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
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, NewContact.this);
            } else if (flag == 2) {
                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(machine_list);
                    plantID = new ArrayList<String>();
                    plantID.add(0, "00000000-0000-0000-0000-000000000000");
                    plantName = new ArrayList<String>();
                    plantName.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        count += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String SiteID = jsonObject2.getString("SiteID");
                        String SiteName = jsonObject2.getString("SiteName");

                        plantID.add(i + 1, SiteID);
                        plantName.add(i + 1, SiteName);
                    }

                    spinneradapterPlant = new ArrayAdapter<String>(NewContact.this,
                            android.R.layout.simple_spinner_item, plantName);
                    spinneradapterPlant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_plant.setAdapter(spinneradapterPlant);

                    int index = -1;
                    for (int i = 0; i < plantID.size(); i++) {
                        if (plantID.get(i).equals(ComapareParentCustomerID)) {
                            index = i;
                            break;
                        }
                    }

                    if (index > 0) {

                        String plantString = plantName.get((int) index);
                        if (!plantString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterPlant.getPosition(plantString);
                            spinner_plant.setSelection(spinnerpos);
                        }
                    } else {
                        if (count == 1) {

                            spinner_plant.setSelection(1);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewContact.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, NewContact.this);
                Config_Engg.logout(NewContact.this);
                Config_Engg.putSharedPreferences(NewContact.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {
                ScanckBar();
                btn_submit_contact.setEnabled(false);
                btn_update_contact.setEnabled(false);
                progressDialog.dismiss();
            }

            progressDialog.dismiss();
        }
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
                intent = new Intent(NewContact.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(NewContact.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(NewContact.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(NewContact.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(NewContact.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(NewContact.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(NewContact.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(NewContact.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(NewContact.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewContact.this, ManageContact.class);
        // intent.putExtra("status", status);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
