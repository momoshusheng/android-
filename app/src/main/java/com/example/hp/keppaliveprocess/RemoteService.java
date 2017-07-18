package com.example.hp.keppaliveprocess;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hp on 2017/7/18.
 */

public class RemoteService extends Service{
    private static final String TAG="gyf";
    private MyBinder myBinder;
    private MyServiceConnection myServiceConnection;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (myBinder==null){
            myBinder=new MyBinder();
        }
        myServiceConnection=new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), myServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }
    class MyBinder extends RemoteConnection.Stub{

        @Override
        public String  getProcessName() throws RemoteException {
                return "RemoteService";
        }
    }
    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG,"建立连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(RemoteService.this,"断开连接",Toast.LENGTH_SHORT);
            //启动被干掉的
            RemoteService.this.startService(new Intent(RemoteService.this, LocalService.class));
            RemoteService.this.bindService(new Intent(RemoteService.this, LocalService.class), myServiceConnection, Context.BIND_IMPORTANT);
        }
    }
}
