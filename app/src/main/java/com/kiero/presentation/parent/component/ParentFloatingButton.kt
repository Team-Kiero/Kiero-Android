package com.kiero.presentation.parent.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.kiero.R
import com.kiero.core.designsystem.theme.KieroTheme

@Composable
fun ParentFloatingButton(
    buttonColor: Color,
    onActiveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onActiveClick,
        containerColor = buttonColor,
        modifier = modifier,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_parent_fab_plus),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ParentFloatingButtonPreview() {
    ParentFloatingButton(
        buttonColor = KieroTheme.colors.white,
        onActiveClick = {}
    )
}