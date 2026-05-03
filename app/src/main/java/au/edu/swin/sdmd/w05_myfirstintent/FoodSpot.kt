package au.edu.swin.sdmd.w05_myfirstintent

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data model represents a food spot
 * Implements Parcelable so they can be passes between activities via Intents
 *
 * @param name          Display name of spot
 * @param location          Address string
 * @param lastVisit     Date of my last visit (formatted string)
 * @param rating        Rating out of 5.0
 * @param description   short description of the spot
 * @param review        My Personal review
 * @param imageResID    Drawable resource ID for the spot's image
 * @param hasVisited    Whether I've visited this place (shown with a switch)
 */
@Parcelize
data class FoodSpot(
    val name: String,
    val location: String,
    val lastVisit: String,
    val rating: Float,
    val description: String,
    val review: String,
    val imageResId: Int,
    val hasVisited: Boolean
) : Parcelable