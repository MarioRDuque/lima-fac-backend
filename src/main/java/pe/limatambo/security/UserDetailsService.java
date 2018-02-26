package pe.limatambo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import pe.limatambo.dao.UsuarioDao;

import pe.limatambo.entidades.Usuario;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsuarioDao usuarioDao;
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TokenUser loadUserByUsername(String username) throws UsernameNotFoundException, DisabledException {

        final Usuario user = usuarioDao.findOneByUserId(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        TokenUser currentUser;
        if (user.getEstado()){
            currentUser = new TokenUser(user);
        }
        else{
            throw new DisabledException("User is not activated (Disabled User)");
        }
        detailsChecker.check(currentUser);
        return currentUser;
    }
}
