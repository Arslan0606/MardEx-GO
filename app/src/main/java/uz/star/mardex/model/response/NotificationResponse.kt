package uz.star.mardex.model.response

import java.io.Serializable

data class NotificationResponse(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val isRead: Boolean
) : Serializable
