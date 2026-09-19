package br.com.biblioteca.feiticos.mapper;

import br.com.biblioteca.feiticos.domain.Feitico;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponse;

public class FeiticoMapper {

    public static FeiticoResponse toResponse(Feitico feitico){
        return new FeiticoResponse(
                feitico.getId(),
                feitico.getNome(),
                feitico.getDescricao(),
                feitico.getTipo(),
                feitico.getNivelDificuldade()
        );
    }
}
