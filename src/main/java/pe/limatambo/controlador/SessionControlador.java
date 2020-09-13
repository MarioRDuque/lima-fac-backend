package pe.limatambo.controlador;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import pe.limatambo.dao.UsuarioDao;
import pe.limatambo.dto.LoginDTO;
import pe.limatambo.entidades.Usuario;
import pe.limatambo.dto.SessionItemDTO;
import pe.limatambo.excepcion.GeneralException;
import pe.limatambo.servicio.MenuServicio;
import pe.limatambo.util.SessionResponse;
import pe.limatambo.util.Mensaje;
import pe.limatambo.util.Respuesta.EstadoOperacionEnum;

/*
 This is a dummy rest controller, for the purpose of documentation (/session) path is map to a filter
 - This will only be invoked if security is disabled
 - If Security is enabled then SessionFilter.java is invoked
 - Enabling and Disabling Security is done at config/applicaton.properties 'security.ignored=/**'
 */
@RestController
public class SessionControlador {
    
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private MenuServicio menuServicio;

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public ResponseEntity newSession(@RequestBody LoginDTO login, HttpServletRequest request) throws GeneralException {
        Usuario usuario = usuarioDao.findOneByUserIdAndPassword(login.getUsername(), login.getPassword()).orElse(null);
        SessionResponse resp = new SessionResponse();
        SessionItemDTO sessionItem = new SessionItemDTO();
        if (usuario!=null) {
            sessionItem.setToken("xxx.xxx.xxx");
            sessionItem.setUsuarioId(usuario.getUserId());
            sessionItem.setNombre(usuario.getNombre());
            sessionItem.setTipoUsuario(usuario.getTipousuario().getId());
            sessionItem.setTipoNombreUsuario(usuario.getTipousuario().getNombre());
            sessionItem.setMenus(menuServicio.listarPorTipoDeUsuario(usuario.getTipousuario().getId()));
            resp.setEstadoOperacion(EstadoOperacionEnum.EXITO.getValor());
            resp.setOperacionMensaje(Mensaje.OPERACION_CORRECTA);
            resp.setItem(sessionItem);
        }
        else{
            throw new BadCredentialsException("Usuario o Contraseña Incorreta");
        }
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
