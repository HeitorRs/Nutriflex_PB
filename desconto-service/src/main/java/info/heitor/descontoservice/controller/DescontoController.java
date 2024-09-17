package info.heitor.descontoservice.controller;

import info.heitor.descontoservice.Service.DescontoService;
import info.heitor.descontoservice.model.DescontoResponsePayload;
import info.heitor.descontoservice.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class DescontoController {
    private DescontoService descontoService;

    @PostMapping
    public ResponseEntity<DescontoResponsePayload> calcularDesconto(@RequestBody Pedido pedido) {
        log.info("Calculando desconto");
        try {
            BigDecimal valorTotalComDesconto = descontoService.calcularDesconto(pedido);
            DescontoResponsePayload responsePayload = new DescontoResponsePayload(valorTotalComDesconto);
            return ResponseEntity.ok(responsePayload);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new DescontoResponsePayload(BigDecimal.ZERO));
        }

    }

}
