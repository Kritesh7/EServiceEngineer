package in.co.cfcs.eserviceengineer.activity_engineer;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.adapter.ComplaintListAdapter;
import in.co.cfcs.eserviceengineer.model.ComplaintDataModel;

public class ManageComplaint extends AppCompatActivity {


    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerComplainList";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerComplainList";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerComplaintAddInitialData";
    private static String METHOD_NAME2 = "AppEngineerComplaintAddInitialData";

    private static String SOAP_ACTION3 = "http://cfcs.co.in/AppEngineerddlSite";
    private static String METHOD_NAME3 = "AppEngineerddlSite";

    ArrayList<ComplaintDataModel> complainList = new ArrayList<ComplaintDataModel>();

    String[] ComplainNo;
    String[] ComplaintTitle;
    String[] ComplainDateTimeText;
    String[] ModelName;
    String[] CustomerName;
    String[] SiteAddress;
    String[] PriorityName;
    String[] TransactionTypeName;
    String[] EscalationShortCode;
    String[] WorkStatusName;
    String[] StatusText;
    String[] ComplainByName;
    String[] IsEditDelete;
    String[] IsServiceReportFill;

    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;

    ListView list;

    Spinner spinner_customer, spinner_plant, spinner_work_status, spinner_status;

    List<String> customerIDList;
    List<String> customerNameList;

    List<String> plantID;
    List<String> plantName;

    List<String> engWorkStatusIDList;
    List<String> engWorkStatusNameList;

    List<String> statusIDList;
    List<String> statusNameList;

    ArrayAdapter<String> spinneradapterCustomer;
    ArrayAdapter<String> spinneradapterPlant;
    ArrayAdapter<String> spinneradapterWorkStatus;

    String SelctedCustomerID;

    Calendar myCalendar;

    String status;
    String customerID;
    String siteID;
    String workStatusID;

    EditText txt_complaint_no, txt_complaint_date_to;

    ProgressDialog progressDialog;
    ProgressDialog progressDialogCustomer;

    CoordinatorLayout maincontainer;

