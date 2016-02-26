package com.socogame.veggies3en;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button buy, query,consume;
	PaymentGooglePlay googlePlay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		buy = (Button) findViewById(R.id.buy);
		query = (Button) findViewById(R.id.query);
		consume = (Button) findViewById(R.id.consume);
		buy.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				googlePlay.pay("1", "");
			}
		});
		googlePlay = new PaymentGooglePlay(this);
		query.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				googlePlay.query();
			}
		});
		consume.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				googlePlay.consume();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		googlePlay.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	protected void onDestroy() {
		googlePlay.onDestroy();
		super.onDestroy();
	}

}
