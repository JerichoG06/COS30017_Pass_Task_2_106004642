package au.edu.swin.sdmd.w05_myfirstintent

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Switch
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

/**
 * Detail activity: editable form for a single FoodSpot
 * Pre-fills all fields with existing data from the Parcelable
 * On back press, validates input and returns updated data to MainActivity
 */
class DetailActivity : AppCompatActivity() {

    companion object {
        // Key used to pass FoodSpot Parcelable
        const val EXTRA_FOOD_SPOT = "extra_food_spot"
        const val EXTRA_SPOT_INDEX = "extra_spot_index"
    }
    // Hold reference to original spot and its index in the list
    private lateinit var originalSpot: FoodSpot
    private var spotIndex: Int = 0
    // View references
    private lateinit var editName: EditText
    private lateinit var editLocation: EditText
    private lateinit var editLastVisit: EditText
    private lateinit var editReview: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var visitedSwitch: Switch
    private lateinit var errorName: TextView
    private lateinit var errorLocation: TextView
    private lateinit var errorDate: TextView
    private lateinit var errorReview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve Parcelable and list index from Intent
        @Suppress("DEPRECATION") //Hides outdated warning
        originalSpot = intent.getParcelableExtra(EXTRA_FOOD_SPOT) ?: run {
            finish()
            return
        }
        spotIndex = intent.getIntExtra(EXTRA_SPOT_INDEX, 0)

        // Apply accent color and spot name to action bar
        supportActionBar?.apply {
            title = originalSpot.name
            setBackgroundDrawable(ColorDrawable(originalSpot.accentColor))
        }

        // Bind all views
        bindViews()

        // Pre-fill all fields with existing spot data
        populateFields()

        // Wire up the date picker to the last visit field
        setupDatePicker()

        // Register back press handler using OnBackPressedCallback - validates before saving
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                // Only save and go back if all fields pass validation
                if (validateFields()) {
                    // Build updated FoodSpot from current form values
                    val updatedSpot = originalSpot.copy(
                        name = editName.text.toString().trim(),
                        location = editLocation.text.toString().trim(),
                        lastVisit = editLastVisit.text.toString().trim(),
                        rating = ratingBar.rating,
                        review = editReview.text.toString().trim(),
                        hasVisited = visitedSwitch.isChecked
                    )

                    // Returned updated spot and index back to MainActivity
                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_FOOD_SPOT, updatedSpot)
                        putExtra(EXTRA_SPOT_INDEX, spotIndex)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)

                    // Disable  callback so the system back press fires normally
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
            // If validation fails, stay on screen - errors shown inline
        })
    }

    /**
     * Binds all view references to their layout counterparts
     */
    private fun bindViews() {
        editName = findViewById(R.id.editName)
        editLocation = findViewById(R.id.editLocation)
        editLastVisit = findViewById(R.id.editLastVisit)
        editReview = findViewById(R.id.editReview)
        ratingBar = findViewById(R.id.detailRatingBar)
        visitedSwitch = findViewById(R.id.detailVisitedSwitch)
        errorName = findViewById(R.id.errorName)
        errorLocation = findViewById(R.id.errorLocation)
        errorDate = findViewById(R.id.errorDate)
        errorReview = findViewById(R.id.errorReview)

        // Image and Description set using also scope function
        findViewById<ImageView>(R.id.detailImage).also {
            it.setImageResource(originalSpot.imageResId)
            it.contentDescription = getString(
                R.string.content_desc_image, originalSpot.name
            )
        }

        //Description isn't editable
        findViewById<TextView>(R.id.detailDescription).text = originalSpot.description
    }

    /**
     * Pre-fills all editable fields with existing spot data,
     * so the user sees the current values rather than an empty input
     */
    private fun populateFields() {
        editName.setText(originalSpot.name)
        editLocation.setText(originalSpot.location)
        editLastVisit.setText(originalSpot.lastVisit)
        editReview.setText(originalSpot.review)
        ratingBar.rating = originalSpot.rating
        visitedSwitch.isChecked = originalSpot.hasVisited
    }

    /**
     * DatePickerDialog is set up on the last visit EditText
     * Tapping the field opens a calendar picker and
     * fills the field with the selected date in YYYY/MM/DD format
     */
    private fun setupDatePicker() {
        editLastVisit.setOnClickListener {
            // Read from current field text (not originalSpot) so
            // re-picking after a change starts from last picked date
            val parts = editLastVisit.text.toString().split("/")
            val calendar = Calendar.getInstance().apply {
                if (parts.size == 3) {
                    set(
                        parts[2].toIntOrNull() ?: get(Calendar.YEAR),
                        (parts[1].toIntOrNull() ?: get(Calendar.MONTH) + 1) - 1,
                        parts[0].toIntOrNull() ?: get(Calendar.DAY_OF_MONTH)
                    )
                }
            }

            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val formatted = "%02d/%02d/%04d".format(day,month + 1, year)
                    editLastVisit.setText(formatted)
                    errorDate.visibility = View.GONE
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    /**
     * Validates all editable fields
     * Shows inline error messages for any failing fields
     *
     * @return true if all fields are valid, false otherwise
     */
    private fun validateFields(): Boolean {
        var isValid = true

        // Name must not be empty
        if (editName.text.toString().trim().isEmpty()) {
            errorName.text = getString(R.string.error_name_empty)
            errorName.visibility = View.VISIBLE
            isValid = false
        } else {
            errorName.visibility = View.GONE
        }

        // Location must not be empty
        if (editLocation.text.toString().trim().isEmpty()) {
            errorLocation.text = getString(R.string.error_location_empty)
            errorLocation.visibility = View.VISIBLE
            isValid = false
        } else {
            errorLocation.visibility = View.GONE
        }

        // Date must match YYYY/MM/DD format
        val dateRegex = Regex("""\d{2}/\d{2}/\d{4}""")
        if (!dateRegex.matches(editLastVisit.text.toString().trim())) {
            errorDate.text = getString(R.string.error_date_invalid)
            errorDate.visibility = View.VISIBLE
            isValid = false
        } else {
            errorDate.visibility = View.GONE
        }

        // Review must be at least 10 characters
        if (editReview.text.toString().trim().length < 10) {
            errorReview.text = getString(R.string.error_review_short)
            errorReview.visibility = View.VISIBLE
            isValid = false
        } else {
            errorReview.visibility = View.GONE
        }

        return isValid
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}