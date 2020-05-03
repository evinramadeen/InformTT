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

public class FavoritesAdapter extends ArrayAdapter<Favorites>
{
    private Context context;
    private List<Favorites> favorites;

    public FavoritesAdapter(Context context, List<Favorites> list)
    {
        super(context, R.layout.favorites_layout,list);
        this.context=context;
        this.favorites=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.favorites_layout,parent,false);

        TextView tvFavorites = convertView.findViewById(R.id.tvFavorites);
        tvFavorites.setText(favorites.get(position).getSub_category());

        return convertView;
    }
}
