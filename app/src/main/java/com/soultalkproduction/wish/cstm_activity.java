package com.soultalkproduction.wish;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class cstm_activity extends AppCompatActivity {


    private static final int RESULT_PICK_CONTACT =1;
    private EditText eTextb,nameb,contb,msgb,dateb,iddb,eventc;
    private Button saveb,viewb,dltb,bpk,back;
    private DatePickerDialog pickerb;
    DatabaseHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cstm_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        saveb = (Button) findViewById(R.id.save_c);

        nameb = (EditText) findViewById(R.id.name_c);

        msgb = (EditText) findViewById(R.id.msg_c);

        contb = (EditText) findViewById(R.id.cont_c);

        eventc = (EditText) findViewById(R.id.event_c);

        mydb = new DatabaseHelper(this);

        viewb = (Button) findViewById(R.id.show_c);

        iddb = (EditText) findViewById(R.id.I_d_c);

        dltb = (Button) findViewById(R.id.delete_c);

        bpk = (Button) findViewById(R.id.phonebook_icon_c);

        back = (Button) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bpk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult (intent, RESULT_PICK_CONTACT);
            }

        });


        AddData();
        ViewData();
        deleteData();

        eTextb = (EditText) findViewById(R.id.date_c);
        eTextb.setInputType(InputType.TYPE_NULL);
        eTextb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                pickerb= new DatePickerDialog(cstm_activity.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        eTextb.setText(dayOfMonth + "/" + (monthOfYear + 1));
                    }
                }, year, month, day);
                pickerb.show();
                pickerb.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Toast.makeText(this, "Failed To pick contact", Toast.LENGTH_SHORT).show();
        }
    }


    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneNo = null;
            Uri uri = data.getData ();
            cursor = getContentResolver ().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            int phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);
            String nam = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNo = cursor.getString (phoneIndex);

            contb.setText (phoneNo);
            nameb.setText (nam);


        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void AddData()
    {
        saveb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_b = nameb.getText().toString();
                String cont_b = contb.getText().toString();
                String message_b = msgb.getText().toString();
                String date_b = eTextb.getText().toString();
                String event_b = "CUSTOM : "+String.valueOf(eventc.getText());

                Integer nn = nameb.getText().toString().length();
                Integer cn = contb.getText().toString().length();
                Integer mn = msgb.getText().toString().length();
                Integer dn = eTextb.getText().toString().length();

                if(nn>0 && cn>0 && mn>0 && dn>0){
                    boolean insertData = mydb.addData(name_b,cont_b,date_b,message_b,event_b);
                    if (insertData == true){
                        Toast.makeText(cstm_activity.this,"Information added",Toast.LENGTH_LONG).show();
                        nameb.setText("");
                        contb.setText("");
                        msgb.setText("");
                        eTextb.setText("");
                    }else {
                        Toast.makeText(cstm_activity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(cstm_activity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void ViewData(){
        viewb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = mydb.showData3();

                StringBuffer buffer = new StringBuffer();
                if (data.getCount()==0){
                    buffer.append("\nNo events added\n");
                }
                while(data.moveToNext()){
                    buffer.append("ID : "+ data.getString(0)+"\n\n");
                    buffer.append("NAME : "+ data.getString(1)+"\n");
                    buffer.append("CONTACT : "+ data.getString(2)+"\n");
                    buffer.append("DATE : "+ data.getString(3)+"\n");
                    buffer.append("MESSAGE : "+ data.getString(4)+"\n\n");

                }

                Display("Customs : ",buffer.toString());
            }
        });
    }


    public void Display(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setInverseBackgroundForced(true);
        builder.setMessage(message);
        builder.show();
    }

    public void deleteData(){
        dltb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = iddb.getText().toString().length();
                if(temp>0){
                    Integer deleteRow = mydb.deletee(iddb.getText().toString());
                    if(deleteRow>0){
                        Toast.makeText(cstm_activity.this,"Successfully deleted information",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(cstm_activity.this,"Please make sure Id Exists",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(cstm_activity.this,"Please enter ID",Toast.LENGTH_LONG).show();
                }

                iddb.setText("");
            }
        });
    }


}
