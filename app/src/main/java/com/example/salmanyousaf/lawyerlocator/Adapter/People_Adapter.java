package com.example.salmanyousaf.lawyerlocator.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Firebase.Signup;
import com.example.salmanyousaf.lawyerlocator.Model.User;
import com.example.salmanyousaf.lawyerlocator.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class People_Adapter extends RecyclerView.Adapter<People_Adapter.MyViewHolder> implements Filterable{

    private List<Signup> mUserdata;
    private List<Signup> mFilterUserdata;
    private CustomFilter mFilter;
    private String mAccountType;
    private CustomItemClickListener mListener;


    public People_Adapter(List<Signup> userdata, String accountType, CustomItemClickListener listener) {
        this.mUserdata = userdata;
        this.mFilterUserdata = userdata;
        this.mAccountType = accountType;
        this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, location, caseType;
        private RatingBar rating;
        private ImageView image;

        private MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name_text_view);
            location = view.findViewById(R.id.location_text_view);
            image = view.findViewById(R.id.image_view);
            caseType = view.findViewById(R.id.caseType_text_view);
            rating = view.findViewById(R.id.ratingBar);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_people, parent, false);
        final MyViewHolder mViewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, mViewHolder.getAdapterPosition());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        holder.name.setText(mUserdata.get(pos).getName());

        holder.location.setText(mUserdata.get(pos).getLocation());

        Picasso.get().load(mUserdata.get(pos).getProfileImage()).fit().into(holder.image);

        if(mAccountType.equals("lawyer"))
        {
            holder.caseType.setText(mUserdata.get(pos).getCaseType());

            //diabling RatingBar
            holder.rating.setVisibility(View.GONE);
        }
        else if(mAccountType.equals("client"))
        {
//            holder.rating.setRating((float) mUserdata.get(pos).getRating());

            //disabling visibility of case_type
            holder.caseType.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mUserdata.size();
    }

    //Filter Interface
    @Override
    public Filter getFilter() {
        if(mFilter== null)
        {
            mFilter=new CustomFilter();
        }
        return mFilter;
    }


    //INNER CLASSES
    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                constraint=constraint.toString().toUpperCase();

                List<User> filters = new ArrayList<>();

                //get specific items
                for(int i=0; i<mFilterUserdata.size(); i++)
                {
                    if(mFilterUserdata.get(i).getName().toUpperCase().contains(constraint))
                    {
                        User userData =new User(mFilterUserdata.get(i).getName(), mFilterUserdata.get(i).getLocation(), mFilterUserdata.get(i).getCaseType(),
                                0, mFilterUserdata.get(i).getProfileImage());
                        filters.add(userData);
                    }
                }

                results.count=filters.size();
                results.values=filters;

            }else
            {
                results.count=mFilterUserdata.size();
                results.values=mFilterUserdata;
            }

            return results;
        }




        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            mUserdata= (List<Signup>) results.values;
            notifyDataSetChanged();
        }
    }

}//class ends


