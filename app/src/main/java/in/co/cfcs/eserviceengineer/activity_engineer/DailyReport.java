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
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.adapter.DailyReportListAdapter;
import in.co.cfcs.eserviceengineer.model.DailyReportDataModel;

public class DailyReport extends AppCompatActivity {


    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerDailyReportList";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerDailyReportList";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    ArrayList<DailyReportDataModel> dailyReportList = new ArrayList<DailyReportDataModel>();

    String[] DailyReportNo;
    String[] Workdone;
    String[] ComplainNo;
    String[] DailyReportDateText;
    String[] Workdone1;
    String[] NextFollowUpDateText;
    String[] NextFollowUpTimeText;
    String[] Traveltime;
    String[] Servicetime;
    String[] IsDelete;
    String[] IsEdit;

    FloatingActionButton fab, fab1, fab2;
    LinearLayout fabLayout1, fabLayout2;
    View fabBGLayout;
    boolean isFABOpen = false;
    String complainno;
    ListView list;
    CoordinatorLayout maincontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        list = findViewById(R.id.daily_report_list_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complainno = getIntent().getExtras().getString("ComplainNo");
//            status = Integer.parseInt(getIntent().getExtras().getString("status"));
        }


        fab = findViewById(R.id.fab);
        maincontainer = findViewById(R.id.maincontainer);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DailyReport.this, AddDailyReport.class);
                i.putExtra("ComplainNo", complainno);
                i.putExtra("IsEditCheck", "false");
                i.putExtra("DailyReportNo", "0");
                startActivity(i);
            }
        });

        Config_Engg.isOnline(DailyReport.this);
        if (Config_Engg.internetStatus == true) {

            new DailyReportAsy().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", DailyReport.this);
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(DailyReport.this, ManageComplaint.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private class DailyReportAsy extends AsyncTask<String, String, String> {

        int flag = 0;
        String DailyReportList;
        String LoginStatus;
        String invalid = "LoginFailed";
        String msgstatus;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DailyReport.this, "Lodaing", "Please wait...", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(DailyReport.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(DailyReport.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("ComplainNo", complainno);
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
                    DailyReportList = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(DailyReportList);
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

                        dailyReportList.clear();

                        DailyReportNo = new String[jsonArray.length()];
                        Workdone = new String[jsonArray.length()];
                        ComplainNo = new String[jsonArray.length()];
                        DailyReportDateText = new String[jsonArray.length()];
                        Workdone1 = new String[jsonArray.length()];
                        NextFollowUpDateText = new String[jsonArray.length()];
                        NextFollowUpTimeText = new String[jsonArray.length()];
                        Traveltime = new String[jsonArray.length()];
                        Servicetime = new String[jsonArray.length()];
                        IsDelete = new String[jsonArray.length()];
                        IsEdit = new String[jsonArray.length()];


                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject1 = jsonArray
                                        .getJSONObject(i);

                                DailyReportDataModel dailyReportDataModel = new DailyReportDataModel(AuthCode, AuthCode, AuthCode, AuthCode,
                                        AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode, AuthCode);
                                dailyReportDataModel.setDailyReportNo(jsonObject1.getString("DailyReportNo").toString());
                                dailyReportDataModel.setWorkdone(jsonObject1.getString("Workdone").toString());
                                dailyReportDataModel.setComplainNo(jsonObject1.getString("ComplainNo").toString());
                                dailyReportDataModel.setDailyReportDateText(jsonObject1.getString("DailyReportDateText").toString());
                                dailyReportDataModel.setWorkdone1(jsonObject1.getString("Workdone1").toString());
                                dailyReportDataModel.setNextFollowUpDateText(jsonObject1.getString("NextFollowUpDateText").toString());
                                dailyReportDataModel.setNextFollowUpTimeText(jsonObject1.getString("NextFollowUpTimeText").toString());
                                dailyReportDataModel.setTraveltime(jsonObject1.getString("Traveltime").toString());
                                dailyReportDataModel.setServicetime(jsonObject1.getString("Servicetime").toString());
                                dailyReportDataModel.setIsDelete(jsonObject1.getString("IsDelete").toString());
                                dailyReportDataModel.setIsEdit(jsonObject1.getString("IsEdit").toString());

                                // Add this object into the ArrayList myList

                                dailyReportList.add(dailyReportDataModel);
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

                Config_Engg.toastShow(msgstatus, DailyReport.this);
                //  progressDialog.dismiss();
                list.setAdapter(null);
            } else {
                if (flag == 2) {
                    list.setAdapter(new DailyReportListAdapter(DailyReport.this, dailyReportList));
                } else {
                    if (flag == 3) {
                        Config_Engg.toastShow("No Response", DailyReport.this);
                        //    progressDialog.dismiss();
                    } else {
                        if (flag == 4) {
                            Config_Engg.toastShow(msgstatus, DailyReport.this);
                            Config_Engg.logout(DailyReport.this);
                            Config_Engg.putSharedPreferences(DailyReport.this, "checklogin", "status", "2");
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
                        Config_Engg.isOnline(DailyReport.this);
                        if (Config_Engg.internetStatus == true) {

                            new DailyReportAsy().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", DailyReport.this);
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
                intent = new Intent(DailyReport.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(DailyReport.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(DailyReport.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(DailyReport.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(DailyReport.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(DailyReport.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(DailyReport.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(DailyReport.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(DailyReport.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

}
