package vn.vifo.logging.repository;

import vn.vifo.logging.entity.Role;
import vn.vifo.logging.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(Constants.RoleEnum name);
}
