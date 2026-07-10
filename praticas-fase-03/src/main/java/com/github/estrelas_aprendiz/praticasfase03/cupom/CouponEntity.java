package com.github.estrelas_aprendiz.praticasfase03.cupom;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
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


    public CouponEntity() {
    }

    public CouponEntity(Long id, String code, String type, BigDecimal value, LocalDateTime expirationDate, BigDecimal minValue, boolean active) {
        this.id = id;
        this.code = code;
        this.type = type;
        this.value = value;
        this.expirationDate = expirationDate;
        this.minValue = minValue;
        this.active = active;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
