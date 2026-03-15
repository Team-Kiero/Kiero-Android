package com.kiero.domain.kid.schedule.usecase

import com.kiero.core.common.util.suspendRunCatching
import com.kiero.data.kid.schedule.repository.ImageUploadRepository
import com.kiero.data.kid.schedule.repository.ScheduleRepository
import javax.inject.Inject

class CompleteScheduleWithImageUseCase @Inject constructor(
    private val imageUploadRepository: ImageUploadRepository,
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(
        uriString: String,
        fileName: String,
        contentType: String,
        scheduleDetailId: Long
    ): Result<Unit> = suspendRunCatching {
        val imageUrl = imageUploadRepository.uploadImage(
            uriString = uriString,
            fileName = fileName,
            contentType = contentType
        ).getOrThrow()

        scheduleRepository.patchScheduleComplete(
            scheduleDetailId = scheduleDetailId,
            imageUrl = imageUrl
        ).getOrThrow()
    }
}