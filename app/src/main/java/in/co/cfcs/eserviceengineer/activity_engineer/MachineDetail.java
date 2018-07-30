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

public class MachineDetail extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerMachineSalesInfoDetail";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerMachineSalesInfoDetail";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";


    String saleID = "";

    TextView txt_customer, txt_plant, txt_plant_address, txt_principal_name, txt_product_model, txt_product_sub_model, txt_product_serial_no, txt_sw_version,
            txt_product_key, txt_date_of_supply, txt_date_install, txt_machine_status, txt_comment, txt_warranty_s_date, txt_warranty_e_date,
            txt_amc_start_date, txt_amc_end_date, txt_approval_status, txt_approval_remark;

    LinearLayout maincontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_detail);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        txt_customer = findViewById(R.id.txt_customer);
        txt_plant_address = findViewById(R.id.txt_plant_address);
        txt_principal_name = findViewById(R.id.txt_principal_name);
        txt_product_model = findViewById(R.id.txt_product_model);
        txt_product_serial_no = findViewById(R.id.txt_product_serial_no);
        txt_sw_version = findViewById(R.id.txt_sw_version);
        txt_product_key = findViewById(R.id.txt_product_key);
        txt_date_of_supply = findViewById(R.id.txt_date_of_supply);
        txt_date_install = findViewById(R.id.txt_date_install);
        txt_machine_status = findViewById(R.id.txt_machine_status);
        txt_comment = findViewById(R.id.txt_comment);
        txt_warranty_s_date = findViewById(R.id.txt_warranty_s_date);
        txt_warranty_e_date = findViewById(R.id.txt_warranty_e_date);
        txt_amc_start_date = findViewById(R.id.txt_amc_start_date);
        txt_amc_end_date = findViewById(R.id.txt_amc_end_date);
        txt_approval_status = findViewById(R.id.txt_approval_status);
        txt_approval_remark = findViewById(R.id.txt_approval_remark);

        maincontainer = findViewById(R.id.maincontainer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            saleID = getIntent().getExtras().getString("SaleID");
//            status = getIntent().getExtras().getString("status");
        }

        Config_Engg.isOnline(MachineDetail.this);
        if (Config_Engg.internetStatus == true) {

            new MachineDetailAsy().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", MachineDetail.this);
        }

    }

    private class MachineDetailAsy extends AsyncTask<String, String, String> {
        int flag;
        String msgstatus;
        String machine_detail_value;
        ProgressDialog progressDialog;
        String MachineDetail;
        JSONArray complainSub;
        String LoginStatus;
        String invalid = "LoginFailed";
        TextView txt_sub_engineer_name;
        int count = 0;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MachineDetail.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            String AuthCode = Config_Engg.getSharedPreferences(MachineDetail.this, "pref_Engg", "AuthCode", "");
            String EngineerID = Config_Engg.getSharedPreferences(MachineDetail.this, "pref_Engg", "EngineerID", "");
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("SaleID", saleID);
            request.addProperty("AuthCode", AuthCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION1, envelope);
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
        protected void onPostExecute(String complain_detail_value) {
            super.onPostExecute(complain_detail_value);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, MachineDetail.this);
            } else if (flag == 2) {
                try {
                    JSONArray jsonArray = new JSONArray(MachineDetail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String CustomerName = jsonObject.getString("CustomerName").toString();
                    txt_customer.setText(CustomerName);

                    String SiteAddress = jsonObject.getString("SiteAddress").toString();
                    txt_plant_address.setText(SiteAddress);

                    String PrincipleName = jsonObject.getString("PrincipleName").toString();
                    txt_principal_name.setText(PrincipleName);

                    String ModelName = jsonObject.getString("ModelName").toString();
                    txt_product_model.setText(ModelName);

                    String SerialNo = jsonObject.getString("SerialNo").toString();
                    txt_product_serial_no.setText(SerialNo);

                    String SWVersion = jsonObject.getString("SWVersion").toString();
                    txt_sw_version.setText(SWVersion);

                    String ProductKey = jsonObject.getString("ProductKey").toString();
                    txt_product_key.setText(ProductKey);

                    String DateOfSupply = jsonObject.getString("DateOfSupply").toString();
                    txt_date_of_supply.setText(DateOfSupply);

                    String DateOfInstallation = jsonObject.getString("DateOfInstallation").toString();
                    txt_date_install.setText(DateOfInstallation);

                    String TransactionTypeText = jsonObject.getString("TransactionTypeText").toString();
                    txt_machine_status.setText(TransactionTypeText);

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

                    String ApproveStatusName = jsonObject.getString("ApproveStatusName").toString();
                    txt_approval_status.setText(ApproveStatusName);

                    String ApproveStatusRemark = jsonObject.getString("ApproveStatusRemark").toString();
                    txt_approval_remark.setText(ApproveStatusRemark);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", MachineDetail.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, MachineDetail.this);
                Config_Engg.logout(MachineDetail.this);
                Config_Engg.putSharedPreferences(MachineDetail.this, "checklogin", "status", "2");
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
                        if (Config_Engg.internetStatus == true) {

                            new MachineDetailAsy().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", MachineDetail.this);
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
                intent = new Intent(MachineDetail.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(MachineDetail.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(MachineDetail.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(MachineDetail.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(MachineDetail.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(MachineDetail.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(MachineDetail.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(MachineDetail.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(MachineDetail.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MachineDetail.this, ManageMachines.class);
        // intent.putExtra("status", status);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
