package com.mikelearning.projecttest.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikelearning.projecttest.Constant;
import com.mikelearning.projecttest.R;
import com.mikelearning.projecttest.Utils.ItemClickListener;
import com.mikelearning.projecttest.Utils.UserPreferences;
import com.mikelearning.projecttest.fragements.Fragment_Locations;
import com.mikelearning.projecttest.model.Chat;
import com.mikelearning.projecttest.model.Chat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {

    private Context context;
    private List<Chat> mChats;

    private MessageAdapterListener listener;
   
    public StatusAdapter(Context context, List<Chat> mChats, MessageAdapterListener listener) {
        this.context = context;
        this.mChats = mChats;
        this.listener = listener;


    }

    public StatusAdapter(Context context, List<Chat> mChats) {
        this.context = context;
        this.mChats = mChats;

    }

    public void add(Chat chat) {

            mChats.add(chat);
            notifyItemInserted(mChats.size() - 1);


    }

    public void clear() {
        mChats.clear();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_list, parent, false);
        ButterKnife.bind(this, view);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {
        Chat chatOption = mChats.get(i);

        String name=chatOption.getFirstname()+" "+chatOption.getLastname();
        String personal_num="Personal No: "+chatOption.getPersonal_contact();
        String family_num="Next of Kin No: "+chatOption.getNextofkin_phone();
        String date_time="Period: "+chatOption.getDate()+" | "+chatOption.getTimestamp();
        String latlng="Location: "+chatOption.getLatitude()+" , "+chatOption.getLongitude();


        holder.mFirstLastName.setText(name);
        holder.mPersonalContact.setText(personal_num);
        holder.mFamilyContact.setText(family_num);
        holder.mDateAndTime.setText(date_time);
        holder.mLocationCordinate.setText(latlng);

        holder.setItemClickListener(pos -> {

            listener.onDataClicked(mChats.get(pos));

        });



    }

    public interface MessageAdapterListener {
        void onDataClicked(Chat data);

    }


    private void showFragmentUser(Fragment fragment) {
        FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

    }





    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }




    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        /** ButterKnife Code **/
        @BindView(R.id.status_list_layout)
        LinearLayout mStatusListLayout;
        @BindView(R.id.first_last_name)
        TextView mFirstLastName;
        @BindView(R.id.personal_contact)
        TextView mPersonalContact;
        @BindView(R.id.family_contact)
        TextView mFamilyContact;
        @BindView(R.id.location_cordinate)
        TextView mLocationCordinate;
        @BindView(R.id.date_and_time)
        TextView mDateAndTime;

        /** ButterKnife Code **/



        ItemClickListener itemClickListener;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View v) {

            this.itemClickListener.onItemClick(this.getLayoutPosition());

        }
    }
}

