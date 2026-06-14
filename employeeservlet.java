package com.ems.servlet;
import com.ems.model.Employee;
import com.ems.util.DBConnection;
import com.ems.util.EmailUtility;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "delete": deleteEmployee(request, response); break;
                case "edit": showEditForm(request, response); break;
                default: listEmployees(request, response); break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("update".equals(action)) {
                updateEmployee(request, response);
            } else {
                insertEmployee(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int page = 1;
        int recordsPerPage = 5;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        
        String sortBy = request.getParameter("sort");
        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";

        List<Employee> list = new ArrayList<>();
        int noOfRecords = 0;

        try (Connection conn = DBConnection.getConnection()) {
            // SQL Pagination and Sorting
            String query = "SELECT SQL_CALC_FOUND_ROWS * FROM employees ORDER BY " + sortBy + " LIMIT ?, ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, (page - 1) * recordsPerPage);
            ps.setInt(2, recordsPerPage);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("department"), rs.getDouble("salary")));
            }
            
            rs = conn.createStatement().executeQuery("SELECT FOUND_ROWS()");
            if (rs.next()) noOfRecords = rs.getInt(1);
        }

        int noOfPages = (int) Math.ceil((double) noOfRecords / recordsPerPage);
        request.setAttribute("employeeList", list);
        request.setAttribute("noOfPages", noOfPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("sort", sortBy);
        
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }

    private void insertEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String dept = request.getParameter("department");
        double salary = Double.parseDouble(request.getParameter("salary"));

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO employees (name, email, department, salary) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, dept);
            ps.setDouble(4, salary);
            ps.executeUpdate();
        }
        
        // Asynchronous/Background logic for Email Notification
        try {
            EmailUtility.sendEmail(email, "Welcome to EMS", "Hello " + name + ",\nYour profile has been created successfully!");
        } catch(Exception e) {
            System.out.println("Email failed to dispatch, configuration required.");
        }

        response.sendRedirect("EmployeeServlet?action=list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee existingEmployee = null;
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                existingEmployee = new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("department"), rs.getDouble("salary"));
            }
        }
        request.setAttribute("employee", existingEmployee);
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String dept = request.getParameter("department");
        double salary = Double.parseDouble(request.getParameter("salary"));

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE employees SET name=?, email=?, department=?, salary=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, dept);
            ps.setDouble(4, salary);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
        response.sendRedirect("EmployeeServlet?action=list");
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int id = Integer.parseInt(request.getParameter("id"));
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        response.sendRedirect("EmployeeServlet?action=list");
    }
}