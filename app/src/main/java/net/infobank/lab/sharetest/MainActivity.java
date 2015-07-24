package net.infobank.lab.sharetest;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_share_text).setOnClickListener(this);
        findViewById(R.id.btn_share_link).setOnClickListener(this);
        findViewById(R.id.btn_share_image).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_text:
                onShareText();
                break;
            case R.id.btn_share_link:
                onShareLink();
                break;
            case R.id.btn_share_image:
                onShareImage();
                break;
        }
    }

    protected void onShareText() {
        String shareBody = "좋은 프로그래머란, 일방통행 도로에서도 양쪽을 모두 보고 건너는 사람이다. -더그 린더.";
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "전산과조교 on Twitter");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "KB카드 공유"));
    }

    protected void onShareLink() {
        String shareBody = "http://www.ize.co.kr/articleView.html?no=2015071812567251270";
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "이수만 아니면 김태호 PD의 시대");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "KB카드 공유"));
    }

    protected void onShareImage() {
        File image = copyAsset("ize.jpg");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "이수만 아니면 김태호 PD의 시대");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.body_text));
        Uri path = Uri.fromFile(image);
        intent.putExtra(Intent.EXTRA_STREAM, path);
        Intent mailer = Intent.createChooser(intent, "KB카드 공유");
        startActivity(mailer);
    }

    private File copyAsset(String filename) {
        File outFile = null;

        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            outFile = new File(getExternalFilesDir(null), filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // NOOP
                }
            }
        }

        return outFile;
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getExternalFilesDir(null), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
