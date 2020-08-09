package ru.borntonight.loftmoney.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import ru.borntonight.loftmoney.LoftApp;
import ru.borntonight.loftmoney.MainActivity;
import ru.borntonight.loftmoney.R;

public class LoginActivity extends AppCompatActivity implements LoginView {

    Button loginButtonView;

    private LoginPresenter loginPresenter = new LoginPresenterImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButtonView = findViewById(R.id.loginButtonView);
        loginPresenter.attachViewState(this);

        configureButton();
    }

    @Override
    protected void onPause() {
        loginPresenter.disposeRequests();
        super.onPause();
    }

    private void configureButton() {
        loginButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.performLogin(((LoftApp) getApplication()).getAuthApi());
            }
        });
    }

    @Override
    public void toggleSending(boolean isActive) {
        loginButtonView.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(String token) {
        Toast.makeText(getApplicationContext(), "User was logged successfully", Toast.LENGTH_SHORT).show();
        ((LoftApp) getApplication()).getSharedPreferences().edit().putString(LoftApp.TOKEN_KEY, token).apply();

        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }
}
