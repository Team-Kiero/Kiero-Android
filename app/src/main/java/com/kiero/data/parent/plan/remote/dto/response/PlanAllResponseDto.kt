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
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("name")
    val name: String,
    @SerialName("colorCode")
    val colorCode: String,
    @SerialName("dayOfWeek")
    val dayOfWeek: String
)

@Serializable
data class NormalScheduleDto(
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