    Button btn_search_find, btn_search_clear;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_complaint);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "Customer");

        Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "Plant");

        Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "DateFrom");

        Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "DateUpto");

        Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "ComplainNo");

        Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "Status");

        Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "WorkStatus");

        list = (ListView) findViewById(R.id.complaint_list_view);

        status = "1";
        customerID = "00000000-0000-0000-0000-000000000000";
        siteID = "00000000-0000-0000-0000-000000000000";
        workStatusID = "0";

        Config_Engg.isOnline(ManageComplaint.this);
        if (Config_Engg.internetStatus == true) {

            new ComplainListAsy(status, customerID, siteID, workStatusID).execute();
            new SetdataInCustomerSpinner().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageComplaint.this);
        }


        fab = findViewById(R.id.fab);
        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);
        maincontainer = findViewById(R.id.maincontainer);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fabBGLayout = findViewById(R.id.fabBGLayout);


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
            public void onClick(View view) {
                showSearchPopByFab();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageComplaint.this, RaiseComplaintActivity.class);
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
                intent = new Intent(ManageComplaint.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(ManageComplaint.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(ManageComplaint.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(ManageComplaint.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(ManageComplaint.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(ManageComplaint.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(ManageComplaint.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(ManageComplaint.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(ManageComplaint.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {

        if (isFABOpen) {
            closeFABMenu();
        } else {
            Intent intent = new Intent(ManageComplaint.this, DashboardActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }

    private void showSearchPopByFab() {

        final EditText txt_complaint_no, txt_complaint_date_from, txt_complaint_date_to;

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View convertView = (View) inflater.inflate(R.layout.add_serach_complain, null);
        alertDialog.setView(convertView);

        spinner_plant = convertView.findViewById(R.id.spinner_plant);
        spinner_customer = convertView.findViewById(R.id.spinner_customer);
        spinner_work_status = convertView.findViewById(R.id.spinner_work_status);
        spinner_status = convertView.findViewById(R.id.spinner_status);

        txt_complaint_date_from = convertView.findViewById(R.id.txt_complaint_date_from);
        txt_complaint_date_to = convertView.findViewById(R.id.txt_complaint_date_to);
        txt_complaint_no = convertView.findViewById(R.id.txt_complaint_no);

        btn_search_find = convertView.findViewById(R.id.btn_search_find);

        btn_search_clear = convertView.findViewById(R.id.btn_search_clear);

        dialog = alertDialog.create();

        myCalendar = Calendar.getInstance();

        String ComparedDateFrom = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "DateFrom", "");

        txt_complaint_date_from.setText(ComparedDateFrom);

        String ComparedDateTo = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "DateUpto", "");

        txt_complaint_date_to.setText(ComparedDateTo);

        String ComparedComplainNo = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "ComplainNo", "");

        txt_complaint_no.setText(ComparedComplainNo);

        String ComparedCustomerName = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "Customer", "");
        String ComparedWorkStatusName = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "WorkStatus", "");

        spinner_customer.setAdapter(spinneradapterCustomer);

        if (!ComparedCustomerName.equalsIgnoreCase("")) {

            int spinnerPosition = spinneradapterCustomer.getPosition(ComparedCustomerName);
            spinner_customer.setSelection(spinnerPosition);

        }

        spinner_work_status.setAdapter(spinneradapterWorkStatus);

        if (!ComparedWorkStatusName.equalsIgnoreCase("")) {

            int spinnerPosition = spinneradapterWorkStatus.getPosition(ComparedWorkStatusName);
            spinner_work_status.setSelection(spinnerPosition);

        }

        // DatePicker Listener for Date From
        final DatePickerDialog.OnDateSetListener date_from = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateFrom();
            }

            private void updateDateFrom() {

                Date date1 = new Date(String.valueOf(myCalendar.getTime()));
                String stringDate = DateFormat.getDateInstance().format(date1);
                txt_complaint_date_from.setText(stringDate);

            }

        };


        // DatePicker Listener for Date to
        final DatePickerDialog.OnDateSetListener date_to = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTo();
            }

            private void updateDateTo() {

                Date date1 = new Date(String.valueOf(myCalendar.getTime()));
                String stringDate = DateFormat.getDateInstance().format(date1);
                txt_complaint_date_to.setText(stringDate);

            }

        };


        // DatePicker Dialog Date From
        txt_complaint_date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                new DatePickerDialog(ManageComplaint.this, android.R.style.Theme_Holo_Dialog, date_from, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // DatePicker Dialog Date To
        txt_complaint_date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO Auto-generated method stub
                new DatePickerDialog(ManageComplaint.this, android.R.style.Theme_Holo_Dialog, date_to, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        String ComparedStatus = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "Status", "");


        statusIDList = new ArrayList<String>();
        statusIDList.add(0, "1");
        statusIDList.add(1, "0");
        statusIDList.add(2, "3");
        statusNameList = new ArrayList<String>();
        statusNameList.add(0, "Open");
        statusNameList.add(1, "Completed");
        statusNameList.add(2, "All");


        ArrayAdapter<String> spinneradapterStatus = new ArrayAdapter<String>(ManageComplaint.this,
                android.R.layout.simple_spinner_item, statusNameList);
        spinneradapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(spinneradapterStatus);


        if (!ComparedStatus.equalsIgnoreCase("")) {
            int spinnerpostion = spinneradapterStatus.getPosition(ComparedStatus);
            spinner_status.setSelection(spinnerpostion);

        }

        spinner_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(ManageComplaint.this);
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


                        ArrayAdapter<String> spinneradapterMachine = new ArrayAdapter<String>(ManageComplaint.this,
                                android.R.layout.simple_spinner_item, plantName);
                        spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_plant.setAdapter(spinneradapterMachine);
                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageComplaint.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_search_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String CustomerID = "";
                String PlantID = "";
                String WorkStatusID = "0";
                String StatusID = "";

                Config_Engg.isOnline(ManageComplaint.this);
                if (Config_Engg.internetStatus == true) {

                    String complainNo = txt_complaint_no.getText().toString();

                    long Customer = spinner_customer.getSelectedItemId();
                    CustomerID = customerIDList.get((int) Customer);

                    long saleID = spinner_plant.getSelectedItemId();
                    PlantID = plantID.get((int) saleID);

                    String DateFrom = txt_complaint_date_from.getText().toString();
                    String DateUpto = txt_complaint_date_to.getText().toString();

                    long WorkStatus = spinner_work_status.getSelectedItemId();
                    WorkStatusID = engWorkStatusIDList.get((int) WorkStatus);

                    long statusID = spinner_status.getSelectedItemId();
                    StatusID = statusIDList.get((int) statusID);

                    complainList.clear();

                    Config_Engg.putSharedPreferences(ManageComplaint.this, "pref_Engg", "Customer", spinner_customer.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageComplaint.this, "pref_Engg", "Plant", spinner_plant.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageComplaint.this, "pref_Engg", "DateFrom", DateFrom);

                    Config_Engg.putSharedPreferences(ManageComplaint.this, "pref_Engg", "DateUpto", DateUpto);

                    Config_Engg.putSharedPreferences(ManageComplaint.this, "pref_Engg", "ComplainNo", complainNo);

                    Config_Engg.putSharedPreferences(ManageComplaint.this, "pref_Engg", "WorkStatus", spinner_work_status.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageComplaint.this, "pref_Engg", "Status", spinner_status.getSelectedItem().toString());

                    dialog.dismiss();

                    new ComplainListAsy(CustomerID, complainNo, PlantID, DateFrom, DateUpto, WorkStatusID, StatusID).execute();

                    closeFABMenu();

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageComplaint.this);
                }

            }
        });


        btn_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "Customer");

                Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "Plant");

                Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "DateFrom");

                Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "DateUpto");

                Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "ComplainNo");

                Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "Status");

                Config_Engg.getSharedPreferenceRemove(ManageComplaint.this, "pref_Engg", "WorkStatus");

                Config_Engg.isOnline(ManageComplaint.this);
                if (Config_Engg.internetStatus == true) {

                    dialog.dismiss();
                    new ComplainListAsy(status, customerID, siteID, workStatusID).execute();
                    closeFABMenu();

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageComplaint.this);
                }

            }
        });


        dialog.show();
    }

    private class ComplainListAsy extends AsyncTask<String, String, String> {

        String msgstatus;
        int flag;
        String statusbtn = "";
        String LoginStatus;
        String invalid = "LoginFailed";
        String ComplainList = "";
        String CustomerID = "";
        String SiteID = "";
        String DateFrom = "";
        String DateUpto = "";
        String complainNo = "";
        String status = "";
        String WorkStatusID = "";

        public ManageComplaint manageComplaint;

        public ComplainListAsy(String status, String customerID, String siteID, String workStatusID) {
            this.status = status;
            this.CustomerID = customerID;
            this.SiteID = siteID;
            this.WorkStatusID = workStatusID;
        }

        public ComplainListAsy(String customerID, String complainNo, String plantID, String dateFrom, String dateUpto, String workStatusID, String statusID) {
            this.CustomerID = customerID;
            this.complainNo = complainNo;
            this.SiteID = plantID;
            this.DateFrom = dateFrom;
            this.DateUpto = dateUpto;
            this.WorkStatusID = workStatusID;
            this.status = statusID;
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ManageComplaint.this, "Loading", "Please wait...", true, false, null);
        }

        @Override
        protected String doInBackground(String... status1) {
            // TODO Auto-generated method stub

            // String status = status1[0];
            int count = 0;

            String EngineerID = Config_Engg.getSharedPreferences(
                    ManageComplaint.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(
                    ManageComplaint.this, "pref_Engg", "AuthCode", "");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("CustomerID", CustomerID);
            request.addProperty("SiteID", SiteID);
            request.addProperty("DateFrom", DateFrom);
            request.addProperty("DateUpto", DateUpto);
            request.addProperty("ComplainNo", complainNo);
            request.addProperty("WorkStatusID", WorkStatusID);
            request.addProperty("Status", status);
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
                    ComplainList = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(ComplainList);
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

                        complainList.clear();

                        ComplainNo = new String[jsonArray.length()];
                        ComplaintTitle = new String[jsonArray.length()];
                        ComplainDateTimeText = new String[jsonArray.length()];
                        ModelName = new String[jsonArray.length()];
                        CustomerName = new String[jsonArray.length()];
                        SiteAddress = new String[jsonArray.length()];
                        PriorityName = new String[jsonArray.length()];
                        TransactionTypeName = new String[jsonArray.length()];
                        EscalationShortCode = new String[jsonArray.length()];
                        WorkStatusName = new String[jsonArray.length()];
                        StatusText = new String[jsonArray.length()];
                        ComplainByName = new String[jsonArray.length()];
                        IsEditDelete = new String[jsonArray.length()];
                        IsServiceReportFill = new String[jsonArray.length()];


                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject1 = jsonArray
                                        .getJSONObject(i);

                                ComplaintDataModel complainListDataModel = new ComplaintDataModel(AuthCode, AuthCode, AuthCode,
                                        AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode);
                                complainListDataModel.setComplainNo(jsonObject1.getString("ComplainNo").toString());
                                complainListDataModel.setComplaintTitle(jsonObject1.getString("ComplaintTitle").toString());
                                complainListDataModel.setComplainDateTimeText(jsonObject1.getString("ComplainDateTimeText").toString());
                                complainListDataModel.setModelName(jsonObject1.getString("ModelName").toString());
                                complainListDataModel.setCustomerName(jsonObject1.getString("CustomerName").toString());
                                complainListDataModel.setSiteAddress(jsonObject1.getString("SiteAddress").toString());
                                complainListDataModel.setPriorityName(jsonObject1.getString("PriorityName").toString());
                                complainListDataModel.setTransactionTypeName(jsonObject1.getString("TransactionTypeName").toString());
                                complainListDataModel.setEscalationShortCode(jsonObject1.getString("EscalationShortCode").toString());
                                complainListDataModel.setWorkStatusName(jsonObject1.getString("WorkStatusName").toString());
                                complainListDataModel.setStatusText(jsonObject1.getString("StatusText").toString());
                                complainListDataModel.setComplainByName(jsonObject1.getString("ComplainByName").toString());
                                complainListDataModel.setIsEditDelete(jsonObject1.getString("IsEditDelete").toString());
                                complainListDataModel.setIsServiceReportFill(jsonObject1.getString("IsServiceReportFill").toString());


                                // Add this object into the ArrayList myList

                                complainList.add(complainListDataModel);
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
                flag = 5;
                Log.e("error is 1 ", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (flag == 1) {

                Config_Engg.toastShow(msgstatus, ManageComplaint.this);
                //  progressDialog.dismiss();
                list.setAdapter(null);

            } else {
                if (flag == 2) {
                    list.setAdapter(new ComplaintListAdapter(ManageComplaint.this, complainList, status));
                } else {
                    if (flag == 3) {
                        Config_Engg.toastShow("No Response", ManageComplaint.this);
                        progressDialog.dismiss();
                    } else {
                        if (flag == 4) {

                            Config_Engg.toastShow(msgstatus, ManageComplaint.this);
                            Config_Engg.logout(ManageComplaint.this);
                            Config_Engg.putSharedPreferences(ManageComplaint.this, "checklogin", "status", "2");
                            finish();
                        } else if (flag == 5) {

                            ScanckBar();
                            progressDialog.dismiss();
                            list.setAdapter(null);
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

                        if (Config_Engg.internetStatus == true) {

                            new ComplainListAsy(status, customerID, siteID, workStatusID).execute();
                            new SetdataInCustomerSpinner().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageComplaint.this);
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
        String LoginStatus;
        String invalid = "LoginFailed";


        @Override
        protected void onPreExecute() {
            progressDialogCustomer = ProgressDialog.show(ManageComplaint.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String EngineerID = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "AuthCode", "");
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
                        JSONArray EngWorkStatus = object.getJSONArray("EngWorkStatus");
                        engWorkStatusList = EngWorkStatus.toString();
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
            }
            return null;
        }


        @Override
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, ManageComplaint.this);
            } else if (flag == 2) {
                try {
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

                        customerNameList.add(i + 1, CustomerName);
                    }

                    spinneradapterCustomer = new ArrayAdapter<String>(ManageComplaint.this, android.R.layout.simple_spinner_item, customerNameList);
                    spinneradapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                try {
                    JSONArray jsonArray2 = new JSONArray(engWorkStatusList);
                    engWorkStatusIDList = new ArrayList<String>();
                    engWorkStatusIDList.add(0, "0");
                    engWorkStatusNameList = new ArrayList<String>();
                    engWorkStatusNameList.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String EngWorkStatusID = jsonObject2.getString("EngWorkStatusID");
                        String EngWorkStatus = jsonObject2.getString("EngWorkStatus");

                        engWorkStatusIDList.add(EngWorkStatusID);
                        engWorkStatusNameList.add(EngWorkStatus);
                    }

                    spinneradapterWorkStatus = new ArrayAdapter<String>(ManageComplaint.this, android.R.layout.simple_spinner_item, engWorkStatusNameList);
                    spinneradapterWorkStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                progressDialogCustomer.dismiss();

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageComplaint.this);
                finish();
            } else {
                if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, ManageComplaint.this);
                    Config_Engg.logout(ManageComplaint.this);
                    Config_Engg.putSharedPreferences(ManageComplaint.this, "checklogin", "status", "2");
                    finish();
                }


            }
            progressDialogCustomer.dismiss();
        }
    }

    private class AddSitePlant extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String machine_detail, machine_list;
        String LoginStatus;
        String invalid = "LoginFailed";
        int count = 0;
        String ComparedPlantName = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "Plant", "");

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(ManageComplaint.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            String EngineerID = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageComplaint.this, "pref_Engg", "AuthCode", "");
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
                Config_Engg.toastShow(msgstatus, ManageComplaint.this);
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

                    spinneradapterPlant = new ArrayAdapter<String>(ManageComplaint.this,
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
                Config_Engg.toastShow("No Response", ManageComplaint.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, ManageComplaint.this);
                Config_Engg.logout(ManageComplaint.this);
                Config_Engg.putSharedPreferences(ManageComplaint.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                dialog.dismiss();
                ScanckBar();

            }

            progressDialog.dismiss();
        }
    }
}
