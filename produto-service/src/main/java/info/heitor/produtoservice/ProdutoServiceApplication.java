package info.heitor.produtoservice;

import info.heitor.produtoservice.model.Produto;
import info.heitor.produtoservice.service.ProdutoService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@OpenAPIDefinition(
		servers = {
				@Server(url = "http://localhost:9999/produto")
		}
)
@SpringBootApplication
public class ProdutoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProdutoServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataInitializer(ProdutoService produtoService) {
		return args -> {
			produtoService.salvarProduto(new Produto("Whey Protein", "Suplemento proteico de alta qualidade", BigDecimal.valueOf(120.50), 50,0));
			produtoService.salvarProduto(new Produto("Camiseta de Treino", "Camiseta leve e respirável para atividades físicas", BigDecimal.valueOf(45.99), 200, 1));
			produtoService.salvarProduto(new Produto("Multivitamínico", "Suplemento com diversas vitaminas essenciais", BigDecimal.valueOf(75.00), 100, 2));
			produtoService.salvarProduto(new Produto("Cinto de Musculação", "Cinto de couro para suporte lombar em exercícios pesados", BigDecimal.valueOf(90.00), 30, 3));
		};
	}
}
