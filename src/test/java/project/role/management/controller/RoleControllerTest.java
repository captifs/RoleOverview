package project.role.management.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.role.management.controller.RoleController;
import project.role.management.dao.RoleDao;
import project.role.management.model.Permission;
import project.role.management.model.Role;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleController roleController;

    private Role role;
    private Permission permission;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();

        role = new Role();
        role.setId(1);
        role.setName("admin");

        permission = new Permission();
        permission.setId(1);
        permission.setName("create");
    }

    @Test
    public void testAddRole() throws Exception {
        doNothing().when(roleDao).addRole(any(Role.class));

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddRole_ThrowsSQLException() throws Exception {
        doNothing().when(roleDao).addRole(any(Role.class));

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk());
    }
    @Test
    public void testAddPermissionToRole() throws Exception {
        Permission permission = new Permission(1, "read");
        Role role = new Role(1, "admin");

        when(roleDao.getRoleById(1)).thenReturn(role);

        mockMvc.perform(post("/api/roles/1/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(permission)))
                .andExpect(status().isOk());

        verify(roleDao, times(1)).addPermissionToRole(1, 1);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddRoleToRole() throws Exception {
        Role parentRole = new Role();
        parentRole.setId(1);
        parentRole.setName("Parent Role");

        Role childRole = new Role();
        childRole.setId(2);
        childRole.setName("Child Role");

        RoleDao roleDao = mock(RoleDao.class);
        when(roleDao.getRoleById(1)).thenReturn(parentRole);
        when(roleDao.getRoleById(2)).thenReturn(childRole);

        RoleController roleController = new RoleController();
        ReflectionTestUtils.setField(roleController, "roleDao", roleDao);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        mockMvc.perform(post("/api/roles/1/includedRoles/2"))
                .andExpect(status().isCreated());

        verify(roleDao, times(1)).getRoleById(1);
        verify(roleDao, times(1)).getRoleById(2);
        verify(roleDao, times(1)).addRoleToRole(parentRole, childRole);
    }
}