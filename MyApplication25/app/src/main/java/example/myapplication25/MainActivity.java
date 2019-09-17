package example.myapplication25;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    Boolean started = false;
    MyThread myTask = new MyThread();
    MyHandler myHandler = new MyHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startCount(View view)
    {
        if(!started)
        {
            //启动计时
            myTask = new MyThread();
            myTask.doStart();
            started = true; //设置状态为启动

            ((Button)findViewById(R.id.id_start)).setText("停止");
        }
        else
        {
            //停止计时
            myTask.doStop();
            started = false;
            ((Button)findViewById(R.id.id_start)).setText("开始");
        }
    }
    class MyThread extends Thread
    {
        boolean quitflag = false;
        //启动线程
        public  void  doStart()
        {
            start();
        }
        public void doStop()
        {
            quitflag = true;
            this.interrupt();
        }

        @Override
        public void run()
        {
            long startTime = System.currentTimeMillis();

            while(!quitflag)
            {
                long now = System.currentTimeMillis();
                int duration = (int)(now - startTime);

                //发送消息
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = duration;
                myHandler.sendMessage(msg);

                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    break;
                }
            }
        }
    }
    public class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
           if(msg.what == 1)
           {
               //从消息里取出参数
               int duration = msg.arg1;
               showTime(duration);
           }
           super.handleMessage(msg);
        }
    }
    public void showTime(int duration)
    {
        int ms = duration % 1000;
        int seconds = duration /1000;
        int ss = seconds % 60;
        int mm = seconds / 60;
        String text = String.format("%02d:%02d:%02d", mm, ss, ms/10);
        TextView currenttime = (TextView)findViewById(R.id.id_time);
        currenttime.setText(text);

    }
}
