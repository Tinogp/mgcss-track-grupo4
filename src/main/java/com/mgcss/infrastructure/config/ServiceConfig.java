package com.mgcss.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.RegisterSolicitudRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.service.ClienteService;
import com.mgcss.service.SolicitudService;
import com.mgcss.service.TecnicoService;

@Configuration
public class ServiceConfig {

    @Bean
    public SolicitudService solicitudService(SolicitudRepository solicitudRepository,
                                             TecnicoRepository tecnicoRepository,
                                             RegisterSolicitudRepository registerSolicitudRepository,
                                             ClienteRepository clienteRepository) {
        return new SolicitudService(solicitudRepository, tecnicoRepository, registerSolicitudRepository, clienteRepository);
    }

    @Bean
    public ClienteService clienteService(ClienteRepository clienteRepository) {
        return new ClienteService(clienteRepository);
    }

    @Bean
    public TecnicoService tecnicoService(TecnicoRepository tecnicoRepository) {
        return new TecnicoService(tecnicoRepository);
    }
}
