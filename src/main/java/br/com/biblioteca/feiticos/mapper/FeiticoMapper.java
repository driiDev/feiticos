package br.com.biblioteca.feiticos.mapper;

import br.com.biblioteca.feiticos.domain.Feitico;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponseDTO;

public class FeiticoMapper {

    public static FeiticoResponseDTO feiticoResponse(Feitico feitico){
        return new FeiticoResponseDTO(
                feitico.getId(),
                feitico.getNome(),
                feitico.getDescricao(),
                feitico.getTipo(),
                feitico.getNivelDificuldade()
        );
    }
}
