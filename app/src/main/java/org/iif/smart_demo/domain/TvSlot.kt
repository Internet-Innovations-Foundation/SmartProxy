package org.iif.smart_demo.domain

import java.util.Date

data class TvSlot(
    val id: Int?,
    val startDate: Date?,
    val endDate: Date?,
    val program: TvProgram?,
    val programElement: TvProgramElement?
)

data class TvProgramElement(
    val name: String?,
    val teaser: String?
)