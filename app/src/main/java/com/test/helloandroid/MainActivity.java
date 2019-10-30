package com.test.helloandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.helloandroid.business.wanandroid.presenter.MainPresenter;
import com.test.lib.event.mybus.Event;
import com.test.lib.event.mybus.EventHandler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainPresenter presenter = new MainPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView statusView = findViewById(R.id.status);

        final List<EventTest> list = new ArrayList<>();

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100000; i++) {
                    EventTest eventTest = new EventTest(i);
                    list.add(eventTest);
                }

                Log.d("test", "register start: ");
                long startTime = System.currentTimeMillis();

                for (EventTest test : list) {
                    test.register();
                }

                Log.d("test", "register end: " + (System.currentTimeMillis() - startTime));
                statusView.setText("Registered");
            }
        });

        findViewById(R.id.unregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "unregister start: ");
                long startTime = System.currentTimeMillis();

                for (EventTest test : list) {
                    test.unregister();
                }

                Log.d("test", "unregister end: " + (System.currentTimeMillis() - startTime));

                statusView.setText("Unregister");

                list.clear();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.fetchOther();
                EventHandler.sendEvent(new EventData());
            }
        });
    }

    private int count = 0;

    @Event
    public void handle(EventData eventData) {
        count++;
        Toast.makeText(this, "handle event: " + count, Toast.LENGTH_SHORT).show();
    }

    private class EventData {

    }

    private class EventTest {

        private int id;

        public EventTest(int id) {
            this.id = id;
        }

        public void register() {
            EventHandler.register(this);
        }

        public void unregister() {
            EventHandler.unregister(this);
        }

        @Event
        public void onEvent(EventData data) {
            Log.d("event", "event receive: " + id);
        }

    }
}
