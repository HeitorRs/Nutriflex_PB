package info.heitor.descontoservice.Service;

import info.heitor.descontoservice.model.Cupom;
import info.heitor.descontoservice.model.Pedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DescontoService {
    public BigDecimal calcularDesconto(Pedido pedido) {
        BigDecimal valorTotal = pedido.getValorTotal();
        BigDecimal descontoPercentual = getDesconto(pedido.getCupom());
        BigDecimal valorDesconto = valorTotal.multiply(descontoPercentual);
        return valorTotal.subtract(valorDesconto);
    }

    private BigDecimal getDesconto(Cupom cupom) {
        return switch (cupom) {
            case CUPOM10 -> new BigDecimal("0.10");
            case CUPOM15 -> new BigDecimal("0.15");
            case CUPOM20 -> new BigDecimal("0.20");
            default -> BigDecimal.ONE;
        };
    }
}
