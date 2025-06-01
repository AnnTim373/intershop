package ru.practicum.intershop.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.domain.Product;

public interface ProductRepository extends R2dbcRepository<Product, Long> {

    @Query(value = "select p.* from shop.product p order by :sort limit :limit offset :offset")
    Flux<Product> findAll(@Param("limit") int limit, @Param("offset") int offset, @Param("sort") String sort);

    @Query(value = "select count(p.*) from shop.product p")
    @NotNull
    Mono<Long> count();

    @Query(value = "select p.* from shop.product p " +
            "where lower(p.name) like lower(:search) or lower(p.description) like lower(:search) " +
            "order by :sort limit :limit offset :offset")
    Flux<Product> findAllBySearch(@Param("limit") int limit,
                                  @Param("offset") int offset,
                                  @Param("sort") String sort,
                                  @Param("search") String search);

    @Query(value = "select count(p.*) from shop.product p " +
            "where lower(p.name) like lower(:search) or lower(p.description) like lower(:search)")
    Mono<Long> countBySearch(@Param("search") String search);

}
