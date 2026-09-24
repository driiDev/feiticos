package br.com.biblioteca.feiticos.service;

import br.com.biblioteca.feiticos.domain.Feitico;
import br.com.biblioteca.feiticos.dtos.request.CadastrarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.request.EditarFeiticoRequest;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponse;
import br.com.biblioteca.feiticos.enums.Tipo;
import br.com.biblioteca.feiticos.exception.FeiticoNaoEncontradoException;
import br.com.biblioteca.feiticos.repository.FeiticoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.model.ChatModel;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeiticoServiceTest {

    @InjectMocks
    private FeiticoService feiticoService;

    @Mock
    private FeiticoRepository feiticoRepository;

    @Mock
    private ChatModel chatModel;

    @Nested
    class SalvarFeitico {

        @Test
        @DisplayName("Deve salvar o feitiço com sucesso e não chamar IA quando a descrição for fornecida")
        void deveSalvarFeiticoSemChamarIaQuandoDescricaoInformada() {

            // arrange
            CadastrarFeiticoRequest feiticoRequest = new CadastrarFeiticoRequest("sumir", "some na escuridao", Tipo.DEFESA, 2);

            Feitico feitico = new Feitico();
            feitico.setId(1L);
            feitico.setNome("sumir");
            feitico.setDescricao("some na escuridao");
            feitico.setTipo(Tipo.DEFESA);
            feitico.setNivelDificuldade(2);

            when(feiticoRepository.save(any(Feitico.class))).thenReturn(feitico);

            // act
            FeiticoResponse response = feiticoService.salvarFeitico(feiticoRequest);

            // assert
            assertNotNull(response);
            assertEquals(1L, response.id());
            assertEquals("sumir", response.nome());
            verify(feiticoRepository).save(any(Feitico.class));
            verifyNoInteractions(chatModel);
        }

        @Test
        @DisplayName("Deve salvar o feitiço com sucesso e chamar IA quando a descrição não for fornecida")
        void deveSalvarFeiticoChamamandoIaQuandoDescricaoNaoForInformada(){

            // arrange
            CadastrarFeiticoRequest request = new CadastrarFeiticoRequest("capa de invisibilidade", null, Tipo.TRANSFIGURACAO, 3);

            Feitico feitico = new Feitico();
            feitico.setId(2L);
            feitico.setNome("capa de invisibilidade");
            feitico.setDescricao("deve se tornar invisivel");
            feitico.setTipo(Tipo.TRANSFIGURACAO);
            feitico.setNivelDificuldade(3);


            when(chatModel.call(anyString())).thenReturn("deve se tornar invisivel");
            when(feiticoRepository.save(any(Feitico.class))).thenReturn(feitico);

            // act
            FeiticoResponse response = feiticoService.salvarFeitico(request);

            // assert
            verify(chatModel).call(anyString());
            verify(feiticoRepository).save(any(Feitico.class));
            assertEquals("deve se tornar invisivel", response.descricao());
        }

        @Test
        @DisplayName("Quando o nivel de dificuldade não for informado, deve receber o valor 5")
        void deveAdicionarNivelDeDificuldadeIgualA5QuandoNaoForInformado(){

            // arrange
            CadastrarFeiticoRequest request = new CadastrarFeiticoRequest("voar", "voando alto para fugir", Tipo.DEFESA, null);

            Feitico feitico = new Feitico();
            feitico.setId(3L);
            feitico.setNome("voar");
            feitico.setDescricao("voando alto para fugir");
            feitico.setTipo(Tipo.DEFESA);
            feitico.setNivelDificuldade(5);

            when(feiticoRepository.save(any(Feitico.class))).thenReturn(feitico);

            // act
            FeiticoResponse response = feiticoService.salvarFeitico(request);

            // assert
            verify(feiticoRepository).save(any(Feitico.class));
            assertEquals(5, response.nivelDificuldade());

        }
    }

    @Nested
    class ListarFeiticos{

        @Test
        @DisplayName("Deve listar todos os feitiços encontrados")
        void deveListarTodosOsFeiticosEncontrados(){

            // arrange
            Feitico feitico1 = new Feitico();
            feitico1.setId(1L);
            feitico1.setNome("voar");
            feitico1.setDescricao("voando alto para fugir");
            feitico1.setTipo(Tipo.DEFESA);
            feitico1.setNivelDificuldade(5);

            Feitico feitico2 = new Feitico();
            feitico2.setId(2L);
            feitico2.setNome("sumir");
            feitico2.setDescricao("some na escuridao");
            feitico2.setTipo(Tipo.DEFESA);
            feitico2.setNivelDificuldade(2);

            List<Feitico> feiticos = List.of(feitico1, feitico2);

            when(feiticoRepository.findAll()).thenReturn(feiticos);

            // act
            List<FeiticoResponse> feiticosEncontrados = feiticoService.listarFeiticos();

            // assert
            assertEquals(2, feiticosEncontrados.size());
            verify(feiticoRepository).findAll();

        }

        @Test
        @DisplayName("Deve retornar uma lista vazia quando não houver feitiços cadastrados")
        void deveRetornarListaVaziaQuandoNaoEncontrarFeiticos(){
            // arrange
            List<Feitico> feiticos = List.of();

            when(feiticoRepository.findAll()).thenReturn(feiticos);

            // act
            List<FeiticoResponse> feiticosEncontrados = feiticoService.listarFeiticos();

            // assert
            assertTrue(feiticosEncontrados.isEmpty());
            verify(feiticoRepository).findAll();
        }
    }

    @Nested
    class BuscarFeiticos{

        @Test
        @DisplayName("Deve retornar feitiço encontrado conforme ID informado")
        void deveBuscarFeiticoPorIdCorretamente() {

            // arrange
            Feitico feitico = new Feitico();
            feitico.setId(1L);
            feitico.setNome("voar");
            feitico.setDescricao("voando alto para fugir");
            feitico.setTipo(Tipo.DEFESA);
            feitico.setNivelDificuldade(5);

            when(feiticoRepository.findById(anyLong())).thenReturn(Optional.of(feitico));

            // act

            FeiticoResponse feiticoEncontrado = feiticoService.buscarFeitico(feitico.getId());

            // assert
            assertNotNull(feiticoEncontrado);
            assertEquals(1L, feiticoEncontrado.id());

            verify(feiticoRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Deve retornar exceção quando nenhum feitiço for encontrado")
        void deveRetornarExcecaoQuandoFeiticoNaoForEncontradoPorId(){

            // arrange
            when(feiticoRepository.findById(anyLong())).thenReturn(Optional.empty());

            // act e assert

            FeiticoNaoEncontradoException exception = assertThrows(
                    FeiticoNaoEncontradoException.class,
                    () -> feiticoService.buscarFeitico(33L)
            );

            assertEquals("Feitiço não encontrado", exception.getMessage());

        }

    }

    @Nested
    class EditarFeiticos {

        @Test
        @DisplayName("Deve atualizar os dados do feitiço com sucesso quando o ID existir")
        void deveEditarFeiticoComSucessoQuandoIdExistir() {

            // arrange
            long id = 1L;
            EditarFeiticoRequest request = new EditarFeiticoRequest("voar alto", "voando ainda mais alto", Tipo.DEFESA, 4);

            Feitico feiticoExistente = new Feitico();
            feiticoExistente.setId(id);
            feiticoExistente.setNome("voar");
            feiticoExistente.setDescricao("voando alto para fugir");
            feiticoExistente.setTipo(Tipo.DEFESA);
            feiticoExistente.setNivelDificuldade(2);

            when(feiticoRepository.findById(id)).thenReturn(Optional.of(feiticoExistente));
            when(feiticoRepository.save(any(Feitico.class))).thenReturn(feiticoExistente);

            // act
            FeiticoResponse response = feiticoService.editarFeitico(id, request);

            // assert
            assertNotNull(response);
            assertEquals("voar alto", response.nome());
            assertEquals("voando ainda mais alto", response.descricao());
            assertEquals(4, response.nivelDificuldade());
            verify(feiticoRepository).findById(id);
            verify(feiticoRepository).save(feiticoExistente);
        }

        @Test
        @DisplayName("Deve lançar exceção e não salvar alterações quando o feitiço não for encontrado")
        void deveLancarExcecaoAoEditarFeiticoInexistente() {

            // arrange
            long idInexistente = 99L;
            EditarFeiticoRequest request = new EditarFeiticoRequest("invisibilidade", "ficar invisivel", Tipo.DEFESA, 3);

            when(feiticoRepository.findById(idInexistente)).thenReturn(Optional.empty());

            // act e assert
            FeiticoNaoEncontradoException exception = assertThrows(
                    FeiticoNaoEncontradoException.class,
                    () -> feiticoService.editarFeitico(idInexistente, request)
            );

            assertEquals("Feitiço não encontrado", exception.getMessage());
            verify(feiticoRepository).findById(idInexistente);
            verify(feiticoRepository, never()).save(any(Feitico.class));
        }
    }

    @Nested
    class DeletarFeitico {

        @Test
        @DisplayName("Deve deletar o feitiço com sucesso quando o ID existir")
        void deveDeletarFeiticoComSucessoQuandoIdExistir() {

            // arrange
            long id = 1L;
            Feitico feitico = new Feitico();
            feitico.setId(id);

            when(feiticoRepository.findById(id)).thenReturn(Optional.of(feitico));
            doNothing().when(feiticoRepository).delete(feitico);

            // act
            feiticoService.deletarFeitico(id);

            // assert
            verify(feiticoRepository).findById(id);
            verify(feiticoRepository).delete(feitico);
        }

        @Test
        @DisplayName("Deve lançar exceção e não deletar quando o ID não for encontrado")
        void deveLancarExcecaoAoDeletarFeiticoInexistente() {

            // arrange
            long idInexistente = 99L;
            when(feiticoRepository.findById(idInexistente)).thenReturn(Optional.empty());

            // act e assert
            FeiticoNaoEncontradoException exception = assertThrows(
                    FeiticoNaoEncontradoException.class,
                    () -> feiticoService.deletarFeitico(idInexistente)
            );

            assertEquals("Feitiço não encontrado", exception.getMessage());
            verify(feiticoRepository).findById(idInexistente);
            verify(feiticoRepository, never()).delete(any(Feitico.class));
        }
    }
}