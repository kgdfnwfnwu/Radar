package com.example.radar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private RadarView graph = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		graph = (RadarView)findViewById(R.id.radarView);
       
        String valueStr ="8,8,8,8,8,8,8,8";
        try {
            String[] valuesStr = valueStr.split(",");
            float[] values = new float[valuesStr.length];
            for(int i = 0; i <valuesStr.length; i++)
                values[i] = Float.parseFloat(valuesStr[i]);
            
            graph.setValues(values);
            graph.postInvalidate();
        } catch (Exception e) {
        	Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
	}
	
	 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
