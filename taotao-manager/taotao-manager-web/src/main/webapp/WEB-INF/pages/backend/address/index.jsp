<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="../../common/bootstrap.jsp" flush="false" />
    <link rel="stylesheet" href="../../../../public/css/fileinput/fileinput.css">
    <script type="text/javascript" src="../../../../public/js/util.js"></script>

    <script type="text/javascript" src="../../../../public/js/fileinput/fileinput.js"></script>
    <script type="text/javascript" src="../../../../public/js/fileinput/locales/zh.js"></script>

    <script type="text/javascript" src="../../../../public/js/bignumber.js/bignumber.min.js"></script>
    <script type="text/javascript" src="../../../../public/js/web3.js"></script>


</head>
<body style="margin-top: 50px;">
<jsp:include page="../common/navbar.jsp" flush="false" />
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2" id="sidebar" style="padding: 0;">
            <jsp:include page="../common/sidebar.jsp" flush="false" />
        </div>
        <div class="col-sm-9 col-md-10">
            <h3>Hi, ${lis.name}</h3>
            <div class="col-sm-3 col-md-4">
                <table class="table">
                    <caption>基本的账户</caption>
                    <thead>
                        <tr>
                            <th>Address</th>
                            <th>Transaction</th>
                            <th>Processor</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="li">

                    <tr>
                        <td>${li.address}</td>
                        <td>${li.transaction}</td>
                        <td>${li.userName}</td>
                        <td>${li.createTime}</td>
                    </tr>
                    </c:forEach>

                    </tbody>
                </table>

                <button class="btn btn-default" id="createAddress" >创建上传账户地址</button>


                <div class="panel panel-default" id="register" style="display: none">
                    <div class="panel-heading">
                        <p class="tc">注册信息</p>
                    </div>
                    <div class="panel-body m15">
                        <div class="form-group">
                            <div class="input-group">
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-user"></span>
                        </span>
                                <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户名" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-lock"></span>
                        </span>
                                <input type="text" class="form-control" id="password" name="password" placeholder="请输入密码" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-lock"></span>
                        </span>
                                <input type="text" class="form-control" id="password1" name="password1" placeholder="请再次输入密码" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary btn-block" id="register1">注册</button>
                        </div>

                    </div>
                </div>
            </div>
            <div class="col-sm-3 col-md-4">
                <button class="btn btn-default" id="exportKey">导出秘钥文件</button>
            </div>


        </div>
    </div>
</div>
<script src="../../../../public/js/keythereum.js" type="text/javascript"></script>
<script type="text/javascript" src="../../../../public/js/backend/address.js"></script>

</body>
</html>