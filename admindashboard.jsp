<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.ems.model.Employee" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
    <h2>Admin Dashboard - Employee Operations</h2>
    
    <h3>${employee != null ? "Edit Employee" : "Add New Employee"}</h3>
    <form action="EmployeeServlet" method="post">
        <input type="hidden" name="action" value="${employee != null ? 'update' : 'insert'}">
        <input type="hidden" name="id" value="${employee != null ? employee.id : ''}">
        Name: <input type="text" name="name" value="${employee != null ? employee.name : ''}" required><br><br>
        Email: <input type="email" name="email" value="${employee != null ? employee.email : ''}" required><br><br>
        Department: <input type="text" name="department" value="${employee != null ? employee.department : ''}"><br><br>
        Salary: <input type="number" step="0.01" name="salary" value="${employee != null ? employee.salary : ''}" required><br><br>
        <input type="submit" value="${employee != null ? 'Update Employee' : 'Add Employee'}">
    </form>

    <hr>

    <h3>Employee Directory</h3>
    <table>
        <tr>
            <th><a href="EmployeeServlet?sort=id">ID</a></th>
            <th><a href="EmployeeServlet?sort=name">Name</a></th>
            <th>Email</th>
            <th><a href="EmployeeServlet?sort=department">Department</a></th>
            <th><a href="EmployeeServlet?sort=salary">Salary</a></th>
            <th>Actions</th>
        </tr>
        <%
            List<Employee> list = (List<Employee>) request.getAttribute("employeeList");
            if (list != null) {
                for (Employee emp : list) {
        %>
        <tr>
            <td><%= emp.getId() %></td>
            <td><%= emp.getName() %></td>
            <td><%= emp.getEmail() %></td>
            <td><%= emp.getDepartment() %></td>
            <td>$<%= emp.getSalary() %></td>
            <td>
                <a href="EmployeeServlet?action=edit&id=<%= emp.getId() %>">Edit</a> | 
                <a href="EmployeeServlet?action=delete&id=<%= emp.getId() %>" onclick="return confirm('Delete record?')">Delete</a>
            </td>
        </tr>
        <%      }
            }
        %>
    </table>

    <br>
    <div>
        <% 
            int currentPage = (Integer) request.getAttribute("currentPage");
            int noOfPages = (Integer) request.getAttribute("noOfPages");
            String sort = (String) request.getAttribute("sort");
            
            for (int i = 1; i <= noOfPages; i++) {
                if (i == currentPage) {
        %>
                    <strong><%= i %></strong>
        <%      } else { %>
                    <a href="EmployeeServlet?page=<%= i %>&sort=<%= sort %>"><%= i %></a>
        <%      }
            }
        %>
    </div>
</body>
</html>