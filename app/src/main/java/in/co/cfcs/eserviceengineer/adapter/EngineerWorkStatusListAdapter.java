package in.co.cfcs.eserviceengineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;

import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.model.EngineerWorkStatusDataModel;

public class EngineerWorkStatusListAdapter extends BaseAdapter {

    ArrayList<EngineerWorkStatusDataModel> EngineerWorkStatusDataModelList = new ArrayList<EngineerWorkStatusDataModel>();

    LayoutInflater inflater;
    Context context;

    public EngineerWorkStatusListAdapter(Context context, ArrayList<EngineerWorkStatusDataModel> engineerWorkStatusDataModelList) {
        this.EngineerWorkStatusDataModelList = engineerWorkStatusDataModelList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return EngineerWorkStatusDataModelList.size();
    }

    @Override
    public EngineerWorkStatusDataModel getItem(int position) {

        return EngineerWorkStatusDataModelList.get(position);
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
        final EngineerWorkStatusListAdapter.MyViewHolder mViewHolder;


        final EngineerWorkStatusDataModel currentListData = getItem(position);

        if (view == null) {
            view = inflater.inflate(R.layout.engineer_work_status_update_list_layout,
                    viewGroup, false);
            mViewHolder = new EngineerWorkStatusListAdapter.MyViewHolder(view);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (EngineerWorkStatusListAdapter.MyViewHolder) view.getTag();
        }


        mViewHolder.txt_complaint_no.setText(currentListData.getComplainNo());
        mViewHolder.txt_update_date.setText(currentListData.getAddDateText());
        mViewHolder.txt_work_status.setText(currentListData.getEngWorkStatus());
        mViewHolder.txt_status_remark.setText(currentListData.getRemark());
        mViewHolder.txt_required_part.setText(currentListData.getSpareNo());
        mViewHolder.txt_update_by.setText(currentListData.getEngineerName());


        return view;
    }

    class MyViewHolder {
        TextView txt_complaint_no, txt_update_date, txt_work_status, txt_status_remark, txt_required_part, txt_update_by;

        //Button btnDetail;
        public MyViewHolder(View view) {
//            card_view = view.findViewById(R.id.card_view);
            txt_complaint_no = view.findViewById(R.id.txt_complaint_no);
            txt_update_date = view.findViewById(R.id.txt_update_date);
            txt_work_status = view.findViewById(R.id.txt_work_status);
            txt_status_remark = view.findViewById(R.id.txt_status_remark);
            txt_required_part = view.findViewById(R.id.txt_required_part);
            txt_update_by = view.findViewById(R.id.txt_update_by);

        }
    }

}
