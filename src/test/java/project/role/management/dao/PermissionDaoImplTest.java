package project.role.management.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import project.role.management.dao.PermissionDao;
import project.role.management.dao.PermissionDaoImpl;
import project.role.management.model.Permission;

@RunWith(SpringRunner.class)
public class PermissionDaoImplTest {

    @TestConfiguration
    static class PermissionDaoImplTestContextConfiguration {
        @Bean
        public PermissionDao permissionDao() {
            return new PermissionDaoImpl();
        }
    }

    @Autowired
    private PermissionDao permissionDao;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    private Permission permission;
    private List<Permission> permissions;

    @Before
    public void setUp() {
        permission = new Permission("create", "create a resource");
        permissions = List.of(permission);

        Mockito.when(jdbcTemplate.update(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1);
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.any(Object[].class),
                Mockito.any(PermissionRowMapper.class))).thenReturn(permission);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(PermissionRowMapper.class)))
                .thenReturn(permissions);
    }

    @Test
    public void whenAddPermission_thenPermissionIsAdded() {
        permissionDao.addPermission(permission);

        Mockito.verify(jdbcTemplate, Mockito.times(1)).update(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString());
    }

    @Test
    public void whenGetPermissionById_thenPermissionIsReturned() {
        Permission found = permissionDao.getPermissionById(1);

        assertNotNull(found);
        assertEquals(permission.getName(), found.getName());
        assertEquals(permission.getDescription(), found.getDescription());
    }

    @Test
    public void whenGetPermissions_thenPermissionsAreReturned() {
        List<Permission> permissionList = permissionDao.getPermissions();
        assertNotNull(permissionList);
        assertFalse(permissionList.isEmpty());
    }

}