<%--
  Created by IntelliJ IDEA.
  User: Khang
  Date: 24/06/2015
  Time: 10:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
Company ID :  <%= request.getParameter("company_id") %>
Company name :  <%= request.getParameter("name") %>
</body>
</html>
