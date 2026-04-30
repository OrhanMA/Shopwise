package com.shopwise.backend.api.dto;

import com.shopwise.backend.domain.ServiceOffering;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        int durationMinutes,
        int pointsReward
) {
    public static ServiceResponse from(ServiceOffering service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getDescription(),
                service.getDurationMinutes(),
                service.getPointsReward()
        );
    }
}
