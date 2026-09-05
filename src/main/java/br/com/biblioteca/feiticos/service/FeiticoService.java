package br.com.biblioteca.feiticos.service;

import br.com.biblioteca.feiticos.domain.Feitico;
import br.com.biblioteca.feiticos.dtos.request.FeiticoEdicaoRequestDTO;
import br.com.biblioteca.feiticos.dtos.request.FeiticoRequestDTO;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponseDTO;
import br.com.biblioteca.feiticos.enums.Tipo;
import br.com.biblioteca.feiticos.exception.FeiticoNaoEncontradoException;
import br.com.biblioteca.feiticos.mapper.FeiticoMapper;
import br.com.biblioteca.feiticos.repository.FeiticoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeiticoService {

    private final FeiticoRepository feiticoRepository;
    private final ChatModel chatModel;

    public FeiticoResponseDTO salvarFeitico(FeiticoRequestDTO request) {

        String descricaoFinal = request.getDescricao();
        Integer nivelDificuldadeFinal = request.getNivelDificuldade();

        if (descricaoFinal == null || descricaoFinal.isBlank()) {

            String prompt = "Você é um grimório técnico e enciclopédico do universo de magia. "
                    + "Gere uma descrição OBJETIVA e FOCADA NO EFEITO PRÁTICO para o feitiço: " + request.getNome()
                    + " que pertence à categoria: " + request.getTipo() + ". "
                    + "A descrição deve explicar puramente o que o feitiço FAZ (Exemplo para 'Aberto': 'Abre portas e janelas trancadas'). "
                    + "ATENÇÃO: Seja direto, use no máximo 200 caracteres e não adicione nenhuma saudação ou texto extra.";

            String respostaIA = chatModel.call(prompt);

            descricaoFinal = respostaIA;
        }

        if (nivelDificuldadeFinal == null) {
                nivelDificuldadeFinal = 3;
        }

        Feitico feitico = new Feitico();
        feitico.setNome(request.getNome());
        feitico.setTipo(request.getTipo());
        feitico.setDescricao(descricaoFinal);
        feitico.setNivelDificuldade(nivelDificuldadeFinal);

        Feitico feiticoSalvo = feiticoRepository.save(feitico);

        return FeiticoMapper.feiticoResponse(feiticoSalvo);
    }

    public List<FeiticoResponseDTO> listarFeiticos(){

        List<Feitico> feiticos = feiticoRepository.findAll();

        return feiticos.stream()
                .map(FeiticoMapper::feiticoResponse).toList();
    }

    public FeiticoResponseDTO buscarFeitico(long id){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado"));

        return FeiticoMapper.feiticoResponse(feitico);

    }

    public FeiticoResponseDTO editarFeitico(long id, FeiticoEdicaoRequestDTO request){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado"));

        if(request.getNome() != null){
             feitico.setNome(request.getNome());
        }
        if(request.getDescricao() != null){
            feitico.setDescricao(request.getDescricao());
        }
        if (request.getTipo() != null){
            feitico.setTipo(request.getTipo());
        }
        if (request.getNivelDificuldade() != null){
            feitico.setNivelDificuldade(request.getNivelDificuldade());
        }

        feiticoRepository.save(feitico);

        return FeiticoMapper.feiticoResponse(feitico);
    }

    public void deletarFeitico(Long id){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado."));

        feiticoRepository.delete(feitico);
    }
}