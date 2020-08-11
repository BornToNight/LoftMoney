package ru.borntonight.loftmoney;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class BalanceView extends View {

    private float expenses, incomes;
    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    public BalanceView(Context context) {
        super(context);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BalanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        expensePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorExpense));
        incomePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAppleGreen));
    }

    public void update(float expenses, float incomes) {
        this.expenses = expenses;
        this.incomes = incomes;

        invalidate();
    }

    // Логика отрисовки
    @Override
    protected void onDraw(Canvas canvas) {
        // Обработка кода в классе родителя
        super.onDraw(canvas);

        float total = expenses + incomes;

        float expensesAngle = 360f * expenses / total;
        float incomesAngle = 360f * incomes / total;

        int space = 15;
        // вписать круг по мин. стороне
        int size = Math.min(getWidth(), getHeight()) - space *2;
        // вычисляем центры
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        // рисуем расходы
        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space, getHeight() - yMargin, 180 - expensesAngle / 2, expensesAngle, true, expensePaint);
        // положительный space = справа
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space, getHeight() - yMargin, 360 - incomesAngle / 2, incomesAngle, true, incomePaint);
    }
}
