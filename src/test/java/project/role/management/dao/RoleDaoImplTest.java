package project.role.management.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import project.role.management.model.Permission;
import project.role.management.model.Role;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleDaoImplTest {
    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private Role role;

    @Test
    public void testAddRole() throws SQLException {
        roleDao.addRole(role);
        verify(roleDao, times(1)).addRole(role);
    }

    @Test
    public void testAddPermissionToRole() throws SQLException {
        int roleId = 1;
        int permissionId = 2;
        roleDao.addPermissionToRole(roleId, permissionId);
        verify(roleDao, times(1)).addPermissionToRole(roleId, permissionId);
    }

    @Test
    public void testAddRoleToRole() throws SQLException {
        Role parentRoleId = new Role();
        Role childRoleId = new Role();
        roleDao.addRoleToRole(parentRoleId, childRoleId);
        verify(roleDao, times(1)).addRoleToRole(parentRoleId, childRoleId);
    }

    @Test
    public void testGetRoleById() throws SQLException {
        int id = 1;
        when(roleDao.getRoleById(id)).thenReturn(role);
        Role result = roleDao.getRoleById(id);
        assertEquals(role, result);
        verify(roleDao, times(1)).getRoleById(id);
    }

    @Test
    public void testGetRoles() throws SQLException {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        when(roleDao.getRoles()).thenReturn(roles);
        List<Role> result = roleDao.getRoles();
        assertEquals(roles, result);
        verify(roleDao, times(1)).getRoles();
    }

    @Test
    public void testGetPermissionsByRoleId() throws SQLException {
        int roleId = 1;
        List<Permission> permissions = new ArrayList<>();
        when(roleDao.getPermissionsByRoleId(roleId)).thenReturn(permissions);
        List<Permission> result = roleDao.getPermissionsByRoleId(roleId);
        assertEquals(permissions, result);
        verify(roleDao, times(1)).getPermissionsByRoleId(roleId);
    }
}