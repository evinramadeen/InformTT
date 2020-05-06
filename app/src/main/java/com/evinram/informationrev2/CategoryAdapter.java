package com.evinram.informationrev2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category>
{
    private Context context;
    private List<Category> categories;
    String textSize;

    public CategoryAdapter(Context context, List<Category> list)
    {
        super(context, R.layout.row_layout, list);
        this.context = context;
        this.categories=list;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//because or data isnt on an activity layout as normal

        convertView = inflater.inflate(R.layout.row_layout,parent,false);

        ImageView ivCategory = convertView.findViewById(R.id.ivCategory);
        TextView tvMainCat = convertView.findViewById(R.id.tvMainCat);
        TextView tvSubCat = convertView.findViewById(R.id.tvSubCat);

        textSize = ApplicationClass.user.getProperty("text_size").toString();
        switch (textSize)
        {
            case "Small":
                tvMainCat.setTextSize(20);
                tvSubCat.setTextSize(14);
                break;
            case "Medium":
                tvMainCat.setTextSize(28);
                tvSubCat.setTextSize(20);
                break;
            case "Large":
                tvMainCat.setTextSize(36);
                tvSubCat.setTextSize(28);
                break;
            default:
                tvMainCat.setTextSize(24);
                tvSubCat.setTextSize(16);
                break;
        }
        tvMainCat.setText(categories.get(position).getMain_category());
        tvSubCat.setText(categories.get(position).getSub_categories());
        String tempPhoto = categories.get(position).getPhoto(); //getting the string from the database with what image should be shown
        //ivCategory.setImageResource(R.drawable.business);

        switch (tempPhoto)
        {
            case "automobile":
                ivCategory.setImageResource(R.drawable.automotive);
                break;
            case "business":
                ivCategory.setImageResource(R.drawable.business);
                break;
            case "finance":
                ivCategory.setImageResource(R.drawable.finance);
                break;
            default:
                ivCategory.setImageResource(R.drawable.lockk);
                break;
        }



        return convertView;
    }


}
