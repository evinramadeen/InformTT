package com.evinram.informationrev2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView =inflater.inflate(R.layout.sub_layout,parent,false);

        TextView tvSubCategory =convertView.findViewById(R.id.tvSubCategory);
        final TextView tvSubDescription = convertView.findViewById(R.id.tvSubDescription);

        tvSubCategory.setText(subCategories.get(position).getSub_category());
        tvSubDescription.setText(subCategories.get(position).getFull_description());

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

        return convertView;
    }
}
