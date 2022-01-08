package com.soultalkproduction.wish;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Calendar;

public class anny_activity extends AppCompatActivity {


    private EditText eTexta,namea,conta,msga,datea,idd;
    private DatePickerDialog pickera;
    private Button savea,viewa,dlta,back;

    DatabaseHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anny_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mydb = new DatabaseHelper(this);

        savea = (Button)findViewById(R.id.save_a);

        namea = (EditText)findViewById(R.id.name_a);

        conta = (EditText) findViewById(R.id.cont_a);

        msga = (EditText)findViewById(R.id.msg_a);

        viewa = (Button) findViewById(R.id.show_a);

        dlta  = (Button) findViewById(R.id.delete_a);

        idd = (EditText) findViewById(R.id.I_d_a);

        back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        AddData();
        ViewData();
        deleteData();

        eTexta = (EditText) findViewById(R.id.date_a);
        eTexta.setInputType(InputType.TYPE_NULL);
        eTexta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                pickera = new DatePickerDialog(anny_activity.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        eTexta.setText(dayOfMonth + "/" + (monthOfYear + 1));
                    }
                }, year, month, day);
                pickera.show();
            }
        });
    }


    public void AddData() {
        savea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(anny_activity.this, "Save Clicked", Toast.LENGTH_LONG);
                String name_a = namea.getText().toString();
                String cont_a = conta.getText().toString();
                String message_a = msga.getText().toString();
                String date_a = eTexta.getText().toString();
                String event_a = "ANNIVERSARY";

                Integer nn = namea.getText().toString().length();
                Integer cn = conta.getText().toString().length();
                Integer mn = msga.getText().toString().length();
                Integer dn = eTexta.getText().toString().length();

                if(nn>0 && cn>0 && mn>0 && dn>0){
                    boolean insertData = mydb.addData(name_a, cont_a, date_a, message_a, event_a);
                    if (insertData == true) {
                        Toast.makeText(anny_activity.this, "Information added", Toast.LENGTH_LONG).show();
                        namea.setText("");
                        conta.setText("");
                        msga.setText("");
                        eTexta.setText("");
                    } else {
                        Toast.makeText(anny_activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(anny_activity.this, "Please fill all the details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void ViewData(){
        viewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = mydb.showData2();

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

                Display("ANNIVERSARIES : ",buffer.toString());
            }
        });
    }

    public void Display(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void deleteData(){
        dlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = idd.getText().toString().length();
                if(temp>0){
                    Integer deleteRow = mydb.deletee(idd.getText().toString());
                    if(deleteRow>0){
                        Toast.makeText(anny_activity.this,"Successfully deleted data",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(anny_activity.this,"Please make sure Id Exists",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(anny_activity.this,"Please enter ID",Toast.LENGTH_LONG).show();
                }

                idd.setText("");
            }
        });
    }
}
