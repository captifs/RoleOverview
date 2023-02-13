package project.role.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.role.management.dao.PermissionDao;
import project.role.management.model.Permission;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionDao permissionDao;

    @PostMapping
    public void addPermission(@RequestBody Permission permission)throws SQLException {
        permissionDao.addPermission(permission);
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable int id) {
        return permissionDao.getPermissionById(id);
    }

    @GetMapping
    public List<Permission> getPermissions() {
        return permissionDao.getPermissions();
    }

    @DeleteMapping("/{id}")
    public void deletePermission(@PathVariable int id) {
        permissionDao.deletePermission(id);
    }

}