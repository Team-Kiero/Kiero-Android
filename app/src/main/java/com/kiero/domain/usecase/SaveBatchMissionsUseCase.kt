package com.kiero.domain.usecase

import com.kiero.core.localstorage.info.UserInfoManager
import com.kiero.data.parent.mission.model.MissionCompleteModel
import com.kiero.data.parent.mission.model.SuggestedMissionModel
import com.kiero.data.parent.mission.repository.AutoMissionRepository
import jakarta.inject.Inject

class SaveBatchMissionsUseCase @Inject constructor(
    private val autoMissionRepository: AutoMissionRepository,
    private val userInfoManager: UserInfoManager
) {
    suspend operator fun invoke(
        missions: List<SuggestedMissionModel>
    ): Result<List<MissionCompleteModel>> {
        val childId = userInfoManager.getChildIdInfo()
            ?: return Result.failure(IllegalStateException("childId 없음"))
        return autoMissionRepository.saveBatchMissions(childId, missions)
    }
}