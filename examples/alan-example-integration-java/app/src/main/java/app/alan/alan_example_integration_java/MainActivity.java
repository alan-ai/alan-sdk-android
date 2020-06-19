package app.alan.alan_example_integration_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.button.AlanButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlanButton alanButton = findViewById(R.id.alan_button);

        AlanConfig alanConfig = AlanConfig.builder()
                .setProjectId("314203787ccd9370974f1bf6b6929c1b2e956eca572e1d8b807a3e2338fdd0dc/prod")
                .build();
        alanButton.initWithConfig(alanConfig);
    }
}