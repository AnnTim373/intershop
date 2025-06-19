package ru.practicum.intershop.mapper.impl;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.Product;
import ru.practicum.intershop.dto.ProductInputDTO;
import ru.practicum.intershop.dto.ProductOutputDTO;
import ru.practicum.intershop.mapper.ProductMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public List<ProductOutputDTO> toDTO(List<Product> products) {
        if (products == null) {
            return null;
        }

        List<ProductOutputDTO> list = new ArrayList<>(products.size());
        for (Product product : products) {
            list.add(toDTO(product));
        }

        return list;
    }

    @Override
    public ProductOutputDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductOutputDTO productOutputDTO = new ProductOutputDTO();

        productOutputDTO.setId(product.getId());
        productOutputDTO.setName(product.getName());
        productOutputDTO.setDescription(product.getDescription());
        productOutputDTO.setPrice(product.getPrice().doubleValue());
        productOutputDTO.setImage(toStringFromProductImage(product.getImage()));

        return productOutputDTO;
    }

    @Override
    public Mono<Product> fromDTO(ProductInputDTO productInputDTO) {
        if (productInputDTO == null) {
            return Mono.empty();
        }
        return fromMultipartToProductImage(productInputDTO.getImage())
                .switchIfEmpty(Mono.just(new byte[0]))
                .map(image -> {
                    if (image != null && image.length < 1) image = null;
                    Product product = new Product();
                    product.setName(productInputDTO.getName());
                    product.setDescription(productInputDTO.getDescription());
                    product.setPrice(BigDecimal.valueOf(productInputDTO.getPrice()));
                    product.setImage(image);
                    return product;
                });
    }

    private String toStringFromProductImage(byte[] image) {
        return image == null ? null : Base64.getEncoder().encodeToString(image);
    }

    private Mono<byte[]> fromMultipartToProductImage(FilePart image) {
        return image == null ? Mono.empty() : DataBufferUtils.join(image.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                });
    }

}
