package com.mgcss.service;

import com.mgcss.domain.Tecnico;
import com.mgcss.domain.repository.TecnicoRepository;
import java.util.Optional;

public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public boolean activarTecnico(Long tecnicoId) {
        Optional<Tecnico> tecnicoOpt = tecnicoRepository.findById(tecnicoId);

        if (tecnicoOpt.isPresent()) {
            Tecnico tecnico = tecnicoOpt.get();
            boolean activado = tecnico.activar();

            if (activado) {
                tecnicoRepository.save(tecnico);
                return true;
            }
        }

        return false;
    }
}
