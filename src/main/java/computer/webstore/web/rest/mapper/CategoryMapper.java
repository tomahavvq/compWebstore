package computer.webstore.web.rest.mapper;

import computer.webstore.domain.*;
import computer.webstore.web.rest.dto.CategoryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper {

    CategoryDTO categoryToCategoryDTO(Category category);

    List<CategoryDTO> categoriesToCategoryDTOs(List<Category> categories);

    @Mapping(target = "products", ignore = true)
    Category categoryDTOToCategory(CategoryDTO categoryDTO);

    List<Category> categoryDTOsToCategories(List<CategoryDTO> categoryDTOs);
}
