package com.junkfood.seal.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.UnfoldMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.ui.common.LocalWindowWidthState

@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    imageModel: Any = R.drawable.sample,
    title: String = "sample title ".repeat(5),
    author: String? = "author sample ".repeat(5),
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected) { onClick() },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(start = 4.dp, end = 12.dp)
                    .align(Alignment.CenterVertically), checked = selected, onCheckedChange = null
            )
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .padding(end = 4.dp)
                    .weight(if (LocalWindowWidthState.current == WindowWidthSizeClass.Compact) 2f else 1f)
            ) {
                AsyncImageImpl(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .aspectRatio(16f / 9f, matchHeightConstraintsFirst = true),
                    model = imageModel,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(3f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                author?.let {
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = author,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }
}

@Composable
@Preview
fun TaskItemPreview() {
    Surface {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                CustomCommandTaskItem(status = TaskStatus.RUNNING)
            }
            item {
                CustomCommandTaskItem(status = TaskStatus.FINISHED)
            }
            item {
                CustomCommandTaskItem(status = TaskStatus.ERROR)
            }
            item {
                CustomCommandTaskItem(status = TaskStatus.CANCELED)
            }
        }
    }
}

enum class TaskStatus {
    RUNNING, ERROR, CANCELED, FINISHED,
}

@Composable
fun CustomCommandTaskItem(
    modifier: Modifier = Modifier,
    status: TaskStatus = TaskStatus.ERROR,
    progress: Float = .85f,
    url: String = "https://www.example.com",
    templateName: String = "Template Example",
    progressText: String = "[sample] Extracting URL: https://www.example.com\n" +
            "[sample] sample: Downloading webpage\n" +
            "[sample] sample: Downloading android player API JSON\n" +
            "[info] Available automatic captions for sample:" + "[info] Available automatic captions for sample:",
    onCopyLog: () -> Unit = {},
    onCopyError: () -> Unit = {},
    onRestart: () -> Unit = {},
    onShowLog: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Surface(
        shape = CardDefaults.shape,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.semantics(mergeDescendants = true) { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (status) {
                    TaskStatus.FINISHED -> {
                        Icon(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp),
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = stringResource(id = R.string.status_completed)
                        )
                    }

                    TaskStatus.CANCELED -> {
                        Icon(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp),
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(id = R.string.status_canceled)
                        )
                    }

                    TaskStatus.RUNNING -> {
                        val animatedProgress by animateFloatAsState(
                            targetValue = progress,
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                        )
                        if (progress < 0)
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp),
                            )
                        else
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(24.dp),
                                strokeWidth = 5.dp,
                                progress = animatedProgress,
                            )
                    }

                    TaskStatus.ERROR -> {
                        Icon(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp),
                            imageVector = Icons.Filled.Error,
                            contentDescription = stringResource(id = R.string.status_error)
                        )
                    }
                }

                Column(
                    Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = templateName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis

                    )
                    Text(
                        text = url,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(
                    modifier = Modifier
                        .align(Alignment.Top)
                        .semantics(mergeDescendants = true) { },
                    onClick = { onShowLog() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.UnfoldMore,
                        contentDescription = stringResource(
                            id = R.string.show_logs
                        )
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(top = 4.dp),
                text = progressText,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                maxLines = 3,
                minLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                FlatButtonChip(
                    icon = Icons.Outlined.ContentCopy,
                    label = stringResource(id = R.string.copy_log)
                ) { onCopyLog() }
                if (status == TaskStatus.ERROR)
                    FlatButtonChip(
                        icon = Icons.Outlined.ErrorOutline,
                        label = stringResource(id = R.string.copy_error_report),
                        iconColor = MaterialTheme.colorScheme.error,
                    ) { onCopyError() }
                if (status == TaskStatus.RUNNING)
                    FlatButtonChip(
                        icon = Icons.Outlined.Cancel,
                        label = stringResource(id = R.string.cancel),
                    ) { onCancel() }
                if (status == TaskStatus.CANCELED || status == TaskStatus.ERROR)
                    FlatButtonChip(
                        icon = Icons.Outlined.RestartAlt,
                        label = stringResource(id = R.string.restart),
                    ) { onRestart() }
            }
        }
    }
}
