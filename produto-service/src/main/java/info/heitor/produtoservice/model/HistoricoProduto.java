package info.heitor.produtoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HISTORICO_PRODUTO")
public class HistoricoProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcaoHistorico acao;

    @Column(name = "codigo_produto", nullable = true)
    private String codigoProduto;

    @Column(nullable = false)
    private LocalDateTime dataHora;

}
