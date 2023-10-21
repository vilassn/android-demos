package com.wavesignal.mvpdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements Contract.View {

    // creating object of TextView class
    private TextView textView;

    // creating object of Button class
    private Button button;

    // creating object of ProgressBar class
    private ProgressBar progressBar;

    // creating object of Presenter interface in Contract
    Contract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assigning ID of the TextView
        textView = findViewById(R.id.textView);

        // assigning ID of the Button
        button = findViewById(R.id.button);

        // assigning ID of the ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // instantiating object of Presenter Interface
        presenter = new Presenter(this, new Model());

        // operations to be performed when
        // user clicks the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonClick();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    // method to display the Course Detail TextView
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }

    @Override
    // method to hide the Course Detail TextView
    public void hideProgress() {
        progressBar.setVisibility(GONE);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    // method to set random string
    // in the Course Detail TextView
    public void setString(String string) {
        textView.setText(string);
    }
}
