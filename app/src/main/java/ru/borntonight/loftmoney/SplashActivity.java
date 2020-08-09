package ru.borntonight.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import ru.borntonight.loftmoney.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkToken();
    }

    private void checkToken() {
        String token = ((LoftApp) getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        if (TextUtils.isEmpty(token)) {
            routeToLogin();
        } else {
            routeToMain();
        }
    }

    private void routeToMain() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void routeToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}