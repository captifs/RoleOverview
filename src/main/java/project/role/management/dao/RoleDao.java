package project.role.management.dao;

import project.role.management.model.Permission;
import project.role.management.model.Role;

import java.sql.SQLException;
import java.util.List;

public interface RoleDao {
    public void addRole(Role role) throws SQLException;
    public void addPermissionToRole(int roleId, int permissionId);
    public void addRoleToRole(Role parentRoleId, Role childRoleId);
    public Role getRoleById(int id);
    public List<Role> getRoles();
    public List<Permission> getPermissionsByRoleId(int roleId);
    public List<Role> getChildRolesByRoleId(int roleId);
    public void updateRole(Role role);
    public void deleteRole(String name);
}