package br.com.biblioteca.feiticos.controller;

import br.com.biblioteca.feiticos.dtos.request.FeiticoRequestDTO;
import br.com.biblioteca.feiticos.dtos.response.FeiticoResponseDTO;
import br.com.biblioteca.feiticos.service.FeiticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feiticos")
@RequiredArgsConstructor
public class FeiticoController {

    private final FeiticoService feiticoService;

    @PostMapping
    public ResponseEntity<FeiticoResponseDTO> cadastrar(@Valid @RequestBody FeiticoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(feiticoService.salvarFeitico(request));
    }
}