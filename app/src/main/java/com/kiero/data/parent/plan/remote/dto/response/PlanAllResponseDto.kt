import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanAllResponseDto(
    @SerialName("isFireLit")
    val isFireLit: Boolean,
    @SerialName("items")
    val items: List<ScheduleItemDto> = emptyList(),
)

@Serializable
data class ScheduleItemDto(
    @SerialName("scheduleId")
    val scheduleId: Long = 0L,
    @SerialName("date")
    val date: String,
    @SerialName("dayOfWeek")
    val dayOfWeek: List<String> = emptyList(),
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("name")
    val name: String,
    @SerialName("colorCode")
    val colorCode: String,
    @SerialName("scheduleStatus")
    val scheduleStatus: String? = null,
)