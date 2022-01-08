package com.soultalkproduction.wish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.soultalkproduction.wish.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soultalkproduction.wish.databinding.ActivityMainBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Animation dropdown,dropup,fi,fo,fst,scnd,trd;
    FirebaseDatabase db;
    String webUrl;
    FirebaseAuth auth;
    TextView slog;
    DatabaseHelper mydb,bdmydb,anmydb,csmydb;

    private NotificationManagerCompat notificationManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sendSMSPermit();


        String devid = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Log.d("deiceidis",devid);
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String im = "Not authorized";
        try {
            im = telephonyManager.getDeviceId();
        }
        catch (Exception e){}
        DocumentReference d = FirebaseFirestore.getInstance().collection("Users").document(devid);
        HashMap<String,String> iddd = new HashMap<String,String>();
        iddd.put("device_id",devid);
        iddd.put("imei_numb",im);
        d.set(iddd);


        dropdown = AnimationUtils.loadAnimation(this,R.anim.dropdown);
        dropup = AnimationUtils.loadAnimation(this,R.anim.dropup);
        fi = AnimationUtils.loadAnimation(this,R.anim.fi);
        fo = AnimationUtils.loadAnimation(this,R.anim.fo);
        fst = AnimationUtils.loadAnimation(this,R.anim.fst);
        scnd = AnimationUtils.loadAnimation(this,R.anim.scnd);
        trd = AnimationUtils.loadAnimation(this,R.anim.trd);

        mydb = new DatabaseHelper(this);

        binding.title.setAnimation(fi);
        binding.annyActb.setAnimation(fst);
        binding.bdayActb.setAnimation(scnd);
        binding.cstmActb.setAnimation(trd);

        slog = (TextView) findViewById(R.id.slog);

        setTextcol(binding.slog,getResources().getColor(R.color.logreen)
        ,getResources().getColor(R.color.logpink));

        LocalTime now = LocalTime.now();

//        String tim = String.valueOf(now.with(LocalTime.MIDNIGHT));
//        tim = tim.split(":",0)[1];


        startService();

        binding.abtlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(webUrl); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });


        binding.bdayActb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bday_act_open();
            }
        });

        binding.annyActb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anny_act_open();
            }
        });

        binding.cstmActb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cstm_act_open();
            }
        });

        binding.abtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.abtinfo.setAnimation(dropdown);
                binding.abtinfo.getAnimation().start();
                binding.abtinfo.setVisibility(View.VISIBLE);
                binding.share.setVisibility(View.VISIBLE);
                binding.today.setVisibility(View.GONE);
            }
        });

        binding.abtth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.abtinfo.setAnimation(dropup);
                binding.abtinfo.getAnimation().start();
                binding.abtinfo.setVisibility(View.GONE);
                binding.share.setVisibility(View.GONE);
                binding.today.setVisibility(View.VISIBLE);
            }
        });

        binding.today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper dbb = new DatabaseHelper(MainActivity.this);
                Cursor dataa = mydb.showData4();
                StringBuffer buffer = new StringBuffer();
                int i = 0,c=0;
                Date d = new Date();
                String curdate = String.valueOf(d.getDate() + "/" + (d.getMonth() + 1));
                while (dataa.moveToNext()) {
                    if (curdate.equals(dataa.getString(3))) {
                        c++;
                        String f = dataa.getString(5);
                        try {
                            f = f.split(":", 0)[1];
                        } catch (Exception e) {
                        }
                        i++;
                        buffer.append(String.valueOf(i + ". " + dataa.getString(1) + " : " + f + "\n"));
                    }
                }

                if(c==0){
                    buffer.append("\nNo events today\n");
                }

                Display("Today's Events : ",buffer.toString());
            }
        });

        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.cont_help).setTitle("Contact")
                        .setMessage("Email your concern at\n" +
                                "soultalk.production@gmail.com\n" +
                                "and add *Wish* in Subject")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wish");
                    String shareMessage= "\nHey! I have been using this application called Wish for a while and I think you should try this\n\nIt's really great to get relieved from remembering ocassions";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share On"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
    }

    //---------------------------------------------- Functionalities--------------------------------------------------


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startService() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, StartTask.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        Date date = new Date();
        String d = formatter.format(date);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(d)+1);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 01);
        Log.d(("timecheck"),String.valueOf(calendar.getTimeInMillis()));
        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    }


    private void setTextcol(TextView slog,int...color){
        TextPaint paint = slog.getPaint();
        float width =   paint.measureText(slog.getText().toString());
        Shader shader = new LinearGradient(0,0,width,slog.getTextSize(),color,null,Shader.TileMode.CLAMP);
        slog.getPaint().setShader(shader);
        slog.setTextColor(color[0]);
    }


    public void bday_act_open()
    {
        Intent intent = new Intent(MainActivity.this, bday_activity.class);
        startActivity(intent);
    }


    public void Display(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setInverseBackgroundForced(true);
        builder.setMessage(message);
        builder.show();
    }

    public void anny_act_open(){
        Intent intent = new Intent(MainActivity.this,anny_activity.class);
        startActivity(intent);
    }

    public void cstm_act_open(){
        Intent intent = new Intent(MainActivity.this,cstm_activity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String im = "start";

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("APP_FILES").document("info");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        binding.namecmp.setText(String.valueOf(document.get("namecmp")));
                        binding.slog.setText(String.valueOf(document.get("slog")));
                        webUrl=String.valueOf(document.get("weblink"));
                        Picasso.get()
                                .load(String.valueOf(document.get("logimg")).trim())
                                .fit().centerInside()
                                .placeholder(R.drawable.soul_talk_logo)
                                .into(binding.logimg, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("check-succ","set");
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("check-err",e.getMessage());
                                    }
                                });

                    } else {
                        webUrl="https://firebasestorage.googleapis.com/v0/b/wish-df567.appspot.com/o/APP_FILES%2Fsoul_talk_logo.jpg?alt=media&token=a18ee85a-570b-47fb-8fcb-560b39ad40ef";
                        Picasso.get()
                                .load(String.valueOf(document.get("logimg")).trim())
                                .fit().centerInside()
                                .placeholder(R.drawable.soul_talk_logo)
                                .into(binding.logimg, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("check-success","set");
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Log.d("check-error",e.getMessage());
                                    }
                                });
                    }
                } else {
                }
            }
        });

    }


    private void sendSMSPermit(){

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        boolean flag =false;
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS)) {
            permissionsNeeded.add("android.permission.READ_PHONE_STATE");
            flag = true;
        }
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS)) {
            permissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
            flag = true;
        }
        if(flag){
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissionsList.toArray(new String[permissionsList.size()]),
                    101);
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                return false;
        }
        return true;
    }
}
