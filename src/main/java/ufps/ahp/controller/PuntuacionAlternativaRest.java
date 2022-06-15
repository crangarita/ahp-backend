package ufps.ahp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.*;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.services.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value="/puntuacionalternativa" ,produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PuntuacionAlternativaRest {
    @Autowired
    PuntuacionAlternativaCriterioServicio puntuacionAlternativaCriterioServiciol;

    @Autowired
    ProblemaService problemaService;

    @Autowired
    PuntuacionAlternativaServicio puntuacionAlternativaServicio;

    @Autowired
    PuntuacionCriterioServicio puntuacionCriterioServicio;

    @Autowired
    DecisorService decisorService;

    @GetMapping(path = "/{token}")
    public ResponseEntity<?> alternativas(@PathVariable String token){
        Problema problema = problemaService.buscar(token);
        puntuacionAlternativaCriterioServiciol.llenarPuntuacionAlternativa(problema.getIdProblema());
        return ResponseEntity.ok("");
    }

    @PostMapping()
    public ResponseEntity<?>guardarPuntuacionCriterio(@RequestBody List<PuntuacionAlternativa> puntuaciones){
        // TODO: Yo espero como valor el puntaje del criterio 1, osea que si el criterio 2 tiene puntuacion "4", yo recibo 1/4
        for(PuntuacionAlternativa puntuacion: puntuaciones) {
            PuntuacionAlternativa puntuacionAnteriorRealizada = puntuacionAlternativaServicio.buscarPuntuacionDecisorProblema(
                    puntuacion.getPuntuacionAlternativaCriterio().getIdPuntuacionAltCrit(), puntuacion.decisor().getEmail());
            if(puntuacionAnteriorRealizada==null){
                Decisor d = decisorService.buscarPorEmail(puntuacion.decisor().getEmail());
                puntuacion.setDecisor(d);

                PuntuacionAlternativaCriterio puntuacionAlternativaCriterio = puntuacionAlternativaCriterioServiciol.buscar(puntuacion.getPuntuacionAlternativaCriterio().getIdPuntuacionAltCrit());
                puntuacion.setPuntuacionAlternativaCriterio(puntuacionAlternativaCriterio);
                puntuacionAlternativaServicio.guardar(puntuacion);

            } else{
                puntuacionAnteriorRealizada.setValor(puntuacion.getValor());
                puntuacionAlternativaServicio.guardar(puntuacionAnteriorRealizada);

            }

        }
        return ResponseEntity.ok(new Mensaje("Puntuación registrada"));
    }

    @GetMapping("/{emailDecisor}/{token}")
    public ResponseEntity<?>calcularVectorPropio(@PathVariable String emailDecisor, @PathVariable String token){

        Problema problema = problemaService.buscar(token);
        List<Object> resultados  = puntuacionAlternativaServicio.calcularMatriz(emailDecisor,token,problema); // 0=proceso, 1=matriz pareada string (1/n), 2=matriz pareada numerico
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/{token}/totalizar")
    public ResponseEntity<?>totalizarCriterios(@PathVariable String token){
        Problema problema = problemaService.buscar(token);
        List<Object> resultados  = puntuacionAlternativaServicio.totalizarMatriz(token,problema);
        if(resultados ==null){
            return new ResponseEntity(new Mensaje("El problema no ha sido calificado por ningún decisor"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(resultados);

    }

    @GetMapping("/puntuacionesanteriores/{emailDecisor}/{token}")
    public ResponseEntity<?>puntuacionesDecisor(@PathVariable String emailDecisor, @PathVariable String token){
        List<PuntuacionAlternativa> puntuaciones = puntuacionAlternativaServicio.puntuacionesDecisor(token,emailDecisor);
        if(puntuaciones==null || puntuaciones.size()==0){
            return new ResponseEntity<>(new Mensaje("Sin puntuaciones guardadas"), HttpStatus.NOT_FOUND);

        }
        return ResponseEntity.ok(puntuaciones);

    }





}
