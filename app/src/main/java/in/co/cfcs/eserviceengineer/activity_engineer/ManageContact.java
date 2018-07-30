package in.co.cfcs.eserviceengineer.activity_engineer;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;


import org.apache.http.impl.client.AutoRetryHttpClient;
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

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.adapter.ContactListAdapter;
import in.co.cfcs.eserviceengineer.model.ContactListDataModel;

public class ManageContact extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerCustomerContactPersonList";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerCustomerContactPersonList";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerComplaintAddInitialData";
    private static String METHOD_NAME2 = "AppEngineerComplaintAddInitialData";

    private static String SOAP_ACTION3 = "http://cfcs.co.in/AppEngineerddlSite";
    private static String METHOD_NAME3 = "AppEngineerddlSite";

    private static String SOAP_ACTION6 = "http://cfcs.co.in/AppEngineerddlPrincipalRegionAppStatus";
    private static String METHOD_NAME6 = "AppEngineerddlPrincipalRegionAppStatus";

    ArrayList<ContactListDataModel> contactList = new ArrayList<ContactListDataModel>();

    String[] ContactPersonId;
    String[] CCustomerID;
    String[] SiteAddress;
    String[] ParentCustomerName;
    String[] ContactPersonName;
    String[] Email;
    String[] Phone;
    String[] Designation;
    String[] AddDateText;
    String[] ApproveStatusRemark;
    String[] ApproveStatusName;
    String[] IsEditDelete;
    String[] AddByName;
    String[] LoginUserName;
    String[] OtherContact;

    String customerID;
    String siteID;
    String approveStatusID;
    String SearchStr;

    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;

    RecyclerView recycler_view;

    Spinner spinner_customer_name, spinner_plant, spinner_approval_status;

    EditText txt_name_email;

    List<String> customerIDList;
    List<String> customerNameList;

    List<String> plantID;
    List<String> plantName;

    List<String> appStatusIDList;
    List<String> appStatusNameList;

    ArrayAdapter<String> spinneradapterCustomer;
    ArrayAdapter<String> spinneradapterPlant;
    ArrayAdapter<String> spinneradapterAppStatus;

    String SelctedCustomerID;

    CoordinatorLayout maincontainer;

    AlertDialog.Builder alertDialog;
    AlertDialog dialog;

    Button btn_search_find, btn_search_clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contact);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "SearchStr");

        Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "CustomerID");

        Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "PlantID");

        Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "appStatus");

        recycler_view = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        customerID = "00000000-0000-0000-0000-000000000000";
        siteID = "00000000-0000-0000-0000-000000000000";
        approveStatusID = "0";
        SearchStr = "";

        Config_Engg.isOnline(ManageContact.this);
        if (Config_Engg.internetStatus == true) {

            new ContactListAsy(customerID, siteID, approveStatusID, SearchStr).execute();
            new SetdataInCustomerSpinner().execute();
            new AddRegionPrincipalZoneAppStatus().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageContact.this);
        }


        fab = findViewById(R.id.fab);
        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fabBGLayout = findViewById(R.id.fabBGLayout);

        maincontainer = findViewById(R.id.maincontainer);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSearchPopByFab();

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageContact.this, NewContact.class);
                startActivity(i);
            }
        });
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);

        fabBGLayout.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));

    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabBGLayout.setVisibility(View.GONE);
        fab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);

                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void showSearchPopByFab() {

        //  final EditText txt_complaint_date_from, txt_complaint_date_to;

        alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View convertView = (View) inflater.inflate(R.layout.add_search_contact, null);
        alertDialog.setView(convertView);

        spinner_plant = convertView.findViewById(R.id.spinner_plant);
        spinner_customer_name = convertView.findViewById(R.id.spinner_customer_name);
        spinner_approval_status = convertView.findViewById(R.id.spinner_approval_status);

        txt_name_email = convertView.findViewById(R.id.txt_name_email);

        btn_search_find = convertView.findViewById(R.id.btn_search_find);

        btn_search_clear = convertView.findViewById(R.id.btn_search_clear);

        dialog = alertDialog.create();

//        Config_Engg.isOnline(ManageContact.this);
//        if (Config_Engg.internetStatus == true) {
//
//            new SetdataInCustomerSpinner().execute();
//            new AddRegionPrincipalZoneAppStatus().execute();
//
//        } else {
//            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageContact.this);
//        }

        String ComparedCustomerName = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "CustomerID", "");
        spinner_customer_name.setAdapter(spinneradapterCustomer);

        if (!ComparedCustomerName.equalsIgnoreCase("")) {

            int spinnerPosition = spinneradapterCustomer.getPosition(ComparedCustomerName);
            spinner_customer_name.setSelection(spinnerPosition);

        }

        String ComparedAppStatus = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "appStatus", "");
        spinner_approval_status.setAdapter(spinneradapterAppStatus);

        if (!ComparedAppStatus.equalsIgnoreCase("")) {

            int spinnerPosition = spinneradapterAppStatus.getPosition(ComparedAppStatus);
            spinner_approval_status.setSelection(spinnerPosition);

        }

        String ComparedSerach = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "SearchStr", "");

        txt_name_email.setText(ComparedSerach);

        spinner_customer_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(ManageContact.this);
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

                        ArrayAdapter<String> spinneradapterMachine = new ArrayAdapter<String>(ManageContact.this,
                                android.R.layout.simple_spinner_item, plantName);
                        spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_plant.setAdapter(spinneradapterMachine);
                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageContact.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_search_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String customerID = "00000000-0000-0000-0000-000000000000";
                String siteID = "00000000-0000-0000-0000-000000000000";
                String approveStatusID = "0";
                String SearchStr = "";

                Config_Engg.isOnline(ManageContact.this);
                if (Config_Engg.internetStatus == true) {

                    SearchStr = txt_name_email.getText().toString();

                    long CustomerID = spinner_customer_name.getSelectedItemId();
                    customerID = customerIDList.get((int) CustomerID);

                    long PlantID = spinner_plant.getSelectedItemId();
                    siteID = plantID.get((int) PlantID);

                    long appStatus = spinner_approval_status.getSelectedItemId();
                    approveStatusID = appStatusIDList.get((int) appStatus);

                    contactList.clear();

                    Config_Engg.putSharedPreferences(ManageContact.this, "pref_Engg", "SearchStr", SearchStr);

                    Config_Engg.putSharedPreferences(ManageContact.this, "pref_Engg", "CustomerID", spinner_customer_name.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageContact.this, "pref_Engg", "PlantID", spinner_plant.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageContact.this, "pref_Engg", "appStatus", spinner_approval_status.getSelectedItem().toString());

                    dialog.dismiss();

                    new ContactListAsy(customerID, siteID, approveStatusID, SearchStr).execute();

                    closeFABMenu();

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageContact.this);
                }

            }
        });


        btn_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "SearchStr");

                Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "CustomerID");

                Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "PlantID");

                Config_Engg.getSharedPreferenceRemove(ManageContact.this, "pref_Engg", "appStatus");

                dialog.dismiss();

                Config_Engg.isOnline(ManageContact.this);
                if (Config_Engg.internetStatus == true) {

                    new ContactListAsy(customerID, siteID, approveStatusID, SearchStr).execute();
                    closeFABMenu();
                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageContact.this);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {

        if (isFABOpen) {
            closeFABMenu();
        } else {
            Intent intent = new Intent(ManageContact.this, DashboardActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }

    private class ContactListAsy extends AsyncTask<String, String, String> {

        String msgstatus;
        int flag;
        String statusbtn = "";
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;
        String ContactList = "";
        String CustomerID = "";
        String SiteID = "";
        String ApproveStatusID = "";
        String SearchStr = "";

        public ContactListAsy(String customerID, String siteID, String approveStatusID, String searchStr) {
            this.CustomerID = customerID;
            this.SiteID = siteID;
            this.ApproveStatusID = approveStatusID;
            this.SearchStr = searchStr;
        }

        public ManageContact manageContact;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ManageContact.this, "Loading", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            int count = 0;

            String EngineerID = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            request.addProperty("CustomerID", CustomerID);
            request.addProperty("SiteID", SiteID);
            request.addProperty("ApproveStatusID", approveStatusID);
            request.addProperty("SearchStr", SearchStr);
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
                    ContactList = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(ContactList);
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

                        contactList.clear();

                        ContactPersonId = new String[jsonArray.length()];
                        CCustomerID = new String[jsonArray.length()];
                        SiteAddress = new String[jsonArray.length()];
                        ParentCustomerName = new String[jsonArray.length()];
                        ContactPersonName = new String[jsonArray.length()];
                        Email = new String[jsonArray.length()];
                        Phone = new String[jsonArray.length()];
                        Designation = new String[jsonArray.length()];
                        AddDateText = new String[jsonArray.length()];
                        ApproveStatusRemark = new String[jsonArray.length()];
                        ApproveStatusName = new String[jsonArray.length()];
                        IsEditDelete = new String[jsonArray.length()];
                        AddByName = new String[jsonArray.length()];
                        LoginUserName = new String[jsonArray.length()];
                        OtherContact = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            count += 1;

                            try {
                                JSONObject jsonObject1 = jsonArray
                                        .getJSONObject(i);

                                ContactListDataModel contactListDataModel = new ContactListDataModel(AuthCode,
                                        AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode);
                                contactListDataModel.setContactPersonId(jsonObject1.getString("ContactPersonId").toString());
                                contactListDataModel.setCustomerID(jsonObject1.getString("CustomerID").toString());
                                contactListDataModel.setSiteAddress(jsonObject1.getString("SiteAddress").toString());
                                contactListDataModel.setParentCustomerName(jsonObject1.getString("ParentCustomerName").toString());
                                contactListDataModel.setContactPersonName(jsonObject1.getString("ContactPersonName").toString());
                                contactListDataModel.setEmail(jsonObject1.getString("Email").toString());
                                contactListDataModel.setPhone(jsonObject1.getString("Phone").toString());
                                contactListDataModel.setDesignation(jsonObject1.getString("Designation").toString());
                                contactListDataModel.setAddDateText(jsonObject1.getString("AddDateText").toString());
                                contactListDataModel.setApproveStatusRemark(jsonObject1.getString("ApproveStatusRemark").toString());
                                contactListDataModel.setApproveStatusName(jsonObject1.getString("ApproveStatusName").toString());
                                contactListDataModel.setIsEditDelete(jsonObject1.getString("IsEditDelete").toString());
                                contactListDataModel.setAddByName(jsonObject1.getString("AddByName").toString());
                                contactListDataModel.setLoginUserName(jsonObject1.getString("LoginUserName").toString());
                                contactListDataModel.setOtherContact(jsonObject1.getString("OtherPhoneNo").toString());

                                contactListDataModel.setCounter(String.valueOf(count));

                                // Add this object into the ArrayList myList

                                contactList.add(contactListDataModel);
                                flag = 2;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    flag = 3;
                    // finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error is 1 ", e.toString());
                flag = 5;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (flag == 1) {

                Config_Engg.toastShow(msgstatus, ManageContact.this);
                progressDialog.dismiss();
                recycler_view.setAdapter(null);

            } else {
                if (flag == 2) {

                    recycler_view.setAdapter(new ContactListAdapter(ManageContact.this, contactList));

                } else {
                    if (flag == 3) {
                        Config_Engg.toastShow("No Response", ManageContact.this);
                        //    progressDialog.dismiss();
                    } else {
                        if (flag == 4) {

                            Config_Engg.toastShow(msgstatus, ManageContact.this);
                            Config_Engg.logout(ManageContact.this);
                            Config_Engg.putSharedPreferences(ManageContact.this, "checklogin", "status", "2");
                            finish();
                        } else if (flag == 5) {


                            ScanckBar();
                            recycler_view.setAdapter(null);
                            progressDialog.dismiss();
                        }

                    }
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

                        Config_Engg.isOnline(ManageContact.this);
                        if (Config_Engg.internetStatus == true) {

                            new ContactListAsy(customerID, siteID, approveStatusID, SearchStr).execute();
                            new SetdataInCustomerSpinner().execute();
                            new AddRegionPrincipalZoneAppStatus().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageContact.this);
                        }


                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        snackbar.show();

    }

    private class SetdataInCustomerSpinner extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String initialData, customerList, transactionList, engWorkStatusList;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ManageContact.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String EngineerID = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "AuthCode", "");
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
                        JSONArray trancsonArray = object.getJSONArray("TransactionType");
                        transactionList = trancsonArray.toString();
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
                Config_Engg.toastShow(msgstatus, ManageContact.this);
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

                    spinneradapterCustomer = new ArrayAdapter<String>(ManageContact.this, android.R.layout.simple_spinner_item, customerNameList);
                    spinneradapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageContact.this);
//                fillListDialog.dismiss();
                finish();
            } else {
                if (flag == 4) {
                    Config_Engg.toastShow(msgstatus, ManageContact.this);
                    Config_Engg.logout(ManageContact.this);
                    Config_Engg.putSharedPreferences(ManageContact.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {
                    ScanckBar();
                }
            }
            progressDialog.dismiss();
        }
    }

    private class AddRegionPrincipalZoneAppStatus extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String LoginStatus;
        String invalid = "LoginFailed";
        String RegionPrincipalAppStatus, appStatus, zone;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ManageContact.this, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "AuthCode", "");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION6, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    RegionPrincipalAppStatus = result.getProperty(0).toString();
                    Object json = new JSONTokener(RegionPrincipalAppStatus).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(RegionPrincipalAppStatus);
                        JSONArray principaljsonArray = object.getJSONArray("AppStatus");
                        appStatus = principaljsonArray.toString();

                        if (RegionPrincipalAppStatus.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(RegionPrincipalAppStatus);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            flag = 2;
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(RegionPrincipalAppStatus);
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
                    JSONArray jsonArray = new JSONArray(RegionPrincipalAppStatus);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, ManageContact.this);
            } else if (flag == 2) {
                try {
//
                    JSONArray jsonArray2 = new JSONArray(appStatus);
                    appStatusIDList = new ArrayList<String>();
                    appStatusIDList.add(0, "");
                    appStatusNameList = new ArrayList<String>();
                    appStatusNameList.add(0, "Select");


                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String ApproveStatusID = jsonObject2.getString("ApproveStatusID");
                        String ApproveStatusName = jsonObject2.getString("ApproveStatusName");

                        appStatusIDList.add(i + 1, ApproveStatusID);
                        //siteNameList.add(SiteName);
                        appStatusNameList.add(i + 1, ApproveStatusName);
                    }

                    spinneradapterAppStatus = new ArrayAdapter<String>(ManageContact.this, android.R.layout.simple_spinner_item, appStatusNameList);
                    spinneradapterAppStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                    flag = 5;
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageContact.this);
//                fillListDialog.dismiss();
                finish();
            } else {
                if (flag == 4) {
                    Config_Engg.toastShow(msgstatus, ManageContact.this);
                    Config_Engg.logout(ManageContact.this);
                    Config_Engg.putSharedPreferences(ManageContact.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {

                    ScanckBar();
                    dialog.dismiss();
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
        String ComparedPlantName = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "PlantID", "");

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(ManageContact.this, "Loading", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            String EngineerID = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageContact.this, "pref_Engg", "AuthCode", "");
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
                Config_Engg.toastShow(msgstatus, ManageContact.this);
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

                    spinneradapterPlant = new ArrayAdapter<String>(ManageContact.this,
                            android.R.layout.simple_spinner_item, plantName);
                    spinneradapterPlant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_plant.setAdapter(spinneradapterPlant);

                    if (!ComparedPlantName.equalsIgnoreCase("")) {

                        int spinnerPosition = spinneradapterPlant.getPosition(ComparedPlantName);
                        spinner_plant.setSelection(spinnerPosition);

                    } else if (count == 1) {

                        spinner_plant.setSelection(1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageContact.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, ManageContact.this);
                Config_Engg.logout(ManageContact.this);
                Config_Engg.putSharedPreferences(ManageContact.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {

                dialog.dismiss();
                ScanckBar();
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
                intent = new Intent(ManageContact.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(ManageContact.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(ManageContact.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(ManageContact.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(ManageContact.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(ManageContact.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(ManageContact.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(ManageContact.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(ManageContact.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
}
