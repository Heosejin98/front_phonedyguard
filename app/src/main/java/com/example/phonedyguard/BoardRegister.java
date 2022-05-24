package com.example.phonedyguard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BoardRegister extends AppCompatActivity {

    private final String BASEURL = "http://3.36.109.233/"; //url
    private EditText title_et, content_et;

    private registInterface registInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_write);

        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);

        Button boardwt = (Button) findViewById(R.id.reg_button); //등록 버튼

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        registInterface = retrofit.create(registInterface.class);

        boardwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPost();
            }
        });
    }

    private void createPost() {

        PostBoard post = new PostBoard(title_et.getText().toString(), content_et.getText().toString()); //String으로 변환하여 값을 보냄.

        Call<PostBoard> call = registInterface.createPost(post);

        call.enqueue(new Callback<PostBoard>() {
            @Override
            public void onResponse(Call<PostBoard> call, Response<PostBoard> response) {
                if (!response.isSuccessful()) {
                    Log.d("code: ", String.valueOf(response.code()));
                    return;
                }

                PostBoard postResponse = response.body(); //post로 값 받아옴

                String content = "";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Content: " + postResponse.getContent() + "\n";

                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<PostBoard> call, Throwable t) {
                Log.d("msg", t.getMessage()); //서버 통신 실패시
            }
        });
    }
}

