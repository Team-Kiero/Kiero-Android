import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanAllResponseDto(
    @SerialName("isFireLit")
    val isFireLit: Boolean,
    @SerialName("recurringSchedules")
    val recurringSchedules: List<RecurringScheduleDto>,
    @SerialName("normalSchedules")
    val normalSchedules: List<NormalScheduleDto>
)

@Serializable
data class RecurringScheduleDto(
    @SerialName("scheduleId")
    val scheduleId: Long= 0L,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("name")
    val name: String,
    @SerialName("colorCode")
    val colorCode: String,
    @SerialName("dayOfWeek")
    val dayOfWeek: String,
    @SerialName("repeatStartDate")
    val repeatStartDate: String,
    @SerialName("repeatEndDate")
    val repeatEndDate: String? = null,
)

@Serializable
data class NormalScheduleDto(
    @SerialName("scheduleId")
    val scheduleId: Long= 0L,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("name")
    val name: String,
    @SerialName("colorCode")
    val colorCode: String,
    @SerialName("date")
    val date: String
)