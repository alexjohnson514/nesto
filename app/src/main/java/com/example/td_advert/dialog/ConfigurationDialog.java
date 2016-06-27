package com.example.td_advert.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.td_advert.LauncharScreen;
import com.example.td_advert.R;
import com.example.td_advert.bean.Station;
import com.example.td_advert.constant.TadvertConstants;
import com.example.td_advert.database.TaxiDetails;
import com.example.td_advert.database.TestAdapter;
import com.example.td_advert.exception.PhoneStatus;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationDialog extends TAdvertBaseDialog {

	private EditText txtTaxiLicense;
	private TextView versionNo, testDeviceTxt;
    private Spinner taxiStation;
    private Context ctx;

	public ConfigurationDialog(Context context, ViewGroup parent) {
		this(context, parent, true);
	}

	public ConfigurationDialog(Context context, ViewGroup parent,
			boolean showRating) {
		super(context, parent);

        ctx = context;

		findViewById(R.id.exit_app_button).setOnClickListener(this);
		findViewById(R.id.save_button).setOnClickListener(this);
		txtTaxiLicense = (EditText) findViewById(R.id.txtLiscenceNo);
        taxiStation = (Spinner) findViewById(R.id.taxiStationSelector);
        testDeviceTxt = (TextView) findViewById(R.id.testDevice);
		versionNo = (TextView) findViewById(R.id.txtbuildno);

        versionNo.setText("Version: " + PhoneStatus.fetchStatus(context).getVersion());
        loadConfigurations();

	}

	private void loadConfigurations() {
        TestAdapter mDbHelper = new TestAdapter(getContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        String taxiNo = mDbHelper.getTaxiNo();
        Integer testDevice = mDbHelper.getTestDeviceStatus();
        ArrayList<Station> stationNames = mDbHelper.getStationNames();
        mDbHelper.close();
        txtTaxiLicense.setText(taxiNo);

        String isTester = "Test Device: False";
        if(testDevice == 1){
            isTester = "Test Device: True";
        }
        testDeviceTxt.setText(isTester);

        List<String> list = new ArrayList<String>();
        list.add("Not Assign");
        for(Station sta : stationNames){
            list.add(sta.getStationName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taxiStation.setAdapter(dataAdapter);
        taxiStation.setSelection(mDbHelper.getTaxiStationID());

//
//        ArrayAdapter<String> adapter = (ArrayAdapter<String>) taxiStation.getAdapter();
//        List<String> spinnerArray =  new ArrayList<String>();
//        for(Station sta : stationNames){
//            adapter.add(sta.getStationName());
//        }
//        adapter.notifyDataSetChanged();

	}

	@Override
	protected int getLayoutId() {
		return R.layout.configuration_dialog;
	}

	@Override
	protected void onClickButton(View view) {
		switch (view.getId()) {
		case R.id.exit_app_button:
			Intent intent = new Intent(TadvertConstants.CLOSE_INTENT);
			this.activity.sendBroadcast(intent);
			this.activity.finish();
			break;

		case R.id.save_button:
			saveConfiguration();
			break;
		}

		this.dismiss();
	}

	private void saveConfiguration() {

		String taxiNo = txtTaxiLicense.getText().toString();

		if (taxiNo != null && !taxiNo.equals("")) {
			/*TestAdapter dbAdapter = new TestAdapter(this.getContext());
			dbAdapter
					.saveTaxiConfiguration(TadvertConstants.CONFIG_TAXI_NO, taxiNo);*/

            TestAdapter mDbHelper = new TestAdapter(getContext());
            mDbHelper.createDatabase();
            mDbHelper.open();
            mDbHelper.SaveTaxiNo(taxiNo);
            mDbHelper.SaveTaxiStationID((int) taxiStation.getSelectedItemId());
            mDbHelper.setUpdateStatus(1);
            mDbHelper.close();

            if(LauncharScreen.isConnectingToInternet(getContext())){
                TaxiDetails sn = new TaxiDetails(getContext());
                sn.syncDetails();
            }
			//AppState.getInstance().setTaxiNumber(taxiNo);
		} else {
			Toast.makeText(this.activity, "Please Enter the Taxi Number",
					Toast.LENGTH_LONG).show();
		}
	}

}
