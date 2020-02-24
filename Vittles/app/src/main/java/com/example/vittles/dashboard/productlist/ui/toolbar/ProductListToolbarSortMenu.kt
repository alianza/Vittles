package com.example.vittles.dashboard.productlist.ui.toolbar

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
import com.example.vittles.R
import com.example.vittles.services.popups.SingleChoiceMenuAdapter
import com.example.domain.product.model.ProductSortingType
import com.example.vittles.dashboard.productlist.SortingTypeTextProvider
import dagger.android.support.DaggerDialogFragment

/**
 * @author Jeroen Flietstra
 *
 */
class ProductListToolbarSortMenu(
    private val provider: SortingTypeTextProvider,
    private val onItemSelected: (sortingType: ProductSortingType) -> Unit,
    private val sortingType: ProductSortingType
) : DaggerDialogFragment(), DialogInterface.OnClickListener {

    private lateinit var adapter: ArrayAdapter<String>

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireActivity().let {
            adapter = SingleChoiceMenuAdapter(
                requireContext(),
                R.layout.select_dialog_singlechoice,
                ProductSortingType.values().map(provider::getSortingTypeText).toTypedArray(),
                provider.getSortingTypeText(sortingType)
            )
            val builder = AlertDialog.Builder(it).apply {
                view?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_cr)
                setCustomTitle(LayoutInflater.from(requireContext()).inflate(R.layout.dialog_sort, null))
                setSingleChoiceItems(adapter, 0, this@ProductListToolbarSortMenu)
            }
            builder.create()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sort, container, false)
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        onItemSelected.invoke(ProductSortingType.values()[p1])
        requireDialog().cancel()
    }
}