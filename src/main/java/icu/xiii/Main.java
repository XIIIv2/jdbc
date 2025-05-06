package icu.xiii;

import icu.xiii.dao.EmployeeDAO;
import icu.xiii.entity.Employee;

public class Main {

    private static EmployeeDAO employeeDAO;

    public static void main(String[] args) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.connect();
        databaseConnector.initDatabase();

        employeeDAO = new EmployeeDAO(databaseConnector);

        printEmployeesList();

        Employee employee;

        getOutput("Find employee by id #1");
        employee = employeeDAO.findById(1);
        if (employee != null) {
            getOutput(employee.toString());
        }

        getOutput("Create new employee");
        employee = new Employee("Carl", 25, "Seller", 1000);
        if (employeeDAO.create(employee)) {
            getOutput("Created!");
            getOutput(employee.toString());
        }

        getOutput("Update employee");
        employee.setSalary(1100);
        if (employeeDAO.update(employee)) {
            getOutput("Updated!");
            getOutput(employee.toString());
        }

        printEmployeesList();

        getOutput("Delete employee");
        if (employeeDAO.delete(employee)) {
            getOutput("Deleted!");
            getOutput(employee.toString());
        }

        printEmployeesList();

        databaseConnector.close();
    }

    private static void getOutput(String output) {
        System.out.println(output);
    }

    private static void printEmployeesList() {
        getOutput("Employees list:");
        employeeDAO.findAll().forEach(employee -> {
            getOutput(employee.toString());
        });
    }
}
