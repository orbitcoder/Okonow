package com.noitacilppa.okonow.ui.home

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.noitacilppa.okonow.ui.theme.Background
import com.noitacilppa.okonow.ui.theme.OnSurface
import com.noitacilppa.okonow.ui.theme.OnSurfaceVariant
import com.noitacilppa.okonow.ui.theme.OutlineVariant
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SecondaryTeal
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHigh
import com.noitacilppa.okonow.ui.theme.SurfaceContainerHighest
import com.noitacilppa.okonow.ui.theme.TertiaryPink

/** Stitch Home / Today — header avatar. */
private const val HomeHeaderAvatarUrl =
    "https://lh3.googleusercontent.com/aida-public/AB6AXuBX2iavuLwaAuBEShstIw4wW3UfA5FwCFXmdCe2q-pg_F3nyallzVK6gkG_iYYfMwAeNlYpHxctHGPqm_qp49-Q1BHHxS8B166fi11QGKsPLW3Y92P6uci6W7FqKybhdoN9vAtNooxwRbt6BFQbK2BwbSn3MxsL_wzg0QrH57s0n4zKnFkjCq_PYGOz5oOQl7Zb9klTVcyOyXlguCNQNwH8IQ7CV5vOT_PPeEQRSRDQKjEoUYYmKvaRwlefMX1deprVMt6VcLXE8NA"

private val PrimaryFixedDim = Color(0xFFB075FF)
private val SecondaryFixedDim = Color(0xFF50EBD5)
private val SecondaryContainerDark = Color(0xFF006B5F)

private val HomeCardCorner = 24.dp

private data class TeamMember(val name: String, val imageUrl: String, val borderColor: Color, val dimmed: Boolean = false)

