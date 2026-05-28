import android.content.Intent
import com.kiero.core.model.fcm.PushData

fun Intent?.toPushDataOrNull(): PushData? {
    val type = this?.getStringExtra("type")?.takeIf { it.isNotBlank() } ?: return null
    val targetId = this.getStringExtra("targetId")
    return PushData(type, targetId)
}