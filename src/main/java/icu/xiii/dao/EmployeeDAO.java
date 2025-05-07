package icu.xiii.dao;

import icu.xiii.DatabaseConnector;
import icu.xiii.entity.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private final String TABLE = "employees";
    private final DatabaseConnector connector;

    public EmployeeDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public Employee findById(int id) {
        String query = "SELECT id, name, age, position, salary FROM `" + TABLE + "` WHERE id = ?";
        Employee employee = null;
        try (PreparedStatement pstmt = connector.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("position"),
                        resultSet.getFloat("salary")
                );
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return employee;
    }

    public List<Employee> findAll() {
        String query = "SELECT id, name, age, position, salary FROM `" + TABLE + "`";
        List<Employee> result = new ArrayList<>();
        try (Statement stmt = connector.getConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("position"),
                        resultSet.getFloat("salary")
                );
                result.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return result;
    }

    public boolean create(Employee employee) {
        String query = "INSERT INTO `" + TABLE + "` (name, age, position, salary) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connector.getConnection().prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setFloat(4, employee.getSalary());
            pstmt.execute();
            try (Statement stmt = connector.getConnection().createStatement()) {
                ResultSet resultSet = stmt.executeQuery("SELECT @@IDENTITY as id");
                resultSet.next();
                employee.setId(resultSet.getInt("id"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(Employee employee) {
        String query = "DELETE FROM `" + TABLE + "` WHERE id = ?";
        try (PreparedStatement pstmt = connector.getConnection().prepareStatement(query)) {
            pstmt.setInt(1, employee.getId());
            pstmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Employee employee) {
        if (findById(employee.getId()) == null) {
            return false;
        }
        String query = "UPDATE `" + TABLE + "` SET name = ?, age = ?, position = ?, salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = connector.getConnection().prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setFloat(4, employee.getSalary());
            pstmt.setInt(5, employee.getId());
            pstmt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return false;
    }
}
