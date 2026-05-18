package com.mgcss.domain.repository;

import java.util.List;
import java.util.Optional;

import com.mgcss.domain.Tecnico;

public interface TecnicoRepository { 
    Tecnico save(Tecnico tecnico); 
    Optional<Tecnico> findById(Long id); 
    List<Tecnico> findAll();
}
