package ufps.ahp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufps.ahp.model.Decisor;
import ufps.ahp.model.DecisorProblema;
import ufps.ahp.model.Problema;
import ufps.ahp.model.dto.DescisorDTO;
import ufps.ahp.security.dto.Mensaje;
import ufps.ahp.security.model.Usuario;
import ufps.ahp.security.servicio.UsuarioService;
import ufps.ahp.services.DecisorProblemaService;
import ufps.ahp.services.DecisorService;
import ufps.ahp.services.ProblemaService;
import ufps.ahp.services.imp.EmailServiceImp;

import javax.mail.MessagingException;
import java.util.List;

@RequestMapping(value= "/decisor",produces = MediaType.APPLICATION_JSON_VALUE)
//@CrossOrigin(origins = "http://angular-ahp.s3-website.us-east-2.amazonaws.com/")
@CrossOrigin
@RestController
public class DecisorRest {

    @Autowired
    DecisorService decisorService;

    @Autowired
    ProblemaService problemaService;
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    DecisorProblemaService decisorProblemaService;

    @Autowired
    EmailServiceImp emailServiceImp;

    @Value("${uribackend}")
    private String urlBackend;

    @Value("${urifrontend}")
    private String urlFrontend;

    @GetMapping
    public ResponseEntity<List<Decisor>> listar() {
        return ResponseEntity.ok(decisorService.listar());
    }

    @GetMapping("/{idProblema}")
    public ResponseEntity<List<Decisor>> decisoresDeProblema(@PathVariable String idProblema) {
        return ResponseEntity.ok(problemaService.decisoresPorProblema(idProblema));
    }

    @PostMapping
    public ResponseEntity<?> agregarDescisor(@RequestBody DescisorDTO descisorDTO) throws MessagingException {

        Problema p = problemaService.buscar(descisorDTO.getProblema());

        Usuario u = usuarioService.findByEmail(descisorDTO.getEmail());

        if(problemaService.existeDecisor(descisorDTO.getEmail(),p.getIdProblema())){
            return new ResponseEntity(new Mensaje("El decisor ya se encuentra registrado en este problema"), HttpStatus.BAD_REQUEST);
        }

        if(u!=null){
            u.setDecisor(new Decisor(descisorDTO.getNombre(), descisorDTO.getEmail()));
        }

        emailServiceImp.enviarEmail("Inscripción descisor problema #"+p.getIdProblema(),

                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Document</title>\n" +
                        "</head>\n" +
                        "\n" +
                        "<body style=\"width: 800px\">\n" +
                        "    <div style=\"background-color: #a5b4fc; width: 100%; padding: 3rem 0;\">\n" +
                        "        <div style=\"text-align: center; background-color: #ffffff; margin: 0 auto; width: 80%; border-radius: 8px;\">\n" +
                        "            <img style=\"margin-top: 3rem; width: 190px\"\n" +
                        "                src=\"https://master.d1oc2nyuhwk984.amplifyapp.com/assets/images/logo.png\" alt=\"logo\">\n" +
                        "            <p style=\"margin: 1rem 0; font-size: 25px;\">Inscripcion problema</p>\n" +
                        "            <p style=\"color: #424242;\">Hola, <b>"+descisorDTO.getNombre()+"</b>, has sido seleccionado para participar como decisor del problema," +p.getDescripcion()+
                        " <br> ingresa al siguiente link para acceder al intrumento:  \n" +
                        "            </p>\n" +
                        "            <div style=\"margin: 2rem auto; width: 120px; background-color: #4f46e5; padding: 8px; border-radius: 6px; \">\n" +
                        "                <a style=\"color: #ffffff; text-decoration: none\" href=\""+urlFrontend+"problem/access/"+p.getIdProblema()+"\">Continuar</a>\n" +
                        "            </div>\n" +
                        "            <div style=\"width: 100%; border-top: 2px solid #a5b4fc; padding: 1rem 0\">\n" +
                        "                <p>Copyright © 2022 Analytic Hierarchy Process <br> Todos los derechos reservados.</p>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>"

                ,descisorDTO.getEmail());

                Decisor de = null;
                if(u!=null)
                    de =new Decisor(descisorDTO.getNombre(),descisorDTO.getEmail(),u);
                else
                    de = (new Decisor(descisorDTO.getNombre(),descisorDTO.getEmail()));
                decisorService.guardar(de);
                decisorProblemaService.guardar(new DecisorProblema(de,p));

        return ResponseEntity.ok(new Mensaje("Inscripción realizada correctamente"));
    }

}
