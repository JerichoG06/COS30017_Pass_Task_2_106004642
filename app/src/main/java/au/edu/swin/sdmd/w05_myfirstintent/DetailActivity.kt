package au.edu.swin.sdmd.w05_myfirstintent

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Detail activity: displays full information for single FoodSpot
 * Receives FoodSpot Parcelable via Intent extras from MainActivity
 */
class DetailActivity : AppCompatActivity() {

    companion object {
        // Key used to pass FoodSpot Parcelable
        const val EXTRA_FOOD_SPOT = "extra_food_spot"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Retrieve Parcelable from incoming Intent
        @Suppress("DEPRECATION") //Hides outdated warning
        val spot: FoodSpot? = intent.getParcelableExtra(EXTRA_FOOD_SPOT)

        //If no data passed, close screen safely
        if (spot == null) {
            finish()
            return
        }

        supportActionBar?.apply {
            title = spot.name
            setBackgroundDrawable(ColorDrawable(spot.accentColor))
        }

        populateViews(spot)
    }

    /**
     * Populates all detail screen views with data from given FoodSpot
     * Image view steup using Kotlin's 'also' scope function
     *
     * @param spot  The FoodSpot being displayed
     */
    private fun populateViews(spot: FoodSpot) {
        //Set image and it's content description together using also
        findViewById<ImageView>(R.id.detailImage).also {
            it.setImageResource(spot.imageResId)
            it.contentDescription = getString(R.string.content_desc_image, spot.name)
        }

        //Populate all text fields
        findViewById<TextView>(R.id.detailName).text = spot.name
        findViewById<TextView>(R.id.detailLocation).text = spot.location
        findViewById<TextView>(R.id.detailLastVisit).text = spot.lastVisit
        findViewById<TextView>(R.id.detailDescription).text = spot.description
        findViewById<TextView>(R.id.detailReview).text = spot.review

        //RatingBar reflects stored rating value
        findViewById<RatingBar>(R.id.detailRatingBar).rating = spot.rating

        //Switch reflects hasVisited - set on creation
        findViewById<Switch>(R.id.detailVisitedSwitch).isChecked = spot.hasVisited
    }
}