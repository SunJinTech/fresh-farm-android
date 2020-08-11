package com.sun.freshfarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import org.jetbrains.anko.support.v4.toast

class EditTextFragment(val callBack: (String) -> Unit) : Fragment() {

    private var title: String? = null
    private var content: String? = null

    private lateinit var titleTV: TextView
    private lateinit var inputEditText: EditText
    private lateinit var saveButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("title")
        content = arguments?.getString("content")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_edit_text, container, false)
        initialView(rootView)
        return rootView
    }

    private fun initialView(rootView: View) {
        titleTV = rootView.findViewById(R.id.title)
        inputEditText = rootView.findViewById(R.id.input)
        saveButton = rootView.findViewById(R.id.saveButton)

        titleTV.text = title
        inputEditText.setText(content)

        saveButton.setOnClickListener {
            callBack(inputEditText.text.toString())
            activity?.onBackPressed()
        }
    }

}