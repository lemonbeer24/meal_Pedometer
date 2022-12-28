package com.example.meal_pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_pill extends AppCompatActivity {

    private ListView list;

    private String[] productNames = {
            "메스틱 검",
            "양베추 + 케일 녹즙",
            "오트밀"
    };

    private String[] values = {
            "33,100원",
            "2,600원",
            "4,000원"
    };

    private Integer[] images = {
            R.drawable.gum,
            R.drawable.green,
            R.drawable.oat
    };

    private String[] urls = {
            "https://smartstore.naver.com/yaksawausa/products/4721231663?NaPm=ct%3Dkib72480%7Cci%3D0zK0003a2WHt3RGPMfke%7Ctr%3Dpla%7Chk%3D40a6a61f6f83df84f0c43b8c30f8d732d157cba9",
            "https://greenjuice.pulmuone.com/product/product/281",
            "http://granola.co.kr/product/detail.html?product_no=1406&cate_no=87&display_group=1&cafe_mkt=naver_ks&mkt_in=Y&ghost_mall_id" +
                    "=naver&ref=naver_open&NaPm=ct%3Dkib7lg7k%7Cci%3D0zK0001g3WHtVWrA91pF%7Ctr%3Dpla%7Chk%3D6d84ce194b5068b89ba705ee9893cc5defe9b4d8"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill);
        CustomList adapter = new CustomList(Activity_pill.this);//어뎁터 생성
        list = (ListView) findViewById(R.id.list);//연결된 엑티비티에서 리스트뷰 객체 가져오기
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 무명 클래스로 이밴트 리스너 적용
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//요소 클릭시 호출
                Intent intent = new Intent(Intent.ACTION_VIEW,  Uri.parse(urls[+position]));
                startActivity(intent);
            }
        });
    }

    public class CustomList extends ArrayAdapter<String> {

        private final Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.list_item, productNames);//부모 클래스인 arrayAdapter 에 생성자 호출
            //데이터가 배열에 있을때 사용
            //파라미터 : 현재 앱 컨텍스트, 레이아웃 아이디(사용자가 만든 레이아웃, or 제공되는 레이아웃), 데이터 배열
            this.context = context;//앱에 정보를 가져오는 주체
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){//제일 중요한. 데이터를 담아 뷰를 생성해 반환하는 메소드
            LayoutInflater inflater = context.getLayoutInflater();//안드로이드에서 뷰를 만들시 가장 기본적인 방법
            View rowView = inflater.inflate(R.layout.list_item,null,true);//뷰 생성
            //파라미터 : 뷰 를 만들고 싶은 레이아웃 파일 의 id,
            //          생성될 view 의 부모,
            //          true 로 설정해 줄 경우 root의 자식 View로 자동으로 추가됩니다.

            ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
            TextView productName = (TextView) rowView.findViewById(R.id.productName);
            TextView value = (TextView) rowView.findViewById(R.id.value);

            productName.setText(productNames[position]);
            imageView.setImageResource(images[position]);
            value.setText(values[position]);

            return rowView;
        }
    }
}