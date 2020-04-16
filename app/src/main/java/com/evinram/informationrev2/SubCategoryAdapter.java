package com.evinram.informationrev2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SubCategoryAdapter extends ArrayAdapter<SubCategory>
{
    private Context context;
    private List<SubCategory> subCategories;

    public SubCategoryAdapter(Context context, List<SubCategory> list)
    {
        super(context, R.layout.sub_layout, list);
        this.context = context;
        this.subCategories= list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView =inflater.inflate(R.layout.sub_layout,parent,false);

        TextView tvSubCategory =convertView.findViewById(R.id.tvSubCategory);
        final TextView tvSubDescription = convertView.findViewById(R.id.tvSubDescription);


        //puts the text on the sub category
        final String subNameHold= subCategories.get(position).getSub_category();
        final String holdText=subCategories.get(position).getFull_description();
        tvSubCategory.setText(subCategories.get(position).getSub_category());
        //tvSubDescription.setText(subCategories.get(position).getFull_description());this is the original way of setting the description without see more

        //creates the on click function to show the subcategory description
        tvSubCategory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(tvSubDescription.getVisibility()==View.GONE)
                {
                    tvSubDescription.setVisibility(View.VISIBLE);
                }
                else if (tvSubDescription.getVisibility()==View.VISIBLE)
                {
                    tvSubDescription.setVisibility(View.GONE);
                }

            }
        });

        int textLength=0;
        String dispText="";

        textLength=holdText.length();
        dispText = holdText.substring(0,textLength)+"... See More"; //this is only for testing, when large description is entered, use a value of 75 instead of textLength.



        SpannableString ss = new SpannableString(dispText);

        ClickableSpan clickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(@NonNull View widget)
            {
                Intent intent = new Intent(context, FullDescription.class);
                intent.putExtra("sub_category", subNameHold);
                intent.putExtra("full_description",holdText);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(context, "Click Registered on item "+ subNameHold, Toast.LENGTH_SHORT).show();
            }
        };

        ss.setSpan(clickableSpan,textLength,textLength+12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSubDescription.setText(ss);
        tvSubDescription.setMovementMethod(LinkMovementMethod.getInstance());



        return convertView;
    }
}
