package info.heitor.pagamentoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ItemPedido {
    private Long produtoId;
    private Long quantidade;
}
