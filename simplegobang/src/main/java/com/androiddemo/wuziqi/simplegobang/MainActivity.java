package com.androiddemo.wuziqi.simplegobang;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private GoBangPanel goBangPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goBangPanel = (GoBangPanel) findViewById(R.id.id_gobang);
        goBangPanel.setOnGameOverListener(new OnGameOverListener()
        {
            /**
             * @param win true,白赢; false,黑赢
             */
            @Override
            public void onGameWin(boolean win)
            {
                String text = win ? "白棋赢了" : "黑棋赢了";
                showDialog(text);
            }
            /**
             * 平局调用
             */
            @Override
            public void onGameHe()
            {
                showDialog("平局");
            }
        });
    }

    private void showDialog(String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("五子棋提示");
        builder.setMessage(msg + ",是否再来一局");
        builder.setPositiveButton("结束", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("再来一局", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                goBangPanel.gobangrestart();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == R.id.action_restart){
            goBangPanel.gobangrestart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
