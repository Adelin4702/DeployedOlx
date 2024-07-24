package com.olxapplication.service;

import com.olxapplication.constants.CategoryMessages;
import com.olxapplication.dtos.CategoryDTO;
import com.olxapplication.dtos.CategoryDetailsDTO;
import com.olxapplication.exception.PatternNotMathcedException;
import com.olxapplication.exception.ResourceNotFoundException;
import com.olxapplication.mappers.CategoryMapper;
import com.olxapplication.entity.Category;
import com.olxapplication.repository.CategoryRepository;
import com.olxapplication.validators.CategoryValidators;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service layer class provides business logic operations for managing categories within the application.
 * It interacts with the CategoryRepository to perform CRUD operations and leverages mappers for DTO conversions.
 */
@Service
@AllArgsConstructor
public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.olxapplication.service.CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final CategoryValidators categoryValidators = new CategoryValidators();

    /**
     * Finds all categories.
     * @return a list of CategoryDetailsDTO objects.
     */
    public List<CategoryDetailsDTO> findCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(CategoryMapper::toCategoryDetailsDTO)
                .collect(Collectors.toList());
    }


    /**
     * Inserts a new category after validating the input.
     * @param categoryDTO the CategoryDTO object of the category to insert.
     * @return a string message indicating the result of the operation.
     */
    public String insert(CategoryDTO categoryDTO) {
        try {
            categoryValidators.categoryDtoValidator(categoryDTO);
            Category category = CategoryMapper.toEntity(categoryDTO);
            category = categoryRepository.save(category);
            LOGGER.debug(CategoryMessages.CATEGORY_INSERTED_SUCCESSFULLY);
            return CategoryMessages.CATEGORY_INSERTED_SUCCESSFULLY;
        }catch (PatternNotMathcedException e){
            LOGGER.error(CategoryMessages.CATEGORY_NOT_INSERTED + e.getMessage());
            return CategoryMessages.CATEGORY_NOT_INSERTED + e.getMessage();
        }
    }

    /**
     * Deletes a category by its id.
     * @param id the id of the category to delete.
     * @return a string message indicating the result of the operation.
     */
    public String deleteCategoryById(String id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (!categoryOptional.isPresent()) {
            LOGGER.error(CategoryMessages.CATEGORY_NOT_FOUND + id);
            return  CategoryMessages.CATEGORY_NOT_FOUND + id;
        } else {
            categoryRepository.delete(categoryOptional.get());
            LOGGER.debug(CategoryMessages.CATEGORY_DELETED_SUCCESSFULLY + id);
            return CategoryMessages.CATEGORY_DELETED_SUCCESSFULLY + id;

        }
    }

    /**
     * Updates the name of a category by its id after validating the input.
     * @param id the id of the category to update.
     * @param categoryDTO the CategoryDetailsDTO object containing the new name of the category.
     * @return a string message indicating the result of the operation.
     */
    public String updateCategoryNameById(String id, CategoryDetailsDTO categoryDTO) {
        try {
            categoryValidators.categoryDetailsDtoValidator(categoryDTO);
            Optional<Category> categoryOptional = categoryRepository.findById(id);

            if (!categoryOptional.isPresent()) {
                LOGGER.error(CategoryMessages.CATEGORY_NOT_FOUND + id);
                return CategoryMessages.CATEGORY_NOT_FOUND + CategoryMessages.CATEGORY_NOT_FOUND + id;
            } else {
                Category toBeUpdated = categoryOptional.get();
                toBeUpdated.setCategoryName(categoryDTO.getCategoryName());
                categoryRepository.save(toBeUpdated);
                LOGGER.debug(CategoryMessages.CATEGORY_UPDATED_SUCCESSFULLY + id);
                return CategoryMessages.CATEGORY_UPDATED_SUCCESSFULLY + id;
            }
        }catch (PatternNotMathcedException e){
            LOGGER.error(CategoryMessages.CATEGORY_NOT_UPDATED + e.getMessage());
            return CategoryMessages.CATEGORY_NOT_UPDATED + e.getMessage();
        }
    }

}
