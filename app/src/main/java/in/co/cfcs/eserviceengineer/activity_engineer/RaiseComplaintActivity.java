package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.utils.SimpleSpanBuilder;

public class RaiseComplaintActivity extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerComplainEditDetail";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerComplainEditDetail";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerComplaintAddInitialData";
    private static String METHOD_NAME2 = "AppEngineerComplaintAddInitialData";

    private static String SOAP_ACTION3 = "http://cfcs.co.in/AppEngineerddlSite";
    private static String METHOD_NAME3 = "AppEngineerddlSite";

    private static String SOAP_ACTION4 = "http://cfcs.co.in/AppEngineerSiteChange";
    private static String METHOD_NAME4 = "AppEngineerSiteChange";

    private static String SOAP_ACTION5 = "http://cfcs.co.in/AppEngineerMachineChange";
    private static String METHOD_NAME5 = "AppEngineerMachineChange";

    private static String SOAP_ACTION6 = "http://cfcs.co.in/AppEngineerContactChange";
    private static String METHOD_NAME6 = "AppEngineerContactChange";

    private static String SOAP_ACTION7 = "http://cfcs.co.in/AppEngineerComplainInsUpdt";
    private static String METHOD_NAME7 = "AppEngineerComplainInsUpdt";

    EditText txt_problem_title, txt_problem_description, txt_problem_date, txt_complain_date,
            txt_expected_date, txt_new_person_name, txt_mail, txt_new_customer_mobile,
            txt_other_contact, txt_country_code;
    Spinner spinner_customer_name, spinner_plant, spinner_machine_model, spinner_machine_serial, spinner_complaint_type, spinner_work_status,
            spinner_status, spinner_existing_customer_contacts;
    Button btn_submit, btn_clear, btn_update;

    TextView txt_complaint_no, txt_region, asm_txt_show, txt_header;

    CheckBox check_customer, check_internal;

    RadioButton radio_off_site, radio_on_site;

    RadioGroup radio_group;

    View view1, view2;

    LinearLayout complaint_view, region_view, spinner_work_status_hide, spinner_work_status_hide_spinner;

    String complainno = "0";
    int status = 0;

    Calendar c;

    List<String> customerIDList;
    List<String> customerNameList;

    List<String> transactionIDList;
    List<String> transactionNameList;

    List<String> engWorkStatusIDList;
    List<String> engWorkStatusNameList;

    List<String> plantID;
    List<String> plantName;

    List<String> machineSerialID;
    List<String> machineSerialName;

    List<String> contactIDList;
    List<String> contactNameList;

    String SelctedCustomerID;
    String SelctedPlantID;
    String SelctedMachineID;
    String SelectedContactID;
    String SelectedComplaintTypeID;
    String SelectedWorkStatusID;

    ArrayAdapter<String> spinneradapterTrans;
    ArrayAdapter<String> spinneradapterCustomer;
    ArrayAdapter<String> spinneradapterMachine;
    ArrayAdapter<String> spinneradapterPlant;
    ArrayAdapter<String> spinneradapterContact;
    ArrayAdapter<String> spinneradapterWorkStatus;

    String ParentCustomerNameUpdate = "";
    String SaleIDUpdate = "";
    String ComplaintTypeUdate = "";
    String ContactPersonIDUpdate = "";
    String WorkStatusIDUpdate1 = "";

    String checkBoxCustomerValue;
    String checkBoxInternalValue;

    String ContactPersonNameUpdate = "";

    String ContactPersonMobileUpdate = "";

    String ContactPersonMailIDUpdate = "";

    String ContactPersonContactNoUpdate = "";

    String ContactPersonConutryUpdate ="";

    int seletedValue = 1;

    String problemTitle = "", problemDesc = "", otherContact = "", ProblemoccurAtDate = "", complaintDate = "", expectedDate = "", newpersonName = "", mailID = "",countryCode="",
            mobile = "";

    LinearLayout maincontainer;

    int checkConatct = 0;

    TextView tv_request_title, tv_customer_name, tv_plant, tv_machine_serial, tv_service_request_type, tv_problem_occurrence, tv_service_request_date, tv_mobile_no;

    String date_time1;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    int dateTimeStatus;
    int dateTimePickerStattus;

    boolean changing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_complaint);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tv_request_title = findViewById(R.id.tv_request_title);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_plant = findViewById(R.id.tv_plant);
        tv_machine_serial = findViewById(R.id.tv_machine_serial);
        tv_service_request_type = findViewById(R.id.tv_service_request_type);
        tv_problem_occurrence = findViewById(R.id.tv_problem_occurrence);
        tv_service_request_date = findViewById(R.id.tv_service_request_date);
        tv_mobile_no = findViewById(R.id.tv_mobile_no);

        SimpleSpanBuilder ssbrequesttitle = new SimpleSpanBuilder();
        ssbrequesttitle.appendWithSpace("Request Title");
        ssbrequesttitle.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_request_title.setText(ssbrequesttitle.build());

        SimpleSpanBuilder ssbCustomerName = new SimpleSpanBuilder();
        ssbCustomerName.appendWithSpace("Customer Name");
        ssbCustomerName.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_customer_name.setText(ssbCustomerName.build());

        SimpleSpanBuilder ssbplant = new SimpleSpanBuilder();
        ssbplant.appendWithSpace("Plant");
        ssbplant.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_plant.setText(ssbplant.build());

        SimpleSpanBuilder ssbMachineSerial = new SimpleSpanBuilder();
        ssbMachineSerial.appendWithSpace("Machine Sr. No.");
        ssbMachineSerial.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_machine_serial.setText(ssbMachineSerial.build());

        SimpleSpanBuilder ssbServicerequestType = new SimpleSpanBuilder();
        ssbServicerequestType.appendWithSpace("Service Request Type");
        ssbServicerequestType.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_service_request_type.setText(ssbServicerequestType.build());

        SimpleSpanBuilder ssbprobleOccur = new SimpleSpanBuilder();
        ssbprobleOccur.appendWithSpace("Problem Occurrence Date/Time");
        ssbprobleOccur.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_problem_occurrence.setText(ssbprobleOccur.build());

        SimpleSpanBuilder ssbServiceRequestDate = new SimpleSpanBuilder();
        ssbServiceRequestDate.appendWithSpace("Service Request Date Time");
        ssbServiceRequestDate.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_service_request_date.setText(ssbServiceRequestDate.build());

