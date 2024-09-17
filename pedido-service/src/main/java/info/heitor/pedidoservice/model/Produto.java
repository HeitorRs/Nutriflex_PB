package info.heitor.pedidoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class Produto {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private int estoque;
}
