package ru.practicum.intershop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;
import ru.practicum.intershop.error.custom_exception.IntershopException;

import java.io.IOException;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    default Page<ProductOutputDTO> toDTO(Page<Product> products) {
        return products.map(this::toDTO);
    }

    ProductOutputDTO toDTO(Product product);

    @Mapping(target = "id", ignore = true)
    Product fromDTO(ProductInputDTO productInputDTO);

    default String toStringFromProductImage(byte[] image) {
        return image == null ? null : Base64.getEncoder().encodeToString(image);
    }

    default byte[] fromMultipartToProductImage(MultipartFile image) {
        try {
            return image == null ? null : image.getBytes();
        } catch (IOException e) {
            throw new IntershopException("Произошла ошибка при загрузке изображения: " +  e.getMessage());
        }
    }

}
