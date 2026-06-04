package com.omarrdev.ithra.dto.response.stats;

public record MonthlyStatResponse(
        int year,
        int month,
        long value
) {}
