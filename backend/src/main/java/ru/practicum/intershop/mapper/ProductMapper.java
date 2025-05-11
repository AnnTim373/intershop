package ru.practicum.intershop.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductOutputDTO;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    default Page<ProductOutputDTO> toDTO(Page<Product> products) {
        return products.map(this::toDTO);
    }

    ProductOutputDTO toDTO(Product product);

    default String toStringFromProductImage(byte[] image) {
        return image == null ? null : Base64.getEncoder().encodeToString(image);
    }

}
