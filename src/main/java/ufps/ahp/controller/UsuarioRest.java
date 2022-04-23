package ufps.ahp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Decisor;
import ufps.ahp.model.Problema;
import ufps.ahp.model.dto.DescisorDTO;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.security.model.Usuario;
import ufps.ahp.security.servicio.UsuarioService;
import ufps.ahp.services.DecisorService;
import ufps.ahp.services.EmailSenderService;
import ufps.ahp.services.ProblemaService;
import ufps.ahp.services.imp.EmailServiceImp;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@RequestMapping(value= "/usuario",produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UsuarioRest {

    @Autowired
    UsuarioService usuarioService;


    @Autowired
    EmailServiceImp emailServiceImp;

    @Autowired
    ProblemaService problemaService;

    @Autowired
    DecisorService decisorService;

    @Value("${uribackend}")
    private String urlBackend;

    @Value("${urifrontend}")
    private String urlFrontend;


    @GetMapping("/usuarioPorID/{idUsuario}")
    public ResponseEntity<?> getUsuarioId(@PathVariable int idUsuario){

        Usuario u = usuarioService.getById(idUsuario).orElse(null);

        if(u==null){
            return new ResponseEntity(new Mensaje("Email no encontrado"),HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(u);
    }

    @GetMapping("/usuarioPorEmail/{email}")
    public ResponseEntity<?> getUsuarioByEmail(@PathVariable String email){

        Usuario u = usuarioService.findByEmail(email);

        if(u==null){
            return new ResponseEntity(new Mensaje("Email no encontrado"),HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(u);
    }

    @GetMapping("/problemas/{email}")
    public ResponseEntity<?> getProblemasByUsuario(@PathVariable String email){

        Usuario u = usuarioService.findByEmail(email);

        if(u==null){
            return new ResponseEntity(new Mensaje("Email no encontrado"),HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(u.problemaCollection());
    }



    @GetMapping
    public ResponseEntity<?> listar(){
        return ResponseEntity.ok(usuarioService.listar());
    }


    @PutMapping
    public ResponseEntity<?> editar(@RequestBody @Valid Usuario usuario, BindingResult br){

        if(br.hasErrors()){
            return new ResponseEntity<>(br.getAllErrors(),HttpStatus.BAD_REQUEST);
        }

        if(usuario == null){
            return new ResponseEntity(new Mensaje("Datos incorrectos"),HttpStatus.BAD_REQUEST);
        }

        usuarioService.guardar(usuario);
        return ResponseEntity.ok(new Mensaje("Usuario editado"));
    }

}
