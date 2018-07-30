package in.co.cfcs.eserviceengineer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.activity_engineer.MachineDetail;
import in.co.cfcs.eserviceengineer.activity_engineer.ManageMachines;
import in.co.cfcs.eserviceengineer.activity_engineer.NewMachine;
import in.co.cfcs.eserviceengineer.model.MachineListDataModal;

public class MachineListAdapter extends RecyclerView.Adapter<MachineListAdapter.MyViewHolder> {

    Context context;
    ArrayList<MachineListDataModal> MachineList = new ArrayList<MachineListDataModal>();

    public MachineListAdapter(ManageMachines manageMachines, ArrayList<MachineListDataModal> machineList) {
        this.MachineList = machineList;
        this.context = manageMachines;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_serial_no, txt_model, txt_principal_name, txt_plant_customer, txt_region, txt_amc_warranty, txt_warranty_date, txt_amc_date,
                txt_add_by, txt_approval_status, txt_p_customer, txt_srno;

        LinearLayout card_view_machine;

        ImageView action_dots;

        public MyViewHolder(View view) {
            super(view);
            card_view_machine = view.findViewById(R.id.card_view_machine);
            txt_serial_no = view.findViewById(R.id.txt_serial_no);
            txt_model = view.findViewById(R.id.txt_model);
            txt_principal_name = view.findViewById(R.id.txt_principal_name);
            txt_plant_customer = view.findViewById(R.id.txt_plant_customer);
            txt_region = view.findViewById(R.id.txt_region);
            txt_add_by = view.findViewById(R.id.txt_add_by);
            txt_approval_status = view.findViewById(R.id.txt_approval_status);
            txt_p_customer = view.findViewById(R.id.txt_p_customer);
            action_dots = view.findViewById(R.id.action_dots);
            txt_srno = view.findViewById(R.id.txt_srno);

        }
    }

    public MachineListAdapter(Context context) {
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View rootView = LayoutInflater.from(context).inflate(R.layout.machine_list_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);


        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MachineListDataModal currentListData = getItem(position);
        final String IsEditDelete = currentListData.getIsEditDelete();
        final String SaleID = currentListData.getSaleID();
        holder.txt_serial_no.setText(currentListData.getSerialNo());
        holder.txt_model.setText(currentListData.getModelName());
        holder.txt_principal_name.setText(currentListData.getPrincipleName());
        holder.txt_plant_customer.setText(currentListData.getCustomerName());
        holder.txt_region.setText(currentListData.getZoneName());
        holder.txt_add_by.setText(currentListData.getAddByNameText());
        holder.txt_approval_status.setText(currentListData.getApproveStatusName());
        holder.txt_p_customer.setText(currentListData.getParentCustomerName());
        holder.txt_srno.setText(currentListData.getCounter());

        if (IsEditDelete.compareTo("false") == 0) {

            holder.action_dots.setVisibility(View.GONE);
        } else {
            holder.action_dots.setVisibility(View.VISIBLE);
        }


        holder.action_dots.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showPopupMenu(holder.action_dots, position, IsEditDelete, SaleID);
            }

        });


        holder.card_view_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(context, MachineDetail.class);
                intent.putExtra("SaleID", currentListData.getSaleID());
//                intent.putExtra("status", Status);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

    }

    private void showPopupMenu(ImageView action_dots, int position, String IsEditDelete, String SaleID) {

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
        inflater.inflate(R.menu.menu_action_machine_list, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position, SaleID, IsEditDelete));
        popup.show();

        Menu popupMenu = popup.getMenu();
        String EditDelete = IsEditDelete;
        if (EditDelete.compareTo("false") == 0) {
            popupMenu.findItem(R.id.action_edit_record).setVisible(false);
        } else {
            popupMenu.findItem(R.id.action_edit_record).setVisible(true);
        }


    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;
        String SaleID;
        String IsEditDelete;

        public MyMenuItemClickListener(int position, String saleID, String isEditDelete) {
            this.position = position;
            this.SaleID = saleID;
            this.IsEditDelete = isEditDelete;
        }

        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            Intent intent = new Intent();
            switch (item.getItemId()) {
                case R.id.action_edit_record:
                    intent.setClass(context, NewMachine.class);
                    intent.putExtra("SaleID", SaleID);
                    intent.putExtra("EditDelete", IsEditDelete);
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
    public int getItemCount() {
        return MachineList.size();
    }

    public MachineListDataModal getItem(int position) {

        return MachineList.get(position);
    }

}
