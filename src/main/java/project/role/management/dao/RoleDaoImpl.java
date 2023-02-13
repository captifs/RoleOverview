package project.role.management.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import project.role.management.model.Permission;
import project.role.management.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addRole(Role role) {
        // Check if the role already exists
        if (!checkIfRoleExists(role.getName())) {
            // Check if there's a cycle in the Role-Role mapping
            if (!checkForCycle(role)) {
                // Add the role to the database
                jdbcTemplate.update("INSERT INTO role (name, description) VALUES (?,?)",
                        role.getName(), role.getDescription());
                // Add the Role-Role mapping
                for (Role includedRole : role.getChildRoles()) {
                    jdbcTemplate.update("INSERT INTO role_role (parent_role_id, child_role_id) VALUES (?,?)",
                            role.getId(), includedRole.getId());
                }
            } else {
                throw new RuntimeException("Cycle detected in the Role-Role mapping");
            }
        } else {
            throw new RuntimeException("Role already exists");
        }
    }

    @Override
    public void updateRole(Role role) {
        String sql = "UPDATE role SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, role.getName(), role.getId());
    }

    @Override
    public void deleteRole(String name) {
        // Check if the role exists
        if (checkIfRoleExists(name)) {
            // Delete the Role-Role mapping
            jdbcTemplate.update("DELETE FROM role_role WHERE parent_role_id = (SELECT id FROM role WHERE name = ?)", name);
            // Delete the role from the database
            jdbcTemplate.update("DELETE FROM role WHERE name = ?", name);
        } else {
            throw new RuntimeException("Role doesn't exist");
        }
    }

    @Override
    public void addPermissionToRole(int roleId, int permissionId) {
        String checkSql = "SELECT COUNT(*) FROM role_permission WHERE role_id = ? AND permission_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[] { roleId, permissionId }, Integer.class);
        if (count == 0) {
            String sql = "INSERT INTO role_permission (role_id, permission_id) VALUES (?, ?)";
            jdbcTemplate.update(sql, roleId, permissionId);
        } else {
            throw new RuntimeException("Permission already exists for role");
        }
    }
    public void addRoleToRole(Role parentRole, Role childRole) {
        if (checkForCycle(parentRole)) {
            throw new RuntimeException("Adding this role mapping will result in a cycle");
        }
        jdbcTemplate.update("INSERT INTO role_role (parent_role_id, child_role_id) VALUES (?, ?)",
                parentRole.getId(), childRole.getId());
    }

    @Override
    public Role getRoleById(int id) {
        String sql = "SELECT * FROM role WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RoleRowMapper());
    }
    @Override
    public List<Role> getRoles() {
        String sql = "SELECT * FROM role";
        return jdbcTemplate.query(sql, new RoleRowMapper());
    }
    @Override
    public List<Permission> getPermissionsByRoleId(int roleId) {
        String sql = "SELECT p.* FROM role_permission rp " +
                "JOIN permission p ON rp.permission_id = p.id " +
                "WHERE rp.role_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roleId}, new PermissionRowMapper());
    }
    @Override
    public List<Role> getChildRolesByRoleId(int roleId) {
        String sql = "SELECT r.* FROM role_role rr " +
                "JOIN role r ON rr.child_role_id = r.id " +
                "WHERE rr.parent_role_id = ?";
        return jdbcTemplate.query(sql, new Object[]{roleId}, new RoleRowMapper());
    }

    private boolean checkIfRoleExists(String name) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM role WHERE name=?",
                new Object[] { name }, Integer.class);
        return count > 0;
    }

    private boolean checkForCycle(Role role) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();
        return checkForCycleHelper(role.getId(), visited, recStack);
    }

    private boolean checkForCycleHelper(int roleId, Set<Integer> visited, Set<Integer> recStack) {
        if (recStack.contains(roleId)) {
            return true;
        }
        if (visited.contains(roleId)) {
            return false;
        }
        visited.add(roleId);
        recStack.add(roleId);
        List<Integer> includedRoleIds = jdbcTemplate.queryForList("SELECT child_role_id FROM role_role WHERE parent_role_id=?",
                new Object[] { roleId }, Integer.class);
        for (Integer includedRoleId : includedRoleIds) {
            if (checkForCycleHelper(includedRoleId, visited, recStack)) {
                return true;
            }
        }
        recStack.remove(roleId);
        return false;
    }
}

