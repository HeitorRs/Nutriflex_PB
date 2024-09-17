package info.heitor.produtoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "PRODUTO")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(nullable = false, unique = true)
    private String codigo;


    public Produto(String nome, String descricao, BigDecimal preco, int estoque, int categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = pegarCategoria(categoria);
    }

    @PrePersist
    public void prePersist() {
        // Gera o código automaticamente usando um UUID
        this.codigo = "PROD-" + UUID.randomUUID().toString();
    }

    public Categoria pegarCategoria(int i) {
        categoria = Categoria.values()[i];
        return categoria;
    }
}
