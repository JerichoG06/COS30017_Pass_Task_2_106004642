package au.edu.swin.sdmd.w05_myfirstintent

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

/**
 * Main Activity: displays a scrollable list of food spot cards
 * Each card shows: image, name, rating
 * Tapping the image launches DetailActivity for editing
 * On return, updates the in-memory list and refreshes the card
 */

class MainActivity : AppCompatActivity() {

    // In-memory list of food spots - mutable so edits can be applied
    private val spots = mutableListOf<FoodSpot>()
    // Root view used to anchor the Snackbar
    private lateinit var rootView: android.view.View

    /**
     * ActivityResultLauncher replaces startActivityForResult
     * Handles the result returned from DetailActivity when the user presses back
     */
    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            // Retrieve updated spot and which index it belongs to
            @Suppress("DEPRECATION")
            val updatedSpot = data.getParcelableExtra<FoodSpot>(
                DetailActivity.EXTRA_FOOD_SPOT
            ) ?: return@registerForActivityResult
            val index = data.getIntExtra(DetailActivity.EXTRA_SPOT_INDEX, 0)

            // Store previous spot so undo can revert to it
            val previousSpot = spots[index]

            // Update in-memory list and refresh card
            spots[index] = updatedSpot
            refreshCard(index)

            // Show Snackbar confirming update and option to UNDO
            Snackbar.make(
                rootView,
                getString(R.string.snackbar_updated, updatedSpot.name),
                Snackbar.LENGTH_LONG
            ).setAction(getString(R.string.snackbar_undo)) {
                // Revert to previous spot data and refresh card again
                spots[index] = previousSpot
                refreshCard(index)
            }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootView = findViewById(android.R.id.content)

        // Populate in-memory list on first creation
        spots.addAll(buildSpotsList())

        // Bind each card to its correct FoodSpot
        bindCard(R.id.imgSpot1, R.id.tvName1, R.id.ratingBar1,0)
        bindCard(R.id.imgSpot2, R.id.tvName2, R.id.ratingBar2, 1)
        bindCard(R.id.imgSpot3, R.id.tvName3, R.id.ratingBar3, 2)
        bindCard(R.id.imgSpot4, R.id.tvName4, R.id.ratingBar4, 3)
    }
    /**
     * Builds initial in-memory list of FoodSpot objects
     * String values pulled from resources to avoid hard-coded strings
     */
    private fun buildSpotsList(): List<FoodSpot> = listOf(
        FoodSpot(
            name = getString(R.string.spot1_name),
            location = getString(R.string.spot1_location),
            lastVisit = getString(R.string.spot1_last_visit),
            rating = 5.0f,
            description = getString(R.string.spot1_description),
            review = getString(R.string.spot1_review),
            imageResId = R.drawable.spot1,
            hasVisited = true,
            accentColor = Color.parseColor("#2d8dfa")
        ),
        FoodSpot(
            name = getString(R.string.spot2_name),
            location = getString(R.string.spot2_location),
            lastVisit = getString(R.string.spot2_last_visit),
            rating = 4.0f,
            description = getString(R.string.spot2_description),
            review = getString(R.string.spot2_review),
            imageResId = R.drawable.spot2,
            hasVisited = true,
            accentColor = Color.parseColor("#e02702")
        ),
        FoodSpot(
            name = getString(R.string.spot3_name),
            location = getString(R.string.spot3_location),
            lastVisit = getString(R.string.spot3_last_visit),
            rating = 4.5f,
            description = getString(R.string.spot3_description),
            review = getString(R.string.spot3_review),
            imageResId = R.drawable.spot3,
            hasVisited = true,
            accentColor = Color.parseColor("#4705e3")
        ),
        FoodSpot(
            name = getString(R.string.spot4_name),
            location = getString(R.string.spot4_location),
            lastVisit = getString(R.string.spot4_last_visit),
            rating = 3.5f,
            description = getString(R.string.spot4_description),
            review = getString(R.string.spot4_review),
            imageResId = R.drawable.spot4,
            hasVisited = true,
            accentColor = Color.parseColor("#0c04b0")
        )
    )

    /**
     * Binds card's views to the spot at the given index
     * Extracted to avoid repeated code across all four cards
     *
     * @param imageId   Resource ID of card's ImageView
     * @param nameId    Resource ID of card's name TextView
     * @param ratingId  Resource ID of card's RatingBar
     * @param index     Index of the spot in the in-memory list
     */
    private fun bindCard(
        imageId: Int,
        nameId: Int,
        ratingId: Int,
        index: Int
    ){
        val spot = spots[index]
        findViewById<ImageView>(imageId).apply {
            setImageResource(spot.imageResId)
            // Pass both the spot and its index so DetailActivity can return to right position
            setOnClickListener {
                detailLauncher.launch(
                    Intent(this@MainActivity, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_FOOD_SPOT, spot)
                        putExtra(DetailActivity.EXTRA_SPOT_INDEX, index)
                    }
                )
            }
        }
        findViewById<TextView>(nameId).text = spot.name
        findViewById<RatingBar>(ratingId).rating = spot.rating
    }

    /**
     * Refreshes a single card after it's been edited
     * Maps the index to the correct view IDs and rebinds
     *
     * @param index Index of updated spot in the in-memory list
     */
    private fun refreshCard(index: Int) {
        val (imageId, nameId, ratingId) = when (index) {
            0 -> Triple(R.id.imgSpot1, R.id.tvName1, R.id.ratingBar1)
            1 -> Triple(R.id.imgSpot2, R.id.tvName2, R.id.ratingBar2)
            2 -> Triple(R.id.imgSpot3, R.id.tvName3, R.id.ratingBar3)
            else -> Triple(R.id.imgSpot4, R.id.tvName4, R.id.ratingBar4)
        }
        bindCard(imageId, nameId, ratingId, index)
    }

}