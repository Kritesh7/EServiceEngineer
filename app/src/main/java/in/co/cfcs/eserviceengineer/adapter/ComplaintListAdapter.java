package in.co.cfcs.eserviceengineer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.activity_engineer.ComplaintDetail;
import in.co.cfcs.eserviceengineer.activity_engineer.DailyReport;
import in.co.cfcs.eserviceengineer.activity_engineer.EngineerWorkStatusUpdate;
import in.co.cfcs.eserviceengineer.activity_engineer.RaiseComplaintActivity;
import in.co.cfcs.eserviceengineer.model.ComplaintDataModel;

public class ComplaintListAdapter extends BaseAdapter {

    ArrayList<ComplaintDataModel> ComplainList = new ArrayList<ComplaintDataModel>();

    LayoutInflater inflater;
    Context context;
    private int i;
    String Status = "";
    int EditStatus = 1;

    public ComplaintListAdapter(Context context, ArrayList<ComplaintDataModel> complainList, String Status) {
        this.ComplainList = complainList;
        this.context = context;
        this.Status = Status;
        inflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return ComplainList.size();
    }

    @Override
    public ComplaintDataModel getItem(int position) {

        return ComplainList.get(position);
    }

    @Override
    public int getViewTypeCount() {


        return getCount();
    }

    @Override
    public int getItemViewType(int position) {


        return position;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        final ComplaintListAdapter.MyViewHolder mViewHolder;


        final ComplaintDataModel currentListData = getItem(position);

        if (view == null) {
            view = inflater.inflate(R.layout.complaint_list_layout,
                    viewGroup, false);
            mViewHolder = new ComplaintListAdapter.MyViewHolder(view);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ComplaintListAdapter.MyViewHolder) view.getTag();
        }


        mViewHolder.txt_complaint_title.setText(currentListData.getComplaintTitle());
        mViewHolder.txt_complaint_no.setText(currentListData.getComplainNo());
        mViewHolder.txt_complain_date.setText(currentListData.getComplainDateTimeText());
        mViewHolder.txt_machine.setText(currentListData.getModelName());
        mViewHolder.txt_plant_customer.setText(currentListData.getCustomerName());
        mViewHolder.txt_service_type.setText(currentListData.getTransactionTypeName());
        mViewHolder.txt_work_status.setText(currentListData.getWorkStatusName());
        mViewHolder.txt_status.setText(currentListData.getStatusText());
        mViewHolder.txt_plant.setText(currentListData.getSiteAddress());


        mViewHolder.action_dots.setOnClickListener(new View.OnClickListener() {
            class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Intent intent = new Intent();
                    switch (item.getItemId()) {
                        case R.id.action_edit_record:
                            intent.setClass(context, RaiseComplaintActivity.class);
                            intent.putExtra("ComplainNo", currentListData.getComplainNo());
                            intent.putExtra("status", Status);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                            return true;
                        case R.id.action_status_update:
                            intent.setClass(context, EngineerWorkStatusUpdate.class);
                            intent.putExtra("ComplainNo", currentListData.getComplainNo());
//                intent.putExtra("status",Status);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                            return true;
                        case R.id.action_daily_report:
                            intent.setClass(context, DailyReport.class);
                            intent.putExtra("ComplainNo", currentListData.getComplainNo());
//                intent.putExtra("status",Status);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                            return true;
//                        case R.id.action_delete:
//                            Toast.makeText(context, "CFCS", Toast.LENGTH_SHORT).show();
//                            return true;
                        default:
                    }

                    return false;
                }
            }

            @Override
            public void onClick(View v) {
                showPopupMenu(mViewHolder.action_dots);
            }

            private void showPopupMenu(ImageView action_dots) {

                PopupMenu popup = new PopupMenu(context, action_dots);

                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_action, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                popup.show();

                Menu popupMenu = popup.getMenu();
                String EditDelete = currentListData.getIsEditDelete();
                if (EditDelete.compareTo("false") == 0) {
                    popupMenu.findItem(R.id.action_edit_record).setVisible(false);
//                    popupMenu.findItem(R.id.action_delete).setVisible(false);

                } else {
                    popupMenu.findItem(R.id.action_edit_record).setVisible(true);
//                    popupMenu.findItem(R.id.action_delete).setVisible(true);
                }

            }
        });


        mViewHolder.card_view_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(context, ComplaintDetail.class);
                intent.putExtra("ComplainNo", currentListData.getComplainNo());
                intent.putExtra("status", Status);
                context.startActivity(intent);
                ((Activity) context).finish();

            }
        });


        return view;
    }

    class MyViewHolder {
        TextView txt_complaint_title, txt_complaint_no, txt_complain_date, txt_machine, txt_plant_customer, txt_service_type,
                txt_work_status, txt_status, txt_plant;
        CardView card_view;
        LinearLayout card_view_complaint, linearLayoutfeedback;
        ImageView iv_edit, iv_status_update, iv_daily_report, iv_delete, action_dots;


        //Button btnDetail;
        public MyViewHolder(View view) {
//            card_view = view.findViewById(R.id.card_view);
            card_view_complaint = view.findViewById(R.id.card_view_complaint);
            txt_complaint_title = view.findViewById(R.id.txt_complaint_title);
            txt_complaint_no = view.findViewById(R.id.txt_complaint_no);
            txt_complain_date = view.findViewById(R.id.txt_complain_date);
            txt_machine = view.findViewById(R.id.txt_machine);
            txt_plant_customer = view.findViewById(R.id.txt_plant_customer);
            txt_service_type = view.findViewById(R.id.txt_service_type);
            txt_work_status = view.findViewById(R.id.txt_work_status);
            txt_status = view.findViewById(R.id.txt_status);
            txt_plant = view.findViewById(R.id.txt_plant);
//            iv_edit = view.findViewById(R.id.iv_edit);
//            iv_status_update = view.findViewById(R.id.iv_status_update);
//            iv_daily_report = view.findViewById(R.id.iv_daily_report);
//            iv_delete = view.findViewById(R.id.iv_delete);
            action_dots = view.findViewById(R.id.action_dots);
        }
    }

}
