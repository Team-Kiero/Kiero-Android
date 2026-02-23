package com.kiero.presentation.parent.mission.component.datepicker.util

import java.time.DayOfWeek

fun DayOfWeek.daysUntil(other: DayOfWeek) = (other.value - value + 7) % 7