package com.example.meal_pedometer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView text_count;
    private ImageButton btn_up, btn_donw;
    private Button btn_start;
    private ImageView imageView_spoon;

    private int[] spoon_images_id;

    private int count, plain_count;
    private boolean t_stop;

    private timeThread tt;

    private MediaPlayer mp;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("식사 만보기");

        spoon_images_id = new int[]{R.drawable.spoon_100, R.drawable.spoon_half_48, R.drawable.icons_bowl_with_spoon_emt};

        text_count = findViewById(R.id.text_count);

        btn_up = findViewById(R.id.btnup);
        btn_up.setOnClickListener(this);

        btn_donw = findViewById(R.id.btndown);
        btn_donw.setOnClickListener(this);

        btn_start = findViewById(R.id.btnstart);
        btn_start.setOnClickListener(this);

        imageView_spoon = findViewById(R.id.image_spoon);

        text_count.setText("30 번 더 씹고 넘기세요");
        count = 30;

        tt = new timeThread();
        t_stop = true;

        mp = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);//xml 파일을 객체로 구현하는 팽창
        //getActionBar().setTitle("asdf");
        //인자 : 메뉴 리소스 ,id 메뉴 객체
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.meunItem_info:
                intent = new Intent(this, Activity_info.class);
                startActivity(intent);
                return true;
            case R.id.meunItem_pill:
                intent = new Intent(this, Activity_pill.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (tt.isAlive() == false) {
            if (v == btn_up) {
                count++;
                text_count.setText(count + " 번 더 씹고 넘기세요");
            }

            if (v == btn_donw) {
                count--;
                text_count.setText(count + " 번 더 씹고 넘기세요");
            }
        }

        if (v == btn_start) {
            Log.d(this.getClass().getName(), "onClick: t_stop : " + t_stop);
            if (t_stop == true) {
                t_stop = false;
                btn_start.setText("중단하기");
                plain_count = count;
                tt = new timeThread();
                tt.start();
            } else {
                tt.interrupt();
                t_stop = true;
                btn_start.setText("시작하기");
            }
        }

    }

    public void start_eatingAudio(){
        mp = MediaPlayer.create(this,R.raw.eating);
        mp.start();
    }

    public void stop_eatingAudio() {
        if(mp != null){
            mp.stop();
            mp.release();
        }
        mp = null;
    }

    public class timeThread extends Thread {
        int i = 0;
        int imgSet = 1;

        @Override
        public void run() {
            while (true) {
                i++;
                try {
                    if(i % 2 == 0) {//2초가 지났을경우
                        count--;
                        //worker 스레드 에서는 직접 ui툴킷을 건드리면 안되므로 이렇게 post 방식으로
                        //메인 스레드에 요청을 보내어 ui 를 수정한다.
                        text_count.post(new Runnable() {
                            @Override
                            public void run() {
                                text_count.setText(count + " 번 더 씹고 넘기세요");
                            }
                        });

                        stop_eatingAudio();
                        start_eatingAudio();

                        if (count <= 0) {//씹기가 끝났을 경우
                            count = plain_count;

                            imageView_spoon.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageView_spoon.setImageResource(spoon_images_id[0]);
                                }
                            });

                            text_count.post(new Runnable() {
                                @Override
                                public void run() {
                                    text_count.setText(count + " 번 더 씹고 넘기세요");
                                }
                            });
                            return;
                        }
                    }

                    //애니메이션에 관한 처리
                    imgSet++;

                    if(imgSet >= 3) {
                        imgSet = 1;
                    }

                    imageView_spoon.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView_spoon.setImageResource(spoon_images_id[imgSet]);
                        }
                    });

                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    imageView_spoon.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView_spoon.setImageResource(spoon_images_id[0]);
                        }
                    });
                    return;
                }

            }
        }

    }

}