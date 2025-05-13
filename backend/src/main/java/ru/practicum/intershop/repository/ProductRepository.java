package ru.practicum.intershop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.intershop.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p from Product p " +
            "where lower(p.name) like lower(:search) or lower(p.description) like lower(:search)")
    Page<Product> findAllBySearch(Pageable pageable, @Param("search") String search);

}
