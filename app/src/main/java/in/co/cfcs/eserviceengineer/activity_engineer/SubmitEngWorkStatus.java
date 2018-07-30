package in.co.cfcs.eserviceengineer.activity_engineer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.model.DecodeImageBean;
import in.co.cfcs.eserviceengineer.model.SparePartListDataModel;
import in.co.cfcs.eserviceengineer.utils.GPSTracker;
import in.co.cfcs.eserviceengineer.utils.SimpleSpanBuilder;
import in.co.cfcs.kriteshfilepicker.FilePickerBuilder;
import in.co.cfcs.kriteshfilepicker.FilePickerConst;
import in.co.cfcs.kriteshfilepicker.utils.Orientation;

public class SubmitEngWorkStatus extends AppCompatActivity implements View.OnClickListener {

    private static String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerWorkStatusIns";
    private static String NAMESPACE = "http://cfcs.co.in/";
    private static String METHOD_NAME1 = "AppEngineerWorkStatusIns";
    private static String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

    private static String SOAP_ACTION2 = "http://cfcs.co.in/AppEngineerWorkStatusMaster";
    private static String METHOD_NAME2 = "AppEngineerWorkStatusMaster";

    Spinner spinner_engg_work_status;

    EditText txt_remark;

    Button btn_submit_work_status, btn_spare_search, btn_take_image, btn_image_view;

    int currentapiVersion = 0;

    public GPSTracker gps;

    Context context = SubmitEngWorkStatus.this;

    String lacti, longi, complainno;

    String imageJson = "";

    String Remark, SelctedEnggWorkStatusID;

    ListView listView_sparePart;

    String searchSparePart;

    LinearLayout llSparePartsLayout;

    private LinearLayout llSpareParts;

    Uri mCapturedImageURI;

    private final int PICK_IMAGE_MULTIPLE = 1;

    ArrayList<String> imagesPathList;
    ArrayList<String> totalImage;

    String[] imagesPath;

    private Bitmap imgbitmap;

    private PopupWindow pwindo;

    List<String> workStatusID;
    List<String> workStatusName;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    // HashMap<String, String> addedPart = new HashMap<String, String>();
    ArrayList<SparePartListDataModel> addedPart = new ArrayList<SparePartListDataModel>();

    String[] edtQytArray;

    List<EditText> allEds;

    String sparePartJson = "";

    EditText edQuantity;

    ArrayAdapter<String> spinneradapterWorkStatus;

    LinearLayout maincontainer;

    TextView tv_status;

