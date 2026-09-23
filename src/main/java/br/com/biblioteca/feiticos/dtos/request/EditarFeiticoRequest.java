package br.com.biblioteca.feiticos.dtos.request;

import br.com.biblioteca.feiticos.enums.Tipo;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditarFeiticoRequest {

    @Size(min = 3)
    private String nome;

    @Size(min =  5)
    private String descricao;

    private Tipo tipo;

    private Integer nivelDificuldade;


}
