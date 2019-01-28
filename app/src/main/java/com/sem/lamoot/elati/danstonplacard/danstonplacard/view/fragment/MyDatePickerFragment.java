package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;

import java.util.Calendar;

/**
 * Fragment representing a calendar allowing the selection of a date for the deadline of consumption of a product
 */
public class MyDatePickerFragment extends DialogFragment {

    public final static String SELECTED_DATE = "com.sem.lamoot.elati.danstonplacard.danstonplacard.view.fragment.SELECTED_DATE";

    private DatePickerDialog.OnDateSetListener dateSetListener =
            (view, year, month, day) ->
            {
                String date  = view.getDayOfMonth() +
                        "/" + (view.getMonth() + 1) +
                        "/" + view.getYear();
                Bundle bundle = new Bundle();
                bundle.putString(SELECTED_DATE, date);
                Intent intent = new Intent().putExtras(bundle);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), R.style.DialogTheme, dateSetListener, year, month, day);
    }


}
