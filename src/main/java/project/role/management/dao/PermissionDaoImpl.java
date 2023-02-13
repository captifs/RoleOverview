package project.role.management.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import project.role.management.model.Permission;

import java.util.List;

@Repository
public class PermissionDaoImpl implements PermissionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addPermission(Permission permission) {
        String sql = "INSERT INTO permission (name, description) VALUES (?, ?)";
        jdbcTemplate.update(sql, permission.getName(), permission.getDescription());
    }
    @Override
    public Permission getPermissionById(int id) {
        String sql = "SELECT * FROM permission WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PermissionRowMapper());
    }
    @Override
    public List<Permission> getPermissions() {
        String sql = "SELECT * FROM permission";
        return jdbcTemplate.query(sql, new PermissionRowMapper());
    }

    @Override
    public void deletePermission(int id){
        String sql = "DELETE FROM permission WHERE id=?";
        jdbcTemplate.update(sql,id);
    }

}