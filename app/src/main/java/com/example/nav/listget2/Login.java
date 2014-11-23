package com.example.nav.listget2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;


public class Login extends Activity implements MongoInterface{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void login( View v )
    {
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        Mongo.getMongo( this ).get("users", "_id", email);
    }

    public void processResult( String result )
    {
        JSONArray arr;
        String email;
        Intent myIntent;

        try
        {
            arr = new JSONArray(result);
            if( arr.length() > 0 )
            {
                myIntent = new Intent( this, ListActivity.class );
                email = arr.getJSONObject(0).getString( "_id" );
                myIntent.putExtra( "email", email );
                startActivity( myIntent );
            }
        }
        catch (JSONException e)
        {
            Toast.makeText( this, "Invald email/password", Toast.LENGTH_LONG).show();
        }
    }
}
