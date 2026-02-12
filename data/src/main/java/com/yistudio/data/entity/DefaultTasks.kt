package com.yistudio.data.entity

object DefaultTasks{
    val initialTasks = listOf(
        // --- SERIES SEED: 初始任务 (状态: ACTIVE) ---
        TaskEntity(
            id = "task_seed_lemonade",
            title = "First Pour",
            description = "Scale Lemonade Stand to Level 5 to prove the concept.",
            type = TaskType.SPRINT,
            triggerType = TaskTriggerType.BUSINESS_LEVEL,
            targetId = "b01",
            targetGoal = 5.0,
            rewardCash = 50000.0,
            status = TaskStatus.ACTIVE
        ),
        TaskEntity(
            id = "task_seed_lemonade_2",
            title = "Second Pour",
            description = "Scale Lemonade Stand to Level 10 to prove the concept.",
            type = TaskType.SPRINT,
            triggerType = TaskTriggerType.BUSINESS_LEVEL,
            targetId = "b01",
            targetGoal = 10.0,
            rewardCash = 100000.0,
            status = TaskStatus.ACTIVE
        ),
        TaskEntity(
            id = "task_seed_lemonade_3",
            title = "Big Pour",
            description = "Scale Lemonade Stand to Level 25 to prove the concept.",
            type = TaskType.SPRINT,
            triggerType = TaskTriggerType.BUSINESS_LEVEL,
            targetId = "b01",
            targetGoal = 25.0,
            rewardCash = 500000.0,
            rewardInfluence = 10.0,
            status = TaskStatus.ACTIVE
        ),
        TaskEntity(
            id = "task_seed_capital",
            title = "Angel Round",
            description = "Accumulate $1,000 in total capital to attract angel investors.",
            type = TaskType.MILESTONE,
            triggerType = TaskTriggerType.ACCUMULATE_CAPITAL,
            targetGoal = 1000.0,
            rewardInfluence = 10.0,
            status = TaskStatus.ACTIVE
        ),

        // --- SERIES A: 锁定任务 (状态: LOCKED, 需满足资产或等级触发) ---
        TaskEntity(
            id = "task_series_a_newspaper",
            title = "Local Monopoly",
            description = "Upgrade Newspaper Delivery to Level 50.",
            type = TaskType.SPRINT,
            triggerType = TaskTriggerType.BUSINESS_LEVEL,
            targetId = "b02",
            targetGoal = 50.0,
            rewardEquity = 1, // 对应 image_69293e.png 中的 Equity
            requiredSeries = 1, // Series A
            status = TaskStatus.LOCKED
        ),
        TaskEntity(
            id = "task_series_a_food_truck",
            title = "Mobile Disruption",
            description = "Unlock the Food Truck and reach Level 10.",
            type = TaskType.MILESTONE,
            triggerType = TaskTriggerType.BUSINESS_LEVEL,
            targetId = "b05",
            targetGoal = 10.0,
            rewardInfluence = 50.0,
            status = TaskStatus.LOCKED
        ),

        // --- MID-GAME: FINANCIAL EMPIRE (触发逻辑: 资产规模 > 1e18) ---
        TaskEntity(
            id = "task_hedge_fund_launch",
            title = "Wall Street Entry",
            description = "Establish a Hedge Fund (b22) and reach Level 100.",
            type = TaskType.MILESTONE,
            triggerType = TaskTriggerType.BUSINESS_LEVEL,
            targetId = "b22",
            targetGoal = 100.0,
            rewardInfluence = 5000.0,
            rewardEquity = 5,
            status = TaskStatus.LOCKED
        ),

        // --- ENDGAME: GALACTIC PHASE (触发逻辑: 资产规模 > 1e25) ---
        TaskEntity(
            id = "task_galactic_conglomerate",
            title = "Interplanetary IPO",
            description = "Scale Galactic Mega Corporation (b30) to Level 1.",
            type = TaskType.MOONSHOT,
            triggerType = TaskTriggerType.BUSINESS_LEVEL,
            targetId = "b30",
            targetGoal = 1.0,
            rewardInfluence = 1e6,
            rewardEquity = 50,
            status = TaskStatus.LOCKED
        )
    )
}
