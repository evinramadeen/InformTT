package com.evinram.informationrev2;

import android.app.Activity;
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

import org.w3c.dom.Text;

import java.util.List;

//check in here, if the application class information is not being passed back in properly when you go into the full description, edit the favorite then go back out.
public class SubCategoryAdapter extends ArrayAdapter<SubCategory>
{
    private Context context;
    private List<SubCategory> subCategories;
    String textSize;//Used to store the text size from the Users table in the database

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

        //Next I am going to put in the functionality to change text size.
        textSize=ApplicationClass.user.getProperty("text_size").toString();

        switch (textSize)
        {
            case "Small":
                tvSubCategory.setTextSize(20);
                tvSubDescription.setTextSize(14);
                break;
            case "Medium":
                tvSubCategory.setTextSize(28);
                tvSubDescription.setTextSize(20);
                break;
            case "Large":
                tvSubCategory.setTextSize(36);
                tvSubDescription.setTextSize(28);
                break;
            default:
                tvSubCategory.setTextSize(24);
                tvSubDescription.setTextSize(16);
                break;
        }
        //puts the text on the sub category
        final String mainCatHold = ApplicationClass.subCategories.get(position).getMain_category();
        final String subNameHold= ApplicationClass.subCategories.get(position).getSub_category();
        final String holdText=ApplicationClass.subCategories.get(position).getFull_description().toString();
        tvSubCategory.setText(ApplicationClass.subCategories.get(position).getSub_category());
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


//This is what I use to make the see more clickable and subsequently go to the new activity when clicked. I also send the data that was clicked to the next activity.
        //I may have to review if it is a good idea to send the data through an intent when my data gets large.
        SpannableString ss = new SpannableString(dispText);

        ClickableSpan clickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick(@NonNull View widget)
            {
                Intent intents = new Intent(context, FullDescription.class);
                intents.putExtra("main_category",mainCatHold);
                intents.putExtra("sub_category", subNameHold);
                intents.putExtra("full_description",holdText);
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK); //flag activity clear task is used to basically end this activity.
                context.startActivity(intents);
               // ((Activity)context).finish();//This is how i end an activity in an adapter. Had to typecast it too an activity so that i could use the finish class.
            }
        };

        ss.setSpan(clickableSpan,textLength,textLength+12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSubDescription.setText(ss);
        tvSubDescription.setMovementMethod(LinkMovementMethod.getInstance());
//I have noticed an issue where the see more can be clicked on a blank part after the see more. Dont forget to find a way to fix that.


        return convertView;
    }


}
