package com.majed.sary.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    val id: Int,
    val title: String,
    val description: String,
    val button_text: String,
    val expiry_status: Boolean,
    val created_at: String,
    val start_date: String,
    val expiry_date: String,
    val image: String,
    val photo: String,
    val promo_code: String?,
    val priority: Int,
    val link: String,
    val level: String,
    val is_available: Boolean,
    val branch: Int
) : Parcelable {
    override fun toString(): String {
        return "Banner(id=$id, title='$title', description='$description', button_text='$button_text', expiry_status=$expiry_status, created_at='$created_at', start_date='$start_date', expiry_date='$expiry_date', image='$image', photo='$photo', promo_code='$promo_code', priority=$priority, link='$link', level='$level', is_available=$is_available, branch=$branch)"
    }
}