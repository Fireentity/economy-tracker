package it.unipd.dei.music_application.ui.dialog

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDivider
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.R
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.utils.DisplayToast
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel

@AndroidEntryPoint
class OptionCategoryModalBottomSheetFragment(
    private val category: Category
) : BottomSheetDialogFragment() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var categoryIdentifier: TextView

    private lateinit var editLayout: LinearLayout
    private lateinit var deleteLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_option_category, container, false)

        initializeViews(view)

        setupCategoryUI()

        setupListeners()
        observeDeleteResult()
        return view
    }


    private fun setupListeners() {
        editLayout.setOnClickListener {
            showEditDialog()
        }

        deleteLayout.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showEditDialog() {
        val title = context?.resources?.getString(R.string.edit_category_title)
        val buttonText = context?.resources?.getString(R.string.edit_category_button)
        val categoryInputDialogFragment =
            CategoryInputDialogFragment(title, buttonText, category)
        categoryInputDialogFragment.show(parentFragmentManager, "CategoryInputDialogFragment")

        dismiss()
    }

    private fun showDeleteConfirmationDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_category_title))
            .setMessage(resources.getString(R.string.delete_category_message))
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.si)) { _, _ -> deleteCategory() }
            .show()

    }

    private fun observeDeleteResult() {
        categoryViewModel.deleteResult.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    DisplayToast.displaySuccess(requireContext())
                    dismiss()
                }

                false -> DisplayToast.displayFailure(requireContext())
                null -> {}
            }
        }
    }

    private fun deleteCategory() {
        categoryViewModel.deleteCategory(category)
    }

    private fun initializeViews(view: View) {
        editLayout = view.findViewById(R.id.edit_layout)
        deleteLayout = view.findViewById(R.id.delete_layout)
        categoryIdentifier = view.findViewById(R.id.category_card_identifier)
    }


    private fun setupCategoryUI() {
        categoryIdentifier.text = category.identifier
    }
}
