package com.dog.translator.talkingdog.prank.simulator

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.*
import androidx.core.content.ContextCompat
import com.dog.translator.talkingdog.prank.simulator.databinding.DialogConfirmationBinding

object DialogUtil {
    private var confirmationDialog: Dialog? = null

    fun showConfirmationDialog(
        context: Context,
        title: String? = null,
        image: Int = R.drawable.ic_sound_argry,
        isCancelable: Boolean? = false,
        isCanceledOnTouchOutside: Boolean? = false,
        btnCloseClicked: (() -> Unit)? = null,

    ): Dialog {

        if (confirmationDialog == null || confirmationDialog?.isShowing == false) {



            confirmationDialog = Dialog(context, R.style.CustomDialog)
            confirmationDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            confirmationDialog?.setCancelable(isCancelable ?: false)
            confirmationDialog?.setCanceledOnTouchOutside(isCanceledOnTouchOutside ?: false)
            val bind: DialogConfirmationBinding = DialogConfirmationBinding.inflate(LayoutInflater.from(context))
            confirmationDialog?.setContentView(bind.root)

            confirmationDialog?.show()
            confirmationDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            this.confirmationDialog!!.window!!
                .setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                )
            val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            this.confirmationDialog!!.window!!.decorView.systemUiVisibility = uiOptions
            this.confirmationDialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            val windowlp = this.confirmationDialog!!.window?.attributes
            windowlp?.gravity = Gravity.CENTER
            this.confirmationDialog!!.window?.attributes = windowlp
            confirmationDialog?.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            bind.tvTitle.text = title?:""
            bind.ivDogTranslate.setImageDrawable(ContextCompat.getDrawable(context, image))
            bind.btnClose.setOnClickListener {
                confirmationDialog?.dismiss()
                btnCloseClicked?.invoke()
            }
        }
        return confirmationDialog!!
    }

    fun cancelDialog(){
        confirmationDialog = null
        confirmationDialog?.cancel()
    }
}