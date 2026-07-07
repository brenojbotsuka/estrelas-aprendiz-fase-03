package com.github.estrelas_aprendiz.praticasfase03.frete;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon") // Alterado para o singular para bater com 'INSERT INTO coupon'
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(name = "coupon_type")
    private String type;

    @Column(name = "coupon_value")
    private BigDecimal value;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "min_value")
    private BigDecimal minValue;

    private boolean active;
}