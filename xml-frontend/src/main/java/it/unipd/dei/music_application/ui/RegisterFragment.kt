package it.unipd.dei.music_application.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.unipd.dei.music_application.R


class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false);
        val topBarButton: View? = view.findViewById(R.id.filter)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.fragment_filter_dialog)
            .create()

        topBarButton?.setOnClickListener {
            dialog.show()
        }

        return view
    }
}