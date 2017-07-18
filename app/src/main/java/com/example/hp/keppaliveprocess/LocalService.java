package com.example.hp.keppaliveprocess;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hp on 2017/7/18.
 */

public class LocalService extends Service{
    private static final String TTAG="gyf";
    private MyServiceConnection myServiceConnection;
    MyBinder myBinder;
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }
    class MyBinder extends RemoteConnection.Stub{

        @Override
        public String  getProcessName() throws RemoteException {
            return "LocalSerivce";
        }
    }
    class MyServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TTAG,"建立连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(LocalService.this,"LocalService销毁",Toast.LENGTH_SHORT);
//启动被干掉的
            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
        }
    }
}
