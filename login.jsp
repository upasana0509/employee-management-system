<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>EMS Login</title>
    <style>body { font-family: Arial; margin: 50px; }</style>
</head>
<body>
    <h2>Employee Management System Login</h2>
    <h4 style="color:red;">${param.error}</h4>
    <form action="LoginServlet" method="post">
        Username: <input type="text" name="username" required><br><br>
        Password: <input type="password" name="password" required><br><br>
        <input type="submit" value="Login">
    </form>
</body>
</html>