package in.co.cfcs.eserviceengineer.activity_engineer;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;

public class DashboardActivity extends AppCompatActivity {


    Button btn_raise_complain, btn_manage_complain, btn_manage_machines, btn_manage_contact, btn_feedback, btn_logout;

    String currentVersion = null;

    String newVersion = null;

    ProgressBar request_status;

    BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;

    LinearLayout maincontainer;


    PieChart pieChart ;
    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;

    BarChart barchart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<String> BarEntryLable;
    ArrayList<BarEntry> entriesbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_bottom_sheet);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //initiate widgets
        btn_raise_complain = findViewById(R.id.btn_raise_complain);
        btn_manage_complain = findViewById(R.id.btn_manage_complain);
        btn_manage_machines = findViewById(R.id.btn_manage_machines);
        btn_manage_contact = findViewById(R.id.btn_manage_contact);
        btn_feedback = findViewById(R.id.btn_feedback);
        btn_logout = findViewById(R.id.btn_logout);
        request_status = findViewById(R.id.request_status);

        maincontainer = findViewById(R.id.maincontainer);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setPeekHeight(130);


        request_status.setProgress(20);


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Config_Engg.isOnline(DashboardActivity.this);
        if (Config_Engg.internetStatus == true) {

        //    new ForceUpdateAsync(currentVersion).execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", DashboardActivity.this);
            finish();
        }

//        //Click Listener
        btn_raise_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, RaiseComplaintActivity.class);
                startActivity(i);
            }
        });

        btn_manage_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ManageComplaint.class);
                startActivity(i);
            }
        });

        btn_manage_machines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ManageMachines.class);
                startActivity(i);
            }
        });
        btn_manage_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ManageContact.class);
                startActivity(i);
            }
        });

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, FeedbackActivity.class);
                startActivity(i);
              //  Toast.makeText(DashboardActivity.this, "Implementation In Process", Toast.LENGTH_LONG).show();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Config_Engg.logout(DashboardActivity.this);
                Config_Engg.putSharedPreferences(DashboardActivity.this, "checklogin", "status", "2");
                finish();
            }
        });

        pieChart = (PieChart) findViewById(R.id.chart1);

        barchart = (BarChart) findViewById(R.id.chart2);

        entries = new ArrayList<>();

        entriesbar = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        BarEntryLable = new ArrayList<String>();

        AddValuesToPIEENTRY();

        AddValuesToBarENTRY();

        AddValuesToPieEntryLabels();

        AddValuesToBarEntryLabels();

        pieDataSet = new PieDataSet(entries, "");
        barDataSet = new BarDataSet(entriesbar,"");

        pieData = new PieData(PieEntryLabels, pieDataSet);
        barData = new BarData(BarEntryLable,barDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);
        barchart.setData(barData);

        pieChart.animateY(3000);
        barchart.animateY(3000);

        pieChart.setHighlightPerTapEnabled(true);
        barchart.setHighlightPerTapEnabled(true);

        pieChart.setCenterText("Machine Status");

        pieChart.setDescription(null);

        barchart.setDescription(null);

        pieChart.setOnChartValueSelectedListener( new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //fire up event

                //   Toast.makeText(Dashboard.this, "item Clicked "+dataSetIndex +" "+ e, Toast.LENGTH_SHORT).show();
                if(e.getXIndex() == 0){

                //    Toast.makeText(DashboardActivity.this, "item Clicked 0 "+dataSetIndex +" "+ e, Toast.LENGTH_SHORT).show();

                } else if(e.getXIndex() == 1){

                 //   Toast.makeText(DashboardActivity.this, "item Clicked 1"+dataSetIndex +" "+ e, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void AddValuesToPIEENTRY(){

        entries.add(new BarEntry(2f, 0));
        entries.add(new BarEntry(4f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(8f, 3));

    }

    public void AddValuesToPieEntryLabels(){

        PieEntryLabels.add("AMC");
        PieEntryLabels.add("Warranty");
        PieEntryLabels.add("Paid");
        PieEntryLabels.add("FOC");


    }

    public void AddValuesToBarENTRY(){

        entriesbar.add(new BarEntry(2f, 0));
        entriesbar.add(new BarEntry(4f, 1));
        entriesbar.add(new BarEntry(6f, 2));
        entriesbar.add(new BarEntry(8f, 3));
        entriesbar.add(new BarEntry(7f, 4));
        entriesbar.add(new BarEntry(3f, 5));
        entriesbar.add(new BarEntry(2f, 6));

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLable.add("January");
        BarEntryLable.add("February");
        BarEntryLable.add("March");
        BarEntryLable.add("April");
        BarEntryLable.add("May");
        BarEntryLable.add("June");
        BarEntryLable.add("July");

    }

    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        public ForceUpdateAsync(String currentVersion){
            this.currentVersion = currentVersion;

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="+getBaseContext().getPackageName()+"&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion!=null && !latestVersion.isEmpty()){
                if (Float.valueOf(currentVersion) < Float.valueOf(latestVersion)) {
                    //show dialog
                    showForceUpdateDialog();
                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(DashboardActivity.this,
                    R.style.LibAppTheme));

            alertDialogBuilder.setTitle(DashboardActivity.this.getString(R.string.youAreNotUpdatedTitle));
            alertDialogBuilder.setMessage(DashboardActivity.this.getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + DashboardActivity.this.getString(R.string.youAreNotUpdatedMessage1));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    DashboardActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + DashboardActivity.this.getPackageName())));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
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
                intent = new Intent(DashboardActivity.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.logout:
                Config_Engg.logout(DashboardActivity.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.profile:
                intent = new Intent(DashboardActivity.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_raise:
                intent = new Intent(DashboardActivity.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_complain:
                intent = new Intent(DashboardActivity.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_machines:
                intent = new Intent(DashboardActivity.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_contact:
                intent = new Intent(DashboardActivity.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(DashboardActivity.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
