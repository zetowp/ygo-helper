package com.jzoft.ygohelper.biz

import java.util.LinkedList
import java.util.NoSuchElementException

/**
 * Created by jjimenez on 9/03/17.
 */
class DuelCalculator {

    private val log: LinkedList<Damage>
    private var pre: Int = 0
    private var sign: Int = 0
    private var half: Boolean = false
    private var quit: Boolean = false
    private var hold: Boolean = false

    val isNegative: Boolean
        get() = sign < 0

    private val amountByPre: Int
        get() {
            if (pre == 50) return 50
            return if (pre < 100) pre * 100 else pre
        }

    init {
        pre = 0
        reset()
        log = LinkedList()
    }

    private fun reset() {
        sign = -1
        hold = false
        resetStatus()
    }

    private fun resetStatus() {
        half = false
        quit = false
    }

    fun preview(): String {
        if (quit) return "QUIT"
        return if (half) "1/2" else "" + absolut(pre)
    }

    fun undo() {
        try {
            val last = log.last
            pre = absolut(last.amount)
            log.removeLast()
        } catch (e: NoSuchElementException) {
        }

    }

    private fun absolut(amount: Int): Int {
        return if (amount < 0) -amount else amount
    }

    fun half() {
        half = true
    }

    fun quit() {
        quit = true
    }

    fun toggleSign() {
        sign *= SIGN_SWITCH
    }

    fun addNumber(number: Int) {
        resetStatus()
        if (hold) {
            pre = pre * 10 + number
        } else {
            hold = true
            pre = number
        }
    }

    fun mark(player: Player) {
        val amount = getAmountByStatus(player)
        if (amount != 0 && validGame()) {
            log.addLast(Damage(player, amount))
            pre = absolut(amount)
            reset()
        }
    }

    fun validGame(): Boolean {
        return getLife(Player.A) > 0 && getLife(Player.B) > 0
    }

    private fun getAmountByStatus(player: Player): Int {
        if (half) return -(getLife(player) / 2)
        return if (quit) -getLife(player) else sign * amountByPre
    }

    fun getLife(player: Player): Int {
        val total = 8000 + totalAmountOf(player)
        return if (total < 0) 0 else total
    }

    private fun totalAmountOf(player: Player): Int {
        var total = 0
        for (d in log)
            if (d.player == player) total += d.amount
        return total
    }

    fun displayLog(): String {
        val builder = StringBuilder(" 8000| 8000\n")
        var a = 8000
        var b = 8000
        for (d in log) {
            val amount = d.amount
            if (d.player == Player.A) {
                a += amount
                builder.append(if (amount > 0) '+' else '-')
                appendAmount(builder, amount)
                builder.append("|     ")
            } else {
                b += amount
                builder.append("     |")
                builder.append(if (amount > 0) '+' else '-')
                appendAmount(builder, amount)
            }
            appendTotal(builder, a, b)
        }
        return builder.toString()
    }

    private fun appendTotal(builder: StringBuilder, a: Int, b: Int) {
        builder.append('\n')
        builder.append(' ')
        appendAmount(builder, if (a < 0) 0 else a)
        builder.append("|")
        builder.append(' ')
        appendAmount(builder, if (b < 0) 0 else b)
        builder.append('\n')
    }

    private fun appendAmount(builder: StringBuilder, amount: Int) {
        val absolut = absolut(amount)
        if (absolut < 1000) builder.append(' ')
        if (absolut < 100) builder.append(' ')
        builder.append(absolut)
    }


    enum class Player {
        A, B
    }

    private class Damage(internal var player: Player, internal var amount: Int)

    companion object {
        private val SIGN_SWITCH = -1
    }
}