private val teamMembers = listOf(
    TeamMember(
        "Marcus",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAixUCcaaK5I32OkxTWWIS8Qo03bU1Bo7j5kCoA2p6x0xLTTVTDSCxu8qzOiILVAd3xq2GlxOu1xJ4crzrcnuIroEH_DDp4Dsshfvh-QPM2jt-HE7SnQ6fq0lgT6y3hmWKyzb1dvyntujsbAsRJty22S5sEIVpsbalY4CEIK_utD7pgjxjciIyJAcqaoeC-Z8uncM4tmSD65XO2AQ7vd7-Mn4l3m_jqqeKgpUSJsyCEhGoU_HQCk8J-U4lbaSz5jXWskleiWiGub_s",
        TertiaryPink
    ),
    TeamMember(
        "Sarah",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuAmQEOpqUrNkkdrJ0MDPkefU5ozb8ets6OjzgwTaPYfLQqXy9YLdpo1ERTDIqRAoZAUnHcHmvoydlu_R8qzRvJgdBXbmf6AxBd6KA0MwkQebAL9TD8Okg_lT5GK2LeQ57uMvaHRBE9TJXonD_GE9OZbG2ISY_B5iBIvPzgbLHv8gzHd7oI2XiGkhZH_OCRgeiLDLtaZiB48a4i_SGpDZXA-G_IpKwA3OdstvLa1VQjCky0NGrmVVS4m7UlTrI9UG9t7MAa77cprzog",
        PrimaryPurple
    ),
    TeamMember(
        "David",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCDuq-xlAzYEeQb9UVf7l9Lt16w51vLdZhCU7TFxH67fYMndDT5nJqDvq0jxqaGqmGAFLE-DkhrckhBstcRUXaPQ-RH1Ly2vaMxb62-mNAklUAgMHSkG2IX4NaNnYO9owgEEte4lB0wWgxYeK5PeJcQaxmxbNNyrtiFwZQhKPjBRN4fO9fWOQtkX_vSjODRKl3pBbKtNb_4ah8Y9h86ieadhqAkop3VOwbd5NOXiYPdS1-xCOylu-iM740yCUA_77NCprGYq-Zk-oI",
        OutlineVariant.copy(alpha = 1f),
        dimmed = true
    ),
    TeamMember(
        "Elena",
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCZ1hmZiXFqBo3LkZpg0UqFFZiVSsK6RouCibUEEjJ1lowPu8gt24mdojqRUig-v9aR52rQSaiIbckq-0i23uwSPc1Ug5phUXv1MmZDgABHG-KESdSz9TMG9lmizTdtS5OsMjucQJEytXmK7TYeJyjTC9IHHqaVO-uLhUCm_pnl0EddRkAfpehoi9Xlbp0VvG4ezj8dRzHTwdcwjRCsfjKqTxA5jloM8gnbqpU6gL9XLrXq1Qg0j5jm3y7g3eBnW0klsE9sPXmbCJ8",
        SecondaryTeal
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTodayScreen(
    modifier: Modifier = Modifier,
    onSettings: () -> Unit = {},
    onSeeAllTasks: () -> Unit = {},
    onAddTask: () -> Unit = {}
) {
    val context = LocalContext.current
    val cardShape = RoundedCornerShape(HomeCardCorner)

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(2.dp, PrimaryPurple.copy(alpha = 0.2f), CircleShape)
                                .padding(2.dp)
                                .clip(CircleShape)
                        ) {
                            AsyncImage(
                                model = HomeHeaderAvatarUrl,
                                contentDescription = "User profile",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column {
                            Text(
                                "Okonow",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                "Good morning, Alex",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = OnSurfaceVariant,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = OnSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background.copy(alpha = 0.92f)
                )
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 112.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                TodayProgressSection(progress = 0.72f, tasksCompletedToday = 12)

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "Today's Focus",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            "See all",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple,
                            modifier = Modifier.clickable {
                                onSeeAllTasks()
                                Toast.makeText(context, "Full task list coming soon", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        GlossyTaskCard(
                            title = "Brand System Audit",
                            subtitle = "10:30 AM • Design Review",
                            priorityLabel = "High",
                            priorityBackground = PrimaryPurple.copy(alpha = 0.1f),
                            priorityText = PrimaryFixedDim,
                            priorityBorder = PrimaryPurple.copy(alpha = 0.2f),
                            subtasksDone = 3,
                            subtasksTotal = 5,
                            category = "Product",
                            categoryColor = TertiaryPink,
                            checkboxAccent = PrimaryPurple,
                            shape = cardShape
                        )
                        GlossyTaskCard(
                            title = "Weekly Sync with Engineers",
                            subtitle = "02:15 PM • Meeting",
                            priorityLabel = "Med",
                            priorityBackground = SecondaryTeal.copy(alpha = 0.1f),
                            priorityText = SecondaryFixedDim,
                            priorityBorder = SecondaryTeal.copy(alpha = 0.2f),
                            subtasksDone = 0,
                            subtasksTotal = 2,
                            category = "Engineering",
                            categoryColor = PrimaryPurple,
                            checkboxAccent = SecondaryTeal,
                            shape = cardShape
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        "Team Activity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(modifier = Modifier.width(0.dp))
                        teamMembers.forEach { member ->
                            TeamAvatarChip(member = member)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                QuoteMotivationCard(shape = RoundedCornerShape(HomeCardCorner))
            }
        }

        FloatingActionButton(
            onClick = onAddTask,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 96.dp),
            containerColor = PrimaryPurple,
            contentColor = Color(0xFF330066),
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 8.dp,
                pressedElevation = 8.dp
            ),
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add task", modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun TodayProgressSection(progress: Float, tasksCompletedToday: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TodayProgressRing(progress = progress)
        Text(
            text = buildAnnotatedString {
                append("You've completed ")
                withStyle(SpanStyle(color = OnSurface, fontWeight = FontWeight.Bold)) {
                    append("$tasksCompletedToday tasks")
                }
                append(" so far today. Almost there!")
            },
            style = MaterialTheme.typography.bodyLarge,
            color = OnSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(0.65f),
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun TodayProgressRing(progress: Float) {
    val ringSize = 256.dp
    Box(
        modifier = Modifier.size(ringSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeTrack = 12.dp.toPx()
            val strokeProgress = 14.dp.toPx()
            val diameter = size.minDimension - strokeProgress
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
            val arcSize = Size(diameter, diameter)
            drawArc(
                color = SurfaceContainerHighest,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeTrack, cap = StrokeCap.Round)
            )
            val sweep = 360f * progress.coerceIn(0f, 1f)
            drawArc(
                brush = Brush.linearGradient(
                    colors = listOf(SecondaryTeal, SecondaryContainerDark),
                    start = topLeft,
                    end = Offset(topLeft.x + arcSize.width, topLeft.y + arcSize.height)
                ),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeProgress, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${(progress * 100).toInt()}%",
                fontSize = 57.sp,
                fontWeight = FontWeight.Black,
                color = OnSurface
            )
            Text(
                "COMPLETE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = SecondaryTeal
            )
        }
    }
}

@Composable
private fun GlossyTaskCard(
    title: String,
    subtitle: String,
    priorityLabel: String,
    priorityBackground: Color,
    priorityText: Color,
    priorityBorder: Color,
    subtasksDone: Int,
    subtasksTotal: Int,
    category: String,
    categoryColor: Color,
    checkboxAccent: Color,
    shape: RoundedCornerShape
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceContainerHigh.copy(alpha = 0.4f))
            .border(1.dp, Color.White.copy(alpha = 0.08f), shape)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color.White.copy(alpha = 0.06f), Color.Transparent),
                        start = Offset(0f, 0f),
                        end = Offset(800f, 800f)
                    )
                )
        )
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(2.dp, checkboxAccent.copy(alpha = 0.3f), CircleShape)
                    .background(checkboxAccent.copy(alpha = 0.05f))
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f, fill = false)) {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = OnSurface)
                        Text(
                            subtitle,
                            fontSize = 14.sp,
                            color = OnSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Text(
                        priorityLabel.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                        color = priorityText,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(priorityBackground)
                            .border(1.dp, priorityBorder, RoundedCornerShape(999.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Checklist,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "$subtasksDone of $subtasksTotal subtasks",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant
                        )
                    }
                    Text(
                        category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = categoryColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(SurfaceContainerHighest)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamAvatarChip(member: TeamMember) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, member.borderColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
        ) {
            AsyncImage(
                model = member.imageUrl,
                contentDescription = member.name,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = if (member.dimmed) 0.5f else 1f },
                contentScale = ContentScale.Crop
            )
        }
        Text(
            member.name,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (member.dimmed) OnSurfaceVariant.copy(alpha = 0.5f) else OnSurfaceVariant
        )
    }
}

@Composable
private fun QuoteMotivationCard(shape: RoundedCornerShape) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceContainerHigh.copy(alpha = 0.6f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), shape)
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 48.dp, y = (-48).dp)
                .size(128.dp)
                .background(PrimaryPurple.copy(alpha = 0.2f), CircleShape)
                .blur(60.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-48).dp, y = 48.dp)
                .size(128.dp)
                .background(SecondaryTeal.copy(alpha = 0.1f), CircleShape)
                .blur(60.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                Icons.Default.FormatQuote,
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(40.dp)
            )
            Text(
                "\"The way to get started is to quit talking and begin doing.\"",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = OnSurface,
                lineHeight = 28.sp
            )
            Text(
                "— Walt Disney",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurfaceVariant
            )
        }
    }
}
