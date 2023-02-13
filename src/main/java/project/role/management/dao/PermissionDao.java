package project.role.management.dao;

import project.role.management.model.Permission;
import project.role.management.model.Role;

import java.util.List;

public interface PermissionDao {

    public void addPermission(Permission permission);
    public Permission getPermissionById(int id);
    public List<Permission> getPermissions();
    public void deletePermission(int id);

}