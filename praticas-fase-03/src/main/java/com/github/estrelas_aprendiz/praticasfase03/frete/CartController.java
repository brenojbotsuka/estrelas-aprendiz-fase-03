package com.github.estrelas_aprendiz.praticasfase03.frete;

import com.github.estrelas_aprendiz.praticasfase03.cupom.CartItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final OrderService orderService;

    public CartController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CartFinalResponse> calculate(@RequestBody CartRequest request) {
        CartFinalResponse response = orderService.calculateCart(request.items(), request.couponCode(), request.cep());
        return ResponseEntity.ok(response);
    }
}

record CartRequest(List<CartItem> items, String couponCode, String cep) {}