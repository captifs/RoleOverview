package project.role.management.dao;

import org.springframework.jdbc.core.RowMapper;
import project.role.management.model.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PermissionRowMapper implements RowMapper<Permission> {
    @Override
    public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
        Permission permission = new Permission();
        permission.setId(rs.getInt("id"));
        permission.setName(rs.getString("name"));
        permission.setDescription(rs.getString("description"));
        return permission;
    }
}