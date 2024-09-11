package org.unizd.rma.babic

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button

import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.unizd.rma.babic.adapter.StateViewAdapter
import org.unizd.rma.babic.databinding.ActivityMainBinding
import org.unizd.rma.babic.models.State
import org.unizd.rma.babic.utils.Status
import org.unizd.rma.babic.utils.clearEditText
import org.unizd.rma.babic.utils.longToastShow
import org.unizd.rma.babic.utils.setupDialog
import org.unizd.rma.babic.utils.validateEditText
import org.unizd.rma.babic.viewmodels.StateViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity() {
    // SharedPreferences reference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var captureIV: ImageView
    private lateinit var captureIV2: ImageView
    private lateinit var imageUri: Uri
    // Declare variables for radio buttons
    private lateinit var radioButtonAsia: RadioButton
    private lateinit var radioButtonEurope: RadioButton
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        captureIV.setImageURI(null)
        captureIV.setImageURI(imageUri)
        captureIV2.setImageURI(null)
        captureIV2.setImageURI(imageUri)


        // Save the image URI to SharedPreferences after capturing/selecting an image
        saveImageUriToSharedPreferences(imageUri)
    }

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val addStateDialog: Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.add_state_dialog)
        }
    }

    private val updateStateDialog: Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.update_state_dialog)
        }
    }


    private val loadingDialog: Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val stateViewModel: StateViewModel by lazy {
        ViewModelProvider(this)[StateViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // Add state start
        val addCloseImg = addStateDialog.findViewById<ImageView>(R.id.closeImg)
        addCloseImg.setOnClickListener { addStateDialog.dismiss() }

        val addETTitle = addStateDialog.findViewById<TextInputEditText>(R.id.edStateTitle)
        val addETTitleL = addStateDialog.findViewById<TextInputLayout>(R.id.edstateTtileL)

        // Load imageUri from SharedPreferences
        imageUri = retrieveImageUriFromSharedPreferences() ?: createImageUri()

        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }

        })


        val addETDesc = addStateDialog.findViewById<TextInputEditText>(R.id.edStateDesc)
        val addETDescL = addStateDialog.findViewById<TextInputLayout>(R.id.edstateDescL)

        // InputFilter to allow only numeric input for addETDesc
        addETDesc.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })

        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }
        })


        val addETSurface = addStateDialog.findViewById<TextInputEditText>(R.id.edstateSurface)
        val addETSurfaceL = addStateDialog.findViewById<TextInputLayout>(R.id.edstateSurfaceL)

        // InputFilter to allow only numeric input for addETDesc
        addETSurface.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })
        addETSurface.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETSurface, addETSurfaceL)
            }
        })


        imageUri = createImageUri()
        captureIV = addStateDialog.findViewById(R.id.selectedImageView)
        val captureImgBtn = addStateDialog.findViewById<Button>(R.id.pickImageButton)
        captureImgBtn.setOnClickListener {

            // Update imageUri to point to the new image
            imageUri = createImageUri()
            contract.launch(imageUri)


        }


        imageUri = createImageUri()
        captureIV2 = updateStateDialog.findViewById(R.id.upselectedImageView)
        val upcaptureBtn = updateStateDialog.findViewById<Button>(R.id.updatepickImageButton)

        upcaptureBtn.setOnClickListener {
            imageUri = createImageUri()
            contract.launch(imageUri)


        }


