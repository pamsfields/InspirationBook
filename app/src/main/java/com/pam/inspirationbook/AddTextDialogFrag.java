package com.pam.inspirationbook;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Pam on 10/31/2017.
 */

public class AddTextDialogFrag extends DialogFragment {

    private static final String TAG = "InspirationBoard.addentrydialogfrag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_text, container, false);

        Dialog af = getDialog();
        af.setTitle(R.string.dialog_title_add_text);
        af.setCancelable(true);

        final EditText entryTextField = (EditText) v.findViewById(R.id.entry_text);

        Button cancelButton = (Button) v.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        Button saveButton = (Button) v.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String entryText = entryTextField.getText().toString();
                NoteEntry noteEntry = new NoteEntry(entryText);
                EntryManager.sEntryManager.addEntry(noteEntry);
                EntryLoader.mEntries.add(noteEntry);
                getActivity().getFragmentManager().popBackStack();
                MainActivity.sAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }
}