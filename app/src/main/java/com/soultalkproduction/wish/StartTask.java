package com.soultalkproduction.wish;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class StartTask extends BroadcastReceiver {

    //the method will be fired when the alarm is triggerred
    DatabaseHelper mydb;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intentt) {
        mydb = new DatabaseHelper(context);

        Date d = new Date();
        String curdate = String.valueOf(d.getDate()+"/"+(d.getMonth()+1));
        Cursor data = mydb.showData4();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int logo=R.drawable.events,i=0;
        StringBuffer buffer=new StringBuffer();
        while(data.moveToNext()){
            if(curdate.equals(data.getString(3))){
                String f = data.getString(5);
                try{f=f.split(":",0)[1];}
                catch(Exception e){}
                i++;
                buffer.append(String.valueOf(i+". " +data.getString(1)+" : "+f+"\n"));


                String msg = data.getString(4);
                String numb = String.valueOf(data.getLong(2));

//                sendSMS(msg,numb);


                try{

                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(numb,null,msg,null,null);
                }
                catch (Exception e){
                    Log.d("errorsend",e.getMessage());
                }

            }


        }

        if(!(buffer.toString().length() >0)){
            buffer.append("No events for today !");
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel=new NotificationChannel("my_notification","notify_event",NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("Check Today's Events");
            notificationChannel.setName("Notifying");
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

        }



        Log.d(("timecheck"),String.valueOf("Ran in bg"));

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), logo))
                .setContentTitle(String.valueOf("Today' Events : "+i))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(buffer.toString()))
                .setContentText(buffer.toString())
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setOnlyAlertOnce(true)
                .setChannelId("my_notification")
                .setColor(Color.parseColor("#FF0A10"));

        assert notificationManager != null;
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, notificationBuilder.build());


    }


    private void sendSMS(String msg,String numb){
    }

}