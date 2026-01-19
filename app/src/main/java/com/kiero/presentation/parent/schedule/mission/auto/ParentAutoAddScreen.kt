package com.kiero.presentation.parent.schedule.mission.auto

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kiero.R
import com.kiero.core.designsystem.component.KieroTopbar
import com.kiero.core.designsystem.component.button.KieroButtonMedium
import com.kiero.core.designsystem.theme.KieroTheme
import com.kiero.presentation.parent.schedule.mission.auto.component.ParentAutoInputField
import com.kiero.presentation.parent.schedule.mission.auto.viewmodel.AutoMissionViewModel

//@Composable
//fun ParentAutoAddRoute(
//    paddingValues: PaddingValues,
//    navigateUp: () -> Unit,
//    childId: Long,
//    viewModel: AutoMissionViewModel = hiltViewModel()
//) {
//    val noticeText by viewModel.noticeText.collectAsState()
//    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
//    val missions by viewModel.missions.collectAsState()
//
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        viewModel.toastMessage.collect { message ->
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        viewModel.shouldNavigateBack.collect {
//            navigateUp()
//        }
//    }
//
//    when {
//        isAnalyzing -> ParentAutoLoadingScreen(paddingValues = paddingValues)
//
//        missions.isNotEmpty() -> ParentAutoResultScreen(
//            paddingValues = paddingValues,
//            viewModel = viewModel,
//            childId = childId,
//            navigateUp = navigateUp
//        )
//
//        else -> ParentAutoAddScreen(
//            paddingValues = paddingValues,
//            navigateUp = { viewModel.handleCancel() },
//            noticeText = noticeText,
//            isAnalyzeEnabled = noticeText.length >= 10,
//            onTextChange = { viewModel.updateNoticeText(it) },
//            onAnalyzeClick = { viewModel.analyzeNotice() }
//        )
//    }
//}

@Composable
fun ParentAutoAddRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    childId: Long,
    viewModel: AutoMissionViewModel = hiltViewModel()
) {
    val noticeText by viewModel.noticeText.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val missions by viewModel.missions.collectAsState()

    // 1. 스낵바 상태 생성 (여기서 만듦)
    val snackbarHostState = remember { SnackbarHostState() }

    // 2. Toast 대신 스낵바를 띄우도록 수정
    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            // 기존: Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            // 변경: 스낵바 상태에 메시지 전달
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.shouldNavigateBack.collect {
            navigateUp()
        }
    }

    when {
        // 3. 여기가 에러 원인이었습니다. 생성한 state를 전달해줍니다.
        isAnalyzing -> ParentAutoLoadingScreen(
            paddingValues = paddingValues,
            snackbarHostState = snackbarHostState // 👈 추가됨
        )

        missions.isNotEmpty() -> ParentAutoResultScreen(
            paddingValues = paddingValues,
            viewModel = viewModel,
            childId = childId,
            navigateUp = navigateUp
        )

        else -> ParentAutoAddScreen(
            paddingValues = paddingValues,
            navigateUp = { viewModel.handleCancel() },
            noticeText = noticeText,
            isAnalyzeEnabled = noticeText.length >= 10,
            onTextChange = { viewModel.updateNoticeText(it) },
            onAnalyzeClick = { viewModel.analyzeNotice() }
        )
    }
}
@Composable
fun ParentAutoAddScreen(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    noticeText: String,
    isAnalyzeEnabled: Boolean,
    onTextChange: (String) -> Unit,
    onAnalyzeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KieroTheme.colors.black)
            .padding(paddingValues)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        KieroTopbar(
            title = "알림장 미션 추가",
            leftIconRes = R.drawable.ic_close_light,
            leftIconClick = navigateUp
        )

        Text(
            text = "이곳에 알림장 내용을 붙여넣어 주세요.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 20.dp, end = 20.dp),
            style = KieroTheme.typography.semiBold.title3,
            color = KieroTheme.colors.gray200
        )

        ParentAutoInputField(
            text = noticeText,
            onTextChange = onTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        KieroButtonMedium(
            text = "분석하고 미션 추가하기",
            onClick = onAnalyzeClick,
            isEnabled = isAnalyzeEnabled,
            containerColor = KieroTheme.colors.main,
            contentColor = KieroTheme.colors.black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))
    }
}