//        SimpleSpanBuilder ssbMobileNo = new SimpleSpanBuilder();
//        ssbMobileNo.appendWithSpace("Mobile No");
//        ssbMobileNo.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
//        tv_mobile_no.setText(ssbMobileNo.build());

        txt_problem_title = findViewById(R.id.txt_problem_title);
        txt_problem_description = findViewById(R.id.txt_problem_description);
        txt_problem_date = findViewById(R.id.txt_problem_date);
        txt_complain_date = findViewById(R.id.txt_complain_date);
        txt_expected_date = findViewById(R.id.txt_expected_date);
        spinner_existing_customer_contacts = findViewById(R.id.spinner_existing_customer_contacts);
        txt_new_person_name = findViewById(R.id.txt_new_person_name);
        txt_mail = findViewById(R.id.txt_mail);
        txt_new_customer_mobile = findViewById(R.id.txt_new_customer_mobile);
        txt_other_contact = findViewById(R.id.txt_other_contact);
        txt_header = findViewById(R.id.txt_header);
        txt_country_code = findViewById(R.id.txt_country_code);

        spinner_customer_name = findViewById(R.id.spinner_customer_name);
        spinner_plant = findViewById(R.id.spinner_plant);
        spinner_machine_model = findViewById(R.id.spinner_machine_model);
        spinner_machine_serial = findViewById(R.id.spinner_machine_serial);
        spinner_complaint_type = findViewById(R.id.spinner_complaint_type);
        spinner_work_status = findViewById(R.id.spinner_work_status);
        spinner_status = findViewById(R.id.spinner_status);
        check_customer = findViewById(R.id.check_customer);
        check_internal = findViewById(R.id.check_internal);
        radio_off_site = findViewById(R.id.radio_off_site);
        radio_on_site = findViewById(R.id.radio_on_site);
        radio_group = findViewById(R.id.radio_group);
        spinner_work_status_hide = findViewById(R.id.spinner_work_status_hide);
        spinner_work_status_hide_spinner = findViewById(R.id.spinner_work_status_hide_spinner);

        txt_complaint_no = findViewById(R.id.txt_complaint_no);
        txt_region = findViewById(R.id.txt_region);
        asm_txt_show = findViewById(R.id.asm_txt_show);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        complaint_view = findViewById(R.id.complaint_view);
        region_view = findViewById(R.id.region_view);

        btn_submit = findViewById(R.id.btn_submit);
        btn_update = findViewById(R.id.btn_update);
        btn_clear = findViewById(R.id.btn_clear);

        maincontainer = findViewById(R.id.maincontainer);

        complaint_view.setVisibility(View.GONE);
        region_view.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);

        c = Calendar.getInstance();

        btn_update.setVisibility(View.GONE);

        txt_header.setText("Raise Service Request");

        radio_off_site.setChecked(true);

        Config_Engg.isOnline(RaiseComplaintActivity.this);
        if (Config_Engg.internetStatus == true) {

            new AddInitialData().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complainno = getIntent().getExtras().getString("ComplainNo");
            status = Integer.parseInt(getIntent().getExtras().getString("status"));

        }

        txt_problem_date.setOnClickListener(new View.OnClickListener() {

            int DateTimeStatus = 0;

            @Override
            public void onClick(View view) {
                datePicker(DateTimeStatus);
            }
        });

        txt_complain_date.setOnClickListener(new View.OnClickListener() {

            int DateTimeStatus = 1;

            @Override
            public void onClick(View view) {
                datePicker(DateTimeStatus);
            }
        });

        txt_expected_date.setOnClickListener(new View.OnClickListener() {
            int DateTimeStatus = 2;

            @Override
            public void onClick(View view) {

                datePicker(DateTimeStatus);
            }
        });

        spinner_customer_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(RaiseComplaintActivity.this);
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

                        ArrayAdapter<String> spinneradapterMachine = new ArrayAdapter<String>(RaiseComplaintActivity.this,
                                android.R.layout.simple_spinner_item, plantName);
                        spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_plant.setAdapter(spinneradapterMachine);
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_plant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(RaiseComplaintActivity.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedPlant = parent.getSelectedItemId();
                    SelctedPlantID = plantID.get((int) SelectedPlant);
                    if (SelectedPlant != 0) {
                        new AddMachineSerial().execute();

                    } else {

                        machineSerialID = new ArrayList<String>();
                        machineSerialID.add(0, "00000000-0000-0000-0000-000000000000");
                        machineSerialName = new ArrayList<String>();
                        machineSerialName.add(0, "Select");


                        ArrayAdapter<String> spinneradapterMachine = new ArrayAdapter<String>(RaiseComplaintActivity.this,
                                android.R.layout.simple_spinner_item, machineSerialName);
                        spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_machine_serial.setAdapter(spinneradapterMachine);

                        if (status != 1) {

                            contactIDList = new ArrayList<String>();
                            contactIDList.add(0, "00000000-0000-0000-0000-000000000000");
                            contactNameList = new ArrayList<String>();
                            contactNameList.add(0, "Select");

                            ArrayAdapter<String> spinneradapterCcontact = new ArrayAdapter<String>(RaiseComplaintActivity.this,
                                    android.R.layout.simple_spinner_item, contactNameList);
                            spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_existing_customer_contacts.setAdapter(spinneradapterCcontact);
                        }

                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_machine_serial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Config_Engg.isOnline(RaiseComplaintActivity.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedMachine = parent.getSelectedItemId();
                    SelctedMachineID = machineSerialID.get((int) SelectedMachine);
                    if (SelectedMachine != 0) {
                        new AddMachineChange().execute();

                    } else {

                        spinner_complaint_type.setSelection(0);
                        asm_txt_show.setVisibility(View.GONE);

                    }


                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_existing_customer_contacts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkConatct += 1;
                Log.i("kritesh", String.valueOf(checkConatct));
                Config_Engg.isOnline(RaiseComplaintActivity.this);
                if (Config_Engg.internetStatus == true) {

                    long SelectedContact = parent.getSelectedItemId();
                    SelectedContactID = contactIDList.get((int) SelectedContact);

                    if (checkConatct == 1 && status == 1) {

                        txt_new_person_name.setText(ContactPersonNameUpdate);

                        txt_new_person_name.setEnabled(true);

                        txt_new_customer_mobile.setText(ContactPersonMobileUpdate);

                        txt_country_code.setText(ContactPersonConutryUpdate);

                        txt_mail.setText(ContactPersonMailIDUpdate);

                        txt_other_contact.setText(ContactPersonContactNoUpdate);

                    }
                    if (checkConatct > 1 && status == 1) {

                        if (SelectedContact != 0) {

                            new AddContactChange().execute();

                        } else {

                            txt_new_person_name.setText("");
                            txt_new_person_name.setEnabled(true);
                            txt_mail.setText("");
                            txt_new_customer_mobile.setText("");
                            txt_other_contact.setText("");
                            txt_country_code.setText("");
                        }

                    } else {

                        if (SelectedContact != 0 && status != 1) {

                            new AddContactChange().execute();

                        } else if (status != 1) {
                            txt_new_person_name.setText("");
                            txt_new_person_name.setEnabled(true);
                            txt_mail.setText("");
                            txt_new_customer_mobile.setText("");
                            txt_other_contact.setText("");
                            txt_country_code.setText("");
                        }

                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
                }

//                Toast.makeText(RaiseComplaintActivity.this,"Count" +" "+ checkConatct, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Config_Engg.isOnline(RaiseComplaintActivity.this);
        if (Config_Engg.internetStatus == true) {

            if (status == 1) {

                complaint_view.setVisibility(View.VISIBLE);
                region_view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                txt_header.setText("Update Service Request");

                new UpdateComplaint().execute();

                btn_update.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.GONE);
            }

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
        }


        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_off_site:
                        seletedValue = 1;
                        break;
                    case R.id.radio_on_site:
                        seletedValue = 2;
                        break;
                }
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String dateforrow = dateFormat.format(c.getTime());
        Date date1 = new Date();
        String stringDate = DateFormat.getDateInstance().format(date1);
        txt_problem_date.setText(stringDate + " " + dateforrow);
        txt_complain_date.setText(stringDate + " " + dateforrow);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config_Engg.isOnline(RaiseComplaintActivity.this);
                if (Config_Engg.internetStatus == true) {

                    int customerPos = spinner_customer_name.getSelectedItemPosition();
                    int plantPos = spinner_plant.getSelectedItemPosition();
                    int serialPos = spinner_machine_serial.getSelectedItemPosition();
                    int complaintTypePos = spinner_complaint_type.getSelectedItemPosition();
                    int workStatusPos = spinner_work_status.getSelectedItemPosition();

                    String checkmobile = txt_new_customer_mobile.getText().toString().trim();
                    String code = txt_country_code.getText().toString().trim();

                    if (txt_problem_title.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Your Problem Title ",
                                RaiseComplaintActivity.this);
                        txt_problem_title.requestFocus();
                        //focusOnView();
                    } else if (customerPos == 0) {
                        Config_Engg.alertBox("Please Select Your Customer Name ",
                                RaiseComplaintActivity.this);
                        spinner_customer_name.requestFocus();
                    } else if (plantPos == 0) {
                        Config_Engg.alertBox("Please Select Your Plant ",
                                RaiseComplaintActivity.this);
                        spinner_plant.requestFocus();
                    } else if (serialPos == 0) {
                        Config_Engg.alertBox("Please Select Your Machine Serial No ",
                                RaiseComplaintActivity.this);
                        spinner_machine_serial.requestFocus();
                    } else if (complaintTypePos == 0) {
                        Config_Engg.alertBox("Please Select Your Complaint Type ",
                                RaiseComplaintActivity.this);
                        spinner_machine_model.requestFocus();
                    } else if (workStatusPos == 0) {
                        Config_Engg.alertBox("Please Select Your Work Status ",
                                RaiseComplaintActivity.this);
                        spinner_machine_model.requestFocus();
                    } else if (!txt_mail.getText().toString().equalsIgnoreCase("") && !isValidMail(txt_mail.getText().toString())) {
                        Config_Engg.alertBox("Please Enter Vaild Email",
                                RaiseComplaintActivity.this);
                        txt_mail.requestFocus();
                    } else if(checkmobile.compareTo("") != 0 || code.compareTo("") !=0){

                        if (checkmobile.compareTo("") != 0 && !isValidMobile(txt_new_customer_mobile.getText().toString().trim())) {
                            Config_Engg.alertBox("Please Enter Vaild Mobile No.",
                                    RaiseComplaintActivity.this);
                            txt_new_customer_mobile.requestFocus();
                        } else if (checkmobile.compareTo("") == 0) {
                            Config_Engg.alertBox("Please Enter Mobile No.", RaiseComplaintActivity.this);
                            txt_new_customer_mobile.requestFocus();
                        }else if (code.compareTo("") == 0) {
                            Config_Engg.alertBox("Please Enter Country Code", RaiseComplaintActivity.this);
                            txt_country_code.requestFocus();
                        }else {
                            long SelectedComplaintType = spinner_complaint_type.getSelectedItemId();
                            SelectedComplaintTypeID = transactionIDList.get((int) SelectedComplaintType);

                            long SelectedWorkStatus = spinner_work_status.getSelectedItemId();
                            SelectedWorkStatusID = engWorkStatusIDList.get((int) SelectedWorkStatus);

                            if (check_customer.isChecked()) {
                                checkBoxCustomerValue = "True";
                            } else {
                                checkBoxCustomerValue = "False";
                            }

                            if (check_internal.isChecked()) {
                                checkBoxInternalValue = "True";
                            } else {
                                checkBoxInternalValue = "False";
                            }

                            problemTitle = txt_problem_title.getText().toString().trim();
                            problemDesc = txt_problem_description.getText().toString().trim();
                            ProblemoccurAtDate = txt_problem_date.getText().toString().trim();
                            complaintDate = txt_complain_date.getText().toString().trim();
                            expectedDate = txt_expected_date.getText().toString().trim();
                            newpersonName = txt_new_person_name.getText().toString().trim();
                            mailID = txt_mail.getText().toString().trim();
                            countryCode = txt_country_code.getText().toString().trim();
                            mobile = txt_new_customer_mobile.getText().toString().trim();
                            otherContact = txt_other_contact.getText().toString().trim();

                            new RaiseComplaintSubmit().execute();

                        }
                    }  else {

                        long SelectedComplaintType = spinner_complaint_type.getSelectedItemId();
                        SelectedComplaintTypeID = transactionIDList.get((int) SelectedComplaintType);

                        long SelectedWorkStatus = spinner_work_status.getSelectedItemId();
                        SelectedWorkStatusID = engWorkStatusIDList.get((int) SelectedWorkStatus);

                        if (check_customer.isChecked()) {
                            checkBoxCustomerValue = "True";
                        } else {
                            checkBoxCustomerValue = "False";
                        }

                        if (check_internal.isChecked()) {
                            checkBoxInternalValue = "True";
                        } else {
                            checkBoxInternalValue = "False";
                        }

                        problemTitle = txt_problem_title.getText().toString().trim();
                        problemDesc = txt_problem_description.getText().toString().trim();
                        ProblemoccurAtDate = txt_problem_date.getText().toString().trim();
                        complaintDate = txt_complain_date.getText().toString().trim();
                        expectedDate = txt_expected_date.getText().toString().trim();
                        newpersonName = txt_new_person_name.getText().toString().trim();
                        mailID = txt_mail.getText().toString().trim();
                        countryCode = txt_country_code.getText().toString().trim();
                        mobile = txt_new_customer_mobile.getText().toString().trim();
                        otherContact = txt_other_contact.getText().toString().trim();

                        new RaiseComplaintSubmit().execute();
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
                }
            }
        });



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


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config_Engg.isOnline(RaiseComplaintActivity.this);
                if (Config_Engg.internetStatus == true) {

                    int customerPos = spinner_customer_name.getSelectedItemPosition();
                    int plantPos = spinner_plant.getSelectedItemPosition();
                    int serialPos = spinner_machine_serial.getSelectedItemPosition();
                    int complaintTypePos = spinner_complaint_type.getSelectedItemPosition();
                    int workStatusPos = spinner_work_status.getSelectedItemPosition();

                    String checkmobile = txt_new_customer_mobile.getText().toString().trim();
                    String code = txt_country_code.getText().toString().trim();

                    if (txt_problem_title.getText().toString().equalsIgnoreCase("")) {
                        Config_Engg.alertBox("Please Enter Your Problem Title ",
                                RaiseComplaintActivity.this);
                        txt_problem_title.requestFocus();
                        //focusOnView();
                    } else if (customerPos == 0) {
                        Config_Engg.alertBox("Please Select Your Customer Name ",
                                RaiseComplaintActivity.this);
                        txt_problem_description.requestFocus();
                    } else if (plantPos == 0) {
                        Config_Engg.alertBox("Please Select Your Plant ",
                                RaiseComplaintActivity.this);
                        spinner_plant.requestFocus();
                    } else if (serialPos == 0) {
                        Config_Engg.alertBox("Please Select Your Machine Serial No ",
                                RaiseComplaintActivity.this);
                        spinner_machine_serial.requestFocus();
                    } else if (complaintTypePos == 0) {
                        Config_Engg.alertBox("Please Select Your Complaint Type ",
                                RaiseComplaintActivity.this);
                        spinner_complaint_type.requestFocus();
                    } else if (workStatusPos == 0) {
                        Config_Engg.alertBox("Please Select Your Work Status ",
                                RaiseComplaintActivity.this);
                        spinner_work_status.requestFocus();
                    } else if (!txt_mail.getText().toString().equalsIgnoreCase("") && !isValidMail(txt_mail.getText().toString())) {
                        Config_Engg.alertBox("Please Enter Vaild Email",
                                RaiseComplaintActivity.this);
                        txt_mail.requestFocus();
                    } else if(checkmobile.compareTo("") != 0 || code.compareTo("") !=0){

                        if (checkmobile.compareTo("") != 0 && !isValidMobile(txt_new_customer_mobile.getText().toString().trim())) {
                            Config_Engg.alertBox("Please Enter Vaild Mobile No.",
                                    RaiseComplaintActivity.this);
                            txt_new_customer_mobile.requestFocus();
                        } else if (checkmobile.compareTo("") == 0) {
                            Config_Engg.alertBox("Please Enter Mobile No.", RaiseComplaintActivity.this);
                            txt_new_customer_mobile.requestFocus();
                        }else if (code.compareTo("") == 0) {
                            Config_Engg.alertBox("Please Enter Country Code", RaiseComplaintActivity.this);
                            txt_country_code.requestFocus();
                        }else {
                            long SelectedComplaintType = spinner_complaint_type.getSelectedItemId();
                            SelectedComplaintTypeID = transactionIDList.get((int) SelectedComplaintType);

                            long SelectedWorkStatus = spinner_work_status.getSelectedItemId();
                            SelectedWorkStatusID = engWorkStatusIDList.get((int) SelectedWorkStatus);

                            if (check_customer.isChecked()) {
                                checkBoxCustomerValue = "True";
                            } else {
                                checkBoxCustomerValue = "False";
                            }

                            if (check_internal.isChecked()) {
                                checkBoxInternalValue = "True";
                            } else {
                                checkBoxInternalValue = "False";
                            }

                            problemTitle = txt_problem_title.getText().toString().trim();
                            problemDesc = txt_problem_description.getText().toString().trim();
                            ProblemoccurAtDate = txt_problem_date.getText().toString().trim();
                            complaintDate = txt_complain_date.getText().toString().trim();
                            expectedDate = txt_expected_date.getText().toString().trim();
                            newpersonName = txt_new_person_name.getText().toString().trim();
                            mailID = txt_mail.getText().toString().trim();
                            countryCode = txt_country_code.getText().toString().trim();
                            mobile = txt_new_customer_mobile.getText().toString().trim();
                            otherContact = txt_other_contact.getText().toString().trim();

                            new RaiseComplaintSubmit().execute();

                        }
                    } else {

                        long SelectedComplaintType = spinner_complaint_type.getSelectedItemId();
                        SelectedComplaintTypeID = transactionIDList.get((int) SelectedComplaintType);

                        long SelectedWorkStatus = spinner_work_status.getSelectedItemId();
                        SelectedWorkStatusID = engWorkStatusIDList.get((int) SelectedWorkStatus);

                        if (check_customer.isChecked()) {
                            checkBoxCustomerValue = "True";
                        } else {
                            checkBoxCustomerValue = "False";
                        }

                        if (check_internal.isChecked()) {
                            checkBoxInternalValue = "True";
                        } else {
                            checkBoxInternalValue = "False";
                        }

                        problemTitle = txt_problem_title.getText().toString().trim();
                        problemDesc = txt_problem_description.getText().toString().trim();
                        ProblemoccurAtDate = txt_problem_date.getText().toString().trim();
                        complaintDate = txt_complain_date.getText().toString().trim();
                        expectedDate = txt_expected_date.getText().toString().trim();
                        newpersonName = txt_new_person_name.getText().toString().trim();
                        mailID = txt_mail.getText().toString().trim();
                        countryCode = txt_country_code.getText().toString().trim();
                        mobile = txt_new_customer_mobile.getText().toString().trim();
                        otherContact = txt_other_contact.getText().toString().trim();

                        new RaiseComplaintSubmit().execute();
                    }

                } else {
                    Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", RaiseComplaintActivity.this);
                }


            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_problem_title.setText("");
                txt_problem_description.setText("");
                spinner_customer_name.setSelection(0);
                spinner_plant.setSelection(0);
                spinner_machine_serial.setSelection(0);
                spinner_complaint_type.setSelection(0);
                spinner_existing_customer_contacts.setSelection(0);
                spinner_work_status.setSelection(0);
                txt_problem_date.setText("");
                txt_complain_date.setText("");
                txt_expected_date.setText("");
                txt_new_person_name.setText("");
                txt_mail.setText("");
                txt_country_code.setText("");
                txt_new_customer_mobile.setText("");
                txt_other_contact.setText("");
                check_customer.setChecked(false);
                check_internal.setChecked(false);
                txt_problem_title.requestFocus();
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

    public void datePicker(int DateTimeStatus) {

        this.dateTimeStatus = DateTimeStatus;
        // Get Current Date

        this.mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        Date date1 = new Date(String.valueOf(c.getTime()));
                        String date_time = DateFormat.getDateInstance().format(date1);

                        //*************Call Time Picker Here ********************
                        tiemPicker(dateTimeStatus, date_time);


                    }
                }, this.mYear, mMonth, mDay);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    private void tiemPicker(final int dateTimeStatus, String date_time) {
        this.dateTimePickerStattus = dateTimeStatus;
        this.date_time1 = date_time;
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        int hour = hourOfDay;
                        int minutes = minute;

                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();

                        if (dateTimePickerStattus == 0) {
                            txt_problem_date.setText(date_time1 + " " + aTime);
                        } else if (dateTimePickerStattus == 1) {
                            txt_complain_date.setText(date_time1 + " " + aTime);
                        } else if (dateTimePickerStattus == 2) {
                            txt_expected_date.setText(date_time1 + " " + aTime);
                        }


                    }
                }, 0, 0, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
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
                intent = new Intent(RaiseComplaintActivity.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(RaiseComplaintActivity.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(RaiseComplaintActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(RaiseComplaintActivity.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(RaiseComplaintActivity.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(RaiseComplaintActivity.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_machines:
                intent = new Intent(RaiseComplaintActivity.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(RaiseComplaintActivity.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(RaiseComplaintActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        if (status == 1) {
            Intent intent = new Intent(RaiseComplaintActivity.this, ManageComplaint.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(RaiseComplaintActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();

        }

    }

    private class UpdateComplaint extends AsyncTask<String, String, String> {
        int flag;
        String msgstatus;
        String complain_detail_value;
        ProgressDialog progressDialog;
        String ComplaintDetail;
        JSONArray complainSub;
        String LoginStatus;
        String invalid = "LoginFailed";
        int count = 0;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RaiseComplaintActivity.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            String AuthCode = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "AuthCode", "");
            String EngineerID = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "EngineerID", "");
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("ComplainNo", complainno);
            request.addProperty("AuthCode", AuthCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    complain_detail_value = result.getProperty(0).toString();

                    Object json = new JSONTokener(complain_detail_value).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(complain_detail_value);
                        JSONArray complainDetailArray = object.getJSONArray("ComplainDetail");
                        ComplaintDetail = complainDetailArray.toString();
                        if (complain_detail_value.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(complain_detail_value);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            flag = 2;
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(complain_detail_value);
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
                    JSONArray jsonArray = new JSONArray(complain_detail_value);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    msgstatus = jsonObject.getString("MsgNotification");
                    flag = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = 5;
            }
            return complain_detail_value;
        }

        @Override
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
            } else if (flag == 2) {
                try {
                    JSONArray jsonArray = new JSONArray(ComplaintDetail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String ComplainNo = jsonObject.getString("ComplainNo").toString();
                    txt_complaint_no.setText(ComplainNo);

                    String Region = jsonObject.getString("ZoneName").toString();
                    txt_region.setText(Region);

                    String ProblemTitle = jsonObject.getString("ComplaintTitle").toString();
                    txt_problem_title.setText(ProblemTitle);

                    String Description = jsonObject.getString("Description").toString();
                    txt_problem_description.setText(Description);

                    String ComplainDateTimeText = jsonObject.getString("TimeOfOccuranceDateTimeText").toString();
                    txt_complain_date.setText(ComplainDateTimeText);

                    String TimeOfOccuranceDateTimeText = jsonObject.getString("TimeOfOccuranceDateTimeText").toString();
                    txt_problem_date.setText(TimeOfOccuranceDateTimeText);

                    String RectifiedDateTimeText = jsonObject.getString("RectifiedDateTimeText").toString();
                    txt_expected_date.setText(RectifiedDateTimeText);

                    String CustomerID = jsonObject.getString("ParentCustomerID").toString();

                    int index = -1;
                    for (int i = 0; i < customerIDList.size(); i++) {
                        if (customerIDList.get(i).equals(CustomerID)) {
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

                    ParentCustomerNameUpdate = jsonObject.getString("CustomerID").toString();

                    SaleIDUpdate = jsonObject.getString("SaleID").toString();

                    ComplaintTypeUdate = jsonObject.getString("ComplaintType").toString();

                    ContactPersonIDUpdate = jsonObject.getString("ContactPersonID").toString();

                    ContactPersonNameUpdate = jsonObject.getString("ContactPersonName").toString();

                    ContactPersonConutryUpdate = jsonObject.getString("CountryCode").toString();

                    ContactPersonMobileUpdate = jsonObject.getString("ContactPersonMobile").toString();

                    ContactPersonMailIDUpdate = jsonObject.getString("ContactPersonMailID").toString();

                    ContactPersonContactNoUpdate = jsonObject.getString("ContactPersonContactNo").toString();

                    WorkStatusIDUpdate1 = jsonObject.getString("WorkStatusID").toString();

                    int index1 = -1;
                    for (int i = 0; i < engWorkStatusIDList.size(); i++) {
                        if (engWorkStatusIDList.get(i).equals(WorkStatusIDUpdate1)) {
                            index1 = i;
                            break;
                        }
                    }

                    if (index1 > 0) {

                        String workStatusString = engWorkStatusNameList.get((int) index1);
                        if (!workStatusString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterWorkStatus.getPosition(workStatusString);
                            spinner_work_status.setSelection(spinnerpos);
                            spinner_work_status.setEnabled(false);
                            spinner_work_status.setClickable(false);
                            spinner_work_status_hide.setVisibility(View.GONE);
                            spinner_work_status_hide_spinner.setVisibility(View.GONE);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", RaiseComplaintActivity.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
                Config_Engg.logout(RaiseComplaintActivity.this);
                Config_Engg.putSharedPreferences(RaiseComplaintActivity.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {
                ScanckBar();
                btn_submit.setEnabled(false);
                btn_update.setEnabled(false);
                btn_clear.setEnabled(false);
                progressDialog.dismiss();

            }
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
            progressDialog = ProgressDialog.show(RaiseComplaintActivity.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String EngineerID = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "AuthCode", "");
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
                flag = 5;
            }
            return null;
        }


        @Override
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
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

                    spinneradapterCustomer = new ArrayAdapter<String>(RaiseComplaintActivity.this, android.R.layout.simple_spinner_item, customerNameList);
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

                    spinneradapterTrans = new ArrayAdapter<String>(RaiseComplaintActivity.this, android.R.layout.simple_spinner_item, transactionNameList);
                    spinneradapterTrans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_complaint_type.setAdapter(spinneradapterTrans);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                if (status == 1) {

                    try {
                        JSONArray jsonArray2 = new JSONArray(engWorkStatusList);
                        engWorkStatusIDList = new ArrayList<String>();
                        engWorkStatusIDList.add(0, "");
                        engWorkStatusNameList = new ArrayList<String>();
                        engWorkStatusNameList.add(0, "Select");
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                            String EngWorkStatusID = jsonObject2.getString("EngWorkStatusID");
                            String EngWorkStatus = jsonObject2.getString("EngWorkStatus");

                            engWorkStatusIDList.add(EngWorkStatusID);
                            engWorkStatusNameList.add(EngWorkStatus);
                        }

                        spinneradapterWorkStatus = new ArrayAdapter<String>(RaiseComplaintActivity.this, android.R.layout.simple_spinner_item, engWorkStatusNameList);
                        spinneradapterWorkStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_work_status.setAdapter(spinneradapterWorkStatus);
                        spinner_work_status.setSelection(2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Log.e("Error is here", e.toString());
                    }

                } else {

                    engWorkStatusIDList = new ArrayList<String>();
                    engWorkStatusIDList.add(0, "");
                    engWorkStatusIDList.add(1, "1");
                    engWorkStatusIDList.add(2, "2");
                    engWorkStatusIDList.add(3, "5");
                    engWorkStatusNameList = new ArrayList<String>();
                    engWorkStatusNameList.add(0, "Select");
                    engWorkStatusNameList.add(1, "To be assigned");
                    engWorkStatusNameList.add(2, "Accepted");
                    engWorkStatusNameList.add(3, "Completed");

                    ArrayAdapter<String> spinneradapterStatus = new ArrayAdapter<String>(RaiseComplaintActivity.this,
                            android.R.layout.simple_spinner_item, engWorkStatusNameList);
                    spinneradapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_work_status.setAdapter(spinneradapterStatus);
                    spinner_work_status.setSelection(2);
                }

                progressDialog.dismiss();

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", RaiseComplaintActivity.this);
                finish();
            } else {
                if (flag == 4) {

                    Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
                    Config_Engg.logout(RaiseComplaintActivity.this);
                    Config_Engg.putSharedPreferences(RaiseComplaintActivity.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {

                    ScanckBar();
                    btn_submit.setEnabled(false);
                    btn_update.setEnabled(false);
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

                        if (status == 1) {

                            complaint_view.setVisibility(View.VISIBLE);
                            region_view.setVisibility(View.VISIBLE);
                            view1.setVisibility(View.VISIBLE);
                            view2.setVisibility(View.VISIBLE);
                            txt_header.setText("Update Service Request");

                            new UpdateComplaint().execute();

                            btn_update.setVisibility(View.VISIBLE);
                            btn_submit.setVisibility(View.GONE);

                            btn_submit.setEnabled(true);
                            btn_update.setEnabled(true);
                            btn_clear.setEnabled(true);
                        } else {

                            new AddInitialData().execute();
                            btn_submit.setEnabled(true);
                            btn_update.setEnabled(true);
                            btn_clear.setEnabled(true);
                        }
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        snackbar.show();

    }

    private class AddSite extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String machine_detail, machine_list;
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;
        int count = 0;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(RaiseComplaintActivity.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            String EngineerID = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "AuthCode", "");
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
            } else if (flag == 2) {
                try {

                    // Add value in Plant List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(machine_list);
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

                    spinneradapterPlant = new ArrayAdapter<String>(RaiseComplaintActivity.this,
                            android.R.layout.simple_spinner_item, plantName);
                    spinneradapterPlant.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_plant.setAdapter(spinneradapterPlant);


                    int index = -1;
                    for (int i = 0; i < plantID.size(); i++) {
                        if (plantID.get(i).equals(ParentCustomerNameUpdate)) {
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
                    flag = 5;
                }

                progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", RaiseComplaintActivity.this);

            } else if (flag == 4) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
                Config_Engg.logout(RaiseComplaintActivity.this);
                Config_Engg.putSharedPreferences(RaiseComplaintActivity.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                ScanckBar();
                btn_submit.setEnabled(false);
                btn_update.setEnabled(false);
                btn_clear.setEnabled(false);
                progressDialog.dismiss();
            }

            progressDialog.dismiss();
        }
    }

    private class AddMachineSerial extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String machine_serial_detail_value, machineList, contactList;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";
        int countMachine = 0;
        int countContact = 0;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RaiseComplaintActivity.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME4);
            String EngineerID = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "AuthCode", "");
            String PlantID = SelctedPlantID;
            request.addProperty("SiteID", PlantID);
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
                    machine_serial_detail_value = result.getProperty(0).toString();

                    Object json = new JSONTokener(machine_serial_detail_value).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(machine_serial_detail_value);
                        JSONArray plantjsonArray = object.getJSONArray("ProductName");
                        machineList = plantjsonArray.toString();
                        JSONArray trancsonArray = object.getJSONArray("ContactPerson");
                        contactList = trancsonArray.toString();
                        if (machine_serial_detail_value.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(machine_serial_detail_value);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            flag = 2;
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(machine_serial_detail_value);
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
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
            } else if (flag == 2) {
                try {
//
                    JSONArray jsonArray2 = new JSONArray(machineList);
                    machineSerialID = new ArrayList<String>();
                    machineSerialID.add(0, "00000000-0000-0000-0000-000000000000");
                    machineSerialName = new ArrayList<String>();
                    machineSerialName.add(0, "Select");

                    for (int i = 0; i < jsonArray2.length(); i++) {
                        countMachine += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String SaleID = jsonObject2.getString("SaleID");
                        String ModelName = jsonObject2.getString("ModelName");

                        machineSerialID.add(i + 1, SaleID);
                        //siteNameList.add(SiteName);
                        machineSerialName.add(i + 1, ModelName);
                    }

                    spinneradapterMachine = new ArrayAdapter<String>(RaiseComplaintActivity.this, android.R.layout.simple_spinner_item, machineSerialName);
                    spinneradapterMachine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_machine_serial.setAdapter(spinneradapterMachine);

                    int index = -1;
                    for (int i = 0; i < machineSerialID.size(); i++) {
                        if (machineSerialID.get(i).equals(SaleIDUpdate)) {
                            index = i;
                            break;
                        }
                    }

                    if (index > 0) {

                        String machineString = machineSerialName.get((int) index);

                        if (!machineString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterMachine.getPosition(machineString);
                            spinner_machine_serial.setSelection(spinnerpos);
                        }
                    } else if (countMachine == 1) {

                        spinner_machine_serial.setSelection(1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }
                try {

                    JSONArray jsonArray2 = new JSONArray(contactList);
                    contactIDList = new ArrayList<String>();
                    contactIDList.add(0, "00000000-0000-0000-0000-000000000000");
                    contactNameList = new ArrayList<String>();
                    contactNameList.add(0, "New");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        countContact += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String ContactPersonID = jsonObject2.getString("ContactPersonID");
                        String ContactPersonName = jsonObject2.getString("ContactPersonName");

                        contactIDList.add(ContactPersonID);
                        contactNameList.add(ContactPersonName);
                    }

                    spinneradapterContact = new ArrayAdapter<String>(RaiseComplaintActivity.this, android.R.layout.simple_spinner_item, contactNameList);
                    spinneradapterContact.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_existing_customer_contacts.setAdapter(spinneradapterContact);

                    int index = -1;
                    for (int i = 0; i < contactIDList.size(); i++) {
                        if (contactIDList.get(i).equals(ContactPersonIDUpdate)) {
                            index = i;
                            break;
                        }
                    }
                    if (index >= 0) {

                        String contacteString = contactNameList.get((int) index);

                        if (!contacteString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterContact.getPosition(contacteString);
                            spinner_existing_customer_contacts.setSelection(spinnerpos);
                        }
                    } else if (countContact == 1) {

                        spinner_existing_customer_contacts.setSelection(1);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }
                progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", RaiseComplaintActivity.this);
//
            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
                Config_Engg.logout(RaiseComplaintActivity.this);
                Config_Engg.putSharedPreferences(RaiseComplaintActivity.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {
                ScanckBar();
                btn_submit.setEnabled(false);
                btn_update.setEnabled(false);
                btn_clear.setEnabled(false);
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
        }
    }

    private class AddMachineChange extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String machine_change_detail_value, machineList;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";
        int countMachine = 0;
        int countContact = 0;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RaiseComplaintActivity.this, "Loading...", "Please Wait....", true, false);
            asm_txt_show.setText("");
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME5);
            String EngineerID = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "AuthCode", "");
            String MachineID = SelctedMachineID;
            request.addProperty("SaleID", MachineID);
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
                    machine_change_detail_value = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(machine_change_detail_value);
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
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
            } else if (flag == 2) {
                try {

                    JSONArray jsonArray2 = new JSONArray(machine_change_detail_value);
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
                    String TransactionType = jsonObject2.getString("TransactionType");
                    String AMCMsg = jsonObject2.getString("AMCMsg");
                    asm_txt_show.setText(AMCMsg);

                    int index = -1;
                    for (int i = 0; i < transactionIDList.size(); i++) {
                        if (transactionIDList.get(i).equals(ComplaintTypeUdate)) {
                            index = i;
                            break;
                        }
                    }

                    if (index > 0) {

                        String transtString = transactionNameList.get((int) index);
                        if (!transtString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterTrans.getPosition(transtString);
                            spinner_complaint_type.setSelection(spinnerpos);
                        }
                    } else {
                        int Transaction = Integer.parseInt(TransactionType);
                        String TransactionString = transactionNameList.get((int) Transaction);

                        if (!TransactionString.equalsIgnoreCase("")) {
                            int spinnerpos = spinneradapterTrans.getPosition(TransactionString);
                            spinner_complaint_type.setSelection(spinnerpos);
                        }

                        if (Transaction <= 2) {
                            asm_txt_show.setVisibility(View.VISIBLE);
                            asm_txt_show.setTextColor(Color.parseColor("#00CC00"));
                        } else if (Transaction == 4) {
                            asm_txt_show.setVisibility(View.VISIBLE);
                            asm_txt_show.setTextColor(Color.parseColor("#0000FF"));
                        } else {
                            asm_txt_show.setVisibility(View.VISIBLE);
                            asm_txt_show.setTextColor(Color.parseColor("#FF0000"));

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }
                progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", RaiseComplaintActivity.this);
//
            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
                Config_Engg.logout(RaiseComplaintActivity.this);
                Config_Engg.putSharedPreferences(RaiseComplaintActivity.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {
                ScanckBar();
                btn_submit.setEnabled(false);
                btn_update.setEnabled(false);
                btn_clear.setEnabled(false);
                progressDialog.dismiss();

            }
            progressDialog.dismiss();
        }
    }

    private class AddContactChange extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String contact_change_detail_value, contactChangeList;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RaiseComplaintActivity.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME6);
            String EngineerID = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "AuthCode", "");
            String ContactPersonID = SelectedContactID;
            request.addProperty("ContactPersonID", ContactPersonID);
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
                    contact_change_detail_value = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(contact_change_detail_value);
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
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
            } else if (flag == 2) {
                try {
                    JSONArray jsonArray2 = new JSONArray(contact_change_detail_value);
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(0);
                    String ContactPersonName = jsonObject2.getString("ContactPersonName");
                    String Email = jsonObject2.getString("Email");
                    String Phone = jsonObject2.getString("Phone");
                    String Code = jsonObject2.getString("CountryCode");

                    txt_new_person_name.setText(ContactPersonName);
                    txt_new_person_name.setEnabled(false);
                    txt_mail.setText(Email);
                    txt_country_code.setText(Code);
                    txt_new_customer_mobile.setText(Phone);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }
                progressDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", RaiseComplaintActivity.this);
//
            } else if (flag == 4) {
                Config_Engg.toastShow(msgstatus, RaiseComplaintActivity.this);
                Config_Engg.logout(RaiseComplaintActivity.this);
                Config_Engg.putSharedPreferences(RaiseComplaintActivity.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {
                ScanckBar();
                btn_submit.setEnabled(false);
                btn_update.setEnabled(false);
                btn_clear.setEnabled(false);
                progressDialog.dismiss();

            }
            progressDialog.dismiss();
        }
    }

    private class RaiseComplaintSubmit extends AsyncTask<String, String, String> {
        int flag;
        String jsonValue, msg;
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RaiseComplaintActivity.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String EngineerID = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(RaiseComplaintActivity.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME7);
            request.addProperty("ComplainNo", complainno);
            request.addProperty("ComplaintTitle", problemTitle);
            request.addProperty("Description", problemDesc);
            request.addProperty("CustomerID", SelctedCustomerID);
            request.addProperty("SiteID", SelctedPlantID);
            request.addProperty("SaleID", SelctedMachineID);
            request.addProperty("ComplaintType", SelectedComplaintTypeID);
            request.addProperty("ComplainServiceTypeID", seletedValue);
            request.addProperty("TimeOfOccurance", ProblemoccurAtDate);
            request.addProperty("ComplainTime", complaintDate);
            request.addProperty("Rectifieddate", expectedDate);
            request.addProperty("WorkStatusID", SelectedWorkStatusID);
            request.addProperty("ContactPersonID", SelectedContactID);
            request.addProperty("ContactPersonName", newpersonName);
            request.addProperty("ContactPersonMailID", mailID);
            request.addProperty("CountryCode",countryCode);
            request.addProperty("ContactPersonMobile", mobile);
            request.addProperty("ContactPersonContactNo", otherContact);
            request.addProperty("MailToCustomer", checkBoxCustomerValue);
            request.addProperty("MailToInternal", checkBoxInternalValue);
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("jsonValue", "cfcs" + jsonValue);
            if (flag == 1) {
                Config_Engg.toastShow(msg, RaiseComplaintActivity.this);
            } else {
                if (flag == 2) {
                    Config_Engg.toastShow(msg, RaiseComplaintActivity.this);
                    Intent intent = new Intent(RaiseComplaintActivity.this, ManageComplaint.class);
                    startActivity(intent);
                    finish();
                } else if (flag == 3) {
                    Config_Engg.toastShow("No Response", RaiseComplaintActivity.this);
                } else if (flag == 4) {
                    Config_Engg.toastShow(msg, RaiseComplaintActivity.this);
                    Config_Engg.logout(RaiseComplaintActivity.this);
                    Config_Engg.putSharedPreferences(RaiseComplaintActivity.this, "checklogin", "status", "2");
                    finish();
                } else if (flag == 5) {
                    ScanckBar();
                    btn_submit.setEnabled(false);
                    btn_update.setEnabled(false);
                    btn_clear.setEnabled(false);
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
            btn_submit.setClickable(true);
        }

    }
}
