package in.co.cfcs.eserviceengineer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.activity_engineer.AddDailyReport;
import in.co.cfcs.eserviceengineer.activity_engineer.DailyReportDetail;
import in.co.cfcs.eserviceengineer.model.DailyReportDataModel;

public class DailyReportListAdapter extends BaseAdapter {

    ArrayList<DailyReportDataModel> DailyReportDataModelsList = new ArrayList<DailyReportDataModel>();

    LayoutInflater inflater;
    Context context;

    public DailyReportListAdapter(Context context, ArrayList<DailyReportDataModel> dailyReportDataModelsList) {
        this.DailyReportDataModelsList = dailyReportDataModelsList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return DailyReportDataModelsList.size();
    }

    @Override
    public DailyReportDataModel getItem(int position) {

        return DailyReportDataModelsList.get(position);
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
        final DailyReportListAdapter.MyViewHolder mViewHolder;

        final DailyReportDataModel currentListData = getItem(position);

        if (view == null) {
            view = inflater.inflate(R.layout.daily_report_list_layout,
                    viewGroup, false);
            mViewHolder = new DailyReportListAdapter.MyViewHolder(view);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (DailyReportListAdapter.MyViewHolder) view.getTag();
        }

        mViewHolder.txt_report_no.setText(currentListData.getDailyReportNo());
        mViewHolder.txt_report_date.setText(currentListData.getDailyReportDateText());
        mViewHolder.txt_work_done.setText(currentListData.getWorkdone());
        mViewHolder.txt_travel_time.setText(currentListData.getTraveltime());
        mViewHolder.txt_service_time.setText(currentListData.getServicetime());
        mViewHolder.txt_next_follow_up.setText(currentListData.getNextFollowUpDateText());

        mViewHolder.action_dots.setOnClickListener(new View.OnClickListener() {
            class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    Intent intent = new Intent();
                    switch (item.getItemId()) {
                        case R.id.action_edit_record:
                            intent.setClass(context, AddDailyReport.class);
                            intent.putExtra("ComplainNo", currentListData.getComplainNo());
                            intent.putExtra("DailyReportNo", currentListData.getDailyReportNo());
                            intent.putExtra("IsEditCheck", currentListData.getIsEdit());
                            context.startActivity(intent);
                            ((Activity) context).finish();
                            return true;
                        case R.id.action_delete:
                            Toast.makeText(context, "CFCS", Toast.LENGTH_SHORT).show();
                            return true;
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
                inflater.inflate(R.menu.menu_action_daily_report, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                popup.show();

                Menu popupMenu = popup.getMenu();

                String IsdeleteCheck = currentListData.getIsDelete();
                if (IsdeleteCheck.compareTo("true") == 0) {
                    popupMenu.findItem(R.id.action_delete).setVisible(true);
                } else {
                    popupMenu.findItem(R.id.action_delete).setVisible(false);
                }

                String IsEditCheck = currentListData.getIsEdit();
                if (IsEditCheck.compareTo("true") == 0) {
                    popupMenu.findItem(R.id.action_edit_record).setVisible(true);
                } else {
                    popupMenu.findItem(R.id.action_edit_record).setVisible(false);
                }


            }
        });

        mViewHolder.card_view_dailyReoprt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(context, DailyReportDetail.class);
                intent.putExtra("DailyReportNo", currentListData.getDailyReportNo());
                intent.putExtra("ComplainNo", currentListData.getComplainNo());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });


        return view;
    }

    class MyViewHolder {
        TextView txt_report_no, txt_report_date, txt_work_done, txt_travel_time, txt_service_time, txt_next_follow_up;
        ImageView iv_edit, iv_delete, action_dots;
        LinearLayout card_view_dailyReoprt;

        //Button btnDetail;
        public MyViewHolder(View view) {
            card_view_dailyReoprt = view.findViewById(R.id.card_view_dailyReoprt);
            txt_report_no = view.findViewById(R.id.txt_report_no);
            txt_report_date = view.findViewById(R.id.txt_report_date);
            txt_work_done = view.findViewById(R.id.txt_work_done);
            txt_travel_time = view.findViewById(R.id.txt_travel_time);
            txt_service_time = view.findViewById(R.id.txt_service_time);
            txt_next_follow_up = view.findViewById(R.id.txt_next_follow_up);
            action_dots = view.findViewById(R.id.action_dots);
//            iv_edit = view.findViewById(R.id.iv_edit);
//            iv_delete = view.findViewById(R.id.iv_delete);

        }
    }

}
