package in.co.cfcs.eserviceengineer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import in.co.cfcs.eserviceengineer.Config_engineer.Config_Engg;
import in.co.cfcs.eserviceengineer.R;
import in.co.cfcs.eserviceengineer.activity_engineer.ManageContact;
import in.co.cfcs.eserviceengineer.activity_engineer.NewContact;
import in.co.cfcs.eserviceengineer.background.DeleteContact;
import in.co.cfcs.eserviceengineer.model.ContactListDataModel;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {
    Context context;
    ArrayList<ContactListDataModel> contactList = new ArrayList<ContactListDataModel>();

    public ContactListAdapter(Context context) {
        this.context = context;

    }

    public ContactListAdapter(ManageContact manageContact, ArrayList<ContactListDataModel> contactList) {
        this.contactList = contactList;
        this.context = manageContact;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_contact_person_name, txt_plant_customer, txt_designation, txt_mail, txt_contact_mobile, txt_status,
                txt_approval_remark, txt_customer, txt_srno, txt_other_contact;

        LinearLayout card_view_contact;

        ImageView action_dots;


        public MyViewHolder(View view) {
            super(view);
            card_view_contact = view.findViewById(R.id.card_view_contact);
            txt_contact_person_name = view.findViewById(R.id.txt_contact_person_name);
            txt_plant_customer = view.findViewById(R.id.txt_plant_customer);
            txt_designation = view.findViewById(R.id.txt_designation);
            txt_mail = view.findViewById(R.id.txt_mail);
            txt_contact_mobile = view.findViewById(R.id.txt_contact_mobile);
            txt_status = view.findViewById(R.id.txt_status);
            txt_approval_remark = view.findViewById(R.id.txt_approval_remark);
            txt_customer = view.findViewById(R.id.txt_customer);
            txt_srno = view.findViewById(R.id.txt_srno);
            action_dots = view.findViewById(R.id.action_dots);
            txt_other_contact = view.findViewById(R.id.txt_other_contact);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.contact_list_layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);

        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ContactListDataModel currentListData = getItem(position);

        final String IsEditDelete = currentListData.getIsEditDelete();
        final String ContactPersonId = currentListData.getContactPersonId();

        holder.txt_contact_person_name.setText(currentListData.getContactPersonName());
        holder.txt_plant_customer.setText(currentListData.getSiteAddress());
        holder.txt_designation.setText(currentListData.getDesignation());
        holder.txt_mail.setText(currentListData.getEmail());
        holder.txt_contact_mobile.setText(currentListData.getPhone());
        holder.txt_status.setText(currentListData.getApproveStatusName());
        holder.txt_approval_remark.setText(currentListData.getApproveStatusRemark());
        holder.txt_customer.setText(currentListData.getParentCustomerName());
        holder.txt_srno.setText(currentListData.getCounter());
        holder.txt_other_contact.setText(currentListData.getOtherContact());

        if (IsEditDelete.compareTo("false") == 0) {

            holder.action_dots.setVisibility(View.GONE);
        } else {
            holder.action_dots.setVisibility(View.VISIBLE);
        }

        holder.action_dots.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                showPopupMenu(holder.action_dots, position, IsEditDelete, ContactPersonId);
            }

        });


    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public ContactListDataModel getItem(int position) {

        return contactList.get(position);
    }

    private void showPopupMenu(ImageView action_dots, int position, String IsEditDelete, String ContactPersonId) {

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
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position, ContactPersonId, IsEditDelete));
        popup.show();

        Menu popupMenu = popup.getMenu();
        String EditDelete = IsEditDelete;
        if (EditDelete.compareTo("false") == 0) {
            popupMenu.findItem(R.id.action_edit_record).setVisible(false);
            popupMenu.findItem(R.id.action_delete).setVisible(false);
        } else {
            popupMenu.findItem(R.id.action_delete).setVisible(true);
            popupMenu.findItem(R.id.action_edit_record).setVisible(true);
        }

    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        int position;
        String contactPersonId;
        String isEditDelete;
        String customerID;
        String contactPersonName;
        String designation;
        String emailID;
        String phoneNo;
        String loginUserName;


        public MyMenuItemClickListener(int position, String ContactPersonId, String IsEditDelete) {
            this.position = position;
            this.contactPersonId = ContactPersonId;
            this.isEditDelete = IsEditDelete;
        }

        public MyMenuItemClickListener() {

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            Intent intent = new Intent();
            switch (item.getItemId()) {
                case R.id.action_edit_record:
                    intent.setClass(context, NewContact.class);
                    intent.putExtra("ContactPersonId", contactPersonId);
                    intent.putExtra("IsEditDelete", isEditDelete);


                    context.startActivity(intent);
                    ((Activity) context).finish();
                    return true;
                case R.id.action_delete:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("Do you want to delete this contact?");

                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            String EngineerID = Config_Engg.getSharedPreferences(context, "pref_Engg", "EngineerID", "");
                            String ContactPersonId = contactPersonId;
                            String AuthCode = Config_Engg.getSharedPreferences(context, "pref_Engg", "AuthCode", "");
//                                String status = currentListData.getStatus();

                            String DeleteDetails[] = {ContactPersonId, EngineerID, AuthCode};

                            new DeleteContact(context).execute(DeleteDetails);
                            dialog.dismiss();
                        }
                    });

                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(context, ManageContact.class);
//                                intent.putExtra("status", currentListData.getStatus());
                            context.startActivity(intent);
                        }
                    });
                    alertDialog.show();

                    return true;
                default:
            }

            return false;
        }
    }
}
