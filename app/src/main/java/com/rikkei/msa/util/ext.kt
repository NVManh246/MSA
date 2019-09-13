package com.rikkei.msa.util

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun EditText.onTextChange(query: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            query(s.toString())
        }
    })
}

inline fun <T : Fragment> T.newInstance(argsBuilder: Bundle.() -> Unit): T =
    this.apply {
        arguments = Bundle().apply(argsBuilder)
    }

inline fun FragmentManager.transaction(func: FragmentTransaction.() -> Unit) {
    val transaction = this.beginTransaction()
    transaction.func()
    transaction.commit()
}
