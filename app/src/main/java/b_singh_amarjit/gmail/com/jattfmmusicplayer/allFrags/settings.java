package b_singh_amarjit.gmail.com.jattfmmusicplayer.allFrags;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import b_singh_amarjit.gmail.com.jattfmmusicplayer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class settings extends Fragment {
    public static String musicQuality = "";
    public static final String SHAREDPREF = "sharedPrefJATTFMMP";
    public static final String boxVal = "checkedBox";

    private RadioGroup mRadioGroup;
    private RadioButton lqB, mqB, hqB, hdB;

    private Context mContext;

    public settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_settings, container, false);
        mContext = getContext();

        mRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        lqB = (RadioButton) v.findViewById(R.id.lowQuality);
        mqB = (RadioButton) v.findViewById(R.id.mediumQuality);
        hqB = (RadioButton) v.findViewById(R.id.highQuality);
        hdB = (RadioButton) v.findViewById(R.id.highDefinition);

        getRadioVal();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean isChecked_LQ = lqB.isChecked();
                boolean isChecked_MQ = mqB.isChecked();
                boolean isChecked_HQ = hqB.isChecked();
                boolean isChecked_HD = hdB.isChecked();

                if(isChecked_HD){
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(boxVal, "HighDef");

                    Toast.makeText(mContext,"HD", Toast.LENGTH_LONG).show();
                    editor.apply();
                }
                else if (isChecked_HQ){
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(boxVal, "HighQuality");

                    Toast.makeText(mContext,"HQ", Toast.LENGTH_LONG).show();
                    editor.apply();
                }
                else if (isChecked_MQ){
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(boxVal, "MediumQuality");

                    Toast.makeText(mContext,"MQ", Toast.LENGTH_LONG).show();
                    editor.apply();
                }
                else if (isChecked_LQ){
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(boxVal, "LowQuality");

                    Toast.makeText(mContext,"LQ", Toast.LENGTH_LONG).show();
                    editor.apply();
                }
                else{
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(boxVal, "LowQuality");
                    editor.apply();
                }
            }
        });


        return v;
    }

    private void getRadioVal(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(boxVal, "MediumQuality");

        if(value.matches("HighDef")){
            hdB.setChecked(true);
        }
        else if(value.matches("HighQuality")){
            hqB.setChecked(true);
        }
        else if(value.matches("MediumQuality")){
            mqB.setChecked(true);
        }
        else if(value.matches("LowQuality")){
            lqB.setChecked(true);
        }
        else{
            mqB.setChecked(true);
        }
    }

}