///////add state dialog

        mainBinding.addStateFABtn.setOnClickListener {
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            clearEditText(addETSurface, addETSurfaceL)


            // Reset imageUri to a new URI
            imageUri = createImageUri()
            captureIV.setImageURI(null) // Clear the ImageView
            addStateDialog.show()
        }
        val saveStateBtn = addStateDialog.findViewById<Button>(R.id.saveBtn)
        saveStateBtn.setOnClickListener {

            // Initialize radio buttons
            radioButtonAsia = addStateDialog.findViewById(R.id.radioButtonAsia)
            radioButtonEurope = addStateDialog.findViewById(R.id.radioButtonEurope)

            // Get the selected option from radio buttons
            val selectedCountry = if (radioButtonAsia.isChecked) {
                "Asia"
            } else {
                "Europe"
            }

            if (validateEditText(addETTitle, addETTitleL) && validateEditText(
                    addETDesc,
                    addETDescL
                ) && validateEditText(addETSurface, addETSurfaceL)
            ) {
                addStateDialog.dismiss()

                val newState = State(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    Date(), // This line represents the date
                    addETSurface.text.toString().trim(),
                    imageUri.toString(),
                    country = selectedCountry
                    // This line represents the surface
                )

                stateViewModel.insertState(newState).observe(this) {

                    when (it.status) {

                        Status.LOADING -> {
                            loadingDialog.show()

                        }

                        Status.SUCCESS -> {

                            loadingDialog.dismiss()
                            if (it.data?.toInt() != -1) {
                                longToastShow("State Added Successfully")

                            }

                        }

                        Status.ERROR -> {

                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }

                        }

                    }
                }

            }
        }
        // Add state end




        // Update State Start


        val updateETTitle = updateStateDialog.findViewById<TextInputEditText>(R.id.updateStateTitle)
        val updateETTitleL = updateStateDialog.findViewById<TextInputLayout>(R.id.updatestateTtileL)

        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }

        })

        val updateETDesc = updateStateDialog.findViewById<TextInputEditText>(R.id.updateStateDesc)
        val updateETDescL = updateStateDialog.findViewById<TextInputLayout>(R.id.updatestateDescL)

        // InputFilter to allow only numeric input for addETDesc
        updateETDesc.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })

        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }
        })


        val updateSurface = updateStateDialog.findViewById<TextInputEditText>(R.id.updateSurface)
        val updateSurfaceL = updateStateDialog.findViewById<TextInputLayout>(R.id.updateSurfaceL)

        // InputFilter to allow only numeric input for addETDesc
        updateSurface.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })
        updateSurface.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateSurface, updateSurfaceL)
            }
        })

        val updateCloseImg = updateStateDialog.findViewById<ImageView>(R.id.updatecloseImg)
        updateCloseImg.setOnClickListener { updateStateDialog.dismiss() }

        val updateStateBtn = updateStateDialog.findViewById<Button>(R.id.updateBtn)


        val stateViewAdapter = StateViewAdapter {

                type, Position, state ->

            if (type == "delete") {

                stateViewModel
//                    .deleteTAsk(state)
                    .deleteStateUsingId(state.id).observe(this) {

                        when (it.status) {

                            Status.LOADING -> {
                                loadingDialog.show()

                            }

                            Status.SUCCESS -> {

                                loadingDialog.dismiss()
                                if (it.data != -1) {
                                    longToastShow("State Deleted Successfully")
                                }
                            }

                            Status.ERROR -> {

                                loadingDialog.dismiss()
                                it.message?.let { it1 -> longToastShow(it1) }
                            }
                        }
                    }
            } else if (type == "update") {
                // Set the image URI to the ImageView before showing the update dialog
                captureIV2.setImageURI(Uri.parse(state.imageUri))
                updateETTitle.setText(state.title)
                updateETDesc.setText(state.description)
                updateSurface.setText(state.surface)
                // Initialize radio buttons
                radioButtonAsia = updateStateDialog.findViewById(R.id.updateradioButtonAsia)
                radioButtonEurope = updateStateDialog.findViewById(R.id.updateradioButtonEurope)

                if (state.country == "Asia"){
                    radioButtonAsia.isChecked = true
                }else if (state.country == "Europe"){
                    radioButtonEurope.isChecked = true
                }

// Set the image URI to the updated image URI
                imageUri = Uri.parse(state.imageUri)

                updateStateBtn.setOnClickListener {
                    // Initialize radio buttons
                    radioButtonAsia = updateStateDialog.findViewById(R.id.updateradioButtonAsia)
                    radioButtonEurope = updateStateDialog.findViewById(R.id.updateradioButtonEurope)

                    // Get the new image URI
                    val newImageUri = if (imageUri == null) Uri.parse(state.imageUri) else imageUri
                    // Get the selected option from radio buttons
                    val selectedCountry = if (radioButtonAsia.isChecked) {
                        "Asia"
                    } else {
                        "Europe"
                    }
                    if (validateEditText(updateETTitle, updateETTitleL) && validateEditText(
                            updateETDesc,
                            updateETDescL
                        ) && validateEditText(updateSurface, updateSurfaceL)
                    ) {

                        val updateState = State(
                            state.id,
                            updateETTitle.text.toString().trim(),
                            updateETDesc.text.toString().trim(),
                            Date(), // This line represents the date
                            updateSurface.text.toString().trim(),
                            newImageUri.toString(),
                            country = selectedCountry

                        )
                        updateStateDialog.dismiss()
                        loadingDialog.show()
                        stateViewModel.
                        updateState(updateState)
                            .observe(this) {

                                when (it.status) {

                                    Status.LOADING -> {
                                        loadingDialog.show()

                                    }

                                    Status.SUCCESS -> {

                                        loadingDialog.dismiss()
                                        if (it.data != -1) {
                                            longToastShow("State Updated Successfully")

                                        }

                                    }

                                    Status.ERROR -> {

                                        loadingDialog.dismiss()
                                        it.message?.let { it1 -> longToastShow(it1) }

                                    }

                                }


                            }
                    }

                }

                updateStateDialog.show()

            }

        }


        mainBinding.stateRV.adapter = stateViewAdapter

        callGetStateList(stateViewAdapter)
        // Update State End


    }

    private fun saveImageUriToSharedPreferences(uri: Uri) {
        val editor = sharedPreferences.edit()
        editor.putString("imageUri", uri.toString())
        editor.apply()
    }
    private fun retrieveImageUriFromSharedPreferences(): Uri? {
        val uriString = sharedPreferences.getString("imageUri", null)
        return uriString?.let { Uri.parse(it) }
    }

    ////capture

    private fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "camera_photos_$timeStamp.png"
        val image = File(filesDir, imageFileName)
        return FileProvider.getUriForFile(this, "org.unizd.rma.babic.FileProvider", image)
    }


    private fun callGetStateList(stateViewAdapter :StateViewAdapter) {
        CoroutineScope(Dispatchers.Main).launch {


            stateViewModel.getStateList().collect {

                when (it.status) {

                    Status.LOADING -> {
                        loadingDialog.show()

                    }

                    Status.SUCCESS -> {

                        it.data?.collect { stateList ->
                            loadingDialog.dismiss()
                            stateViewAdapter.addAllState(stateList)
                        }


                    }

                    Status.ERROR -> {

                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }

                    }

                }

            }


        }


    }
}