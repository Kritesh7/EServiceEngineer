package in.co.cfcs.eserviceengineer.activity_engineer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.io.InputStream;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;

public class DailyReportDetail extends AppCompatActivity {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerDailyReportdetails";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerDailyReportdetails";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    String dailyreportNo;

    TextView txt_work_done_plant, txt_work_detail, txt_suggestion, txt_cause_of_faliure, txt_next_follow_up, txt_reason_close, txt_travel_cost,
            txt_other_exp, txt_travel_time, txt_service_time, txt_engg_exp_detail, txt_sign_by_name, txt_sign_by_mobile, txt_customer_remark, txt_sign_by_email, txt_service_charge;

    LinearLayout add_card_view_spare_part;

    Bitmap bitmap = null;

    ImageView imv_signature;

    String complainno;

    LinearLayout maincontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report_detail);


        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        txt_work_done_plant = findViewById(R.id.txt_work_done_plant);
        txt_work_detail = findViewById(R.id.txt_work_detail);
        txt_suggestion = findViewById(R.id.txt_suggestion);
        txt_next_follow_up = findViewById(R.id.txt_next_follow_up);
        txt_reason_close = findViewById(R.id.txt_reason_close);
        txt_travel_cost = findViewById(R.id.txt_travel_cost);
        txt_other_exp = findViewById(R.id.txt_other_exp);
        txt_travel_time = findViewById(R.id.txt_travel_time);
        txt_service_time = findViewById(R.id.txt_service_time);
        txt_engg_exp_detail = findViewById(R.id.txt_engg_exp_detail);
        txt_sign_by_name = findViewById(R.id.txt_sign_by_name);
        txt_sign_by_mobile = findViewById(R.id.txt_sign_by_mobile);
        txt_sign_by_email = findViewById(R.id.txt_sign_by_email);
        txt_service_charge = findViewById(R.id.txt_service_charge);
        txt_customer_remark = findViewById(R.id.txt_customer_remark);
        imv_signature = findViewById(R.id.imv_signature);
        maincontainer = findViewById(R.id.maincontainer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dailyreportNo = getIntent().getExtras().getString("DailyReportNo");
            complainno = getIntent().getExtras().getString("ComplainNo");
        }

        Config_Engg.isOnline(DailyReportDetail.this);
        if (Config_Engg.internetStatus == true) {

            new DailyReportDetailAsy().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", DailyReportDetail.this);
        }

    }

    private class DailyReportDetailAsy extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String dailyReport_detail_value, DailyReportdetail;
        String SparesConsumed;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(DailyReportDetail.this, "Loading...", "Please Wait....", true, false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String EngineerID = Config_Engg.getSharedPreferences(DailyReportDetail.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(DailyReportDetail.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("DailyReportNo", dailyreportNo);
            request.addProperty("AuthCode", AuthCode);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    dailyReport_detail_value = result.getProperty(0).toString();

                    Object json = new JSONTokener(dailyReport_detail_value).nextValue();
                    if (json instanceof JSONObject) {
                        JSONObject object = new JSONObject(dailyReport_detail_value);
                        JSONArray dailyReportdetail = object.getJSONArray("DailyReportdetail");
                        DailyReportdetail = dailyReportdetail.toString();
                        JSONArray sparesConsumed = object.getJSONArray("SparesConsumed");
                        SparesConsumed = sparesConsumed.toString();
                        if (dailyReport_detail_value.compareTo("true") == 0) {
                            JSONArray jsonArray = new JSONArray(dailyReport_detail_value);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            msgstatus = jsonObject.getString("MsgNotification");
                            flag = 1;
                        } else {
                            flag = 2;
                            JSONObject jsonObject = dailyReportdetail.getJSONObject(0);
                            String signUrl = jsonObject.getString("SignatureImage1").toString();
                            setSign(Config_Engg.BASE_URL + signUrl);
                        }

                    } else if (json instanceof JSONArray) {

                        JSONArray jsonArray = new JSONArray(dailyReport_detail_value);
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
                    JSONArray jsonArray = new JSONArray(dailyReport_detail_value);
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
                Config_Engg.toastShow(msgstatus, DailyReportDetail.this);
            } else if (flag == 2) {
                try {
                    JSONArray jsonArray = new JSONArray(DailyReportdetail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String Workdone = jsonObject.getString("Workdone").toString();
                    txt_work_done_plant.setText(Workdone);

                    String WorkDetail = jsonObject.getString("WorkDetail").toString();
                    txt_work_detail.setText(WorkDetail);

                    String Suggestion = jsonObject.getString("Suggestion").toString();
                    txt_suggestion.setText(Suggestion);

                    String NextFollowUpTimeText = jsonObject.getString("NextFollowUpTimeText").toString();
                    String NextFollowUpDateText = jsonObject.getString("NextFollowUpDateText").toString();
                    txt_next_follow_up.setText(NextFollowUpDateText + " " + NextFollowUpTimeText);

                    String ReasonForNotClose = jsonObject.getString("ReasonForNotClose").toString();
                    txt_reason_close.setText(ReasonForNotClose);

                    String EngineerTravelExpense = jsonObject.getString("EngineerTravelExpense").toString();
                    txt_travel_cost.setText(EngineerTravelExpense);

                    String EngineerOtherExpense = jsonObject.getString("EngineerOtherExpense").toString();
                    txt_other_exp.setText(EngineerOtherExpense);

                    String Traveltime = jsonObject.getString("Traveltime").toString();
                    txt_travel_time.setText(Traveltime);

                    String Servicetime = jsonObject.getString("Servicetime").toString();
                    txt_service_time.setText(Servicetime);

                    String ExpenseDetail = jsonObject.getString("ExpenseDetail").toString();
                    txt_engg_exp_detail.setText(ExpenseDetail);

                    String SignByName = jsonObject.getString("SignByName").toString();
                    txt_sign_by_name.setText(SignByName);

                    String SignByMobile = jsonObject.getString("SignByMobile").toString();
                    txt_sign_by_mobile.setText(SignByMobile);

                    String SignByMailID = jsonObject.getString("SignByMailID").toString();
                    txt_sign_by_email.setText(SignByMailID);

                    String CustomerRemark = jsonObject.getString("CustomerRemark").toString();
                    txt_customer_remark.setText(CustomerRemark);

                    String ServiceCharge = jsonObject.getString("ServiceChargeAmount").toString();
                    txt_service_charge.setText(ServiceCharge);

                    imv_signature.setImageBitmap(bitmap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {

                    add_card_view_spare_part = findViewById(R.id.add_card_view_spare_part);

                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lparams.setMargins(50, 8, 50, 8);
                    CardView cardView = new CardView(DailyReportDetail.this);
                    cardView.setLayoutParams(lparams);
                    // Set CardView corner radius
                    cardView.setRadius(5);
                    // Set cardView content padding
                    // cardView.setContentPadding(15, 8, 15, 8);
                    // Set a background color for CardView
                    //  cardView.setCardBackgroundColor(Color.parseColor("#FFC6D6C3"));
                    // Set the CardView maximum elevation
                    // cardView.setMaxCardElevation(20);
                    cardView.setClickable(true);
                    // Set CardView elevation
                    cardView.setCardElevation(20);
                    add_card_view_spare_part.addView(cardView);

                    LinearLayout parentInCardView = new LinearLayout(DailyReportDetail.this);
                    parentInCardView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    parentInCardView.setOrientation(LinearLayout.VERTICAL);
                    parentInCardView.setPadding(20, 20, 20, 20);
                    cardView.addView(parentInCardView);

                    LinearLayout childSpareDetail = new LinearLayout(DailyReportDetail.this);
                    childSpareDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    childSpareDetail.setOrientation(LinearLayout.HORIZONTAL);
                    parentInCardView.addView(childSpareDetail);

                    LinearLayout.LayoutParams Textparams = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.3f);

                    TextView SNo = new TextView(DailyReportDetail.this);
                    SNo.setLayoutParams(Textparams);
                    SNo.setText("SNo");
                    SNo.setTypeface(null, Typeface.BOLD);

                    LinearLayout.LayoutParams Textparams2 = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.3f);

                    TextView Part_No = new TextView(DailyReportDetail.this);
                    Part_No.setLayoutParams(Textparams2);
                    Part_No.setText("Part No");
                    Part_No.setTypeface(null, Typeface.BOLD);


                    TextView Qty = new TextView(DailyReportDetail.this);
                    Qty.setLayoutParams(Textparams);
                    Qty.setText("Qty");
                    Qty.setTypeface(null, Typeface.BOLD);

                    childSpareDetail.addView(SNo);
                    childSpareDetail.addView(Part_No);
                    childSpareDetail.addView(Qty);

                    JSONArray jsonArray = new JSONArray(SparesConsumed);
                    //  JSONObject jsonObject = jsonArray.getJSONObject(0);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String DailyReportNo = jsonObject2.getString("DailyReportNo");
                        String SpareID = jsonObject2.getString("SpareID");
                        String PartNo = jsonObject2.getString("PartNo");
                        String SpareDesc = jsonObject2.getString("SpareDesc");
                        String SpareQuantity = jsonObject2.getString("SpareQuantity");
                        String OrderBy = jsonObject2.getString("OrderBy");
                        String SpareDate = jsonObject2.getString("SpareDate");

                        LinearLayout.LayoutParams forMarginTop = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        forMarginTop.setMargins(0, 10, 0, 0);

                        LinearLayout childSpareDetailDynamic = new LinearLayout(DailyReportDetail.this);
                        childSpareDetailDynamic.setLayoutParams(forMarginTop);
                        childSpareDetailDynamic.setOrientation(LinearLayout.HORIZONTAL);
                        parentInCardView.addView(childSpareDetailDynamic);

                        LinearLayout.LayoutParams forMarginBottom = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        forMarginBottom.setMargins(0, 10, 0, 0);

                        LinearLayout childSpareDetailDecs = new LinearLayout(DailyReportDetail.this);
                        childSpareDetailDecs.setLayoutParams(forMarginBottom);
                        childSpareDetailDecs.setOrientation(LinearLayout.HORIZONTAL);
                        parentInCardView.addView(childSpareDetailDecs);

                        LinearLayout.LayoutParams Textparams3 = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.3f);
//                        Textparams.setMargins(50, 8, 50, 8);


                        TextView SNo1 = new TextView(DailyReportDetail.this);
                        SNo1.setLayoutParams(Textparams3);
                        SNo1.setText(SpareID);
                        //  SNo1.setTypeface(null, Typeface.BOLD);

                        LinearLayout.LayoutParams Textparams4 = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 3.3f);
                        Textparams4.setMargins(0, 10, 0, 10);

                        TextView Part_No1 = new TextView(DailyReportDetail.this);
                        Part_No1.setLayoutParams(Textparams4);
                        Part_No1.setText(PartNo);
                        // Part_No1.setTypeface(null, Typeface.BOLD);

                        TextView Qty1 = new TextView(DailyReportDetail.this);
                        Qty1.setLayoutParams(Textparams3);
                        Qty1.setText(SpareQuantity);
                        // Qty1.setTypeface(null, Typeface.BOLD);

                        LinearLayout.LayoutParams Textparams6 = new LinearLayout.LayoutParams(
                                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                        // Textparams6.setMargins(0,0,0,10);

                        TextView DescText = new TextView(DailyReportDetail.this);
                        DescText.setLayoutParams(Textparams6);
                        DescText.setText("Detail : " + SpareDesc);

                        LinearLayout.LayoutParams Textparams7 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        Textparams7.setMargins(0, 5, 0, 0);

                        View view001 = new View(DailyReportDetail.this);
                        view001.setLayoutParams(Textparams7);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            view001.setElevation(2);
                        }
                        view001.setBackgroundColor(Color.parseColor("#e0e0e0"));

                        childSpareDetailDynamic.addView(SNo1);
                        childSpareDetailDynamic.addView(Part_No1);
                        childSpareDetailDynamic.addView(Qty1);

                        childSpareDetailDecs.addView(DescText);

                        parentInCardView.addView(view001);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", DailyReportDetail.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, DailyReportDetail.this);
                Config_Engg.logout(DailyReportDetail.this);
                Config_Engg.putSharedPreferences(DailyReportDetail.this, "checklogin", "status", "2");
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
                        Config_Engg.isOnline(DailyReportDetail.this);
                        if (Config_Engg.internetStatus == true) {

                            new DailyReportDetailAsy().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", DailyReportDetail.this);
                        }
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();

    }

    private void setSign(String s) {
        Log.e("signUrl", "cfcs " + s);
        try {
            InputStream inputStream = new java.net.URL(s).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
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
                intent = new Intent(DailyReportDetail.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(DailyReportDetail.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(DailyReportDetail.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(DailyReportDetail.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_raise:
                intent = new Intent(DailyReportDetail.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(DailyReportDetail.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(DailyReportDetail.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(DailyReportDetail.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(DailyReportDetail.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(DailyReportDetail.this, DailyReport.class);
        i.putExtra("ComplainNo", complainno);
        startActivity(i);
    }
}
