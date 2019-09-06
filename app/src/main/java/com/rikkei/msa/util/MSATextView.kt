package com.rikkei.msa.util

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class MSATextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): TextView(context, attrs, defStyle) {

    fun setTextSizeMSA(value: Float) {
        this.textSize = value
    }

}
