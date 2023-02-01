package com.udacity.asteroidradar.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.PictureOfDay
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.util.Constants.IMAGE_TYPE

@BindingAdapter("setPictureOfDayAndDescription")
fun bindPictureOfDayAndDescription(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    pictureOfDay?.let { picture ->
        imageView.contentDescription = picture.title
        if (picture.mediaType == IMAGE_TYPE) {
            Picasso.get()
                .load(picture.url)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
    }
}

/**
 * When there is no Asteroid data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)
//    {
////        scroll the list to the top after the diffs are calculated and posted
//        recyclerView.scrollToPosition(0)
//    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription=imageView.resources.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription=imageView.resources.getString(R.string.not_hazardous_asteroid_image)

    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("goneIfNotNullOrEmpty")
fun goneIfNotNullOrEmpty(view: View, asteroids: List<Asteroid>?) {
    view.visibility = if (asteroids.isNullOrEmpty()) View.VISIBLE else View.GONE
}
