package in.co.cfcs.eserviceengineer.activity_engineer;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.adapter.MachineListAdapter;
import in.co.cfcs.eserviceengineer.model.MachineListDataModal;

public class ManageMachines extends AppCompatActivity {


    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerMachineSalesInfoList";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerMachineSalesInfoList";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerComplaintAddInitialData";
    private static String METHOD_NAME2 = "AppEngineerComplaintAddInitialData";

    private static String SOAP_ACTION3 = "http://cfcs.co.in/AppEngineerddlSite";
    private static String METHOD_NAME3 = "AppEngineerddlSite";

    private static String SOAP_ACTION4 = "http://cfcs.co.in/AppEngineerddlModelByCustomer";
    private static String METHOD_NAME4 = "AppEngineerddlModelByCustomer";

    private static String SOAP_ACTION5 = "http://cfcs.co.in/AppEngineerddlEngineer";
    private static String METHOD_NAME5 = "AppEngineerddlEngineer";

    private static String SOAP_ACTION6 = "http://cfcs.co.in/AppEngineerddlPrincipalRegionAppStatus";
    private static String METHOD_NAME6 = "AppEngineerddlPrincipalRegionAppStatus";


    ArrayList<MachineListDataModal> machineList = new ArrayList<MachineListDataModal>();

    String[] WarrantyStartDateText;
    String[] WarrantyEndDateText;
    String[] AMCStartDateText;
    String[] AMCEndDateText;
    String[] CustomerName;
    String[] ParentCustomerName;
    String[] PrincipleName;
    String[] ModelName;
    String[] SubMachineModelName;
    String[] SerialNo;
    String[] TransactionTypeName;
    String[] ZoneName;
    String[] ApproveStatusName;
    String[] ApproveStatusRemark;
    String[] AddByName;
    String[] IsEditDelete;
    String[] AddByNameText;
    String[] SaleID;

    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;


    String zoneID;
    String customerID;
    String siteID;
    String modelID;
    String transactionType;
    String approveStatusID;
    String SearchStr;

    ListView list;

    RecyclerView recycler_view;

    Spinner spinner_plant, spinner_customer, spinner_model, spinner_warranty_amc_status, spinner_responsible_engg, spinner_approval_status;

    List<String> customerIDList;
    List<String> customerNameList;

    List<String> plantID;
    List<String> plantName;


    List<String> transactionIDList;
    List<String> transactionNameList;

    List<String> modelListID;
    List<String> modelListName;

    List<String> enggID;
    List<String> enggName;

    List<String> appStatusIDList;
    List<String> appStatusNameList;

    ArrayAdapter<String> spinneradapterCustomer;
    ArrayAdapter<String> spinneradapterPlant;
    ArrayAdapter<String> spinneradapterModel;
    ArrayAdapter<String> spinneradapterTrans;
    ArrayAdapter<String> spinneradapterEngg;
    ArrayAdapter<String> spinneradapterAppStatus;

    String SelctedCustomerID;
    String SelctedPlanrID;

    RecyclerView.LayoutManager mLayoutManager;

    CoordinatorLayout maincontainer;

