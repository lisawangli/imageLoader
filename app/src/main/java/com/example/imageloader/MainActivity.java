package com.example.imageloader;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.library.Glide;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView = findViewById(R.id.image);
        final ImageView imageView1 = findViewById(R.id.image1);
        final ImageView imageView2 = findViewById(R.id.image2);
        final ImageView imageView3 = findViewById(R.id.image3);
        findViewById(R.id.clickMe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(MainActivity.this).load("http://file02.16sucai.com/d/file/2014/0704/e53c868ee9e8e7b28c424b56afe2066d.jpg").loading(R.mipmap.ic_launcher).into(imageView);
                Glide.with(MainActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576155086936&di=17f0d1724eb891431ef4f7401b82ee4d&imgtype=0&src=http%3A%2F%2Ffile02.16sucai.com%2Fd%2Ffile%2F2015%2F0128%2F8b0f093a8edea9f7e7458406f19098af.jpg").loading(R.mipmap.ic_launcher).into(imageView1);
                Glide.with(MainActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576155086936&di=857ffd039dd8b0a83ff85c11dc6cf04e&imgtype=0&src=http%3A%2F%2Ffile02.16sucai.com%2Fd%2Ffile%2F2014%2F0829%2F372edfeb74c3119b666237bd4af92be5.jpg").loading(R.mipmap.ic_launcher).into(imageView2);
                Glide.with(MainActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576155086935&di=4e4b55893de316f2c367d25cc3afc656&imgtype=0&src=http%3A%2F%2Ffile02.16sucai.com%2Fd%2Ffile%2F2015%2F0408%2F779334da99e40adb587d0ba715eca102.jpg").loading(R.mipmap.ic_launcher).into(imageView3);

            }
        });
    }
}
