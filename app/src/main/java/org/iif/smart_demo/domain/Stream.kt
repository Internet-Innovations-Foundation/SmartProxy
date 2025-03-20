package org.iif.smart_demo.domain

data class Stream(
    val id: Int?,
    val name: String?,
    val hlsVideoSrc: String?,
    val nextTimeSlots: List<TvSlot>?
) {
    val currentShowName: String?
        get() = nextTimeSlots?.firstOrNull()?.program?.name
}