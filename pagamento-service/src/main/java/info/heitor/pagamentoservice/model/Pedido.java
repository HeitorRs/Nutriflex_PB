package info.heitor.pagamentoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class Pedido {
    private String id;
    private List<ItemPedido> itens;
    private BigDecimal valorTotal;
    private String cupom;
    private BigDecimal valorTotalComDesconto;
    private PedidoStatus status;
    private Long serial;
    private String traceId;

    public void criarSerial(){
        this.serial = new Random().nextLong(4000);
    }
}
