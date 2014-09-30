package vc.ddns.luna.sert.binaryfileuploadtest.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends Activity {
    private String savePath;
    private Recording recording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");

        findViewById(R.id.button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savePath = Environment.getExternalStorageDirectory() + "/" + "recordingTest.mp4";
                        recording = new Recording(savePath);
                        recording.recStart();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("録音");
                        builder.setPositiveButton("録音終了",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        recording.recEnd();

                                        Uri.Builder uriBuilder = new Uri.Builder();
                                        uriBuilder.scheme("http");
                                        uriBuilder.encodedAuthority("example.com:3000");
                                        uriBuilder.path("/api/v1/speaking_answers");
                                        String uriStr = uriBuilder.build().toString();

                                        try {
                                            RequestParams params = new RequestParams();
                                            params.put("token", "xxxxxxxxxxx");
                                            params.put("answer_file", new File(savePath));
                                            AsyncHttpClient client = new AsyncHttpClient();
                                            client.post(uriStr, params, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                                    if(bytes != null)
                                                        System.out.println(new String(bytes));
                                                    else
                                                        System.out.println("Success");
                                                }

                                                @Override
                                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                                    if(bytes != null)
                                                        System.out.println(new String(bytes));
                                                    else
                                                        System.out.println("Failed");
                                                }
                                            });
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        builder.setCancelable(false);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
