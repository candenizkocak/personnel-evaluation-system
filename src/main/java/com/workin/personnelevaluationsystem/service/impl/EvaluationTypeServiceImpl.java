package com.workin.personnelevaluationsystem.service.impl;

import com.workin.personnelevaluationsystem.dto.EvaluationTypeDTO;
import com.workin.personnelevaluationsystem.exception.BadRequestException;
import com.workin.personnelevaluationsystem.exception.ResourceNotFoundException;
import com.workin.personnelevaluationsystem.model.EvaluationType;
import com.workin.personnelevaluationsystem.repository.EvaluationTypeRepository;
import com.workin.personnelevaluationsystem.service.EvaluationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluationTypeServiceImpl implements EvaluationTypeService {

    private final EvaluationTypeRepository typeRepository;

    @Autowired
    public EvaluationTypeServiceImpl(EvaluationTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    private EvaluationTypeDTO convertToDto(EvaluationType type) {
        if (type == null) return null;
        return EvaluationTypeDTO.builder()
                .typeID(type.getTypeID())
                .name(type.getName())
                .description(type.getDescription())
                .build();
    }

    private EvaluationType convertToEntity(EvaluationTypeDTO typeDTO) {
        if (typeDTO == null) return null;
        return EvaluationType.builder()
                .typeID(typeDTO.getTypeID())
                .name(typeDTO.getName())
                .description(typeDTO.getDescription())
                .build();
    }

    @Override
    public EvaluationTypeDTO createEvaluationType(EvaluationTypeDTO typeDTO) {
        if (typeRepository.findByName(typeDTO.getName()).isPresent()) {
            throw new BadRequestException("Evaluation type with name '" + typeDTO.getName() + "' already exists.");
        }
        EvaluationType type = convertToEntity(typeDTO);
        EvaluationType savedType = typeRepository.save(type);
        return convertToDto(savedType);
    }

    @Override
    public Optional<EvaluationTypeDTO> getEvaluationTypeById(Integer id) {
        return typeRepository.findById(id).map(this::convertToDto);
    }

    @Override
    public List<EvaluationTypeDTO> getAllEvaluationTypes() {
        return typeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationTypeDTO updateEvaluationType(Integer id, EvaluationTypeDTO typeDetailsDTO) {
        EvaluationType typeToUpdate = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation type not found with ID: " + id));

        if (!typeToUpdate.getName().equals(typeDetailsDTO.getName())) {
            typeRepository.findByName(typeDetailsDTO.getName())
                    .ifPresent(t -> {
                        if (!t.getTypeID().equals(id)) {
                            throw new BadRequestException("Evaluation type with name '" + typeDetailsDTO.getName() + "' already exists.");
                        }
                    });
        }

        typeToUpdate.setName(typeDetailsDTO.getName());
        typeToUpdate.setDescription(typeDetailsDTO.getDescription());

        EvaluationType updatedType = typeRepository.save(typeToUpdate);
        return convertToDto(updatedType);
    }

    @Override
    public void deleteEvaluationType(Integer id) {
        if (!typeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluation type not found with ID: " + id);
        }
        // Add check for existing forms/reviews using this type before deleting.
        // If the type is referenced by EvaluationForms, deleting it directly will cause a DataIntegrityViolationException.
        typeRepository.deleteById(id);
    }
}