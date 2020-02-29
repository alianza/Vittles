package com.example.vittles.services.popups

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.vittles.R
import kotlinx.android.synthetic.main.dialog_title.view.*

/**
 * @author Jeroen Flietstra
 *
 */
class SingleChoiceMenu<T>(
    private val provider: OptionTextProvider<T>,
    private val onItemSelected: (option: T) -> Unit,
    private val defaultOption: T,
    private val options: Array<T>,
    private val titleResource: Int
) : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var adapter: ArrayAdapter<String>

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            adapter = SingleChoiceMenuAdapter(
                requireContext(),
                R.layout.select_dialog_singlechoice,
                options.map(provider::getText).toTypedArray(),
                provider.getText(defaultOption)
            )
            val builder = AlertDialog.Builder(it).apply {
                view?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_cr)
                setCustomTitle(SingleChoiceMenuTitle(requireContext(), titleResource))
                setSingleChoiceItems(adapter, 0, this@SingleChoiceMenu)
            }
            builder.create()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_title, container, false)
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        onItemSelected.invoke(options[p1])
        requireDialog().cancel()
    }

    inner class SingleChoiceMenuTitle(context: Context, titleResource: Int) : ConstraintLayout(context) {

        init {
            LayoutInflater.from(context).inflate(R.layout.dialog_title, this).apply {
                findViewById<TextView>(R.id.tvCustomTitle).text = context.getString(titleResource)
            }
        }
    }
}