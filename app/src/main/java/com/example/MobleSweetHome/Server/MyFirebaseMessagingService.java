package com.example.MobleSweetHome.Server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.MobleSweetHome.R;
import com.example.MobleSweetHome.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService  extends FirebaseMessagingService
{
    NotificationManager notificationManager;
    String tag = "hyo";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...


        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, tag);
        wakeLock.acquire(3000);



        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("From: " + remoteMessage.getFrom());


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        sendNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody()); // 백그라운드 노티피케이션
        sendNotification(title, body); // 포그라운드 노티피케이션
    }

//    private void sendNotification(String from, String body) {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fire);
//
//        Intent intent = new Intent(this, LoginActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_MUTABLE);
//
//        NotificationCompat.Builder builder = getBuilder("00", "1234")
//                .setSmallIcon(android.R.drawable.ic_notification_overlay)
//                .setContentTitle("화재경보발생")
//                .setContentText(body)
//                .setLargeIcon(bitmap)
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
//                .setContentIntent(pendingIntent);
//
//        Notification notification = builder.build();
//        notificationManager.notify(1000, notification);
//    }


    private void sendNotification(String title, String body) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.firenotification);

        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = getBuilder("00", "1234")
                .setSmallIcon(R.drawable.sweethomeicon)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(bitmap)
                .setAutoCancel(true) // Notification을 클릭하면 자동으로 사라지도록
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notificationManager.notify(1000, notification);

//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_CANCEL_CURRENT);
//
//        String channelId = "mbsh";
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_stat_notification)
//                        .setContentTitle("화제시스템 경보발생")
//                        .setContentText(messageBody)
////                        .setAutoCancel(true)
////                        .setPriority(NotificationCompat.PRIORITY_MAX)
////                        .setDefaults(Notification.DEFAULT_VIBRATE)
////                        .setSound(defaultSoundUri)
//                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
//                        .setContentIntent(pendingIntent);
//
//
//        Notification notification = notificationBuilder.build();
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public NotificationCompat.Builder getBuilder(String channel_id, String channel_name) {
        // 낮은 버전을 사용하는 사용자가 있는 경우에 대한 처리 - 8.0부터 채널을 통해서 관리하도록 처리
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 버전 체크, 안드로이드 8.0
            // 채널을 만들고 등록하는 작업을 하도록
            NotificationChannel channel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_HIGH);

            // NotificaionManager를 통해서 채널을 등록한다. - Builder에 의해서 만들어진 Notification은 채널에 의해 관리
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true); // 진동

            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, channel_id);
        } else {
            // 이전 버전은 옛날 방식으로 Builder를 얻어오기
            builder = new NotificationCompat.Builder(this, channel_id);
        }
        return builder;
    }
}
