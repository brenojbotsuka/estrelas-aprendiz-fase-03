INSERT INTO coupon(code, coupon_type, coupon_value, expiration_date, min_value, active)
VALUES('PROMO2026', 'PERCENTAGE', 20.00, '2026-12-31 23:59:59', 0.00, true);

INSERT INTO coupon(code, coupon_type, coupon_value, expiration_date, min_value, active)
VALUES('EXPIRADO', 'FIXED', 10.00, '2026-01-01 00:00:00', 0.00, true);

INSERT INTO coupon(code, coupon_type, coupon_value, expiration_date, min_value, active)
VALUES('INATIVO', 'FIXED', 15.00, '2026-12-31 23:59:59', 0.00, false);