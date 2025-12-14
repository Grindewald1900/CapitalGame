package com.example.domain.main

import com.example.domain.entity.BusinessModel

interface BusinessRepository {
    /**
     * Ordered list of business definitions.
     * Order controls unlock sequence (0, 1, 2, ...)
     */
    fun getAllBusinesses(): List<BusinessModel>
}