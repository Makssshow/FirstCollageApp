package com.example.firstcollageapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class number extends Fragment {

    private Vibrator vibrator;
    private TextView tvInfo;
    private EditText edit;
    private Button but, reset_but, close_but;
    private Animation animation, fade_out, fade_in_text, error;

    final Random r = new Random();
    public int random = r.nextInt(10), attempts = 1;
    public boolean work = true, animBoolean = false;
    long[] error_vibro = {0, 100, 50, 100}, win_vibro = {0, 200, 100, 200}, lose_vibro = {0, 30};


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View vf = inflater.inflate(R.layout.fragment_number, container, false);
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        fade_out = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        fade_in_text = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_text);
        error = AnimationUtils.loadAnimation(getActivity(), R.anim.error);
        tvInfo = vf.findViewById(R.id.textView);
        edit = vf.findViewById(R.id.edit);
        but = vf.findViewById(R.id.button);
        close_but = vf.findViewById(R.id.button2);
        reset_but = vf.findViewById(R.id.reset_button);
        edit.addTextChangedListener(EditCheck);

        but.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!work) { //ЕСЛИ ЗАНОВО
                    reset();
                } else { //ЕСЛИ ИГРА НАЧИНАЕТСЯ
                    diapozonCheck();
                }
            }
        });
        reset_but.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                reset();
                reset_but.startAnimation(fade_out); //ИСЧЕЗАНИЕ КНОПКИ
            }
        });
        close_but.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

        return vf;
    }


    private final TextWatcher EditCheck = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String textEditCheck = edit.getText().toString();
            but.setEnabled(!textEditCheck.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            edit.setOnKeyListener(new View.OnKeyListener() {
                                      public boolean onKey(View v, int keyCode, KeyEvent event) {
                                          if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                                  (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                              but.performClick();
                                          }
                                          return false;
                                      }
                                  }
            );
        }
    };


    public void diapozonCheck() {
        String editText = edit.getText().toString();
        if (Integer.parseInt(editText) < 0 || Integer.parseInt(editText) > 9) { //ЕСЛИ НЕ В ДИАПОЗОНЕ ЗНАЧЕНИЙ
            tvInfo.setText(getResources().getString(R.string.error));
            tvInfo.setTextColor(getResources().getColor(R.color.colorError));
            tvInfo.startAnimation(error);
            vibrator.vibrate(error_vibro, -1);
            edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
        } else { //ЕСЛИ В ДИАПОЗОНЕ
            game();
        }
    }

    public void game() {
        String editText = edit.getText().toString();
        tvInfo.setTextColor(Color.BLACK);
        edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        if (Integer.parseInt(editText) == random) { //ЕСЛИ УГАДАЛ
            win();
        } else { //ЕСЛИ НЕ УГАДАЛ
            lose();
        }
    }

    @SuppressLint("SetTextI18n")
    public void win() {
        tvInfo.setText(getResources().getString(R.string.ahead) + " " + attempts + " " + getResources().getString(R.string.aheadPart2));
        tvInfo.startAnimation(fade_in_text);//Изменение текста при успехе
        edit.setEnabled(false);
        vibrator.vibrate(win_vibro, -1);
        if (animBoolean) {
            reset_but.startAnimation(fade_out);
            animBoolean = false;
        }
        but.setText(getResources().getString(R.string.reset_button));
        edit.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        work = false;
    }

    public void lose() {
        String editText = edit.getText().toString();
        if (!animBoolean) {
            reset_but.startAnimation(animation);
            animBoolean = true;
        }
        attempts++;
        vibrator.vibrate(lose_vibro, -1);
        edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorLose)));
        edit.setHint(editText);
        edit.setText("");
        tvInfo.setText(getResources().getString(R.string.lose));
        tvInfo.startAnimation(fade_in_text);
    }


    public void reset() {
        tvInfo.setText(getResources().getString(R.string.know_number));
        tvInfo.startAnimation(fade_in_text);//Изменение текста при успехе
        tvInfo.setTextColor(Color.BLACK);
        attempts = 1;
        but.setText(getResources().getString(R.string.submit_button));
        edit.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        random = r.nextInt(10);
        edit.setEnabled(true);
        edit.setHint("");
        edit.setText("");
        animBoolean = false;
        work = true;
    }


}