package com.ljheee.jokes;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    Button btn;
    ProgressDialog mPDialog;

    private static List<String> list = new ArrayList<>();
    int index = 0;
    static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirst){
                    btn.setEnabled(false);
                    //创建ProgressDialog对象
                    mPDialog = new ProgressDialog(MainActivity.this);
                    // 设置进度条风格，风格为圆形，旋转的
                    mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mPDialog.show();
                    Volley_Get();
                    isFirst = false;
                }else{

                    if(index==list.size()){
                        index = 0;
                    }
                    if(index<=list.size()){
                        textView.setText(list.get(index++));
                    }
                }

            }
        });

    }

    /**
     * 解析接口
     */
    private void Volley_Get() {

        String url = "http://japi.juhe.cn/joke/content/list.from?key=56e5f85c150ebd54461ae4fb7c6705ec&page=10&pagesize=1&sort=asc&time=1418745237";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Method.PUBLIC,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String json) {// 成功
                        Volley_Json(json);
                    }
                },
                new Response.ErrorListener() {
                     @Override
                    public void onErrorResponse(VolleyError errorLog) {// 失败
                         setErrorInfo(errorLog.getMessage());
                    }
             });
        queue.add(request);

    }

    /**
     * 获取出错
     * @param str
     */
    private void setErrorInfo(String str){
        Toast.makeText(MainActivity.this,"获取出错"+str,Toast.LENGTH_SHORT).show();

    }

    /**
     * 解析Json
     * @param json
     */
    private void Volley_Json(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonObject2 = jsonObject.getJSONObject("result");
            JSONArray ja = jsonObject2.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject3 = (JSONObject) ja.get(i);
//                textView.setText(jsonObject3.getString("content"));
                list.add(jsonObject3.getString("content"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            btn.setEnabled(true);
            mPDialog.hide();
            textView.setText(list.get(index++));
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 选项菜单--点击事件处理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                finish();
                break;

            case R.id.action_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
