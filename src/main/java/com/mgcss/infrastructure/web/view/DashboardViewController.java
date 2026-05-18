package com.mgcss.infrastructure.web.view;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mgcss.domain.Cliente;
import com.mgcss.domain.Solicitud;
import com.mgcss.domain.Tecnico;
import com.mgcss.service.ClienteService;
import com.mgcss.service.SolicitudService;
import com.mgcss.service.TecnicoService;

@Controller
public class DashboardViewController {

    private final ClienteService clienteService;
    private final SolicitudService solicitudService;
    private final TecnicoService tecnicoService;

    public DashboardViewController(ClienteService clienteService,
                                   SolicitudService solicitudService,
                                   TecnicoService tecnicoService) {
        this.clienteService = clienteService;
        this.solicitudService = solicitudService;
        this.tecnicoService = tecnicoService;
    }

    @GetMapping({"/", "/ui"})
    public String dashboard(@RequestParam(required = false) Long solicitudId, Model model) {
        List<Cliente> clientes = clienteService.getAllCliente();
        List<Solicitud> solicitudes = solicitudService.listarSolicitudes();
        List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
        List<Tecnico> tecnicosActivosLista = tecnicos.stream()
                .filter(Tecnico::isActivo)
                .toList();
        Optional<Solicitud> solicitudSeleccionada = Optional.empty();

        if (solicitudId != null) {
            solicitudSeleccionada = solicitudService.obtenerSolicitud(solicitudId);
        }

        long abiertas = solicitudes.stream()
                .filter(solicitud -> Solicitud.Estado.ABIERTA.equals(solicitud.getEstadoActual()))
                .count();
        long enProceso = solicitudes.stream()
                .filter(solicitud -> Solicitud.Estado.EN_PROCESO.equals(solicitud.getEstadoActual()))
                .count();
        long cerradas = solicitudes.stream()
                .filter(solicitud -> Solicitud.Estado.CERRADA.equals(solicitud.getEstadoActual()))
                .count();

        model.addAttribute("clientes", clientes);
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("tecnicosActivosLista", tecnicosActivosLista);
        model.addAttribute("solicitudConsultada", solicitudSeleccionada.orElse(null));
        model.addAttribute("solicitudConsultadaId", solicitudId);
        model.addAttribute("totalClientes", clientes.size());
        model.addAttribute("totalSolicitudes", solicitudes.size());
        model.addAttribute("totalTecnicos", tecnicos.size());
        model.addAttribute("tecnicosActivos", tecnicos.stream().filter(Tecnico::isActivo).count());
        model.addAttribute("solicitudesAbiertas", abiertas);
        model.addAttribute("solicitudesEnProceso", enProceso);
        model.addAttribute("solicitudesCerradas", cerradas);
        model.addAttribute("tiposCliente", Cliente.TipoCliente.values());
        model.addAttribute("especialidades", Tecnico.Especialidad.values());

        return "dashboard";
    }

    @PostMapping("/ui/clientes")
    public String crearCliente(@RequestParam String nombre,
                               @RequestParam String email,
                               @RequestParam Cliente.TipoCliente tipoCliente,
                               RedirectAttributes redirectAttributes) {
        clienteService.crearCliente(nombre, email, tipoCliente);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente creado correctamente.");
        return "redirect:/ui";
    }

    @PostMapping("/ui/solicitudes")
    public String crearSolicitud(@RequestParam String descripcion,
                                 @RequestParam Long clienteId,
                                 RedirectAttributes redirectAttributes) {
        try {
            solicitudService.crearSolicitud(descripcion, clienteId);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud registrada correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/ui";
    }

    @PostMapping("/ui/tecnicos")
    public String crearTecnico(@RequestParam String nombre,
                               @RequestParam Tecnico.Especialidad especialidad,
                               RedirectAttributes redirectAttributes) {
        tecnicoService.crearTecnico(new Tecnico(nombre, especialidad));
        redirectAttributes.addFlashAttribute("mensaje", "Tecnico creado correctamente.");
        return "redirect:/ui";
    }

    @PostMapping("/ui/tecnicos/{id}/activar")
    public String activarTecnico(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean activado = tecnicoService.activarTecnico(id);
        redirectAttributes.addFlashAttribute(activado ? "mensaje" : "error",
                activado ? "Tecnico activado." : "No se pudo activar el tecnico.");
        return "redirect:/ui";
    }

    @PostMapping("/ui/solicitudes/{id}/asignar")
    public String asignarTecnico(@PathVariable Long id,
                                 @RequestParam Long tecnicoId,
                                 RedirectAttributes redirectAttributes) {
        try {
            boolean asignada = solicitudService.asignarTecnico(id, tecnicoId);
            redirectAttributes.addFlashAttribute(asignada ? "mensaje" : "error",
                    asignada ? "Tecnico asignado a la solicitud." : "No se pudo asignar el tecnico.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/ui#solicitudes";
    }

    @PostMapping("/ui/solicitudes/{id}/iniciar")
    public String iniciarProceso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean iniciada = solicitudService.iniciarProceso(id);
            redirectAttributes.addFlashAttribute(iniciada ? "mensaje" : "error",
                    iniciada ? "Solicitud puesta en proceso." : "La solicitud necesita un tecnico asignado para iniciarse.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/ui#solicitudes";
    }

    @PostMapping("/ui/solicitudes/{id}/cerrar")
    public String cerrarSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean cerrada = solicitudService.cerrarSolicitud(id);
            redirectAttributes.addFlashAttribute(cerrada ? "mensaje" : "error",
                    cerrada ? "Solicitud cerrada." : "La solicitud no se puede cerrar en su estado actual.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/ui#solicitudes";
    }

    @PostMapping("/ui/solicitudes/{id}/reabrir")
    public String reabrirSolicitud(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean reabierta = solicitudService.reabrirSolicitud(id);
            redirectAttributes.addFlashAttribute(reabierta ? "mensaje" : "error",
                    reabierta ? "Solicitud reabierta." : "La solicitud no se puede reabrir en su estado actual.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/ui#solicitudes";
    }
}
