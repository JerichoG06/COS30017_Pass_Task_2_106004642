package au.edu.swin.sdmd.w05_myfirstintent

import android.content.Intent
import android.media.Rating
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Main Activity: displays a 2x2 grid of food spots cards
 * Each card shows: image, name, rating, and see more details button
 * Tapping the button launches DetailActivity with the spot's data as a Parcelable
 */

class MainActivity : AppCompatActivity() {

    // In-memory list of food spots - no disk storage used
    private lateinit var spots: List<FoodSpot>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Build in-memory data list
        spots = buildSpotsList()

        // Bind each card to its correct FoodSpot
        bindCard(R.id.imgSpot1, R.id.tvName1, R.id.ratingBar1, R.id.btnDetails1, spots[0])
        bindCard(R.id.imgSpot2, R.id.tvName2, R.id.ratingBar2, R.id.btnDetails2, spots[1])
        bindCard(R.id.imgSpot3, R.id.tvName3, R.id.ratingBar3, R.id.btnDetails3, spots[2])
        bindCard(R.id.imgSpot4, R.id.tvName4, R.id.ratingBar4, R.id.btnDetails4, spots[3])
        }
    /**
     * Builds and returns in-memory list of FoodSport objects
     * String values are pulled from resources to avoid hard-coded strings
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
            hasVisited = true
        ),
        FoodSpot(
            name = getString(R.string.spot2_name),
            location = getString(R.string.spot2_location),
            lastVisit = getString(R.string.spot2_last_visit),
            rating = 4.0f,
            description = getString(R.string.spot2_description),
            review = getString(R.string.spot2_review),
            imageResId = R.drawable.spot2,
            hasVisited = true
        ),
        FoodSpot(
            name = getString(R.string.spot3_name),
            location = getString(R.string.spot3_location),
            lastVisit = getString(R.string.spot3_last_visit),
            rating = 4.5f,
            description = getString(R.string.spot3_description),
            review = getString(R.string.spot3_review),
            imageResId = R.drawable.spot3,
            hasVisited = true
        ),
        FoodSpot(
            name = getString(R.string.spot4_name),
            location = getString(R.string.spot4_location),
            lastVisit = getString(R.string.spot4_last_visit),
            rating = 3.5f,
            description = getString(R.string.spot4_description),
            review = getString(R.string.spot4_review),
            imageResId = R.drawable.spot4,
            hasVisited = true
        )
    )

    /**
     * Binds card's views to a FoodSpot and wires up the detail button
     * Extracted to avoid repeated code across all four cards
     *
     * @param imageId   Resource ID of card's ImageView
     * @param nameId    Resource ID of card's name TextView
     * @param ratingId  Resource ID of card's RatingBar
     * @param buttonId  Resource ID of card's Button
     * @param spot      FoodSpot data to display on this card
     */
    private fun bindCard(
        imageId: Int,
        nameId: Int,
        ratingId: Int,
        buttonId: Int,
        spot: FoodSpot
    ){
        findViewById<ImageView>(imageId).apply {
            setImageResource(spot.imageResId)
            //Clicking the image launches detail screen
            setOnClickListener {
                startActivity(Intent(this@MainActivity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_FOOD_SPOT, spot)
                })
            }
        }
        findViewById<TextView>(nameId).text = spot.name
        findViewById<RatingBar>(ratingId).rating = spot.rating

        // Button to launch DetailActivity and pass FoodSpot as Parcelable
        findViewById<Button>(buttonId).setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_FOOD_SPOT, spot)
            }
            startActivity(intent)
        }
    }

}