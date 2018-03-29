package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    public final static String SHOW_ADDITIONAL = "SHOW ADDITIONAL FIELDS";
    public final static String HIDE_ADDITIONAL = "HIDE ADDITIONAL FIELDS";
    public final static int CONTACTS_MANAGER_REQUEST_CODE = 2;
    Button showAdditionalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        showAdditionalButton = findViewById(R.id.show_additional);
        showAdditionalButton.setText(SHOW_ADDITIONAL);
        Button saveButton = findViewById(R.id.save);
        Button cancelButton = findViewById(R.id.cancel);

        MyButtonListener buttonListener = new MyButtonListener();
        showAdditionalButton.setOnClickListener(buttonListener);
        saveButton.setOnClickListener(buttonListener);
        cancelButton.setOnClickListener(buttonListener);

        EditText phoneEditText = findViewById(R.id.phone_number);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    class MyButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.show_additional:
                    LinearLayout extendedFields = findViewById(R.id.extended_fields);
                    if (extendedFields.getVisibility() == View.VISIBLE) {
                        extendedFields.setVisibility(View.GONE);
                        showAdditionalButton.setText(SHOW_ADDITIONAL);
                    }
                    else if (extendedFields.getVisibility() == View.GONE) {
                        extendedFields.setVisibility(View.VISIBLE);
                        showAdditionalButton.setText(HIDE_ADDITIONAL);
                    }
                    break;
                case R.id.save:
                    String name = ((EditText) findViewById(R.id.name)).getText().toString();
                    String phone = ((EditText) findViewById(R.id.phone_number)).getText().toString();
                    String email = ((EditText) findViewById(R.id.email)).getText().toString();
                    String address = ((EditText) findViewById(R.id.address)).getText().toString();
                    String jobTitle = ((EditText) findViewById(R.id.job_title)).getText().toString();
                    String company = ((EditText) findViewById(R.id.company)).getText().toString();
                    String website = ((EditText) findViewById(R.id.website)).getText().toString();
                    String im = ((EditText) findViewById(R.id.im)).getText().toString();

                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                    if (!name.isEmpty()) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    }
                    if (!phone.isEmpty()) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                    }
                    if (!email.isEmpty()) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    }
                    if (!address.isEmpty()) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                    }
                    if (!jobTitle.isEmpty()) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                    }
                    if (!company.isEmpty()) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                    }
                    ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                    if (!website.isEmpty()) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                        contactData.add(websiteRow);
                    }
                    if (!im.isEmpty()) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                        contactData.add(imRow);
                    }
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                    // startActivity(intent);
                    startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
                    break;
                case R.id.cancel:
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}
