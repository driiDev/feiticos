package br.com.biblioteca.feiticos.dtos.request;

import br.com.biblioteca.feiticos.enums.Tipo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeiticoEdicaoRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    private Tipo tipo;

    private Integer nivelDificuldade;


}
