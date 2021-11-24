package uz.star.mardex.model.requests.updateuser

import uz.star.mardex.model.requests.location.LocationRequest


data class EditedPersonalData(
    val location: LocationRequest? = null,
    val name: String? = null,
    val phone: String? = null,
    val pic: String? = null,
    val fcm_token: String? = null,
    val lang: String? = null
)