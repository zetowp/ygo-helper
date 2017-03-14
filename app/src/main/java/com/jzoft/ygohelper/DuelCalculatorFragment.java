package com.jzoft.ygohelper;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jzoft.ygohelper.biz.DuelCalculator;
import com.jzoft.ygohelper.databinding.CalculatorFragmentBinding;
import com.jzoft.ygohelper.utils.ActivityUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by jjimenez on 9/03/17.
 */
public class DuelCalculatorFragment extends YgoFragment {

    private static final String INFO_CALCULATOR = "Duel Calculator Instructions:\n" +
            "\n" +
            "- The calculator mark houndreds if the value is less than 100 and es not 50\n" +
            "- To pass damage tap on the player.\n" +
            "- To heal tap - to toggle to + and then tap the player\n" +
            "- You can undo actions and see duel log\n" +
            "- The quit make the player to surrender (lp = 0)\n" +
            "- To reestart click on the restart icon in the menu";
    DuelCalculator calculator;
    private Activity activity;
    private CalculatorFragmentBinding binding;


    @Override
    public String getTitle() {
        return "Duel Calculator";
    }

    @Override
    public List<Integer> getOptions() {
        return Arrays.asList(R.id.reset, R.id.info);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.reset:
                ActivityUtils.okSimpleCancelAlert(createRestartHolder(), createRestartAction());
                break;
            case R.id.info:
                ActivityUtils.simpleAlert(getActivity(), INFO_CALCULATOR);
            default:
                break;
        }
        return true;
    }

    @NonNull
    private ActivityUtils.AlertHolder createRestartHolder() {
        return new ActivityUtils.AlertHolder("Do you want to start a new Duel?", "Start Now!", "Not this time", getActivity());
    }

    @NonNull
    private DialogInterface.OnClickListener createRestartAction() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                calculator = new DuelCalculator();
                toBaseState(binding);
                dialog.dismiss();
            }
        };
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.calculator_fragment, container, false);
        activity = getActivity();
        calculator = new DuelCalculator();
        binding.log.setOnClickListener(showLog());
        binding.undo.setOnClickListener(undoAction());
        binding.half.setOnClickListener(halfAction());
        binding.quit.setOnClickListener(quitAction());
        binding.plusMinusToggle.setOnClickListener(toggleSign(binding.plusMinusToggle, binding.preCalculation));
        binding.zero.setOnClickListener(buildCalculatorNumeral(0));
        binding.one.setOnClickListener(buildCalculatorNumeral(1));
        binding.two.setOnClickListener(buildCalculatorNumeral(2));
        binding.three.setOnClickListener(buildCalculatorNumeral(3));
        binding.four.setOnClickListener(buildCalculatorNumeral(4));
        binding.five.setOnClickListener(buildCalculatorNumeral(5));
        binding.six.setOnClickListener(buildCalculatorNumeral(6));
        binding.seven.setOnClickListener(buildCalculatorNumeral(7));
        binding.eight.setOnClickListener(buildCalculatorNumeral(8));
        binding.nine.setOnClickListener(buildCalculatorNumeral(9));
        binding.playerA.setOnClickListener(buildRefresher(DuelCalculator.Player.A));
        binding.playerB.setOnClickListener(buildRefresher(DuelCalculator.Player.B));
        toBaseState(binding);
        return binding.getRoot();
    }

    private View.OnClickListener buildRefresher(final DuelCalculator.Player player) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.mark(player);
                toBaseState(binding);
                if (!calculator.validGame())
                    ActivityUtils.okSimpleCancelAlert(createRestartHolder(), createRestartAction());
            }
        };
    }

    private View.OnClickListener buildCalculatorNumeral(final int number) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.addNumber(number);
                writePre(calculator);
            }
        };
    }

    private View.OnClickListener toggleSign(final Button plusMinusToggle, final TextView preCalculation) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.toggleSign();
                if (calculator.isNegative()) {
                    defaulToggle(plusMinusToggle, preCalculation);
                } else {
                    setIcon(plusMinusToggle, getDrawable(R.drawable.plus));
                    preCalculation.setBackgroundColor(getColor(R.color.preCalculatorHeal));
                }
            }
        };
    }

    private int getColor(int id) {
        return ContextCompat.getColor(activity, id);
    }

    private Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(activity, id);
    }

    private void defaulToggle(Button plusMinusToggle, TextView preCalculation) {
        Drawable icon = getDrawable(R.drawable.minus);
        setIcon(plusMinusToggle, icon);
        preCalculation.setBackgroundColor(getColor(R.color.preCalculatorRed));
    }

    private void setIcon(Button plusMinusToggle, Drawable icon) {
        icon.setBounds(0, 0, icon.getIntrinsicHeight(), icon.getIntrinsicWidth());
        plusMinusToggle.setCompoundDrawables(null, icon, null, getDrawable(R.drawable.minus));
    }

    private View.OnClickListener quitAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.quit();
                writePre(calculator);
            }
        };
    }

    private View.OnClickListener halfAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.half();
                writePre(calculator);
            }
        };
    }

    private void toBaseState(CalculatorFragmentBinding binding) {
        binding.playerALife.setText(String.format(Locale.US, "%d", calculator.getLife(DuelCalculator.Player.A)));
        binding.playerBLife.setText(String.format(Locale.US, "%d", calculator.getLife(DuelCalculator.Player.B)));
        defaulToggle(binding.plusMinusToggle, binding.preCalculation);
        binding.preCalculation.setText(calculator.getPre());
    }

    private View.OnClickListener showLog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDuelLog(activity, calculator.displayLog());
            }
        };
    }

    private void showDuelLog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.LogAlert);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTypeface(Typeface.create("courier", Typeface.BOLD));
    }


    private View.OnClickListener undoAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculator.undo();
                toBaseState(binding);
            }
        };
    }


    private void writePre(DuelCalculator calculator) {
        binding.preCalculation.setText(calculator.getPre());
    }

}
