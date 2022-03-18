package org.aibles.failwall.user.repository;

import org.aibles.failwall.user.model.UserRole;
import org.aibles.failwall.user.model.compositekey.UserRoleId;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleId> {
}
