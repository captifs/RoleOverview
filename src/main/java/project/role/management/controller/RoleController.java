package project.role.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.role.management.dao.RoleDao;
import project.role.management.model.Permission;
import project.role.management.model.Role;

import java.sql.SQLException;
import java.util.List;
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleDao roleDao;

    @PostMapping
    public void addRole(@RequestBody Role role) throws SQLException {
        roleDao.addRole(role);
    }
    @PostMapping("/{roleId}/permissions")
    public void addPermissionToRole(@PathVariable int roleId, @RequestBody Permission permission) {
        roleDao.addPermissionToRole(roleId, permission.getId());
    }

    @PostMapping("/{parentRoleId}/includedRoles/{childRoleId}")
    public ResponseEntity<Void> addRoleToRole(@PathVariable int parentRoleId, @PathVariable int childRoleId) {
        Role parentRole = roleDao.getRoleById(parentRoleId);
        Role childRole = roleDao.getRoleById(childRoleId);

        roleDao.addRoleToRole(parentRole, childRole);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable int id) {
        return roleDao.getRoleById(id);
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleDao.getRoles();
    }

    @GetMapping("/{roleId}/permissions")
    public List<Permission> getPermissionsByRoleId(@PathVariable int roleId) {
        return roleDao.getPermissionsByRoleId(roleId);
    }

    @GetMapping("/{roleId}/roles")
    public List<Role> getChildRolesByRoleId(@PathVariable int roleId) {
        return roleDao.getChildRolesByRoleId(roleId);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Void> updateRole(@PathVariable int roleId, @RequestBody Role role) {
        Role currentRole = roleDao.getRoleById(roleId);
        if (currentRole == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentRole.setName(role.getName());
        roleDao.updateRole(currentRole);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}