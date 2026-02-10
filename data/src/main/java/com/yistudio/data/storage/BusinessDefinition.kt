package com.yistudio.data.storage

import com.yistudio.data.entity.BusinessEntity

val BusinessDefinition = listOf(
    // --- EARLY GAME: Minutes to Unlock ---
    BusinessEntity("b01", "Lemonade Stand", 0, 10.0, false),           // Free/Starting
    BusinessEntity("b02", "Newspaper Delivery", 0, 100.0, false),      // Minutes
    BusinessEntity("b03", "Hot Dog Cart", 0, 500.0, false),            // Minutes
    BusinessEntity("b04", "Coffee Shop", 0, 2_500.0, true),            // 15-30 Minutes
    BusinessEntity("b05", "Food Truck", 0, 12_000.0, true),            // ~1 Hour

    // --- MID GAME: Hours to Unlock ---
    BusinessEntity("b06", "Bakery", 0, 100_000.0, true),               // 2-4 Hours
    BusinessEntity("b07", "Local Diner", 0, 800_000.0, true),          // 6-8 Hours
    BusinessEntity("b08", "Pizza Franchise", 0, 6_500_000.0, true),    // 12 Hours
    BusinessEntity("b09", "Supermarket", 0, 50_000_000.0, true),       // 18 Hours
    BusinessEntity("b10", "Shopping Mall", 0, 400_000_000.0, true),    // ~24 Hours

    // --- LOGISTICS PHASE: Transitioning to Days ---
    BusinessEntity("b11", "Logistics Warehouse", 0, 3_500_000_000.0, false),
    BusinessEntity("b12", "Trucking Fleet", 0, 30_000_000_000.0, true),
    BusinessEntity("b13", "International Shipping", 0, 250_000_000_000.0, true),
    BusinessEntity("b14", "Air Cargo Company", 0, 2_000_000_000_000.0, true),
    BusinessEntity("b15", "Global Logistics Network", 0, 15_000_000_000_000.0, true),

    // --- TECH PHASE: Days of Progress ---
    BusinessEntity("b16", "Mobile App Startup", 0, 150_000_000_000_000.0, false),
    BusinessEntity("b17", "SaaS Company", 0, 1_500_000_000_000_000.0, true),
    BusinessEntity("b18", "AI Research Lab", 0, 15_000_000_000_000_000.0, false),
    BusinessEntity("b19", "Cloud Computing Platform", 0, 150_000_000_000_000_000.0, true),
    BusinessEntity("b20", "Tech Conglomerate", 0, 1_500_000_000_000_000_000.0, true),

    // --- FINANCIAL EMPIRE: Deep Idling ---
    BusinessEntity("b21", "Investment Firm", 0, 2e19, true),
    BusinessEntity("b22", "Hedge Fund", 0, 3e20, true),
    BusinessEntity("b23", "Global Bank", 0, 5e21, true),
    BusinessEntity("b24", "Stock Exchange", 0, 8e22, true),
    BusinessEntity("b25", "Sovereign Wealth Fund", 0, 1e24, true),

    // --- GALACTIC PHASE: Endgame / Weeks ---
    BusinessEntity("b26", "Satellite Network", 0, 2e25, false),
    BusinessEntity("b27", "Space Mining Corp", 0, 5e26, false),
    BusinessEntity("b28", "Orbital Manufacturing", 0, 1e28, true),
    BusinessEntity("b29", "Interplanetary Trade Guild", 0, 2e29, true),
    BusinessEntity("b30", "Galactic Mega Corporation", 0, 1e31, true)
)