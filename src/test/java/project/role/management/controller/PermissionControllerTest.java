package project.role.management.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.role.management.dao.PermissionDao;
import project.role.management.model.Permission;

@RunWith(MockitoJUnitRunner.class)
public class PermissionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PermissionDao permissionDao;

    @InjectMocks
    private PermissionController permissionController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController).build();
    }

    @Test
    public void testAddPermission() throws Exception {
        Permission permission = new Permission();
        permission.setId(1);
        permission.setName("Permission1");

        mockMvc.perform(post("/api/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(permission)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPermissionById() throws Exception {
        Permission permission = new Permission();
        permission.setId(1);
        permission.setName("Permission1");

        when(permissionDao.getPermissionById(1)).thenReturn(permission);

        mockMvc.perform(get("/api/permissions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPermissions() throws Exception {
        List<Permission> permissions = new ArrayList<>();
        Permission permission1 = new Permission();
        permission1.setId(1);
        permission1.setName("Permission1");
        Permission permission2 = new Permission();
        permission2.setId(2);
        permission2.setName("Permission2");
        permissions.add(permission1);
        permissions.add(permission2);

        when(permissionDao.getPermissions()).thenReturn(permissions);
        List<Permission> result = permissionController.getPermissions();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(permissionDao, times(1)).getPermissions();
    }
}