    private static final int CUSTOM_REQUEST_CODE = 532;
    private ArrayList<String> photoPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_eng_work_status);

        //Set Company logo in action bar with AppCompatActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cfcs_bg);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tv_status = findViewById(R.id.tv_status);

        SimpleSpanBuilder ssbStatus = new SimpleSpanBuilder();
        ssbStatus.appendWithSpace("Eng. Work Status");
        ssbStatus.append("*", new ForegroundColorSpan(Color.RED), new RelativeSizeSpan(1));
        tv_status.setText(ssbStatus.build());

        spinner_engg_work_status = findViewById(R.id.spinner_engg_work_status);
        btn_spare_search = findViewById(R.id.btn_spare_search);
        txt_remark = findViewById(R.id.txt_remark);
        btn_take_image = findViewById(R.id.btn_take_image);
        btn_image_view = findViewById(R.id.btn_image_view);
        btn_submit_work_status = findViewById(R.id.btn_submit_work_status);
        llSpareParts = (LinearLayout) findViewById(R.id.llSpareParts);
        llSparePartsLayout = (LinearLayout) findViewById(R.id.llSparePartsLayout);

        maincontainer = findViewById(R.id.maincontainer);

        totalImage = new ArrayList<String>();

        btn_take_image.setOnClickListener(this);
        btn_image_view.setOnClickListener(this);

        currentapiVersion = android.os.Build.VERSION.SDK_INT;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            complainno = getIntent().getExtras().getString("ComplainNo");

        }

        btn_spare_search.setVisibility(View.GONE);

        Config_Engg.isOnline(SubmitEngWorkStatus.this);
        if (Config_Engg.internetStatus == true) {

            new WorkStatusListAsy().execute();

        } else {
            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", SubmitEngWorkStatus.this);
        }

        spinner_engg_work_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                long SelectedWorkStatusID = parent.getSelectedItemId();
                String SelctedEnggWorkStatus = workStatusID.get((int) SelectedWorkStatusID);
                if (SelctedEnggWorkStatus != null) {
                    if (SelctedEnggWorkStatus.compareTo("4") == 0 || SelctedEnggWorkStatus.compareTo("6") == 0) {
                        btn_spare_search.setVisibility(View.VISIBLE);
                    } else {
                        btn_spare_search.setVisibility(View.GONE);
                        if (allEds != null) {
                            allEds.clear();
                        }
                        if (addedPart != null) {
                            addedPart.clear();
                        }
                        llSpareParts.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_spare_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowpopSpareSearch();
            }
        });

        btn_submit_work_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int workstatus = spinner_engg_work_status.getSelectedItemPosition();


                if (allEds != null) {
                    edtQytArray = new String[allEds.size()];
                }
                if (allEds != null) {
                    if (allEds.size() > 0) {

                        for (int i = 0; i < allEds.size(); i++) {
                            edtQytArray[i] = allEds.get(i).getText().toString();
                        }
                    }
                }

                Remark = txt_remark.getText().toString().trim();

                if (addedPart.size() > 0) {
                    makeJsonSparePart();
                }

                if (workstatus == 0) {
                    long SelectedEnggStatus = spinner_engg_work_status.getSelectedItemId();
                    SelctedEnggWorkStatusID = workStatusID.get((int) SelectedEnggStatus);
                    Config_Engg.alertBox("Please Select Work Status", context);
                } else {
                    long SelectedEnggStatus = spinner_engg_work_status.getSelectedItemId();
                    SelctedEnggWorkStatusID = workStatusID.get((int) SelectedEnggStatus);

                    if (currentapiVersion <= 22) {
                        // 7 means rejected
                        if (SelctedEnggWorkStatusID.compareTo("") == 0 && (Remark.compareTo("") == 0 || (Remark.isEmpty()))) {
                            Config_Engg.alertBox("Please write remark", context);
                        } else {
                            getEngineerLocation();
                        }
                        //getEngineerLocation();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            } else {
                                if (SelctedEnggWorkStatusID.compareTo("7") == 0 && (Remark.compareTo("") == 0 || (Remark.isEmpty()))) {
                                    Config_Engg.alertBox("Please write remark", context);
                                } else {
                                    getEngineerLocation();
                                }
                            }
                        }
                    }

                }
            }
        });

    }

    private void makeJsonSparePart() {


        try {
            Gson gson = new Gson();
            JSONObject jsonObj = new JSONObject();
            JSONArray array = new JSONArray();
            for (int i = 0; i < addedPart.size(); i++) {
                String PartID = addedPart.get(i).getSpareID();
                String PartNo1 = addedPart.get(i).getSparePartNo();
                String SpareDesc = addedPart.get(i).getSpareDesc();
                String Quantity = edtQytArray[i];
                SparePartListDataModel diary = getImageObjectFilled(PartID, Quantity);
                String case_json = gson.toJson(diary);
                JSONObject objImg = new JSONObject(case_json);
                array.put(objImg);
                jsonObj.put("members", array);
                //Log.e("make json size is ", array+" null");

            }
            Log.e("make json size is ", " cfcs " + jsonObj.toString());
            sparePartJson = array.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private SparePartListDataModel getImageObjectFilled(String partID, String quantity) {
        SparePartListDataModel bean = new SparePartListDataModel();
        bean.setSpareID(partID);
        bean.setQuantity(quantity);
        return bean;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_image_view:

                if (totalImage.size() > 0) {
                    //makeJson();
                    initiateImagePopupWindow();
                } else {
                    Config_Engg.toastShow("No images selected", this);
                }
                break;

            case R.id.btn_take_image:


                if (currentapiVersion <= 22) {
                    //   int maxCount = MAX_ATTACHMENT_COUNT - docPaths.size();

                    FilePickerBuilder.getInstance()
                            .setMaxCount(5)
                            .setSelectedFiles(photoPaths)
                            .setActivityTheme(R.style.FilePickerTheme)
                            .setActivityTitle("Please select media")
                            .enableVideoPicker(false)
                            .enableCameraSupport(true)
                            .showGifs(false)
                            .showFolderView(false)
                            .enableSelectAll(true)
                            .enableImagePicker(true)
                            .setCameraPlaceholder(R.drawable.custom_camera)
                            .withOrientation(Orientation.UNSPECIFIED)
                            .pickPhoto(SubmitEngWorkStatus.this, CUSTOM_REQUEST_CODE);

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
                        } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            //   int maxCount = MAX_ATTACHMENT_COUNT - docPaths.size();
                            FilePickerBuilder.getInstance()
                                    .setMaxCount(5)
                                    .setSelectedFiles(photoPaths)
                                    .setActivityTheme(R.style.FilePickerTheme)
                                    .setActivityTitle("Please select media")
                                    .enableVideoPicker(false)
                                    .enableCameraSupport(true)
                                    .showGifs(false)
                                    .showFolderView(false)
                                    .enableSelectAll(true)
                                    .enableImagePicker(true)
                                    .setCameraPlaceholder(R.drawable.custom_camera)
                                    .withOrientation(Orientation.UNSPECIFIED)
                                    .pickPhoto(SubmitEngWorkStatus.this, CUSTOM_REQUEST_CODE);
                        }
                    }
                }


                break;
            default:
                break;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CUSTOM_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    photoPaths = new ArrayList<>();
                    totalImage.clear();
                    imagesPath = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).toArray(new String[0]);

                    for (int i = 0; i < imagesPath.length; i++) {
                        photoPaths.add(imagesPath[i]);
                    }
                    totalImage.addAll(photoPaths);

                    if (totalImage.size() > 0) {
                        btn_image_view.setText(totalImage.size() + " Image");
                    } else {
                        btn_image_view.setText("No Image");
                    }


                }
                break;

        }


    }

    private void initiateImagePopupWindow() {

        Button btnOK;
        TextView tvEmptyView;
        ListView ImgList;
        RecyclerView recycler_view;

        try {
            LayoutInflater inflater = (LayoutInflater) SubmitEngWorkStatus.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.layout_view_image,
                    (ViewGroup) findViewById(R.id.popupLayout));
            pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnOK = (Button) layout.findViewById(R.id.btnOK);

            recycler_view = layout.findViewById(R.id.recycler_view);


            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(new SelectedImageAdapter(this));

            layout.invalidate();

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                    if (totalImage.size() > 0) {
                        btn_image_view.setText(totalImage.size() + " Image");
                    } else {
                        btn_image_view.setText("No Image");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ShowpopSpareSearch {

        {

            final EditText txt_search_sparePart;
            Button btn_search;
            ImageView imv_closed;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = getLayoutInflater();
            final View convertView = (View) inflater.inflate(R.layout.spare_layout_request, null);
            alertDialog.setView(convertView);

            txt_search_sparePart = (EditText) convertView.findViewById(R.id.txt_search_sparePart);
            btn_search = (Button) convertView.findViewById(R.id.btn_search);
//            imv_closed = convertView.findViewById(R.id.imv_closed);

            listView_sparePart = (ListView) convertView.findViewById(R.id.listView_sparePart);


            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    searchSparePart = txt_search_sparePart.getText().toString();
                    if (searchSparePart.compareTo("") == 0 || searchSparePart.isEmpty()) {
                        Config_Engg.alertBox("Please Enter text to search", context);
                    } else {
                        if (searchSparePart.length() > 2) {
                            new SparePartsAsync().execute();

                        } else {
                            Config_Engg.alertBox("Please Enter atleast 3 text to search", context);
                        }
                    }
                }
            });


            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Log.e("addedPart size ", " cfcs " + addedPart.size());
                    llSparePartsLayout.removeAllViews();

                    allEds = new ArrayList<EditText>();

                    if (addedPart.size() > 0) {
                        final String partID = "";
                        String partName = "";
                        llSpareParts.setVisibility(View.VISIBLE);
                        for (int i = 0; i < addedPart.size(); i++) {
                            final String pID = addedPart.get(i).getSpareID();
                            final String pName = addedPart.get(i).getSparePartNo();
                            //                         final String quant = addedPart.get(i).getQuantity();
//                            final String remarks = addedPart.get(i).getSpareDesc();


                            LinearLayout.LayoutParams paramtest = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    0.33f
                            );


                            final LinearLayout linearLayout = new LinearLayout(SubmitEngWorkStatus.this);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                            final TextView tvSparepart = new TextView(SubmitEngWorkStatus.this);
                            tvSparepart.setTextColor(Color.BLACK);
                            tvSparepart.setLayoutParams(paramtest);
                            tvSparepart.setGravity(Gravity.CENTER);
                            tvSparepart.setPadding(0, 2, 0, 0);

                            Log.e("pName ", "cfcs " + pName);
                            tvSparepart.setText(" " + pName);

                            edQuantity = new EditText(SubmitEngWorkStatus.this);
                            edQuantity.setHint("Qty");
                            edQuantity.setTextColor(Color.BLACK);
                            edQuantity.setLayoutParams(paramtest);
                            edQuantity.setGravity(Gravity.CENTER);
                            edQuantity.setTextSize(12);
                            edQuantity.setPadding(0, 0, 0, 0);
                            edQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);


                            ImageView imageView = new ImageView(SubmitEngWorkStatus.this);
                            imageView.setImageResource(R.drawable.ic_delete);
                            imageView.setLayoutParams(paramtest);


                            linearLayout.addView(tvSparepart);
                            linearLayout.addView(edQuantity);
                            linearLayout.addView(imageView);
                            llSparePartsLayout.addView(linearLayout);
                            llSparePartsLayout.setVisibility(View.VISIBLE);
                            partName = partName + ", " + pName;

                            allEds.add(edQuantity);

                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder altDialog = new AlertDialog.Builder(SubmitEngWorkStatus.this);
                                    altDialog.setMessage("Do You Want to delete !!");

                                    altDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            addedPart.remove(pID);
                                            allEds.remove(pID);
                                            linearLayout.setVisibility(View.GONE);

                                            if (addedPart.size() > 0) {
                                                llSpareParts.setVisibility(View.VISIBLE);
                                            } else {
                                                llSpareParts.setVisibility(View.GONE);
                                            }
                                        }
                                    });

                                    altDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    altDialog.show();
                                }
                            });
                        }

                        dialog.dismiss();
                    }
                }
            });

            alertDialog.show();

        }
    }

    public class SparePartsAsync extends AsyncTask<String, String, String> {

        private String SOAP_ACTION1 = "http://cfcs.co.in/AppEngineerPartSearch";
        private String METHOD_NAME1 = "AppEngineerPartSearch";
        private String NAMESPACE = "http://cfcs.co.in/";
        private String URL = Config_Engg.BASE_URL + "Engineer/WebApi/EngineerWebService.asmx?";

        ArrayList<SparePartListDataModel> SparePartArrayList = new ArrayList<SparePartListDataModel>();

        String[] SpareID;
        String[] SparePartNo;
        String[] SpareDesc;

        ProgressDialog progressSparePart;
        int flag = 0;
        String msgstatus = "", json_value = "", partDetail = "";
        JSONObject jsonObject;
        JSONArray jsonArray;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            SparePartList.setEmptyView(tvEmptyView);
            SparePartArrayList.clear();
            progressSparePart = ProgressDialog.show(context, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            request.addProperty("PartName", searchSparePart);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                SoapObject result = (SoapObject) envelope.bodyIn;
                if (result != null) {
                    json_value = result.getProperty(0).toString();

                    jsonArray = new JSONArray(json_value);
                    jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.has("MsgNotification")) {
                        msgstatus = jsonObject.getString("MsgNotification");
                        flag = 1;
                    } else {

                        SpareID = new String[jsonArray.length()];
                        SparePartNo = new String[jsonArray.length()];
                        SpareDesc = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SparePartListDataModel partListDataModel = new SparePartListDataModel();
                                partListDataModel.setSpareID(jsonObject1.getString("SpareID").toString());
                                partListDataModel.setSparePartNo(jsonObject1.getString("SparePartNo").toString());
                                partListDataModel.setSpareDesc(jsonObject1.getString("SpareDesc").toString());
                                // Add this object into the ArrayList SparePartArrayList
                                SparePartArrayList.add(partListDataModel);
                                flag = 2;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        flag = 2;
                    }
                } else {
                    flag = 3;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json_value;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (flag == 1) {
                Config_Engg.toastShow(msgstatus, context);
                listView_sparePart.setAdapter(null);
//                listView_sparePart.setEmptyView(tvEmptyView);

            } else {
                if (flag == 2) {
                    listView_sparePart.setAdapter(new SparePartListAdapter(context, SparePartArrayList));
                } else {
                    if (flag == 3) {
                        Config_Engg.toastShow("No Response", context);
                    }
                }
            }
            progressSparePart.dismiss();
        }
    }

    public class SparePartListAdapter extends BaseAdapter {

        ArrayList<SparePartListDataModel> myArrayList = new ArrayList<SparePartListDataModel>();
        LayoutInflater inflater;
        Context context;

        //   HashMap<String, String> addedPart = new HashMap<String, String>();

        public SparePartListAdapter(Context context, ArrayList<SparePartListDataModel> myArrayList) {

            this.myArrayList = myArrayList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myArrayList.size();
        }

        @Override
        public SparePartListDataModel getItem(int position) {
            return myArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.spare_part_list, parent, false);
                mViewHolder = new MyViewHolder();
                mViewHolder.txt_spare_part_no = convertView.findViewById(R.id.txt_spare_part_no);
                mViewHolder.txt_spare_desc = convertView.findViewById(R.id.txt_spare_desc);
                mViewHolder.btn_image_add_sparePart = convertView.findViewById(R.id.btn_image_add_sparePart);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            final SparePartListDataModel currentListData = getItem(position);
            mViewHolder.txt_spare_part_no.setText(currentListData.getSparePartNo());
            mViewHolder.txt_spare_desc.setText(currentListData.getSpareDesc());


            mViewHolder.btn_image_add_sparePart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addedPart.size() > 0) {
                        int flagn = 0;
                        for (int iq = 0; iq < addedPart.size(); iq++) {
                            String addedPartNo = addedPart.get(iq).getSparePartNo();
                            String PartNo = currentListData.getSpareID();
                            if (addedPartNo.compareTo(PartNo) == 0) {
                                Config_Engg.toastShow("Part Added", context);
                                flagn = 0;
                                break;
                            } else {
                                flagn = 1;
                            }
                        }
                        if (flagn == 1) {
                            SparePartListDataModel partBean = new SparePartListDataModel();
                            partBean.setSpareID("" + currentListData.getSpareID());
                            partBean.setSparePartNo("" + currentListData.getSparePartNo());
                            partBean.setSpareDesc("" + currentListData.getSpareDesc());
                            addedPart.add(partBean);
                            Config_Engg.toastShow("Part Added", context);
                            myArrayList.remove(position);
                            notifyDataSetChanged();
                        }
                    } else {
                        SparePartListDataModel partBean = new SparePartListDataModel();
                        partBean.setSpareID("" + currentListData.getSpareID());
                        partBean.setSparePartNo("" + currentListData.getSparePartNo());
                        partBean.setSpareDesc("" + currentListData.getSpareDesc());
                        addedPart.add(partBean);
                        Config_Engg.toastShow("Part Added", context);
                        myArrayList.remove(position);
                        notifyDataSetChanged();
                    }
                }

            });


            return convertView;
        }

        class MyViewHolder {
            ImageView btn_image_add_sparePart;
            TextView txt_spare_desc, txt_spare_part_no;
        }
    }

    class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.MyViewHolder> {

        Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView selected_imageView;
            ImageButton btnDelete;

            public MyViewHolder(View view) {
                super(view);
                selected_imageView = (ImageView) view.findViewById(R.id.selected_imageView);
                btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);
            }
        }


        public SelectedImageAdapter(Context context) {
            this.context = context;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_image_view, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            imgbitmap = BitmapFactory.decodeFile(totalImage.get(position), options);

            holder.selected_imageView.setImageBitmap(imgbitmap);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    totalImage.remove(position);
                    photoPaths.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return totalImage.size();
        }
    }

    public void getEngineerLocation() {

        gps = new GPSTracker(SubmitEngWorkStatus.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            lacti = String.valueOf(latitude);
            longi = String.valueOf(longitude);

            new UpdateStatusAsync().execute();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public class UpdateStatusAsync extends AsyncTask<String, String, String> {
        String jsonValue, status, id;
        int flag;
        String msgstatus;
        ProgressDialog progressDialog;
        String LoginStatus;
        String invalid = "LoginFailed";

        ProgressDialog UpdtProgressDialog;

        public void makeJson() {

            try {
                Gson gson = new Gson();
                JSONObject jsonObj = new JSONObject();
                JSONArray array = new JSONArray();
                for (int i = 0; i < totalImage.size(); i++) {
                    String imgPath = totalImage.get(i);
                    final DecodeImageBean diary = getImageObjectFilled(imgPath);
                    String case_json = gson.toJson(diary);
                    JSONObject objImg = new JSONObject(case_json);
                    array.put(objImg);
                    jsonObj.put("members", array);
                }
                Log.e("ImagesJson", " cfcs " + jsonObj.toString());
                imageJson = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private DecodeImageBean getImageObjectFilled(String imgPath) {

            DecodeImageBean bean = new DecodeImageBean();
            int dotposition = imgPath.lastIndexOf(".");
            String ImgExtension = imgPath.substring(dotposition + 1, imgPath.length());
            String ImgString = decodeImage(imgPath);
            bean.setImageExtension(ImgExtension);
            bean.setImageString(ImgString);
            return bean;
        }

        public String decodeImage(String imgPath) {

            Bitmap decodedBitmap = BitmapFactory.decodeFile(imgPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodedBitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] ba = baos.toByteArray();
            String imgString = Base64.encodeToString(ba, Base64.DEFAULT);
            return imgString;
        }

        @Override
        protected void onPreExecute() {
            Log.e("totalSpareParts ", " cfcs " + totalImage);
            progressDialog = ProgressDialog.show(SubmitEngWorkStatus.this, "Loading...", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if (totalImage.size() > 0) {
                makeJson();
            }

            String EngineerID = Config_Engg.getSharedPreferences(SubmitEngWorkStatus.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(SubmitEngWorkStatus.this, "pref_Engg", "AuthCode", "");

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

            request.addProperty("ComplainNo", complainno);
            request.addProperty("EngWorkStatusID", SelctedEnggWorkStatusID);
            request.addProperty("Remark", Remark);
            request.addProperty("EngLang", longi);
            request.addProperty("EngLat", lacti);
            // request.addProperty("SparePart", sparePartJson);
            request.addProperty("ImgJson", imageJson);
            request.addProperty("EngineerID", EngineerID);
            request.addProperty("AuthCode", AuthCode);

            if (addedPart.size() > 0) {
                request.addProperty("SparePart", sparePartJson);
            } else {
                request.addProperty("SparePart", "");
            }


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
                        msgstatus = jsonObject.getString("MsgNotification");
                        if (LoginStatus.equals(invalid)) {

                            flag = 4;
                        } else {

                            flag = 1;
                        }
                    } else {
                        msgstatus = jsonObject.getString("MsgNotification");
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (flag == 1) {

                if (msgstatus.compareTo("success") == 0) {
                    Config_Engg.toastShow("Your Status Updated Successfully !", SubmitEngWorkStatus.this);
                    addedPart.clear();
                    totalImage.clear();
                    btn_image_view.setText("No image");
                    //tvAllPart.setText("");
                    llSpareParts.setVisibility(View.GONE);
                    Log.e("status ", " cfcs " + status);
                    txt_remark.setText("");
                    spinner_engg_work_status.setSelection(0);

                    Intent i = new Intent(SubmitEngWorkStatus.this, EngineerWorkStatusUpdate.class);
                    i.putExtra("ComplainNo", complainno);
                    startActivity(i);

                } else {
                    Config_Engg.toastShow(msgstatus, SubmitEngWorkStatus.this);
                }

            } else if (flag == 2) {
                Config_Engg.toastShow(msgstatus, SubmitEngWorkStatus.this);
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", SubmitEngWorkStatus.this);
            } else if (flag == 4) {


                Config_Engg.toastShow(msgstatus, SubmitEngWorkStatus.this);
                Config_Engg.logout(SubmitEngWorkStatus.this);
                Config_Engg.putSharedPreferences(SubmitEngWorkStatus.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {
                ScanckBar();
                progressDialog.dismiss();
                btn_submit_work_status.setEnabled(false);
            }
            btn_submit_work_status.setClickable(true);
            progressDialog.dismiss();
        }
    }

    private class WorkStatusListAsy extends AsyncTask<String, String, String> {

        int flag;
        String msgstatus;
        String workStatus_detail, workStatus_list;

        String LoginStatus;
        String invalid = "LoginFailed";

        ProgressDialog progressDialog;

        int count = 0;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(SubmitEngWorkStatus.this, "Loading", "Please Wait....", true, false);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            String EngineerID = Config_Engg.getSharedPreferences(SubmitEngWorkStatus.this, "pref_Engg", "EngineerID", "");
            String AuthCode = Config_Engg.getSharedPreferences(SubmitEngWorkStatus.this, "pref_Engg", "AuthCode", "");

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
                    workStatus_detail = result.getProperty(0).toString();
                    JSONArray jsonArray = new JSONArray(workStatus_detail);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    workStatus_list = jsonArray.toString();
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
                Config_Engg.toastShow(msgstatus, SubmitEngWorkStatus.this);
            } else if (flag == 2) {
                try {

                    // Add value in EnggWork List Status Spinner
                    JSONArray jsonArray2 = new JSONArray(workStatus_list);
                    workStatusID = new ArrayList<String>();
                    workStatusID.add(0, "");
                    workStatusName = new ArrayList<String>();
                    workStatusName.add(0, "Select");
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        count += 1;
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                        String EngWorkStatusID = jsonObject2.getString("EngWorkStatusID");
                        String EngWorkStatus = jsonObject2.getString("EngWorkStatus");

                        workStatusID.add(i + 1, EngWorkStatusID);
                        workStatusName.add(i + 1, EngWorkStatus);
                    }

                    workStatusID.remove(1);
                    workStatusName.remove(1);

                    spinneradapterWorkStatus = new ArrayAdapter<String>(SubmitEngWorkStatus.this,
                            android.R.layout.simple_spinner_item, workStatusName);
                    spinneradapterWorkStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_engg_work_status.setAdapter(spinneradapterWorkStatus);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("Error is here", e.toString());
                }

                //  fillListDialog.dismiss();
            } else if (flag == 3) {
                Config_Engg.toastShow("No Response", SubmitEngWorkStatus.this);

            } else if (flag == 4) {

                Config_Engg.toastShow(msgstatus, SubmitEngWorkStatus.this);
                Config_Engg.logout(SubmitEngWorkStatus.this);
                Config_Engg.putSharedPreferences(SubmitEngWorkStatus.this, "checklogin", "status", "2");
                finish();
            } else if (flag == 5) {

                ScanckBar();
                btn_submit_work_status.setEnabled(false);
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

                        Config_Engg.isOnline(SubmitEngWorkStatus.this);
                        if (Config_Engg.internetStatus == true) {

                            new WorkStatusListAsy().execute();

                        } else {
                            Config_Engg.toastShow("No Internet Connection! Please Reconnect Your Internet", SubmitEngWorkStatus.this);
                        }
                        btn_submit_work_status.setEnabled(true);


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
                intent = new Intent(SubmitEngWorkStatus.this, ChangePassword.class);
                startActivity(intent);
                finish();

                return (true);
            case R.id.logout:

                Config_Engg.logout(SubmitEngWorkStatus.this);
                finish();
                Config_Engg.putSharedPreferences(this, "checklogin", "status", "2");
                return (true);

            case R.id.dashboard:
                intent = new Intent(SubmitEngWorkStatus.this, DashboardActivity.class);
                startActivity(intent);
                finish();

                return (true);
            case R.id.profile:
                intent = new Intent(SubmitEngWorkStatus.this, ProfileUpdate.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_raise:
                intent = new Intent(SubmitEngWorkStatus.this, RaiseComplaintActivity.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_complain:
                intent = new Intent(SubmitEngWorkStatus.this, ManageComplaint.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_machines:
                intent = new Intent(SubmitEngWorkStatus.this, ManageMachines.class);
                startActivity(intent);
                finish();
                return (true);
            case R.id.btn_contact:
                intent = new Intent(SubmitEngWorkStatus.this, ManageContact.class);
                startActivity(intent);
                finish();
                return (true);

            case R.id.btn_menu_feedback:
                intent = new Intent(SubmitEngWorkStatus.this, FeedbackActivity.class);
                startActivity(intent);
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SubmitEngWorkStatus.this, EngineerWorkStatusUpdate.class);
        intent.putExtra("ComplainNo", complainno);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
