package br.com.biblioteca.feiticos.service;

import br.com.biblioteca.feiticos.domain.Feitico;
import br.com.biblioteca.feiticos.dtos.request.EditarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.request.CadastrarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponse;
import br.com.biblioteca.feiticos.exception.FeiticoNaoEncontradoException;
import br.com.biblioteca.feiticos.mapper.FeiticoMapper;
import br.com.biblioteca.feiticos.repository.FeiticoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeiticoService {

    private final FeiticoRepository feiticoRepository;
    private final ChatModel chatModel;

    public FeiticoResponse salvarFeitico(CadastrarFeiticoRequest request) {

        String descricaoFinal = request.getDescricao();
        Integer nivelDificuldadeFinal = request.getNivelDificuldade();

        if (descricaoFinal == null || descricaoFinal.isBlank()) {

            String prompt = """
                Você é um grimório técnico e direto.
                Gere uma descrição de 1 frase (efeito prático e direto) para o feitiço: %s.
                Responda apenas com o efeito, sem introduções, aspas ou saudações.
                Exemplo: Abre portas e janelas trancadas por meios físicos ou mágicos simples.
                """.formatted(request.getNome());

            descricaoFinal = chatModel.call(prompt);
        }

        if (nivelDificuldadeFinal == null) {
                nivelDificuldadeFinal = 5;
        }

        Feitico feitico = new Feitico();
        feitico.setNome(request.getNome());
        feitico.setTipo(request.getTipo());
        feitico.setDescricao(descricaoFinal);
        feitico.setNivelDificuldade(nivelDificuldadeFinal);

        Feitico feiticoSalvo = feiticoRepository.save(feitico);

        return FeiticoMapper.toResponse(feiticoSalvo);
    }

    public List<FeiticoResponse> listarFeiticos(){

        List<Feitico> feiticos = feiticoRepository.findAll();

        return feiticos.stream()
                .map(FeiticoMapper::toResponse).toList();
    }

    public FeiticoResponse buscarFeitico(long id){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado"));

        return FeiticoMapper.toResponse(feitico);

    }

    public FeiticoResponse editarFeitico(long id, EditarFeiticoRequest request){

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

        return FeiticoMapper.toResponse(feitico);
    }

    public void deletarFeitico(Long id){

        Feitico feitico = feiticoRepository.findById(id)
                .orElseThrow(() -> new FeiticoNaoEncontradoException("Feitiço não encontrado"));

        feiticoRepository.delete(feitico);
    }
}