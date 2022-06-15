package ufps.ahp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.*;
import ufps.ahp.negocio.PlayGround;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.services.DecisorService;
import ufps.ahp.services.ProblemaService;
import ufps.ahp.services.PuntuacionCriterioServicio;
import ufps.ahp.services.PuntuacionServicio;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value="/puntuacioncriterio" ,produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PuntuacionCriterioRest {

    @Autowired
    PuntuacionServicio puntuacionServicio;

    @Autowired
    PuntuacionCriterioServicio puntuacionCriterioServicio;

    @Autowired
    ProblemaService problemaService;

    @Autowired
    DecisorService decisorService;

    @Autowired
    PlayGround playGround;

    @PostMapping()
    public ResponseEntity<?>guardarPuntuacionCriterio(@RequestBody List<Puntuacion> puntuaciones){
        // TODO: Yo espero como valor el puntaje del criterio 1, osea que si el criterio 2 tiene puntuacion "4", yo recibo 1/4
        for(Puntuacion puntuacion: puntuaciones) {
            Puntuacion puntuacionAnteriorRealizada = puntuacionServicio.buscarPuntuacionDecisorProblema
                    (puntuacion.getPuntuacionCriterio().getIdPuntuacionDecisor(), puntuacion.decisor().getEmail());
            if(puntuacionAnteriorRealizada==null){
                Decisor d = decisorService.buscarPorEmail(puntuacion.decisor().getEmail());
                puntuacion.setDecisor(d);

                PuntuacionCriterio puntuacionCriterio = puntuacionCriterioServicio.buscar(puntuacion.getPuntuacionCriterio().getIdPuntuacionDecisor());
                puntuacion.setPuntuacionCriterio(puntuacionCriterio);
                puntuacionServicio.guardar(puntuacion);

            } else{
                puntuacionAnteriorRealizada.setValor(puntuacion.getValor());
                puntuacionServicio.guardar(puntuacionAnteriorRealizada);

            }

        }
        return ResponseEntity.ok(new Mensaje("Puntuación registrada"));
    }


    @GetMapping("/{emailDecisor}/{token}")
    public ResponseEntity<?>calcularVectorPropio(@PathVariable String emailDecisor, @PathVariable String token){
        Problema problema = problemaService.buscar(token);
        List<Object> resultados  = puntuacionServicio.calcularMatriz(emailDecisor,token,problema); // 0=proceso, 1=matriz pareada string (1/n), 2=matriz pareada numerico
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/puntuacionesanteriores/{emailDecisor}/{token}")
    public ResponseEntity<?>puntuacionesDecisor(@PathVariable String emailDecisor, @PathVariable String token){
        List<Puntuacion> puntuaciones = puntuacionServicio.puntuacionesDecisor(token,emailDecisor);
        if(puntuaciones==null || puntuaciones.size()==0){
            return new ResponseEntity<>(new Mensaje("Sin puntuaciones guardadas"), HttpStatus.NOT_FOUND);

        }
        return ResponseEntity.ok(puntuaciones);

    }

    @GetMapping("/{token}/totalizar")
    public ResponseEntity<?>totalizarCriterios(@PathVariable String token){
        Problema problema = problemaService.buscar(token);
        List<Object> resultados  = puntuacionServicio.totalizarMatriz(token,problema);
        if(resultados ==null){
            return new ResponseEntity(new Mensaje("El problema no ha sido calificado por ningún decisor"),HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(resultados);

    }


}
