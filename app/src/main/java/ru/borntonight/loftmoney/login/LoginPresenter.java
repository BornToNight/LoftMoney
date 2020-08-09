package ru.borntonight.loftmoney.login;

import ru.borntonight.loftmoney.remote.AuthApi;

public interface LoginPresenter {
    void performLogin(AuthApi authApi);
    void attachViewState(LoginView loginView);
    void disposeRequests();
}
