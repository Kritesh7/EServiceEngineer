package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.adapter.EngineerWorkStatusListAdapter;
import in.co.cfcs.eserviceengineer.model.EngineerWorkStatusDataModel;

public class EngineerWorkStatusUpdate extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerWorkStatusList";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerWorkStatusList";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    ArrayList<EngineerWorkStatusDataModel> engineerWorkStatusDataModelList = new ArrayList<EngineerWorkStatusDataModel>();

    String[] ComplainNo;
    String[] EngWorkStatus;
    String[] Remark;
    String[] AddDateText;
    String[] EngineerName;
    String[] SpareNo;
    String[] IsEditDelete;

    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;

    String complainno = "";

    ListView list;

    CoordinatorLayout maincontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineer_work_status_update);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        list = (ListView) findViewById(R.id.engg_work_status_update_list_view);
        maincontainer = findViewById(R.id.maincontainer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complainno = getIntent().getExtras().getString("ComplainNo");
//            status = Integer.parseInt(getIntent().getExtras().getString("status"));
        }

        Config_Engg.isOnline(EngineerWorkStatusUpdate.this);
        if (Config_Engg.internetStatus == true) {

            new EngineerWorkStatusUpdateListAsy().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", EngineerWorkStatusUpdate.this);
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
                Intent i = new Intent(EngineerWorkStatusUpdate.this, SubmitEngWorkStatus.class);
                i.putExtra("ComplainNo", complainno);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(EngineerWorkStatusUpdate.this, ManageComplaint.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private class EngineerWorkStatusUpdateListAsy extends AsyncTask<String, String, String> {

        String msgstatus;
        int flag;
        String statusbtn = "";
        String LoginStatus;
        String invalid = "LoginFailed";
        ProgressDialog progressDialog;
        String EngineerWorkStatusList, StatusList;
        String status = "";

        public EngineerWorkStatusUpdate engineerWorkStatusUpdate;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(EngineerWorkStatusUpdate.this, "", "Please wait...", true, false, null);
        }

        @Override
        protected String doInBackground(String... status1) {
            // TODO Auto-generated method stub

            // String status = status1[0];
            int count = 0;

            String EngineerID = Config_Engg.getSharedPreferences(
                    EngineerWorkStatusUpdate.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(
                    EngineerWorkStatusUpdate.this, "pref_Engg", "AuthCode", "");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("ComplainNo", complainno);
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
                    EngineerWorkStatusList = result.getProperty(0).toString();
                    Object json = new JSONTokener(EngineerWorkStatusList).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(EngineerWorkStatusList);
                        JSONArray StatusjsonArray = object.getJSONArray("UpdateStatusList");
                        StatusList = StatusjsonArray.toString();
                        if (EngineerWorkStatusList.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(EngineerWorkStatusList);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            engineerWorkStatusDataModelList.clear();

                            ComplainNo = new String[StatusjsonArray.length()];
                            EngWorkStatus = new String[StatusjsonArray.length()];
                            Remark = new String[StatusjsonArray.length()];
                            AddDateText = new String[StatusjsonArray.length()];
                            EngineerName = new String[StatusjsonArray.length()];
                            SpareNo = new String[StatusjsonArray.length()];
                            IsEditDelete = new String[StatusjsonArray.length()];


                            for (int i = 0; i < StatusjsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject1 = StatusjsonArray
                                            .getJSONObject(i);

                                    EngineerWorkStatusDataModel engineerWorkStatusDataModel = new EngineerWorkStatusDataModel(AuthCode, AuthCode, AuthCode,
                                            AuthCode, AuthCode, AuthCode, AuthCode);
                                    engineerWorkStatusDataModel.setComplainNo(jsonObject1.getString("ComplainNo").toString());
                                    engineerWorkStatusDataModel.setEngWorkStatus(jsonObject1.getString("EngWorkStatus").toString());
                                    engineerWorkStatusDataModel.setRemark(jsonObject1.getString("Remark").toString());
                                    engineerWorkStatusDataModel.setAddDateText(jsonObject1.getString("AddDateText").toString());
                                    engineerWorkStatusDataModel.setEngineerName(jsonObject1.getString("EngineerName").toString());
                                    engineerWorkStatusDataModel.setSpareNo(jsonObject1.getString("SpareNo").toString());
                                    engineerWorkStatusDataModel.setIsEditDelete(jsonObject1.getString("IsEditDelete").toString());

                                    // Add this object into the ArrayList myList

                                    engineerWorkStatusDataModelList.add(engineerWorkStatusDataModel);
                                    flag = 2;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(EngineerWorkStatusList);
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
                    JSONArray jsonArray = new JSONArray(EngineerWorkStatusList);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    msgstatus = jsonObject.getString("MsgNotification");
                    flag = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error is 1 ", e.toString());
                flag = 5;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (flag == 1) {

                Config_Engg.toastShow(msgstatus, EngineerWorkStatusUpdate.this);
                //  progressDialog.dismiss();
                list.setAdapter(null);

            } else {
                if (flag == 2) {
                    list.setAdapter(new EngineerWorkStatusListAdapter(EngineerWorkStatusUpdate.this, engineerWorkStatusDataModelList));
                } else {
                    if (flag == 3) {
                        Config_Engg.toastShow("No Response", EngineerWorkStatusUpdate.this);
                        //    progressDialog.dismiss();
                    } else {
                        if (flag == 4) {

                            Config_Engg.toastShow(msgstatus, EngineerWorkStatusUpdate.this);
                            Config_Engg.logout(EngineerWorkStatusUpdate.this);
                            Config_Engg.putSharedPreferences(EngineerWorkStatusUpdate.this, "checklogin", "status", "2");
                            finish();
                        } else if (flag == 5) {

                            ScanckBar();

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
                        Config_Engg.isOnline(EngineerWorkStatusUpdate.this);
                        if (Config_Engg.internetStatus == true) {

                            new EngineerWorkStatusUpdateListAsy().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", EngineerWorkStatusUpdate.this);
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
                intent = new Intent(EngineerWorkStatusUpdate.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(EngineerWorkStatusUpdate.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(EngineerWorkStatusUpdate.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(EngineerWorkStatusUpdate.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(EngineerWorkStatusUpdate.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(EngineerWorkStatusUpdate.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(EngineerWorkStatusUpdate.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(EngineerWorkStatusUpdate.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(EngineerWorkStatusUpdate.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

}
