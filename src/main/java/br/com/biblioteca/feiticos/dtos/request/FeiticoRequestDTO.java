package br.com.biblioteca.feiticos.dtos.request;

import br.com.biblioteca.feiticos.enums.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class FeiticoRequestDTO {

    @NotBlank(message = "O nome do feitiço é obrigatório.")
    private String nome;

    private String descricao;

    @NotNull(message = "O tipo do feitiço é obrigatório.")
    private Tipo tipo;

    private Integer nivelDificuldade;

}
