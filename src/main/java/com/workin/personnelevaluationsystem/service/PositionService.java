package com.workin.personnelevaluationsystem.service;

import com.workin.personnelevaluationsystem.dto.PositionDTO;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    PositionDTO createPosition(PositionDTO positionDTO);
    Optional<PositionDTO> getPositionById(Integer id);
    List<PositionDTO> getAllPositions();
    PositionDTO updatePosition(Integer id, PositionDTO positionDetailsDTO);
    void deletePosition(Integer id);
}