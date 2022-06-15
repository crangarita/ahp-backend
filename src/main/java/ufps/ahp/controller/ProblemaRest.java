package ufps.ahp.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.dao.PuntuacionAlternativaCriterioDAO;
import ufps.ahp.model.*;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.security.model.Usuario;
import ufps.ahp.security.servicio.UsuarioService;
import ufps.ahp.services.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@CrossOrigin
@RequestMapping(value="/problema" ,produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j

public class ProblemaRest {

    @Autowired
    ProblemaService problemaService;

    @Autowired
    CriterioService criterioService;

    @Autowired
    AlternativaService alternativaService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PuntuacionCriterioServicio puntuacionCriterioServicio;

    @Autowired
    PuntuacionAlternativaCriterioServicio puntuacionAlternativaCriterioServicio;

    @Autowired
    DecisorService decisorService;

    @Autowired
    PuntuacionAlternativaCriterioDAO puntuacionAlternativaCriterioDAO;


    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(problemaService.listar());
    }

    @CrossOrigin
    @GetMapping(path = "/{token}")
    public ResponseEntity<?> encontrarProblema(@PathVariable String token){
        return ResponseEntity.ok(problemaService.buscar(token));
    }

    @CrossOrigin
    @GetMapping(path = "/{id}/id")
    public ResponseEntity<?> encontrarProblemaId(@PathVariable int id){
        return ResponseEntity.ok(problemaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody @Valid Problema problema, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        if(problema == null){
            return new ResponseEntity(new Mensaje("Datos incorrectos"),HttpStatus.BAD_REQUEST);
        }
        problema.setToken(UUID.randomUUID().toString());
        problema.setEstado(true);
        problema.setFechaCreacion(new Date());
        problemaService.guardar(problema);
        return ResponseEntity.ok(problema);
    }

    @PostMapping(path="/criterios/{token}")
    public ResponseEntity<?> agregarCriteriosDeProblema(@PathVariable String token, @RequestBody List<Criterio> criterios){

        Problema p = problemaService.buscar(token);
        if(p.criterioCollection().size()==0){
            for(Criterio c: criterios){
                c.setProblema(p);
                criterioService.guardar(c);
            }
            puntuacionCriterioServicio.agregarCriteriosPuntuacion(p.getIdProblema());
        }else{
            for(Criterio c1: criterios){
                c1.setProblema(p);
                Criterio c = criterioService.guardar(c1);
                puntuacionAlternativaCriterioDAO.llenarPuntuacionAlternativa(p.getIdProblema(),c.getIdCriterio());

            }
            problemaService.guardar(p);
            p = problemaService.buscar(token);

            for(Criterio c: criterios){
                puntuacionCriterioServicio.agregarCriteriosPuntuacionIndividual(c.getIdCriterio(), criterios);
            }

            }
        return ResponseEntity.ok(criterios);
    }

    @PostMapping(path="/alternativas/{token}")
    public ResponseEntity<?> agregarAlternativasDeProblema(@PathVariable String token, @RequestBody List<Alternativa> alternativas){
        Problema p = problemaService.buscar(token);

        if(p.alternativaCollection().size()==0){
            for(Alternativa alt: alternativas){
                alt.setProblema(p);
                alternativaService.guardar(alt);
            }
            puntuacionAlternativaCriterioServicio.llenarPuntuacionAlternativa(p.getIdProblema());
        }else{
            for(Alternativa alt1: alternativas){
                alt1.setProblema(p);
                alternativaService.guardar(alt1);
            }

            for(Alternativa alt: alternativas){
                puntuacionAlternativaCriterioServicio.llenarPuntuacionAlternativaIndividual(alt.getIdAlternativa(), alternativas);
            }
        }

        return ResponseEntity.ok(alternativas);
    }

    @DeleteMapping(path = "/{token}")
    public ResponseEntity<?> eliminarProblema(@PathVariable String token){

        Problema p = problemaService.buscar(token);
        if(p == null){
            return new ResponseEntity(new Mensaje("El problema no existe"),HttpStatus.BAD_REQUEST);
        }
        p.setEstado(false);
        problemaService.guardar(p);

        return ResponseEntity.ok(new Mensaje("Problema #"+p.getIdProblema()+" deshabilitado"));
    }

    @GetMapping(path = "/{token}/activar")
    public ResponseEntity<?> activarProblema(@PathVariable String token){

        Problema p = problemaService.buscar(token);
        if(p == null){
            return new ResponseEntity(new Mensaje("El problema no existe"),HttpStatus.BAD_REQUEST);
        }
        p.setEstado(true);
        problemaService.guardar(p);

        return ResponseEntity.ok(new Mensaje("Problema #"+p.getIdProblema()+" deshabilitado"));
    }

    @PutMapping
    public ResponseEntity<?> editar(@RequestBody @Valid Problema problema, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        if(problema == null){
            return new ResponseEntity(new Mensaje("Datos incorrectos"),HttpStatus.BAD_REQUEST);
        }

        problema.setUsuario(usuarioService.getById(problema.getUsuario().getIdUsuario()).orElse(null));
        problemaService.guardar(problema);
        return ResponseEntity.ok("Problema creado");
    }

    @GetMapping(path ="/criterios/{token}")
    public ResponseEntity<?> criteriorPorProblema(@PathVariable String token){
        return ResponseEntity.ok(problemaService.buscar(token).criterioCollection());
    }

    @GetMapping(path ="/alternativas/{token}")
    public ResponseEntity<?> alternativaPorProblema(@PathVariable String token){
        return ResponseEntity.ok(problemaService.buscar(token).alternativaCollection());
    }

    @GetMapping(path ="/decisores/{token}")
    public ResponseEntity<?> decisoresPorProblema(@PathVariable String token){
        return ResponseEntity.ok(problemaService.buscar(token).decisorProblemas());
    }

    @GetMapping(path="/criteriosComparados/{idProblema}") // Metodo para obtener los pares a comparar en la puntuacion de criterios
    public ResponseEntity<?> obtenerParesCriterios(@PathVariable String idProblema){
        Problema p = problemaService.buscar(idProblema);
        return ResponseEntity.ok(puntuacionCriterioServicio.obtenerParesCriterios(idProblema));
    }

    @GetMapping(path="/alternativaComparadas/{idProblema}") // Metodo para obtener los pares a comparar en la puntuacion de criterios
    public ResponseEntity<?> obtenerParesAlternativas(@PathVariable String idProblema){
        Problema p = problemaService.buscar(idProblema);
        return ResponseEntity.ok(puntuacionCriterioServicio.obtenerParesAlternativa(idProblema));
    }

    @GetMapping(path ="/accesoproblema/{token}/{emailDecisor}")
    public ResponseEntity<?> accesoAProblemaDecisor(@PathVariable String token, @PathVariable String emailDecisor){
        Problema p = problemaService.buscar(token);

        if(p==null) return new ResponseEntity<>(new Mensaje("El problema no existe"), HttpStatus.NOT_FOUND);

        Decisor d = decisorService.buscarDecisorProblema(token,emailDecisor).orElse(null);

        if(p.getFechaFinalizacion().before(new Date())){
            return new ResponseEntity<>(new Mensaje("La calificacion de este problema ha finalizado"), HttpStatus.BAD_REQUEST);
        }

        if(d == null){
            return new ResponseEntity<>(new Mensaje("El decisor no tiene acceso al problema seleccionado"), HttpStatus.UNAUTHORIZED);
        }


        return ResponseEntity.ok(new Mensaje("Bienvenido al problema"));
    }
}
