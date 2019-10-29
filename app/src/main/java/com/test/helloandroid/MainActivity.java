package com.test.helloandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.test.helloandroid.business.wanandroid.presenter.MainPresenter;
import com.test.lib.event.mybus.Event;
import com.test.lib.event.mybus.EventHandler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MainPresenter presenter = new MainPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventHandler.register(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.fetchOther();
                EventHandler.sendEvent(new EventData());
            }
        });
    }

    @Event
    public void handle(EventData eventData) {
        Toast.makeText(this, "handle event", Toast.LENGTH_SHORT).show();
    }

    private class EventData {

    }
}
