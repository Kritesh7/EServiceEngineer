package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import in.co.cfcs.eserviceengineer.utils.SimpleSpanBuilder;

public class NewMachine extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerMachineSaleInfoInsUpdt";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerMachineSaleInfoInsUpdt";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerComplaintAddInitialData";
    private static String METHOD_NAME2 = "AppEngineerComplaintAddInitialData";

    private static String SOAP_ACTION3 = "http://cfcs.co.in/AppEngineerddlSite";
    private static String METHOD_NAME3 = "AppEngineerddlSite";

    private static String SOAP_ACTION4 = "http://cfcs.co.in/AppEngineerddlPrincipalRegionAppStatus";
    private static String METHOD_NAME4 = "AppEngineerddlPrincipalRegionAppStatus";

    private static String SOAP_ACTION5 = "http://cfcs.co.in/AppEngineerddlArea";
    private static String METHOD_NAME5 = "AppEngineerddlArea";

    private static String SOAP_ACTION6 = "http://cfcs.co.in/AppEngineerddlModel";
    private static String METHOD_NAME6 = "AppEngineerddlModel";

    private static String SOAP_ACTION7 = "http://cfcs.co.in/AppEngineerddlEngineer";
    private static String METHOD_NAME7 = "AppEngineerddlEngineer";

    List<String> customerIDList;
    List<String> customerNameList;

    List<String> transactionIDList;
    List<String> transactionNameList;

    List<String> plantID;
    List<String> plantName;

    List<String> principalIDList;
    List<String> principalNameList;

    List<String> zoneIDList;
    List<String> zoneNameList;

    List<String> areaID;
    List<String> areaName;

    List<String> modelID;
    List<String> modelName;

    List<String> enggID;
    List<String> enggName;

    List<String> enggSecondaryID;
    List<String> enggSecondaryName;

    String SelctedCustomerID;
    String SelctedPlantID;
    String SelctedZoneID;
    String SelctedPrincipalID;
    String SelectedWarrantyAmcID;
    String SelectedModelID;
    String SelectedEnggID;
    String SelectedAreaID;

    ArrayAdapter<String> spinneradapterTrans;
    ArrayAdapter<String> spinneradapterCustomer;
    ArrayAdapter<String> spinneradapterPlant;
    ArrayAdapter<String> spinneradapterPrincipal;
    ArrayAdapter<String> spinneradapterZone;
    ArrayAdapter<String> spinneradapterArea;
    ArrayAdapter<String> spinneradapterModel;
    ArrayAdapter<String> spinneradapterEngg;
    ArrayAdapter<String> spinneradapterEnggS;

    Spinner spinner_customer_name, spinner_plant, spinner_warranty_amc, spinner_installation_region, spinner_installation_area,
            spinner_principal, spinner_machine_model, spinner_primary_respon, spinner_secondary_respon;

    LinearLayout warranty_llout_hideShow, amc_llout_hideShow;

    EditText txt_date_of_supply, txt_date_install, txt_warranty_s_date, txt_warranty_e_date, txt_amc_start_date,
            txt_amc_end_date, txt_machine_serial, txt_sw_version, txt_product_key, txt_office_file_no, txt_amc_file_no, txt_comment;

    Button btn_submit, btn_clear, btn_update;

    String serialNo = "", SWVersion = "", productKey = "", fileNo = "", AMCFileNo = "", comments = "", dateOfSupply = "", dateOfInstallation = "",
            warrantyStartDate = "", warrantyEndDate = "", AMCStartDate = "", AMCEndDate = "";

    long SelectedWarrantyAmc;

    String saleID;
    String isEditDelete = "";

    String PlantIdUpdate = "";
    String AreaIDUpdate = "";
    String ModelIDUpdate = "";

    LinearLayout maincontainer;

    TextView tv_customer_name, tv_plant, tv_installation_region, tv_installation_area, tv_warrantyAMC, tv_machine_mode, tv_machine_serial;

    int mYear;
    int mMonth;
    int mDay;
    int dateTimeStatus;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_machine);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_plant = findViewById(R.id.tv_plant);
        tv_installation_region = findViewById(R.id.tv_installation_region);
        tv_installation_area = findViewById(R.id.tv_installation_area);
        tv_warrantyAMC = findViewById(R.id.tv_warrantyAMC);
        tv_machine_mode = findViewById(R.id.tv_machine_mode);
        tv_machine_serial = findViewById(R.id.tv_machine_serial);

        SimpleSpanBuilder ssbCustomer = new SimpleSpanBuilder();
        ssbCustomer.appendWithSpace("Customer Name");
        ssbCustomer.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_customer_name.setText(ssbCustomer.build());

        SimpleSpanBuilder ssbPlant = new SimpleSpanBuilder();
        ssbPlant.appendWithSpace("Plant");
        ssbPlant.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_plant.setText(ssbPlant.build());

        SimpleSpanBuilder ssbInstallRegion = new SimpleSpanBuilder();
        ssbInstallRegion.appendWithSpace("Installation Region");
        ssbInstallRegion.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_installation_region.setText(ssbInstallRegion.build());

        SimpleSpanBuilder ssbInstallArea = new SimpleSpanBuilder();
        ssbInstallArea.appendWithSpace("Installation Area");
        ssbInstallArea.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_installation_area.setText(ssbInstallArea.build());

        SimpleSpanBuilder ssbwarranty = new SimpleSpanBuilder();
        ssbwarranty.appendWithSpace("Warranty Amc Status");
        ssbwarranty.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_warrantyAMC.setText(ssbwarranty.build());

        SimpleSpanBuilder ssbMachineModel = new SimpleSpanBuilder();
        ssbMachineModel.appendWithSpace("Machine Model");
        ssbMachineModel.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_machine_mode.setText(ssbMachineModel.build());

        SimpleSpanBuilder ssbMachineSerial = new SimpleSpanBuilder();
        ssbMachineSerial.appendWithSpace("Machine Sr. No.");
        ssbMachineSerial.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_machine_serial.setText(ssbMachineSerial.build());

        spinner_customer_name = findViewById(R.id.spinner_customer_name);
        spinner_plant = findViewById(R.id.spinner_plant);
        spinner_warranty_amc = findViewById(R.id.spinner_warranty_amc);
        spinner_installation_region = findViewById(R.id.spinner_installation_region);
        spinner_installation_area = findViewById(R.id.spinner_installation_area);
        spinner_principal = findViewById(R.id.spinner_principal);
        spinner_machine_model = findViewById(R.id.spinner_machine_model);
        warranty_llout_hideShow = findViewById(R.id.warranty_llout_hideShow);
        amc_llout_hideShow = findViewById(R.id.amc_llout_hideShow);
        spinner_primary_respon = findViewById(R.id.spinner_primary_respon);
        spinner_secondary_respon = findViewById(R.id.spinner_secondary_respon);
        txt_date_of_supply = findViewById(R.id.txt_date_of_supply);
        txt_date_install = findViewById(R.id.txt_date_install);
        txt_warranty_s_date = findViewById(R.id.txt_warranty_s_date);
        txt_warranty_e_date = findViewById(R.id.txt_warranty_e_date);
        txt_amc_start_date = findViewById(R.id.txt_amc_start_date);
        txt_amc_end_date = findViewById(R.id.txt_amc_end_date);
        txt_machine_serial = findViewById(R.id.txt_machine_serial);
        txt_sw_version = findViewById(R.id.txt_sw_version);
        txt_product_key = findViewById(R.id.txt_product_key);
        txt_office_file_no = findViewById(R.id.txt_office_file_no);
        txt_amc_file_no = findViewById(R.id.txt_amc_file_no);
        txt_comment = findViewById(R.id.txt_comment);

        maincontainer = findViewById(R.id.maincontainer);

        btn_submit = findViewById(R.id.btn_submit);
        btn_clear = findViewById(R.id.btn_clear);
        btn_update = findViewById(R.id.btn_update);

        spinner_customer_name.requestFocus();

        warranty_llout_hideShow.setVisibility(View.GONE);
        amc_llout_hideShow.setVisibility(View.GONE);

        btn_update.setVisibility(View.GONE);

        saleID = "00000000-0000-0000-0000-000000000000";

        Config_Engg.isOnline(NewMachine.this);
        if (Config_Engg.internetStatus == true) {

            new AddInitialData().execute();

            new AddRegionPrincipalZoneAppStatus().execute();

            new Engineer().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            saleID = getIntent().getExtras().getString("SaleID");
            isEditDelete = getIntent().getExtras().getString("EditDelete");
        }

        if (isEditDelete.compareTo("true") == 0) {
            btn_submit.setVisibility(View.GONE);
            btn_update.setVisibility(View.VISIBLE);

            new FillMachineData().execute();

        }

        txt_date_of_supply.setOnClickListener(new View.OnClickListener() {
            int DateTimeStatus = 1;

            @Override
            public void onClick(View view) {

                datePicker(DateTimeStatus);
            }
        });

        txt_date_install.setOnClickListener(new View.OnClickListener() {
            int DateTimeStatus = 2;

            @Override
            public void onClick(View view) {

                datePicker(DateTimeStatus);
            }
        });

        txt_warranty_s_date.setOnClickListener(new View.OnClickListener() {
            int DateTimeStatus = 3;

            @Override
            public void onClick(View view) {

                datePicker(DateTimeStatus);
            }
        });

        txt_warranty_e_date.setOnClickListener(new View.OnClickListener() {
            int DateTimeStatus = 4;

            @Override
            public void onClick(View view) {

                datePicker(DateTimeStatus);
            }
        });

        txt_amc_start_date.setOnClickListener(new View.OnClickListener() {
            int DateTimeStatus = 5;

            @Override
            public void onClick(View view) {

                datePicker(DateTimeStatus);
            }
        });

        txt_amc_end_date.setOnClickListener(new View.OnClickListener() {
            int DateTimeStatus = 6;

            @Override
            public void onClick(View view) {

                datePicker(DateTimeStatus);
            }
        });


        spinner_customer_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(NewMachine.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedCustomer = parent.getSelectedItemId();
                    SelctedCustomerID = customerIDList.get((int) SelectedCustomer);
                    if (SelectedCustomer != 0) {
                        new AddSite().execute();

                    } else {

                        plantID = new ArrayList<String>();
                        plantID.add(0, "");
                        plantName = new ArrayList<String>();
                        plantName.add(0, "Select");


                        ArrayAdapter<String> spinneradapterMachine = new ArrayAdapter<String>(NewMachine.this,
                                android.R.layout.simple_spinner_item, plantName);
                        spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_plant.setAdapter(spinneradapterMachine);
                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_installation_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(NewMachine.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedZone = parent.getSelectedItemId();
                    SelctedZoneID = zoneIDList.get((int) SelectedZone);
                    if (SelectedZone != 0) {
                        new AddArea().execute();

                    } else {

                        areaID = new ArrayList<String>();
                        areaID.add(0, "");
                        areaName = new ArrayList<String>();
                        areaName.add(0, "Select");

                        ArrayAdapter<String> spinneradapterArea = new ArrayAdapter<String>(NewMachine.this,
                                android.R.layout.simple_spinner_item, areaName);
                        spinneradapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_installation_area.setAdapter(spinneradapterArea);
                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_principal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(NewMachine.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedPrincipal = parent.getSelectedItemId();
                    SelctedPrincipalID = principalIDList.get((int) SelectedPrincipal);
                    if (SelectedPrincipal != 0) {

                        new AddModel().execute();

                    } else {

                        modelID = new ArrayList<String>();
                        modelID.add(0, "");
                        modelName = new ArrayList<String>();
                        modelName.add(0, "Select");

                        ArrayAdapter<String> spinneradapterModel = new ArrayAdapter<String>(NewMachine.this,
                                android.R.layout.simple_spinner_item, modelName);
                        spinneradapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_machine_model.setAdapter(spinneradapterModel);
                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_warranty_amc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(NewMachine.this);
                if (Config_Engg.internetStatus == true) {

                    SelectedWarrantyAmc = parent.getSelectedItemId();
                    SelectedWarrantyAmcID = transactionIDList.get((int) SelectedWarrantyAmc);
                    if (SelectedWarrantyAmc == 0) {

                        warranty_llout_hideShow.setVisibility(View.GONE);
                        amc_llout_hideShow.setVisibility(View.GONE);

                    } else if (SelectedWarrantyAmc == 1) {

                        warranty_llout_hideShow.setVisibility(View.VISIBLE);
                        amc_llout_hideShow.setVisibility(View.VISIBLE);

                        if (!txt_date_install.getText().toString().equalsIgnoreCase("")) {
                            txt_warranty_s_date.setText(txt_date_install.getText().toString());

                        }

                    } else if (SelectedWarrantyAmc == 2) {

                        warranty_llout_hideShow.setVisibility(View.VISIBLE);
                        amc_llout_hideShow.setVisibility(View.GONE);

                        if (!txt_date_install.getText().toString().equalsIgnoreCase("")) {
                            txt_warranty_s_date.setText(txt_date_install.getText().toString());
                        }

                    } else {
                        warranty_llout_hideShow.setVisibility(View.GONE);
                        amc_llout_hideShow.setVisibility(View.GONE);

                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config_Engg.isOnline(NewMachine.this);
                if (Config_Engg.internetStatus == true) {

                    int customerPos = spinner_customer_name.getSelectedItemPosition();
                    int plantPos = spinner_plant.getSelectedItemPosition();
                    int regionPos = spinner_installation_region.getSelectedItemPosition();
                    int areaPos = spinner_installation_area.getSelectedItemPosition();
                    int modelPos = spinner_machine_model.getSelectedItemPosition();
                    int workAMCPos = spinner_warranty_amc.getSelectedItemPosition();

                    if (transactionIDList.size() > 0) {

                        if (SelectedWarrantyAmc == 1) {

                            if (txt_amc_start_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Enter Your AMC Start Date ",
                                        NewMachine.this);
                                txt_amc_start_date.requestFocus();
                                //focusOnView();
                            } else if (txt_amc_end_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Select Your AMC End Date ", NewMachine.this);
                                txt_amc_end_date.requestFocus();
                            }

                        } else if (SelectedWarrantyAmc == 2) {

                            txt_amc_start_date.setText("");
                            txt_amc_end_date.setText("");

                            if (txt_warranty_s_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Enter Your Warranty Start Date ",
                                        NewMachine.this);
                                txt_amc_start_date.requestFocus();
                                //focusOnView();
                            } else if (txt_warranty_e_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Select Your Warranty End Date ", NewMachine.this);
                                txt_amc_end_date.requestFocus();
                            }

                        } else {
                            txt_warranty_s_date.setText("");
                            txt_warranty_e_date.setText("");
                            txt_amc_start_date.setText("");
                            txt_amc_end_date.setText("");
                        }

                    }

                    if (customerPos == 0) {
                        Config_Engg.alertBox("Please Select Your Customer Name ", NewMachine.this);
                        spinner_customer_name.requestFocus();
                    } else if (plantPos == 0) {
                        Config_Engg.alertBox("Please Select Your Plant ",
                                NewMachine.this);
                        spinner_plant.requestFocus();
                    } else if (regionPos == 0) {
                        Config_Engg.alertBox("Please Select Your Region ",
                                NewMachine.this);
                        spinner_installation_region.requestFocus();
                    } else if (areaPos == 0) {
                        Config_Engg.alertBox("Please Select Your Area ",
                                NewMachine.this);
                        spinner_installation_area.requestFocus();
                    } else if (modelPos == 0) {
                        Config_Engg.alertBox("Please Select YourModel ",
                                NewMachine.this);
                        spinner_machine_model.requestFocus();
                    } else if (txt_machine_serial.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Your Machine Serial No. ",
                                NewMachine.this);
                        txt_machine_serial.requestFocus();
                        //focusOnView();
                    } else if (workAMCPos == 0) {
                        Config_Engg.alertBox("Please Select Warranty/AMC ",
                                NewMachine.this);
                        spinner_warranty_amc.requestFocus();
                    } else {

                        long SelectedPlant = spinner_plant.getSelectedItemId();
                        SelctedPlantID = plantID.get((int) SelectedPlant);

                        long SelectedModel = spinner_machine_model.getSelectedItemId();
                        SelectedModelID = modelID.get((int) SelectedModel);

                        long SelectedEnggS = spinner_secondary_respon.getSelectedItemId();
                        SelectedEnggID = enggSecondaryID.get((int) SelectedEnggS);

                        long SelectedArea = spinner_installation_area.getSelectedItemId();
                        SelectedAreaID = areaID.get((int) SelectedArea);

                        dateOfSupply = txt_date_of_supply.getText().toString();
                        dateOfInstallation = txt_date_install.getText().toString();
                        warrantyStartDate = txt_warranty_s_date.getText().toString();
                        warrantyEndDate = txt_warranty_e_date.getText().toString();
                        AMCStartDate = txt_amc_start_date.getText().toString();
                        AMCEndDate = txt_amc_end_date.getText().toString();
                        serialNo = txt_machine_serial.getText().toString();
                        SWVersion = txt_sw_version.getText().toString();
                        productKey = txt_product_key.getText().toString();
                        fileNo = txt_office_file_no.getText().toString();
                        AMCFileNo = txt_amc_file_no.getText().toString();
                        comments = txt_comment.getText().toString();

                        new AddMachine().execute();

                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
                }


            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config_Engg.isOnline(NewMachine.this);
                if (Config_Engg.internetStatus == true) {

                    int customerPos = spinner_customer_name.getSelectedItemPosition();
                    int plantPos = spinner_plant.getSelectedItemPosition();
                    int regionPos = spinner_installation_region.getSelectedItemPosition();
                    int areaPos = spinner_installation_area.getSelectedItemPosition();
                    int modelPos = spinner_machine_model.getSelectedItemPosition();
                    int workAMCPos = spinner_warranty_amc.getSelectedItemPosition();

                    if (transactionIDList.size() > 0) {

                        if (SelectedWarrantyAmc == 1) {

                            if (txt_amc_start_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Enter Your AMC Start Date ",
                                        NewMachine.this);
                                txt_amc_start_date.requestFocus();
                                //focusOnView();
                            } else if (txt_amc_end_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Select Your AMC End Date ", NewMachine.this);
                                txt_amc_end_date.requestFocus();
                            }

                        } else if (SelectedWarrantyAmc == 2) {

                            txt_amc_start_date.setText("");
                            txt_amc_end_date.setText("");

                            if (txt_warranty_s_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Enter Your Warranty Start Date ",
                                        NewMachine.this);
                                txt_amc_start_date.requestFocus();
                                //focusOnView();
                            } else if (txt_warranty_e_date.getText().toString().equalsIgnoreCase("")) {
                                Config_Engg.alertBox("Please Select Your Warranty End Date ", NewMachine.this);
                                txt_amc_end_date.requestFocus();
                            }

                        } else {
                            txt_warranty_s_date.setText("");
                            txt_warranty_e_date.setText("");
                            txt_amc_start_date.setText("");
                            txt_amc_end_date.setText("");
                        }

                    }

                    if (customerPos == 0) {
                        Config_Engg.alertBox("Please Select Your Customer Name ", NewMachine.this);
                        spinner_customer_name.requestFocus();
                    } else if (plantPos == 0) {
                        Config_Engg.alertBox("Please Select Your Plant ",
                                NewMachine.this);
                        spinner_plant.requestFocus();
                    } else if (regionPos == 0) {
                        Config_Engg.alertBox("Please Select Your Region ",
                                NewMachine.this);
                        spinner_installation_region.requestFocus();
                    } else if (areaPos == 0) {
                        Config_Engg.alertBox("Please Select Your Area ",
                                NewMachine.this);
                        spinner_installation_area.requestFocus();
                    } else if (modelPos == 0) {
                        Config_Engg.alertBox("Please Select YourModel ",
                                NewMachine.this);
                        spinner_machine_model.requestFocus();
                    } else if (txt_machine_serial.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Your Machine Serial No. ",
                                NewMachine.this);
                        txt_machine_serial.requestFocus();
                        //focusOnView();
                    } else if (workAMCPos == 0) {
                        Config_Engg.alertBox("Please Select Warranty/AMC ",
                                NewMachine.this);
                        spinner_warranty_amc.requestFocus();
                    } else {

                        long SelectedPlant = spinner_plant.getSelectedItemId();
                        SelctedPlantID = plantID.get((int) SelectedPlant);

                        long SelectedModel = spinner_machine_model.getSelectedItemId();
                        SelectedModelID = modelID.get((int) SelectedModel);

                        long SelectedEnggS = spinner_secondary_respon.getSelectedItemId();
                        SelectedEnggID = enggSecondaryID.get((int) SelectedEnggS);

                        long SelectedArea = spinner_installation_area.getSelectedItemId();
                        SelectedAreaID = areaID.get((int) SelectedArea);

                        dateOfSupply = txt_date_of_supply.getText().toString();
                        dateOfInstallation = txt_date_install.getText().toString();
                        warrantyStartDate = txt_warranty_s_date.getText().toString();
                        warrantyEndDate = txt_warranty_e_date.getText().toString();
                        AMCStartDate = txt_amc_start_date.getText().toString();
                        AMCEndDate = txt_amc_end_date.getText().toString();
                        serialNo = txt_machine_serial.getText().toString();
                        SWVersion = txt_sw_version.getText().toString();
                        productKey = txt_product_key.getText().toString();
                        fileNo = txt_office_file_no.getText().toString();
                        AMCFileNo = txt_amc_file_no.getText().toString();
                        comments = txt_comment.getText().toString();

                        new AddMachine().execute();

                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
                }

            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_customer_name.setSelection(0);
                spinner_plant.setSelection(0);
                spinner_installation_region.setSelection(0);
                spinner_installation_area.setSelection(0);
                spinner_principal.setSelection(0);
                spinner_machine_model.setSelection(0);
                txt_machine_serial.setText("");
                txt_sw_version.setText("");
                txt_product_key.setText("");
                txt_product_key.setText("");
                txt_office_file_no.setText("");
                txt_amc_file_no.setText("");
                txt_date_of_supply.setText("");
                txt_date_install.setText("");
                spinner_secondary_respon.setSelection(0);
                spinner_warranty_amc.setSelection(0);
                txt_warranty_s_date.setText("");
                txt_warranty_e_date.setText("");
                txt_amc_start_date.setText("");
                txt_amc_end_date.setText("");
                txt_comment.setText("");
                spinner_customer_name.requestFocus();

            }
        });


    }

    //date time picker
    public void datePicker(final int DateTimeStatus) {
        c = Calendar.getInstance();

        this.dateTimeStatus = DateTimeStatus;
        // Get Current Date

        this.mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Date date1 = new Date(String.valueOf(c.getTime()));
                        String date_time = DateFormat.getDateInstance().format(date1);

                        if (DateTimeStatus == 1) {
                            txt_date_of_supply.setText(date_time);
                        } else if (DateTimeStatus == 2) {
                            txt_date_install.setText(date_time);
                        } else if (DateTimeStatus == 3) {
                            txt_warranty_s_date.setText(date_time);
                        } else if (DateTimeStatus == 4) {
                            txt_warranty_e_date.setText(date_time);
                        } else if (DateTimeStatus == 5) {
                            txt_amc_start_date.setText(date_time);
                        } else if (DateTimeStatus == 6) {
                            txt_amc_end_date.setText(date_time);
                        }

                    }
                }, this.mYear, mMonth, mDay);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                getString(R.string.clear), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // todo on click

                        if (DateTimeStatus == 1) {
                            txt_date_of_supply.setText("");
                        } else if (DateTimeStatus == 2) {
                            txt_date_install.setText("");
                        } else if (DateTimeStatus == 3) {
                            txt_warranty_s_date.setText("");
                        } else if (DateTimeStatus == 4) {
                            txt_warranty_e_date.setText("");
                        } else if (DateTimeStatus == 5) {
                            txt_amc_start_date.setText("");
                        } else if (DateTimeStatus == 6) {
                            txt_amc_end_date.setText("");
                        }

                    }
                });

        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
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
            progressDialog = ProgressDialog.show(NewMachine.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");
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
                Config_Engg.toastShow(msgstatus, NewMachine.this);
            } else if (flag == 2) {
                try {
//
                    JSONArray jsonArray2 = new JSONArray(customerList);
                    customerIDList = new ArrayList<String>();
                    customerIDList.add(0, "");
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

                    spinneradapterCustomer = new ArrayAdapter<String>(NewMachine.this, android.R.layout.simple_spinner_item, customerNameList);
                    spinneradapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_customer_name.setAdapter(spinneradapterCustomer);

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

                    spinneradapterTrans = new ArrayAdapter<String>(NewMachine.this, android.R.layout.simple_spinner_item, transactionNameList);
                    spinneradapterTrans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_warranty_amc.setAdapter(spinneradapterTrans);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewMachine.this);
//                fillListDialog.dismiss();
                finish();
            } else {
                if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, NewMachine.this);
                    Config_Engg.logout(NewMachine.this);
                    Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {

                    ScanckBar();
                    btn_update.setEnabled(false);
                    btn_submit.setEnabled(false);
                    btn_clear.setEnabled(false);
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

                        Config_Engg.isOnline(NewMachine.this);
                        if (Config_Engg.internetStatus == true) {

                            new AddInitialData().execute();

                            new AddRegionPrincipalZoneAppStatus().execute();

                            new Engineer().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", NewMachine.this);
                        }

                        if (isEditDelete.compareTo("true") == 0) {
                            btn_submit.setVisibility(View.GONE);
                            btn_update.setVisibility(View.VISIBLE);

                            new FillMachineData().execute();

                        }

                        btn_update.setEnabled(true);
                        btn_submit.setEnabled(true);
                        btn_clear.setEnabled(true);

                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }

    private class AddSite extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String plant_detail, plant_list;
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;
        int count = 0;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(NewMachine.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            String EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");
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
                    plant_detail = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(plant_detail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    plant_list = jsonArray.toString();
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
                Config_Engg.toastShow(msgstatus, NewMachine.this);
            } else if (flag == 2) {
                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(plant_list);
                    plantID = new ArrayList<String>();
                    plantID.add(0, "");
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

                    spinneradapterPlant = new ArrayAdapter<String>(NewMachine.this,
                            android.R.layout.simple_spinner_item, plantName);
                    spinneradapterPlant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_plant.setAdapter(spinneradapterPlant);

                    int index = -1;
                    for (int i = 0; i < plantID.size(); i++) {
                        if (plantID.get(i).equals(PlantIdUpdate)) {
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
                    } else if (count == 1) {

                        spinner_plant.setSelection(1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewMachine.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, NewMachine.this);
                Config_Engg.logout(NewMachine.this);
                Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                ScanckBar();
                btn_update.setEnabled(false);
                btn_submit.setEnabled(false);
                btn_clear.setEnabled(false);
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
        String RegionPrincipalAppStatus, principal, zone;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewMachine.this, "Loading", "Please Wait", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION4, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    RegionPrincipalAppStatus = result.getProperty(0).toString();
                    Object json = new JSONTokener(RegionPrincipalAppStatus).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(RegionPrincipalAppStatus);
                        JSONArray principaljsonArray = object.getJSONArray("Principal");
                        principal = principaljsonArray.toString();
                        JSONArray zoneArray = object.getJSONArray("Zone");
                        zone = zoneArray.toString();
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
                Config_Engg.toastShow(msgstatus, NewMachine.this);
            } else if (flag == 2) {
                try {
//
                    JSONArray jsonArray2 = new JSONArray(principal);
                    principalIDList = new ArrayList<String>();
                    principalIDList.add(0, "");
                    principalNameList = new ArrayList<String>();
                    principalNameList.add(0, "Select");

                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String PrincipleID = jsonObject2.getString("PrincipleID");
                        String PrincipleName = jsonObject2.getString("PrincipleName");

                        principalIDList.add(i + 1, PrincipleID);
                        //siteNameList.add(SiteName);
                        principalNameList.add(i + 1, PrincipleName);
                    }

                    spinneradapterPrincipal = new ArrayAdapter<String>(NewMachine.this, android.R.layout.simple_spinner_item, principalNameList);
                    spinneradapterPrincipal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_principal.setAdapter(spinneradapterPrincipal);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                try {

                    JSONArray jsonArray2 = new JSONArray(zone);
                    zoneIDList = new ArrayList<String>();
                    zoneIDList.add(0, "");
                    zoneNameList = new ArrayList<String>();
                    zoneNameList.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String ZoneID = jsonObject2.getString("ZoneID");
                        String ZoneName = jsonObject2.getString("ZoneName");

                        zoneIDList.add(ZoneID);
                        zoneNameList.add(ZoneName);
                    }

                    spinneradapterZone = new ArrayAdapter<String>(NewMachine.this, android.R.layout.simple_spinner_item, zoneNameList);
                    spinneradapterZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_installation_region.setAdapter(spinneradapterZone);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewMachine.this);
//                fillListDialog.dismiss();
                finish();
            } else {
                if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, NewMachine.this);
                    Config_Engg.logout(NewMachine.this);
                    Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {

                    ScanckBar();
                    btn_update.setEnabled(false);
                    btn_submit.setEnabled(false);
                    btn_clear.setEnabled(false);
                }

            }
            progressDialog.dismiss();

        }
    }

    private class AddArea extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String area_detail, area_list;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";
        int count = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewMachine.this, "Loading", "Please Wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");
            String SeletedZoneID = SelctedZoneID;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);
            request.addProperty("ZoneID", SeletedZoneID);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION5, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    area_detail = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(area_detail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    area_list = jsonArray.toString();
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
                Config_Engg.toastShow(msgstatus, NewMachine.this);
            } else if (flag == 2) {
                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(area_list);
                    areaID = new ArrayList<String>();
                    areaID.add(0, "");
                    areaName = new ArrayList<String>();
                    areaName.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        count += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String AreaID = jsonObject2.getString("AreaID");
                        String AreaName = jsonObject2.getString("AreaName");

                        areaID.add(i + 1, AreaID);
                        areaName.add(i + 1, AreaName);
                    }

                    spinneradapterArea = new ArrayAdapter<String>(NewMachine.this,
                            android.R.layout.simple_spinner_item, areaName);
                    spinneradapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_installation_area.setAdapter(spinneradapterArea);

                    int index = -1;
                    for (int i = 0; i < areaID.size(); i++) {
                        if (areaID.get(i).equals(AreaIDUpdate)) {
                            index = i;
                            break;
                        }
                    }

                    if (index > 0) {

                        String AreaString = areaName.get((int) index);
                        if (!AreaString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterArea.getPosition(AreaString);
                            spinner_installation_area.setSelection(spinnerpos);
                        }
                    } else if (count == 1) {

                        spinner_installation_area.setSelection(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewMachine.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, NewMachine.this);
                Config_Engg.logout(NewMachine.this);
                Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {
                ScanckBar();
                btn_update.setEnabled(false);
                btn_submit.setEnabled(false);
                btn_clear.setEnabled(false);
            }

            progressDialog.dismiss();

        }
    }

    private class AddModel extends AsyncTask<String, String, String> {

        String msgstatus;
        int flag = 0;
        String model_detail, model_list;
        ProgressDialog progressDialog;
        int count = 0;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewMachine.this, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");
            String SelctedPrincipal = SelctedPrincipalID;
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);
            request.addProperty("PrincipalID", SelctedPrincipal);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION6, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    model_detail = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(model_detail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    model_list = jsonArray.toString();
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
                Config_Engg.toastShow(msgstatus, NewMachine.this);
            } else if (flag == 2) {
                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(model_list);
                    modelID = new ArrayList<String>();
                    modelID.add(0, "");
                    modelName = new ArrayList<String>();
                    modelName.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        count += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String ModelID = jsonObject2.getString("ModelID");
                        String ModelName = jsonObject2.getString("ModelName");

                        modelID.add(i + 1, ModelID);
                        modelName.add(i + 1, ModelName);
                    }

                    spinneradapterModel = new ArrayAdapter<String>(NewMachine.this,
                            android.R.layout.simple_spinner_item, modelName);
                    spinneradapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_machine_model.setAdapter(spinneradapterModel);


                    int index = -1;
                    for (int i = 0; i < modelID.size(); i++) {
                        if (modelID.get(i).equals(ModelIDUpdate)) {
                            index = i;
                            break;
                        }
                    }
                    if (index > 0) {

                        String ModelString = modelName.get((int) index);
                        if (!ModelString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterModel.getPosition(ModelString);
                            spinner_machine_model.setSelection(spinnerpos);
                        }
                    } else if (count == 1) {

                        spinner_machine_model.setSelection(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }
                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewMachine.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, NewMachine.this);
                Config_Engg.logout(NewMachine.this);
                Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {
                ScanckBar();
                btn_update.setEnabled(false);
                btn_submit.setEnabled(false);
                btn_clear.setEnabled(false);
            }

            progressDialog.dismiss();

        }
    }

    private class Engineer extends AsyncTask<String, String, String> {

        String msgstatus;
        String engg_detail, engg_list;
        int flag = 0;
        String EngineerID;
        ProgressDialog progressDialog;
        int count = 0;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewMachine.this, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME7);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION7, envelope);
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
                Config_Engg.toastShow(msgstatus, NewMachine.this);
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

                    spinneradapterEngg = new ArrayAdapter<String>(NewMachine.this,
                            android.R.layout.simple_spinner_item, enggName);


                    spinneradapterEngg.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_primary_respon.setAdapter(spinneradapterEngg);


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
                            spinner_primary_respon.setSelection(spinnerpos);
                            spinner_primary_respon.setEnabled(false);
                            spinner_primary_respon.setClickable(false);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(engg_list);
                    enggSecondaryID = new ArrayList<String>();
                    enggSecondaryID.add(0, "00000000-0000-0000-0000-000000000000");
                    enggSecondaryName = new ArrayList<String>();
                    enggSecondaryName.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        count += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String EngineerID = jsonObject2.getString("EngineerID");
                        String EngineerName = jsonObject2.getString("EngineerName");

                        enggSecondaryID.add(i + 1, EngineerID);
                        enggSecondaryName.add(i + 1, EngineerName);
                    }

                    spinneradapterEnggS = new ArrayAdapter<String>(NewMachine.this,
                            android.R.layout.simple_spinner_item, enggSecondaryName);


                    spinneradapterEnggS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_secondary_respon.setAdapter(spinneradapterEnggS);

                    if (count == 1) {

                        spinner_machine_model.setSelection(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewMachine.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, NewMachine.this);
                Config_Engg.logout(NewMachine.this);
                Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {
                ScanckBar();
                btn_update.setEnabled(false);
                btn_submit.setEnabled(false);
                btn_clear.setEnabled(false);
            }

            progressDialog.dismiss();

        }
    }

    private class AddMachine extends AsyncTask<String, String, String> {

        int flag;
        String jsonValue, msg;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewMachine.this, "Loading", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("SaleID", saleID);
            request.addProperty("CustomerID", SelctedPlantID);
            request.addProperty("ModelID", SelectedModelID);
            request.addProperty("SerialNo", serialNo);
            request.addProperty("DateOfSupply", dateOfSupply);
            request.addProperty("DateOfInstallation", dateOfInstallation);
            request.addProperty("WarrantyStartDate", warrantyStartDate);
            request.addProperty("WarrantyEndDate", warrantyEndDate);
            request.addProperty("AMCStartDate", AMCStartDate);
            request.addProperty("AMCEndDate", AMCEndDate);
            request.addProperty("Comments", comments);
            request.addProperty("TransactionType", SelectedWarrantyAmcID);
            request.addProperty("SWVersion", SWVersion);
            request.addProperty("ProductKey", productKey);
            request.addProperty("KomaxFileNo", fileNo);
            request.addProperty("AMCFileNo", AMCFileNo);
            request.addProperty("SecondPersonID", SelectedEnggID);
            request.addProperty("ZoneID", SelctedZoneID);
            request.addProperty("AreaID", SelectedAreaID);
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
                        msg = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {

                            flag = 4;
                        } else {

                            flag = 2;
                        }
                    } else {
                        flag = 1;
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
                Config_Engg.toastShow(msg, NewMachine.this);
            } else {
                if (flag == 2) {
                    Config_Engg.toastShow(msg, NewMachine.this);
                    Intent intent = new Intent(NewMachine.this, ManageMachines.class);
                    startActivity(intent);
                    finish();
                } else if (flag == 3) {
                    Config_Engg.toastShow("No Response", NewMachine.this);
                } else if (flag == 4) {
                    Config_Engg.toastShow(msg, NewMachine.this);
                    Config_Engg.logout(NewMachine.this);
                    Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {
                    ScanckBar();
                    btn_update.setEnabled(false);
                    btn_submit.setEnabled(false);
                    btn_clear.setEnabled(false);
                }
            }
            progressDialog.dismiss();
            btn_submit.setClickable(true);

        }
    }

    private class FillMachineData extends AsyncTask<String, String, String> {

        private String SOAP_ACTION = "http://cfcs.co.in/AppEngineerMachineSalesInfoDetail";
        private String NAMESPACE = "http://cfcs.co.in/";
        private String METHOD_NAME = "AppEngineerMachineSalesInfoDetail";
        private String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

        int flag;
        String msgstatus;
        String machine_detail_value;
        ProgressDialog progressDialog;
        String MachineDetail;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewMachine.this, "Loading...", "Please wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            String AuthCode = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "AuthCode", "");
            String EngineerID = Config_Engg.getSharedPreferences(NewMachine.this, "pref_Engg", "EngineerID", "");
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("SaleID", saleID);
            request.addProperty("AuthCode", AuthCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    machine_detail_value = result.getProperty(0).toString();

                    Object json = new JSONTokener(machine_detail_value).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(machine_detail_value);
                        JSONArray complainDetailArray = object.getJSONArray("MachineSale");
                        MachineDetail = complainDetailArray.toString();
                        if (machine_detail_value.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(machine_detail_value);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            flag = 2;
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(machine_detail_value);
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
                    JSONArray jsonArray = new JSONArray(machine_detail_value);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    msgstatus = jsonObject.getString("MsgNotification");
                    flag = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = 5;
            }
            return machine_detail_value;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, NewMachine.this);
            } else if (flag == 2) {
                try {
                    JSONArray jsonArray = new JSONArray(MachineDetail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String CustomerIDUpdate = jsonObject.getString("ParentCustomerID").toString();

                    int index = -1;
                    for (int i = 0; i < customerIDList.size(); i++) {
                        if (customerIDList.get(i).equals(CustomerIDUpdate)) {
                            index = i;
                            break;
                        }
                    }

                    if (index > 0) {

                        String customerString = customerNameList.get((int) index);

                        if (!customerString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterCustomer.getPosition(customerString);
                            spinner_customer_name.setSelection(spinnerpos);
                        }
                    }

                    PlantIdUpdate = jsonObject.getString("CustomerID").toString();

                    String ZoneIDUpdate = jsonObject.getString("ZoneID").toString();

                    int indexZone = -1;
                    for (int i = 0; i < zoneIDList.size(); i++) {
                        if (zoneIDList.get(i).equals(ZoneIDUpdate)) {
                            indexZone = i;
                            break;
                        }
                    }

                    if (indexZone > 0) {

                        String ZoneString = zoneNameList.get((int) indexZone);

                        if (!ZoneString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterZone.getPosition(ZoneString);
                            spinner_installation_region.setSelection(spinnerpos);
                        }
                    }

                    AreaIDUpdate = jsonObject.getString("AreaID").toString();

                    String PrincipleIDUpdate = jsonObject.getString("PrincipleID").toString();

                    int indexPrincipal = -1;
                    for (int i = 0; i < principalIDList.size(); i++) {
                        if (principalIDList.get(i).equals(PrincipleIDUpdate)) {
                            indexPrincipal = i;
                            break;
                        }
                    }

                    if (indexPrincipal > 0) {

                        String PrinicipalString = principalNameList.get((int) indexPrincipal);

                        if (!PrinicipalString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterPrincipal.getPosition(PrinicipalString);
                            spinner_principal.setSelection(spinnerpos);
                        }
                    }

                    ModelIDUpdate = jsonObject.getString("ModelID").toString();

                    String TransactionTypeIDUpdate = jsonObject.getString("TransactionType").toString();

                    int indexTrans = -1;
                    for (int i = 0; i < transactionIDList.size(); i++) {
                        if (transactionIDList.get(i).equals(TransactionTypeIDUpdate)) {
                            indexTrans = i;
                            break;
                        }
                    }

                    if (indexTrans > 0) {

                        String TransString = transactionNameList.get((int) indexTrans);

                        if (!TransString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterTrans.getPosition(TransString);
                            spinner_warranty_amc.setSelection(spinnerpos);
                        }
                    }

                    String SecondPersonIDUpdate = jsonObject.getString("SecondPersonID").toString();

                    int indexEngg = -1;
                    for (int i = 0; i < enggSecondaryID.size(); i++) {
                        if (enggSecondaryID.get(i).equals(SecondPersonIDUpdate)) {
                            indexEngg = i;
                            break;
                        }
                    }

                    if (indexEngg > 0) {

                        String EnggString = enggSecondaryName.get((int) indexEngg);

                        if (!EnggString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterEnggS.getPosition(EnggString);
                            spinner_secondary_respon.setSelection(spinnerpos);
                        }
                    }

                    String SerialNo = jsonObject.getString("SerialNo").toString();
                    txt_machine_serial.setText(SerialNo);

                    String SWVersion = jsonObject.getString("SWVersion").toString();
                    txt_sw_version.setText(SWVersion);

                    String ProductKey = jsonObject.getString("ProductKey").toString();
                    txt_product_key.setText(ProductKey);

                    String FileNO = jsonObject.getString("KomaxFileNo").toString();
                    txt_office_file_no.setText(FileNO);

                    String AmcFileNo = jsonObject.getString("AMCFileNo").toString();
                    txt_amc_file_no.setText(AmcFileNo);

                    String DateOfSupply = jsonObject.getString("DateOfSupply").toString();
                    txt_date_of_supply.setText(DateOfSupply);

                    String DateOfInstallation = jsonObject.getString("DateOfInstallation").toString();
                    txt_date_install.setText(DateOfInstallation);

                    String Comments = jsonObject.getString("Comments").toString();
                    txt_comment.setText(Comments);

                    String WarrantyStartDate = jsonObject.getString("WarrantyStartDate").toString();
                    txt_warranty_s_date.setText(WarrantyStartDate);

                    String WarrantyEndDate = jsonObject.getString("WarrantyEndDate").toString();
                    txt_warranty_e_date.setText(WarrantyEndDate);

                    String AMCStartDate = jsonObject.getString("AMCStartDate").toString();
                    txt_amc_start_date.setText(AMCStartDate);

                    String AMCEndDate = jsonObject.getString("AMCEndDate").toString();
                    txt_amc_end_date.setText(AMCEndDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", NewMachine.this);

            } else if (flag == 4) {
                Config_Engg.toastShow(msgstatus, NewMachine.this);
                Config_Engg.logout(NewMachine.this);
                Config_Engg.putSharedPreferences(NewMachine.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {
                ScanckBar();
                btn_update.setEnabled(false);
                btn_submit.setEnabled(false);
                btn_clear.setEnabled(false);
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
                intent = new Intent(NewMachine.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(NewMachine.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(NewMachine.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(NewMachine.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(NewMachine.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(NewMachine.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_machines:
                intent = new Intent(NewMachine.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(NewMachine.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(NewMachine.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NewMachine.this, ManageMachines.class);
        startActivity(i);
    }

}
