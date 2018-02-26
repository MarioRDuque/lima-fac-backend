package pe.limatambo.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.limatambo.entidades.Usuario;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findOneByUserId(String userId);
    Optional<Usuario> findOneByUserIdAndPassword(String userId, String password);
}

