package com.example.salmanyousaf.lawyerlocator.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.salmanyousaf.lawyerlocator.Helper.HelperMethods;
import com.example.salmanyousaf.lawyerlocator.Interfaces.CustomItemClickListener;
import com.example.salmanyousaf.lawyerlocator.Model.Message;
import com.example.salmanyousaf.lawyerlocator.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class Message_Adapter extends RecyclerView.Adapter<Message_Adapter.MyViewHolder> /*implements Filterable*/{

    private List<Message> mUserdata;
    private CustomItemClickListener mListener;
    private String mSender;
    private HelperMethods helperMethods = new HelperMethods();


    public Message_Adapter(List<Message> userdata, String sender, CustomItemClickListener listener) {
        this.mUserdata = userdata;
        this.mSender = sender;
        this.mListener = listener;
        //this.mFilterUserdata = userdata;
    }



     class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView senderMessage, senderDate, recieverMessage, recieverDate;

        private MyViewHolder(View view) {
            super(view);

            senderMessage =  view.findViewById(R.id.tvSenderMessage);
            senderDate =  view.findViewById(R.id.tvSenderDate);

            recieverMessage =  view.findViewById(R.id.tvRecieverMessage);
            recieverDate =  view.findViewById(R.id.tvRecieverDate);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_messages, parent, false);

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
    public void onBindViewHolder(MyViewHolder holder, int pos) {

        String message = null;
        try {
            message = URLDecoder.decode(mUserdata.get(pos).getMessage(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(mSender.equals(mUserdata.get(pos).getMessageSender()))
        {
            holder.senderMessage.setText(message);
            holder.senderDate.setText(helperMethods.FormatDateTime(mUserdata.get(pos).getMessageDateTime()));
            holder.recieverMessage.setVisibility(View.VISIBLE);
            holder.recieverDate.setVisibility(View.VISIBLE);

            holder.recieverMessage.setVisibility(View.GONE);
            holder.recieverDate.setVisibility(View.GONE);
        }
        else //if(mSender.equals(mUserdata.get(pos).getMessageReciever()))
        {
            holder.recieverMessage.setText(message);
            holder.recieverDate.setText(helperMethods.FormatDateTime(mUserdata.get(pos).getMessageDateTime()));
            holder.recieverMessage.setVisibility(View.VISIBLE);
            holder.recieverDate.setVisibility(View.VISIBLE);

            holder.senderMessage.setVisibility(View.GONE);
            holder.senderDate.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return mUserdata.size();
    }



    /*
    //Filter Interface
    @Override
    public Filter getFilter()
    {
        if(mFilter== null)
        {
            mFilter=new CustomFilter();
        }
        return mFilter;
    }



    //INNER CLASSES



    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                constraint=constraint.toString().toUpperCase();

                ArrayList<UserData> filters = new ArrayList<UserData>();

                //get specific items
                for(int i=0; i<mFilterUserdata.size(); i++)
                {
                    if(mFilterUserdata.get(i).getName().toUpperCase().contains(constraint))
                    {
                        UserData userData =new UserData(mFilterUserdata.get(i).getName(), mFilterUserdata.get(i).getLocation(), mFilterUserdata.get(i).getCaseType(),
                                mFilterUserdata.get(i).getRatingStars(), mFilterUserdata.get(i).getProfileImage());
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
            mUserdata=(ArrayList<UserData>) results.values;
            notifyDataSetChanged();
        }

    }
    */

}//class ends







//**************************************************************************************************







//Previous work
    /*
    Context mContext;
    ArrayList<UserData> mUserdata;
    CustomFilter mFilter;
    ArrayList<UserData> mFilterUserdata;

    String mAccountType;

    public People_Adapter (Context context, ArrayList<UserData> userdata, String accountType)
    {
        this.mContext = context;
        this.mUserdata = userdata;
        this.mFilterUserdata = userdata;
        this.mAccountType = accountType;
    }

    @Override
    public int getCount()
    {
        return mUserdata.size();
    }

    @Override
    public Object getItem(int pos)
    {
        return mUserdata.get(pos);
    }

    @Override
    public long getItemId(int pos)
    {
        return mUserdata.indexOf(getItem(pos));
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.list_item_people, null);
        }

        TextView textViewName = (TextView) convertView.findViewById(R.id.name_text_view);
        TextView textViewLocation = (TextView) convertView.findViewById(R.id.location_text_view);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);

        textViewName.setText(mUserdata.get(pos).getUsername());
        textViewLocation.setText(mUserdata.get(pos).getLocation());
        imageView.setImageResource(mUserdata.get(pos).getProfileImage());

        if(mAccountType.equals("lawyer"))
        {
            TextView textViewCaseType = (TextView) convertView.findViewById(R.id.castType_text_view);
            textViewCaseType.setText(mUserdata.get(pos).getCaseType());

            //diabling RatingBar
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            ratingBar.setEnabled(false);
        }
        else if(mAccountType.equals("client"))
        {
            RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            ratingBar.setRating(mUserdata.get(pos).getRatingStars());

            //disabling CaseTypetextView
            TextView textViewCaseType = (TextView) convertView.findViewById(R.id.castType_text_view);
            textViewCaseType.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public Filter getFilter()
    {
        if(mFilter== null)
        {
            mFilter=new CustomFilter();
        }
        return mFilter;
    }

    //INNER CLASS
    class CustomFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                constraint=constraint.toString().toUpperCase();

                ArrayList<UserData> filters=new ArrayList<UserData>();

                //get specific items
                for(int i=0; i<mFilterUserdata.size(); i++)
                {
                    if(mFilterUserdata.get(i).getUsername().toUpperCase().contains(constraint))
                    {
                        UserData userData =new UserData(mFilterUserdata.get(i).getUsername(),mFilterUserdata.get(i).getLocation(), mFilterUserdata.get(i).getCaseType());
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
            mUserdata=(ArrayList<UserData>) results.values;
            notifyDataSetChanged();
        }
    }

    */


