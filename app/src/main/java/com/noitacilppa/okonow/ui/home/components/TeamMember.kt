package com.noitacilppa.okonow.ui.home.components

import androidx.compose.ui.graphics.Color
import com.noitacilppa.okonow.ui.theme.PrimaryPurple
import com.noitacilppa.okonow.ui.theme.SecondaryTeal
import com.noitacilppa.okonow.ui.theme.TertiaryPink
import com.noitacilppa.okonow.ui.theme.OutlineVariant

data class TeamMember(val name: String, val imageUrl: String, val borderColor: Color, val dimmed: Boolean = false)

val teamMembers = listOf(
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
