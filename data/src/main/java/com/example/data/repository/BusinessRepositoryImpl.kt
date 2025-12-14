package com.example.data.repository

import com.example.domain.entity.BusinessModel
import com.example.domain.main.BusinessRepository
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor() : BusinessRepository {
    private val definitions = listOf(
        BusinessModel("b01", "Lemonade Stand", 0, 1.0, false),
        BusinessModel("b02", "Newspaper Delivery", 0, 3.0, false),
        BusinessModel("b03", "Hot Dog Cart", 0, 6.0, false),
        BusinessModel("b04", "Coffee Shop", 0, 12.0, true),
        BusinessModel("b05", "Food Truck", 0, 25.0, true),
        BusinessModel("b06", "Bakery", 0, 45.0, true),
        BusinessModel("b07", "Local Diner", 0, 80.0, true),
        BusinessModel("b08", "Pizza Franchise", 0, 150.0, true),
        BusinessModel("b09", "Supermarket", 0, 280.0, true),
        BusinessModel("b10", "Shopping Mall", 0, 520.0, true),

        BusinessModel("b11", "Logistics Warehouse", 0, 950.0, false),
        BusinessModel("b12", "Trucking Fleet", 0, 1_800.0, true),
        BusinessModel("b13", "International Shipping", 0, 3_500.0, true),
        BusinessModel("b14", "Air Cargo Company", 0, 6_500.0, true),
        BusinessModel("b15", "Global Logistics Network", 0, 12_000.0, true),

        BusinessModel("b16", "Mobile App Startup", 0, 22_000.0, false),
        BusinessModel("b17", "SaaS Company", 0, 40_000.0, true),
        BusinessModel("b18", "AI Research Lab", 0, 75_000.0, false),
        BusinessModel("b19", "Cloud Computing Platform", 0, 140_000.0, true),
        BusinessModel("b20", "Tech Conglomerate", 0, 260_000.0, true),

        BusinessModel("b21", "Investment Firm", 0, 480_000.0, true),
        BusinessModel("b22", "Hedge Fund", 0, 900_000.0, true),
        BusinessModel("b23", "Global Bank", 0, 1_700_000.0, true),
        BusinessModel("b24", "Stock Exchange", 0, 3_200_000.0, true),
        BusinessModel("b25", "Sovereign Wealth Fund", 0, 6_000_000.0, true),

        BusinessModel("b26", "Satellite Network", 0, 11_000_000.0, false),
        BusinessModel("b27", "Space Mining Corp", 0, 21_000_000.0, false),
        BusinessModel("b28", "Orbital Manufacturing", 0, 40_000_000.0, true),
        BusinessModel("b29", "Interplanetary Trade Guild", 0, 75_000_000.0, true),
        BusinessModel("b30", "Galactic Mega Corporation", 0, 140_000_000.0, true)
    )

    override fun getAllBusinesses(): List<BusinessModel> = definitions
}