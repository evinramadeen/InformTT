package com.evinram.informationrev2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SingleChoiceDialogFragment extends DialogFragment
{

    int position=-1; //-1 allows it to not have any radio button checked by default.

//this function will be used to get the choice and carry it back to the base activity that activated the dialog.
    public interface SingleChoiceListener
    {
        void onPositiveButtonClicked(String[] list, int position);
        void onNegativeButtonClicked();
    }

//make a variable of the type Single choice listener to use.
    SingleChoiceListener mListener;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            mListener = (SingleChoiceListener) context;
        } catch (Exception e)
        {
            throw new ClassCastException(getActivity().toString()+"Single Choice listener must be implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //I created a list in the list.xml file and I am accessing it here.
        final String[] list = getActivity().getResources().getStringArray(R.array.text_size_choice);


        builder.setTitle("Please select a Font Size");
        builder.setSingleChoiceItems(list, position, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                position = which;//determining and obtaining which of the choices was selected.

            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {//Since I am currently unable to keep whatever option the user selected to display when they press the change font button again, I am leaving the options blank.
                //to allow for this, I am making sure that the user enters a selection or it just basically comes out of the dialog box when he presses ok without doing anything.
                if(position<0 || position>2)
                {
                    Toast.makeText(getContext(), "No Selection Made!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mListener.onPositiveButtonClicked(list,position);//obtain what was clicked by the user and pass it to the listener
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                mListener.onNegativeButtonClicked();

            }
        });
        return builder.create();

    }
}
