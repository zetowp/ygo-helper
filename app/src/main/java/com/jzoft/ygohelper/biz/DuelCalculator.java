package com.jzoft.ygohelper.biz;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by jjimenez on 9/03/17.
 */
public class DuelCalculator {
    private static final int SIGN_SWITCH = -1;

    private final LinkedList<Damage> log;
    private int pre;
    private int sign;
    private boolean half;
    private boolean quit;
    private boolean hold;

    public DuelCalculator() {
        pre = 0;
        reset();
        log = new LinkedList<Damage>();
    }

    private void reset() {
        sign = -1;
        hold = false;
        resetStatus();
    }

    private void resetStatus() {
        half = false;
        quit = false;
    }

    public String getPre() {
        if (quit) return "QUIT";
        if (half) return "1/2";
        return "" + absolut(pre);
    }

    public void undo() {
        try {
            Damage last = log.getLast();
            pre = absolut(last.amount);
            log.removeLast();
        } catch (NoSuchElementException e) {
        }
    }

    private int absolut(int amount) {
        return amount < 0 ? -amount : amount;
    }

    public void half() {
        half = true;
    }

    public void quit() {
        quit = true;
    }

    public void toggleSign() {
        sign *= SIGN_SWITCH;
    }

    public boolean isNegative() {
        return sign < 0;
    }

    public void addNumber(int number) {
        resetStatus();
        if (hold) {
            pre = (pre * 10) + number;
        } else {
            hold = true;
            pre = number;
        }
    }

    public void mark(Player player) {
        int amount = getAmountByStatus(player);
        if (amount != 0 && validGame()) {
            log.addLast(new Damage(player, amount));
            pre = absolut(amount);
            reset();
        }
    }

    public boolean validGame() {
        return getLife(Player.A) > 0 && getLife(Player.B) > 0;
    }

    private int getAmountByStatus(Player player) {
        if (half) return -(getLife(player) / 2);
        if (quit) return -getLife(player);
        return sign * getAmountByPre();
    }

    private int getAmountByPre() {
        if (pre == 50) return 50;
        if (pre < 100) return pre * 100;
        return pre;
    }

    public int getLife(Player player) {
        int total = 8000 + totalAmountOf(player);
        return total < 0 ? 0 : total;
    }

    private int totalAmountOf(Player player) {
        int total = 0;
        for (Damage d : log)
            if (d.player == player) total += d.amount;
        return total;
    }

    public String displayLog() {
        StringBuilder builder = new StringBuilder(" 8000| 8000\n");
        int a = 8000;
        int b = 8000;
        for (Damage d : log) {
            int amount = d.amount;
            if (d.player == Player.A) {
                a += amount;
                builder.append(amount > 0 ? '+' : '-');
                appendAmount(builder, amount);
                builder.append("|     ");
            } else {
                b += amount;
                builder.append("     |");
                builder.append(amount > 0 ? '+' : '-');
                appendAmount(builder, amount);
            }
            appendTotal(builder, a, b);
        }
        return builder.toString();
    }

    private void appendTotal(StringBuilder builder, int a, int b) {
        builder.append('\n');
        builder.append(' ');
        appendAmount(builder, a < 0 ? 0 : a);
        builder.append("|");
        builder.append(' ');
        appendAmount(builder, b < 0 ? 0 : b);
        builder.append('\n');
    }

    private void appendAmount(StringBuilder builder, int amount) {
        int absolut = absolut(amount);
        if (absolut < 1000) builder.append(' ');
        if (absolut < 100) builder.append(' ');
        builder.append(absolut);
    }


    public enum Player {A, B;}

    private static class Damage {
        Player player;
        int amount;

        public Damage(Player player, int amount) {
            this.player = player;
            this.amount = amount;
        }
    }
}
