package com.example.vittles.services.popups

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.vittles.R

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
                setCustomTitle(LayoutInflater.from(requireContext()).inflate(titleResource, null))
                setSingleChoiceItems(adapter, 0, this@SingleChoiceMenu)
            }
            builder.create()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort, container, false)
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        onItemSelected.invoke(options[p1])
        requireDialog().cancel()
    }
}