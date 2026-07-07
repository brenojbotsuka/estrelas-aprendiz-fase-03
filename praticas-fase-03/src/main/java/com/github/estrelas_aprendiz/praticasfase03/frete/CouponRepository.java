package com.github.estrelas_aprendiz.praticasfase03.frete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
   Optional<CouponEntity> findByCodeAndActiveTrue(String code);

}
