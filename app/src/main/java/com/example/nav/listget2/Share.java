package com.example.nav.listget2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Share extends Activity
{
    private static final int CONTACT_PICKER_RESULT = 1001;
    private static EditText toEmail = null;
    private static EditText emailSubject = null;
    private static EditText emailBody = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        toEmail = (EditText) findViewById(R.id.invite_email);
        emailSubject = (EditText) findViewById(R.id.subject);
        emailBody = (EditText) findViewById(R.id.emailBody);
    }

    public void doLaunchContactPicker(View view)
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "";
                    try
                    {
                        Uri result = data.getData();
                        Log.v("DEBUG_TAG", "Got a contact result: "
                                + result.toString());

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[] { id },
                                null);

                        int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                        // let's just get the first email
                        if (cursor.moveToFirst())
                        {
                            email = cursor.getString(emailIdx);
                            Log.v("DEBUG_TAG", "Got email: " + email);
                        } else
                        {
                            Log.w("DEBUG_TAG", "No results");
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("DEBUG_TAG", "Failed to get email data", e);
                    }
                    finally
                    {
                        if (cursor != null)
                        {
                            cursor.close();
                        }
                        EditText emailEntry = (EditText) findViewById(R.id.invite_email);
                        emailEntry.setText(email);
                        if (email.length() == 0)
                        {
                            Toast.makeText(this, "No email found for contact.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
            }
        }
        else
        {
            Log.w("DEBUG_TAG", "Warning: activity result not ok");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.menu_clear:

                //E-mail will be set to this edit text
                toEmail.setText("");
                emailBody.setText("");
                emailSubject.setText("");
                break;

            case R.id.menu_send:

                String to = toEmail.getText().toString();
                String subject = emailSubject.getText().toString();
                String message = emailBody.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);

                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                // prompts email client
                email.setType("message/rfc822");

                //Enables user to pick another e-mail application other then the default
                startActivity(Intent.createChooser(email, "Choose"));

                break;
        }
        return true;
    }
}