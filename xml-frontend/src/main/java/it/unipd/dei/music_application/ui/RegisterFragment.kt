package it.unipd.dei.music_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.unipd.dei.music_application.R


class RegisterFragment : Fragment() {
    private val items = arrayOf("Option 1", "Option 2", "Option 3")
    private val selectedItems = BooleanArray(items.size) { false }
    private val dialog = MaterialAlertDialogBuilder(requireContext())
        .setMultiChoiceItems(items, selectedItems)  { _, which, isChecked ->
            selectedItems[which] = isChecked
        }
        .create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false);
        val topBarButton: View? = view.findViewById(R.id.filter)

        topBarButton?.setOnClickListener {
            dialog.show()
        }

        return view
    }
}