package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;

public class ComplaintDetail extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerComplainDetail";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerComplainDetail";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";


    TextView txt_problem_title, txt_problem_description, txt_plant, txt_customer, txt_machine_modal, txt_machine, txt_complaint_type,
            txt_service_type, txt_complaint_date, txt_problem_occurred_at, txt_expected_completion_date, txt_priority, txt_escaltion_level,
            txt_complaint_no, txt_responsible_engg, txt_region, txt_contact_person_name, txt_mobile, txt_other_contact, txt_email,
            txt_work_status, txt_complain_status, txt_complain_log_by;

    String complainno = "", status = "";

    LinearLayout maincontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        txt_problem_title = findViewById(R.id.txt_problem_title);
        txt_problem_description = findViewById(R.id.txt_problem_description);
        txt_plant = findViewById(R.id.txt_plant);
        txt_customer = findViewById(R.id.txt_customer);
        txt_machine_modal = findViewById(R.id.txt_machine_modal);
        txt_machine = findViewById(R.id.txt_machine);
        txt_complaint_type = findViewById(R.id.txt_complaint_type);
        txt_service_type = findViewById(R.id.txt_service_type);
        txt_complaint_date = findViewById(R.id.txt_complaint_date);
        txt_problem_occurred_at = findViewById(R.id.txt_problem_occurred_at);
        txt_expected_completion_date = findViewById(R.id.txt_expected_completion_date);
        txt_priority = findViewById(R.id.txt_priority);
        txt_escaltion_level = findViewById(R.id.txt_escaltion_level);
        txt_complaint_no = findViewById(R.id.txt_complaint_no);
        txt_responsible_engg = findViewById(R.id.txt_responsible_engg);
        txt_region = findViewById(R.id.txt_region);
        txt_contact_person_name = findViewById(R.id.txt_contact_person_name);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_other_contact = findViewById(R.id.txt_other_contact);
        txt_email = findViewById(R.id.txt_email);
        txt_work_status = findViewById(R.id.txt_work_status);
        txt_complain_status = findViewById(R.id.txt_complain_status);
        txt_complain_log_by = findViewById(R.id.txt_complain_log_by);
        maincontainer = findViewById(R.id.maincontainer);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complainno = getIntent().getExtras().getString("ComplainNo");
            status = getIntent().getExtras().getString("status");
        }

        Config_Engg.isOnline(ComplaintDetail.this);
        if (Config_Engg.internetStatus == true) {

            new ComplainDetailAsy().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ComplaintDetail.this);
        }
    }

    private class ComplainDetailAsy extends AsyncTask<String, String, String> {
        int flag;
        String msgstatus;
        String complain_detail_value;
        ProgressDialog progressDialog;
        String ComplaintDetail, ComplainSubordinate;
        JSONArray complainSub;
        String LoginStatus;
        String invalid = "LoginFailed";
        TextView txt_sub_engineer_name;
        int count = 0;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ComplaintDetail.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            String AuthCode = Config_Engg.getSharedPreferences(ComplaintDetail.this, "pref_Engg", "AuthCode", "");
            String EngineerID = Config_Engg.getSharedPreferences(ComplaintDetail.this, "pref_Engg", "EngineerID", "");
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
                Config_Engg.toastShow(msgstatus, ComplaintDetail.this);
            } else if (flag == 2) {
                try {
                    JSONArray jsonArray = new JSONArray(ComplaintDetail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String ComplainNo = jsonObject.getString("ComplainNo").toString();
                    txt_complaint_no.setText(ComplainNo);

                    String ProblemTitle = jsonObject.getString("ComplaintTitle").toString();
                    txt_problem_title.setText(ProblemTitle);

                    String Description = jsonObject.getString("Description").toString();
                    txt_problem_description.setText(Description);

                    String ComplainDateTimeText = jsonObject.getString("ComplainDateTimeText").toString();
                    txt_complaint_date.setText(ComplainDateTimeText);

                    String TimeOfOccuranceDateTimeText = jsonObject.getString("TimeOfOccuranceDateTimeText").toString();
                    txt_problem_occurred_at.setText(TimeOfOccuranceDateTimeText);

                    String RectifiedDateTimeText = jsonObject.getString("RectifiedDateTimeText").toString();
                    txt_expected_completion_date.setText(RectifiedDateTimeText);

//                    String AddDateTimeText = jsonObject.getString("AddDateTimeText").toString();
//                    txt_principal_detail.setText(AddDateTimeText);

                    String SiteAddress = jsonObject.getString("SiteAddress").toString();
                    txt_plant.setText(SiteAddress);


                    String CustomerName = jsonObject.getString("CustomerName").toString();
                    txt_customer.setText(CustomerName);

//                    String ParentCustomerName = jsonObject.getString("ParentCustomerName").toString();
//                    txt_complain_detail_time.setText(ParentCustomerName);


                    //     String PrincipalName = jsonObject.getString("PrincipalName").toString();

                    String SubMachineModelName = jsonObject.getString("SubMachineModelName").toString();
                    txt_machine_modal.setText(SubMachineModelName);

                    String MachineSerialNo = jsonObject.getString("MachineSerialNo").toString();
                    txt_machine.setText(MachineSerialNo);

                    String PriorityText = jsonObject.getString("PriorityText").toString();
                    txt_priority.setText(PriorityText);

                    String EngineerName = jsonObject.getString("EngineerName").toString();
                    txt_responsible_engg.setText(EngineerName);

                    String ZoneName = jsonObject.getString("ZoneName").toString();
                    txt_region.setText(ZoneName);

                    String ComplaintTypeText = jsonObject.getString("ComplaintTypeText").toString();
                    txt_complaint_type.setText(ComplaintTypeText);

                    String ComplainServiceTypeName = jsonObject.getString("ComplainServiceTypeName").toString();
                    txt_service_type.setText(ComplainServiceTypeName);

                    String ComplainByName = jsonObject.getString("ComplainByName").toString();
                    txt_complain_log_by.setText(ComplainByName);

                    String EscalationName = jsonObject.getString("EscalationName").toString();
                    txt_escaltion_level.setText(EscalationName);

                    String ContactPersonName = jsonObject.getString("ContactPersonName").toString();
                    txt_contact_person_name.setText(ContactPersonName);

                    String ContactPersonMailID = jsonObject.getString("ContactPersonMailID").toString();
                    txt_email.setText(ContactPersonMailID);

                    String ContactPersonMobile = jsonObject.getString("ContactPersonMobile").toString();
                    txt_mobile.setText(ContactPersonMobile);

                    String ContactPersonContactNo = jsonObject.getString("ContactPersonContactNo").toString();
                    txt_other_contact.setText(ContactPersonContactNo);

                    String WorkStatusName = jsonObject.getString("WorkStatusName").toString();
                    txt_work_status.setText(WorkStatusName);

                    String StatusText = jsonObject.getString("StatusText").toString();
                    txt_complain_status.setText(StatusText);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", ComplaintDetail.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, ComplaintDetail.this);
                Config_Engg.logout(ComplaintDetail.this);
                Config_Engg.putSharedPreferences(ComplaintDetail.this, "checklogin", "status", "2");
                finish();

            } else if (flag == 5) {

                ScanckBar();
                progressDialog.dismiss();
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

                        Config_Engg.isOnline(ComplaintDetail.this);
                        if (Config_Engg.internetStatus == true) {

                            new ComplainDetailAsy().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", ComplaintDetail.this);
                        }


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
                intent = new Intent(ComplaintDetail.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(ComplaintDetail.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(ComplaintDetail.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(ComplaintDetail.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(ComplaintDetail.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(ComplaintDetail.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(ComplaintDetail.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(ComplaintDetail.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(ComplaintDetail.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ComplaintDetail.this, ManageComplaint.class);
        intent.putExtra("status", status);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