    Button btn_search_find, btn_search_clear;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_machines);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "SerialNo");

        Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "CustomerName");

        Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "Planr");

        Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "Model");

        Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "Trans");

        Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "AppStatus");

        //   list = findViewById(R.id.machine_list_view);

        recycler_view = findViewById(R.id.recycler_view);
        maincontainer = findViewById(R.id.maincontainer);

        mLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        zoneID = "0";
        customerID = "00000000-0000-0000-0000-000000000000";
        siteID = "00000000-0000-0000-0000-000000000000";
        modelID = "00000000-0000-0000-0000-000000000000";
        transactionType = "0";
        approveStatusID = "0";
        SearchStr = "";

        Config_Engg.isOnline(ManageMachines.this);
        if (Config_Engg.internetStatus == true) {

            new MachineListAsy(zoneID, customerID, siteID, modelID, transactionType, approveStatusID, SearchStr).execute();

            new SetdataInCustomerSpinner().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageMachines.this);
        }

        fab = findViewById(R.id.fab);
        fabLayout1 = (LinearLayout) findViewById(R.id.fabLayout1);
        fabLayout2 = (LinearLayout) findViewById(R.id.fabLayout2);

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
            public void onClick(View v) {
                showSearchPopByFab();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageMachines.this, NewMachine.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putParcelable("ListState", mLayoutManager.onSaveInstanceState());
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

        final EditText txt_serial_no;

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View convertView = (View) inflater.inflate(R.layout.add_serach_machine, null);
        alertDialog.setView(convertView);

        spinner_plant = convertView.findViewById(R.id.spinner_plant);
        spinner_customer = convertView.findViewById(R.id.spinner_customer);
        spinner_model = convertView.findViewById(R.id.spinner_model);
        spinner_warranty_amc_status = convertView.findViewById(R.id.spinner_warranty_amc_status);
        spinner_responsible_engg = convertView.findViewById(R.id.spinner_responsible_engg);
        spinner_approval_status = convertView.findViewById(R.id.spinner_approval_status);

        txt_serial_no = convertView.findViewById(R.id.txt_serial_no);

        btn_search_find = convertView.findViewById(R.id.btn_search_find);

        btn_search_clear = convertView.findViewById(R.id.btn_search_clear);

        dialog = alertDialog.create();

        String ComparedSerialNo = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "SerialNo", "");

        txt_serial_no.setText(ComparedSerialNo);

        String ComparedCustomerName = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "CustomerName", "");
        String ComparedWorkStatusName = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "Trans", "");

        Config_Engg.isOnline(ManageMachines.this);
        if (Config_Engg.internetStatus == true) {

            new Engineer().execute();
            new AddRegionPrincipalZoneAppStatus().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageMachines.this);
        }

        spinner_customer.setAdapter(spinneradapterCustomer);


        if (!ComparedCustomerName.equalsIgnoreCase("")) {

            int spinnerPosition = spinneradapterCustomer.getPosition(ComparedCustomerName);
            spinner_customer.setSelection(spinnerPosition);

        }

        spinner_warranty_amc_status.setAdapter(spinneradapterTrans);

        if (!ComparedWorkStatusName.equalsIgnoreCase("")) {

            int spinnerPosition = spinneradapterTrans.getPosition(ComparedWorkStatusName);
            spinner_warranty_amc_status.setSelection(spinnerPosition);

        }


        spinner_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(ManageMachines.this);
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


                        ArrayAdapter<String> spinneradapterMachine = new ArrayAdapter<String>(ManageMachines.this,
                                android.R.layout.simple_spinner_item, plantName);
                        spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_plant.setAdapter(spinneradapterMachine);
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageMachines.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_plant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(ManageMachines.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedPlant = parent.getSelectedItemId();
                    SelctedPlanrID = plantID.get((int) SelectedPlant);
                    if (SelectedPlant != 0) {
                        new AddModel().execute();

                    } else {

                        modelListID = new ArrayList<String>();
                        modelListID.add(0, "00000000-0000-0000-0000-000000000000");
                        modelListName = new ArrayList<String>();
                        modelListName.add(0, "Select");

                        ArrayAdapter<String> spinneradapterModel1 = new ArrayAdapter<String>(ManageMachines.this,
                                android.R.layout.simple_spinner_item, modelListName);
                        spinneradapterModel1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_model.setAdapter(spinneradapterModel1);
                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageMachines.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_search_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String zoneID = "0";
                String customerID = "00000000-0000-0000-0000-000000000000";
                String siteID = "00000000-0000-0000-0000-000000000000";
                String modelID = "00000000-0000-0000-0000-000000000000";
                String transactionType = "0";
                String approveStatusID = "0";
                String SearchStr = "";

                Config_Engg.isOnline(ManageMachines.this);
                if (Config_Engg.internetStatus == true) {

                    SearchStr = txt_serial_no.getText().toString();

                    long saleID = spinner_plant.getSelectedItemId();
                    siteID = plantID.get((int) saleID);

                    long spiCustomer = spinner_customer.getSelectedItemId();
                    customerID = customerIDList.get((int) spiCustomer);

                    long ModelID = spinner_model.getSelectedItemId();
                    modelID = modelListID.get((int) ModelID);

                    long TransId = spinner_warranty_amc_status.getSelectedItemId();
                    transactionType = transactionIDList.get((int) TransId);

                    long AppStatusID = spinner_approval_status.getSelectedItemId();
                    approveStatusID = appStatusIDList.get((int) AppStatusID);

                    machineList.clear();

                    Config_Engg.putSharedPreferences(ManageMachines.this, "pref_Engg", "SerialNo", SearchStr);

                    Config_Engg.putSharedPreferences(ManageMachines.this, "pref_Engg", "CustomerName", spinner_customer.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageMachines.this, "pref_Engg", "Planr", spinner_plant.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageMachines.this, "pref_Engg", "Model", spinner_model.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageMachines.this, "pref_Engg", "Trans", spinner_warranty_amc_status.getSelectedItem().toString());

                    Config_Engg.putSharedPreferences(ManageMachines.this, "pref_Engg", "AppStatus", spinner_approval_status.getSelectedItem().toString());

                    dialog.dismiss();

                    new MachineListAsy(zoneID, customerID, siteID, modelID, transactionType, approveStatusID, SearchStr).execute();

                    closeFABMenu();

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageMachines.this);
                }

            }
        });


        btn_search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "SerialNo");

                Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "CustomerName");

                Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "Planr");

                Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "Model");

                Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "Trans");

                Config_Engg.getSharedPreferenceRemove(ManageMachines.this, "pref_Engg", "AppStatus");

                Config_Engg.isOnline(ManageMachines.this);
                if (Config_Engg.internetStatus == true) {

                    dialog.dismiss();
                    new MachineListAsy(zoneID, customerID, siteID, modelID, transactionType, approveStatusID, SearchStr).execute();
                    closeFABMenu();
                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageMachines.this);
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
            Intent intent = new Intent(ManageMachines.this, DashboardActivity.class);
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }

    private class MachineListAsy extends AsyncTask<String, String, String> {

        String msgstatus;
        int flag;
        String statusbtn = "";
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;
        String MachineList = "";
        String ZoneID = "";
        String CustomerID = "";
        String SiteID = "";
        String ModelID = "";
        String TransactionType = "";
        String ApproveStatusID = "";
        String SearchStr = "";

        public ManageMachines manageMachines;

        public MachineListAsy(String zoneID, String customerID, String siteID, String modelID, String transactionType, String approveStatusID, String searchStr) {
            this.ZoneID = zoneID;
            this.CustomerID = customerID;
            this.SiteID = siteID;
            this.ModelID = modelID;
            this.TransactionType = transactionType;
            this.ApproveStatusID = approveStatusID;
            this.SearchStr = searchStr;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ManageMachines.this, "Loading", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            int count = 0;

            String EngineerID = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("ZoneID", ZoneID);
            request.addProperty("CustomerID", CustomerID);
            request.addProperty("SiteID", SiteID);
            request.addProperty("ModelID", ModelID);
            request.addProperty("TransactionType", transactionType);
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
                    MachineList = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(MachineList);
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

                        machineList.clear();

                        WarrantyStartDateText = new String[jsonArray.length()];
                        WarrantyEndDateText = new String[jsonArray.length()];
                        AMCStartDateText = new String[jsonArray.length()];
                        AMCEndDateText = new String[jsonArray.length()];
                        CustomerName = new String[jsonArray.length()];
                        ParentCustomerName = new String[jsonArray.length()];
                        PrincipleName = new String[jsonArray.length()];
                        ModelName = new String[jsonArray.length()];
                        SubMachineModelName = new String[jsonArray.length()];
                        SerialNo = new String[jsonArray.length()];
                        TransactionTypeName = new String[jsonArray.length()];
                        ZoneName = new String[jsonArray.length()];
                        ApproveStatusName = new String[jsonArray.length()];
                        ApproveStatusRemark = new String[jsonArray.length()];
                        AddByName = new String[jsonArray.length()];
                        IsEditDelete = new String[jsonArray.length()];
                        AddByNameText = new String[jsonArray.length()];
                        SaleID = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {

                            count += 1;

                            try {
                                JSONObject jsonObject1 = jsonArray
                                        .getJSONObject(i);

                                MachineListDataModal machineListDataModal = new MachineListDataModal(AuthCode, AuthCode, AuthCode,
                                        AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode);
                                machineListDataModal.setWarrantyStartDateText(jsonObject1.getString("WarrantyStartDateText").toString());
                                machineListDataModal.setWarrantyEndDateText(jsonObject1.getString("WarrantyEndDateText").toString());
                                machineListDataModal.setAMCStartDateText(jsonObject1.getString("AMCStartDateText").toString());
                                machineListDataModal.setAMCEndDateText(jsonObject1.getString("AMCEndDateText").toString());
                                machineListDataModal.setCustomerName(jsonObject1.getString("CustomerName").toString());
                                machineListDataModal.setParentCustomerName(jsonObject1.getString("ParentCustomerName").toString());
                                machineListDataModal.setPrincipleName(jsonObject1.getString("PrincipleName").toString());
                                machineListDataModal.setModelName(jsonObject1.getString("ModelName").toString());
                                machineListDataModal.setSubMachineModelName(jsonObject1.getString("SubMachineModelName").toString());
                                machineListDataModal.setSerialNo(jsonObject1.getString("SerialNo").toString());
                                machineListDataModal.setTransactionTypeName(jsonObject1.getString("TransactionTypeName").toString());
                                machineListDataModal.setZoneName(jsonObject1.getString("ZoneName").toString());
                                machineListDataModal.setApproveStatusName(jsonObject1.getString("ApproveStatusName").toString());
                                machineListDataModal.setApproveStatusRemark(jsonObject1.getString("ApproveStatusRemark").toString());
                                machineListDataModal.setAddByName(jsonObject1.getString("AddByName").toString());
                                machineListDataModal.setIsEditDelete(jsonObject1.getString("IsEditDelete").toString());
                                machineListDataModal.setAddByNameText(jsonObject1.getString("AddByNameText").toString());
                                machineListDataModal.setSaleID(jsonObject1.getString("SaleID").toString());

                                machineListDataModal.setCounter(String.valueOf(count));

                                // Add this object into the ArrayList myList

                                machineList.add(machineListDataModal);
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

                Config_Engg.toastShow(msgstatus, ManageMachines.this);
                //  progressDialog.dismiss();
                recycler_view.setAdapter(null);

            } else {
                if (flag == 2) {

                    recycler_view.setAdapter(new MachineListAdapter(ManageMachines.this, machineList));

                } else {
                    if (flag == 3) {
                        Config_Engg.toastShow("No Response", ManageMachines.this);
                        //    progressDialog.dismiss();
                    } else {
                        if (flag == 4) {

                            Config_Engg.toastShow(msgstatus, ManageMachines.this);
                            Config_Engg.logout(ManageMachines.this);
                            Config_Engg.putSharedPreferences(ManageMachines.this, "checklogin", "status", "2");
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
                        Config_Engg.isOnline(ManageMachines.this);
                        if (Config_Engg.internetStatus == true) {

                            new MachineListAsy(zoneID, customerID, siteID, modelID, transactionType, approveStatusID, SearchStr).execute();

                            new SetdataInCustomerSpinner().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ManageMachines.this);
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
            //      progressDialog = ProgressDialog.show(ManageMachines.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String EngineerID = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "AuthCode", "");
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
                Config_Engg.toastShow(msgstatus, ManageMachines.this);
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

                    spinneradapterCustomer = new ArrayAdapter<String>(ManageMachines.this, android.R.layout.simple_spinner_item, customerNameList);
                    spinneradapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }
                try {

                    JSONArray jsonArray2 = new JSONArray(transactionList);
                    transactionIDList = new ArrayList<String>();
                    transactionIDList.add(0, "");
                    transactionNameList = new ArrayList<String>();
                    transactionNameList.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String TranscationID = jsonObject2.getString("TransactionTypeID");
                        String TransactionName = jsonObject2.getString("TransactionTypeName");

                        transactionIDList.add(TranscationID);
                        transactionNameList.add(TransactionName);
                    }

                    spinneradapterTrans = new ArrayAdapter<String>(ManageMachines.this, android.R.layout.simple_spinner_item, transactionNameList);
                    spinneradapterTrans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //    progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageMachines.this);
//                fillListDialog.dismiss();
                finish();
            } else {
                if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, ManageMachines.this);
                    Config_Engg.logout(ManageMachines.this);
                    Config_Engg.putSharedPreferences(ManageMachines.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {

                    ScanckBar();

                }

            }
            //  progressDialog.dismiss();
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
        String ComparedPlantName = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "Plant", "");

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(ManageMachines.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            String EngineerID = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "AuthCode", "");
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
                Config_Engg.toastShow(msgstatus, ManageMachines.this);
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

                    spinneradapterPlant = new ArrayAdapter<String>(ManageMachines.this,
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
                progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageMachines.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, ManageMachines.this);
                Config_Engg.logout(ManageMachines.this);
                Config_Engg.putSharedPreferences(ManageMachines.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                ScanckBar();
                dialog.dismiss();

            }

            progressDialog.dismiss();
        }
    }

    private class AddModel extends AsyncTask<String, String, String> {

        String msgstatus;
        int flag = 0;
        String model_detail, model_list;
        int count = 0;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";
        String ComparedModel = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "Model", "");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ManageMachines.this, "Loading", "Please wait", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "AuthCode", "");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);
            request.addProperty("CustomerID", SelctedCustomerID);
            request.addProperty("SiteID", SelctedPlanrID);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION4, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {

                    model_detail = result.getProperty(0).toString();

                    Object json = new JSONTokener(model_detail).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(model_detail);
                        JSONArray plantjsonArray = object.getJSONArray("Model");
                        model_list = plantjsonArray.toString();
                        if (model_detail.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(model_detail);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            flag = 2;
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(model_detail);
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
                Config_Engg.toastShow(msgstatus, ManageMachines.this);
            } else if (flag == 2) {
                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(model_list);
                    modelListID = new ArrayList<String>();
                    modelListID.add(0, "00000000-0000-0000-0000-000000000000");
                    modelListName = new ArrayList<String>();
                    modelListName.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        count += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String ModelID = jsonObject2.getString("ModelID");
                        String ModelName = jsonObject2.getString("ModelName");

                        modelListID.add(i + 1, ModelID);
                        modelListName.add(i + 1, ModelName);
                    }

                    spinneradapterModel = new ArrayAdapter<String>(ManageMachines.this,
                            android.R.layout.simple_spinner_item, modelListName);
                    spinneradapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_model.setAdapter(spinneradapterModel);


                    if (!ComparedModel.equalsIgnoreCase("")) {

                        int spinnerPosition = spinneradapterModel.getPosition(ComparedModel);
                        spinner_model.setSelection(spinnerPosition);

                    } else if (count == 1) {

                        spinner_model.setSelection(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageMachines.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, ManageMachines.this);
                Config_Engg.logout(ManageMachines.this);
                Config_Engg.putSharedPreferences(ManageMachines.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                ScanckBar();
                dialog.dismiss();


            }

            progressDialog.dismiss();

        }
    }

    private class Engineer extends AsyncTask<String, String, String> {

        String msgstatus;
        String engg_detail, engg_list;
        int flag = 0;
        ProgressDialog progressDialog;
        String EngineerID;
        int count = 0;
        String LoginStatus;
        String invalid = "LoginFailed";
        String ComparedEngg = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "Engg", "");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ManageMachines.this, "Loading...", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            EngineerID = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    engg_detail = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(engg_detail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    engg_list = jsonArray.toString();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, ManageMachines.this);
            } else if (flag == 2) {
                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(engg_list);
                    enggID = new ArrayList<String>();
                    enggID.add(0, "");
                    enggName = new ArrayList<String>();
                    enggName.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        count += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String EngineerID = jsonObject2.getString("EngineerID");
                        String EngineerName = jsonObject2.getString("EngineerName");

                        enggID.add(i + 1, EngineerID);
                        enggName.add(i + 1, EngineerName);
                    }

                    spinneradapterEngg = new ArrayAdapter<String>(ManageMachines.this,
                            android.R.layout.simple_spinner_item, enggName);

                    spinneradapterEngg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner_responsible_engg.setAdapter(spinneradapterEngg);

                    int index = -1;
                    for (int i = 0; i < enggID.size(); i++) {
                        if (enggID.get(i).equals(EngineerID)) {
                            index = i;
                            break;
                        }
                    }

                    if (index > 0) {

                        String EnggString = enggName.get((int) index);
                        if (!EnggString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterEngg.getPosition(EnggString);
                            spinner_responsible_engg.setSelection(spinnerpos);
                            spinner_responsible_engg.setEnabled(false);
                            spinner_responsible_engg.setClickable(false);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }
                progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageMachines.this);
                //      progressDialog.dismiss();
            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, ManageMachines.this);
                Config_Engg.logout(ManageMachines.this);
                Config_Engg.putSharedPreferences(ManageMachines.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                ScanckBar();
                progressDialog.dismiss();

            }

            progressDialog.dismiss();

        }
    }

    private class AddRegionPrincipalZoneAppStatus extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;
        String RegionPrincipalAppStatus, appStatus, zone;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ManageMachines.this, "Loading...", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "AuthCode", "");
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
                Config_Engg.toastShow(msgstatus, ManageMachines.this);
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

                    spinneradapterAppStatus = new ArrayAdapter<String>(ManageMachines.this, android.R.layout.simple_spinner_item, appStatusNameList);
                    spinneradapterAppStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    String ComparedAppStatus = Config_Engg.getSharedPreferences(ManageMachines.this, "pref_Engg", "AppStatus", "");
                    spinner_approval_status.setAdapter(spinneradapterAppStatus);

                    if (!ComparedAppStatus.equalsIgnoreCase("")) {

                        int spinnerPosition = spinneradapterAppStatus.getPosition(ComparedAppStatus);
                        spinner_approval_status.setSelection(spinnerPosition);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ManageMachines.this);
                progressDialog.dismiss();
                finish();
            } else {
                if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, ManageMachines.this);
                    Config_Engg.logout(ManageMachines.this);
                    Config_Engg.putSharedPreferences(ManageMachines.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {

                    ScanckBar();

                }
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
                intent = new Intent(ManageMachines.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(ManageMachines.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(ManageMachines.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(ManageMachines.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(ManageMachines.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(ManageMachines.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(ManageMachines.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(ManageMachines.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(ManageMachines.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }


}
