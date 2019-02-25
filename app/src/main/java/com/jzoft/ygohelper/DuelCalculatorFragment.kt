package com.jzoft.ygohelper

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.jzoft.ygohelper.biz.DuelCalculator
import com.jzoft.ygohelper.utils.ActivityUtils
import kotlinx.android.synthetic.main.calculator_fragment.*

import java.util.Arrays
import java.util.Locale

/**
 * Created by jjimenez on 9/03/17.
 */
class DuelCalculatorFragment : YgoFragment() {
    private lateinit var calculator: DuelCalculator

    override val title = "Duel Calculator"
    override val options = Arrays.asList(R.id.reset, R.id.info)
    override val fragmentLayout = R.layout.calculator_fragment

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.reset -> ActivityUtils.okSimpleCancelAlert(createRestartHolder(), createRestartAction())
            R.id.info -> ActivityUtils.simpleAlert(context!!, INFO_CALCULATOR)
            else -> {
            }
        }
        return true
    }

    private fun createRestartHolder(): ActivityUtils.AlertHolder {
        return ActivityUtils.AlertHolder("Do you want to start a new Duel?", "Start Now!", "Not this time", context!!)
    }

    private fun createRestartAction(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { dialog, _ ->
            calculator = DuelCalculator()
            toBaseState()
            dialog.dismiss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calculator = DuelCalculator()
        log.setOnClickListener(showLog())
        undo.setOnClickListener(undoAction())
        half.setOnClickListener(halfAction())
        quit.setOnClickListener(quitAction())
        plusMinusToggle.setOnClickListener(toggleSign(plusMinusToggle, preCalculation))
        zero.setOnClickListener(buildCalculatorNumeral(0))
        one.setOnClickListener(buildCalculatorNumeral(1))
        two.setOnClickListener(buildCalculatorNumeral(2))
        three.setOnClickListener(buildCalculatorNumeral(3))
        four.setOnClickListener(buildCalculatorNumeral(4))
        five.setOnClickListener(buildCalculatorNumeral(5))
        six.setOnClickListener(buildCalculatorNumeral(6))
        seven.setOnClickListener(buildCalculatorNumeral(7))
        eight.setOnClickListener(buildCalculatorNumeral(8))
        nine.setOnClickListener(buildCalculatorNumeral(9))
        playerA.setOnClickListener(buildRefresher(DuelCalculator.Player.A))
        playerB.setOnClickListener(buildRefresher(DuelCalculator.Player.B))
        toBaseState()
    }


    private fun buildRefresher(player: DuelCalculator.Player): View.OnClickListener {
        return View.OnClickListener {
            calculator.mark(player)
            toBaseState()
            if (!calculator.validGame())
                ActivityUtils.okSimpleCancelAlert(createRestartHolder(), createRestartAction())
        }
    }

    private fun buildCalculatorNumeral(number: Int): View.OnClickListener {
        return View.OnClickListener {
            calculator.addNumber(number)
            writePre(calculator)
        }
    }

    private fun toggleSign(plusMinusToggle: Button, preCalculation: TextView): View.OnClickListener {
        return View.OnClickListener {
            calculator.toggleSign()
            if (calculator.isNegative) {
                defaulToggle(plusMinusToggle, preCalculation)
            } else {
                setIcon(plusMinusToggle, getDrawable(R.drawable.plus)!!)
                preCalculation.setBackgroundColor(getColor(R.color.preCalculatorHeal))
            }
        }
    }

    private fun getColor(id: Int): Int {
        return ContextCompat.getColor(context!!, id)
    }

    private fun getDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(context!!, id)
    }

    private fun defaulToggle(plusMinusToggle: Button, preCalculation: TextView) {
        val icon = getDrawable(R.drawable.minus)
        setIcon(plusMinusToggle, icon!!)
        preCalculation.setBackgroundColor(getColor(R.color.preCalculatorRed))
    }

    private fun setIcon(plusMinusToggle: Button, icon: Drawable) {
        icon.setBounds(0, 0, icon.intrinsicHeight, icon.intrinsicWidth)
        plusMinusToggle.setCompoundDrawables(null, icon, null, getDrawable(R.drawable.minus))
    }

    private fun quitAction(): View.OnClickListener {
        return View.OnClickListener {
            calculator.quit()
            writePre(calculator)
        }
    }

    private fun halfAction(): View.OnClickListener {
        return View.OnClickListener {
            calculator.half()
            writePre(calculator)
        }
    }

    private fun toBaseState() {
        playerALife.setText(String.format(Locale.US, "%d", calculator.getLife(DuelCalculator.Player.A)))
        playerBLife.setText(String.format(Locale.US, "%d", calculator.getLife(DuelCalculator.Player.B)))
        defaulToggle(plusMinusToggle, preCalculation)
        preCalculation.text = calculator.preview()
    }

    private fun showLog(): View.OnClickListener {
        return View.OnClickListener { showDuelLog(activity, calculator.displayLog()) }
    }

    private fun showDuelLog(activity: Activity?, message: String) {
        val builder = AlertDialog.Builder(activity!!, R.style.LogAlert)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
        val textView = alertDialog.findViewById<View>(android.R.id.message) as TextView?
        textView!!.typeface = Typeface.create("courier", Typeface.BOLD)
    }


    private fun undoAction(): View.OnClickListener {
        return View.OnClickListener {
            calculator.undo()
            toBaseState()
        }
    }


    private fun writePre(calculator: DuelCalculator) {
        preCalculation.text = calculator.preview()
    }

    companion object {

        private val INFO_CALCULATOR = "Duel Calculator Instructions:\n" +
                "\n" +
                "- The calculator mark houndreds if the value is less than 100 and es not 50\n" +
                "- To pass damage tap on the player.\n" +
                "- To heal tap - to toggle to + and then tap the player\n" +
                "- You can undo actions and see duel log\n" +
                "- The quit make the player to surrender (lp = 0)\n" +
                "- To reestart click on the restart icon in the menu"
    }

}
