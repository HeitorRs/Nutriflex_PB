package info.heitor.descontoservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    private BigDecimal valorTotal;
    private Cupom cupom;
}
