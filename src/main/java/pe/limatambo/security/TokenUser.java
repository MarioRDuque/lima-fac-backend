package pe.limatambo.security;

import org.springframework.security.core.authority.AuthorityUtils;
import pe.limatambo.entidades.Usuario;
public class TokenUser extends org.springframework.security.core.userdetails.User {
    private Usuario user;

    public TokenUser(Usuario user) {
        super( user.getUserId(), user.getPassword(), AuthorityUtils.createAuthorityList("ADMIN"));
        this.user = user;
    }

    public Usuario getUsuario() {
        return user;
    }

}
