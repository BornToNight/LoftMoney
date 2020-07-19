package ru.borntonight.loftmoney;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private EditText name, price;
    private ImageView check;
    private TextView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        check = findViewById(R.id.check);
        add = findViewById(R.id.add);

        // Слушатель изменения текста
        TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Если оба поля заполнены - перекрасить текст и кнопку
                if (name.getText().toString().length() != 0 && price.getText().toString().length() != 0) {
                    add.setTextColor(getResources().getColor(R.color.colorAppleGreen));
                    check.setColorFilter(getResources().getColor(R.color.colorAppleGreen));
                    // если хотя бы одно пустое - перекрасить обратно
                } else {
                    add.setTextColor(getResources().getColor(R.color.colorWhiteThree));
                    check.setColorFilter(getResources().getColor(R.color.colorWhiteThree));
                }
            }
        };

        name.addTextChangedListener(myTextWatcher);
        price.addTextChangedListener(myTextWatcher);
    }

    // События нажатия кнопки "Добавить"
    public void addItem (View v) {
        // Отобразить уведомление, если одно из полей пустое
        if (name.getText().toString().length() == 0 || price.getText().toString().length() == 0) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        } else {
            // todo добавление
        }
    }
}