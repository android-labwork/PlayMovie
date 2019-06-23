package com.aitekteam.developer.playmovie.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.aitekteam.developer.playmovie.ChangeLanguageActivity;
import com.aitekteam.developer.playmovie.MainActivity;
import com.aitekteam.developer.playmovie.R;
import com.aitekteam.developer.playmovie.interfaces.NetworkAPI;
import com.aitekteam.developer.playmovie.models.Movies;
import com.aitekteam.developer.playmovie.services.HttpsHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlarmReceiver extends BroadcastReceiver implements Callback<Movies> {
    private String messagee, title;
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.reminder_name);
            String description = context.getString(R.string.reminder_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.reminder_name), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

        title = "Hay Friends";
        Log.d("HAHAHA", "Test");

        try{
            boolean status = intent.getBooleanExtra(ChangeLanguageActivity.SHR_PRF, false);
            if (status) {
                messagee = "Come to use play movie today";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.reminder_name))
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(messagee)
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(1001, builder.build());
            }
            else {
                Retrofit retrofitInstance = new Retrofit
                        .Builder()
                        .baseUrl("https://api.themoviedb.org/3/discover/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(HttpsHandler.getInstance().build())
                        .build();

                NetworkAPI api = retrofitInstance.create(NetworkAPI.class);
                Call<Movies> call = api.getMovies(context.getString(R.string.api_key), "en-US", 1);

                call.enqueue(this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        if (response.isSuccessful() && response.body() != null) {
            Movies data = response.body();
            int count = 0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            final String currentDate = simpleDateFormat.format(new Date());
            for (int i = 0; i < data.getItems().size(); i++) {
                if (currentDate.equalsIgnoreCase(data.getItems().get(i).getDate()))
                    count++;
            }

            messagee = "You Have " + count + " New Movies Release";

            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.reminder_name))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(title)
                    .setContentText(messagee)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            notificationManager.notify(1001, builder.build());
        }
    }

    @Override
    public void onFailure(Call<Movies> call, Throwable t) {

    }
}
