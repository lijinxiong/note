<%--
  Created by IntelliJ IDEA.
  User: jinxiong
  Date: 2017/3/3
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <script type="text/javascript">
        function refresh() {
            document.getElementById("image").src = "/servlet/ImageServlet?" + Math.random();
            //保证src值每次不一样，浏览器有缓冲，导致不能更新
        }

    </script>
    <title>Title</title>
</head>
<body>
<img src="/servlet/ImageServlet" onclick="refresh()" id="image">
</body>
</html>
