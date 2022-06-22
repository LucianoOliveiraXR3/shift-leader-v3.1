package br.com.crearesistemas.shift_leader.ui.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import br.com.crearesistemas.shift_leader.R

class DialogUtil {

    fun openErrorDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_simple_message)
        dialog.setTitle("ERROR")
        val dialogOk = dialog.findViewById<Button>(R.id.btn_dialog_ok)

        dialogOk.setOnClickListener {
            dialog.cancel()
        }

        val message = dialog.findViewById<TextView>(R.id.dialog_info)

        message.text = context.resources.getString(R.string.msg_generic_error)
        message.setTextColor(Color.parseColor("#DA1F1F"))

        dialog.show()
    }

    fun openSuccessDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_simple_message)
        dialog.setTitle("SUCCESS")
        val dialogOk = dialog.findViewById<Button>(R.id.btn_dialog_ok)

        dialogOk.setOnClickListener {
            dialog.cancel()
        }

        val message = dialog.findViewById<TextView>(R.id.dialog_info)
        message.text = context.resources.getString(R.string.msg_generic_success)
        message.setTextColor(Color.parseColor("#000000"))

        dialog.show()
    }

}