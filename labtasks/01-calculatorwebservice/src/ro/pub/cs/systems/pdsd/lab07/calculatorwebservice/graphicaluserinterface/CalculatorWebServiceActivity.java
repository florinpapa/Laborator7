package ro.pub.cs.systems.pdsd.lab07.calculatorwebservice.graphicaluserinterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import ro.pub.cs.systems.pdsd.lab07.calculatorwebservice.R;
import ro.pub.cs.systems.pdsd.lab07.calculatorwebservice.general.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CalculatorWebServiceActivity extends Activity {
	
	private EditText operator1EditText, operator2EditText;
	private TextView resultTextView;
	private Spinner operationsSpinner, methodsSpinner;
	
	private class CalculatorWebServiceThread extends Thread {
		
		@Override
		public void run() {
			
			// TODO: exercise 4
			// get operators 1 & 2 from corresponding edit texts (operator1EditText, operator2EditText)
			EditText op1_edit = (EditText)findViewById(R.id.operator1_edit_text);
			String op1 = op1_edit.getText().toString();
			
			EditText op2_edit = (EditText)findViewById(R.id.operator2_edit_text);
			String op2 = op2_edit.getText().toString();
			
			// signal missing values through error messages
			if (op1 == null || op2 == null) {
				Log.e("ERR", "Null ooperators");
				return;
			}
			
			// get operation from operationsSpinner
			Spinner op_spinner = (Spinner) findViewById(R.id.operations_spinner);
			String operation = op_spinner.getSelectedItem().toString();
			
			// create an instance of a HttpClient object
			HttpClient httpClient = new DefaultHttpClient();
			
			// get method used for sending request from methodsSpinner
			Spinner meth_sp = (Spinner) findViewById(R.id.methods_spinner);
			String method = meth_sp.getSelectedItem().toString();
			
			HttpResponse response = null;
			if (method.equals("GET")) {
				Log.e("Err", "Sending GET request");
				HttpGet httpGet = new HttpGet(Constants.GET_WEB_SERVICE_ADDRESS
                        + "?" + Constants.OPERATION_ATTRIBUTE + "=" + operation
                        + "&" + Constants.OPERATOR1_ATTRIBUTE + "=" + op1
                        + "&" + Constants.OPERATOR2_ATTRIBUTE + "=" + op2);
				try {
					response = httpClient.execute(httpGet);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final HttpEntity httpGetEntity = response.getEntity();
				if (httpGetEntity != null) {  
				    // do something with the response
				    final TextView tv = (TextView) findViewById(R.id.result_text_view);
				    try {
						tv.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									String s = EntityUtils.toString(httpGetEntity);
									Log.e("Err", "Got response " + s);
									tv.setText(s);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);
				List<NameValuePair> params = new ArrayList<NameValuePair>();        
				params.add(new BasicNameValuePair(Constants.OPERATION_ATTRIBUTE, operation));
				params.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, op1));
				params.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, op2));
				try {
				  UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				  httpPost.setEntity(urlEncodedFormEntity);
				} catch (UnsupportedEncodingException unsupportedEncodingException) {
				  Log.e(Constants.TAG, unsupportedEncodingException.getMessage());
				  if (Constants.DEBUG) {
				    unsupportedEncodingException.printStackTrace();
				  }						
				}
				try {
					response = httpClient.execute(httpPost);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				final HttpEntity httpGetEntity = response.getEntity();
				if (httpGetEntity != null) {  
				    // do something with the response
				    final TextView tv = (TextView) findViewById(R.id.result_text_view);
				    try {
						tv.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									String s = EntityUtils.toString(httpGetEntity);
									Log.e("Err", "Got response " + s);
									tv.setText(s);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// 1. GET
			// a) build the URL into a HttpGet object (append the operators / operations to the Internet address)
			// b) create an instance of a ResultHandler object
			// c) execute the request, thus generating the result
			
			// 2. POST
			// a) build the URL into a HttpPost object
			// b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
			// c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
			// d) create an instance of a ResultHandler object
			// e) execute the request, thus generating the result
			Log.e("Err", "here here");
			
			// display the result in resultTextView
			            
		}
	}
	
	private DisplayResultButtonClickListener displayResultButtonClickListener = new DisplayResultButtonClickListener();
	private class DisplayResultButtonClickListener implements Button.OnClickListener {
		
		@Override
		public void onClick(View view) {
			new CalculatorWebServiceThread().start();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator_web_service);
		
		operator1EditText = (EditText)findViewById(R.id.operator1_edit_text);
		operator2EditText = (EditText)findViewById(R.id.operator2_edit_text);
		
		resultTextView = (TextView)findViewById(R.id.result_text_view);
		
		operationsSpinner = (Spinner)findViewById(R.id.operations_spinner);
		methodsSpinner = (Spinner)findViewById(R.id.methods_spinner);
		
		Button displayResultButton = (Button)findViewById(R.id.display_result_button);
		displayResultButton.setOnClickListener(displayResultButtonClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculator_web, menu);
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
