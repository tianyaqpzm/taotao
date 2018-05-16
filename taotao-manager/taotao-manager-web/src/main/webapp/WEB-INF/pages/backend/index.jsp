<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="../common/bootstrap.jsp" flush="false" />
</head>
<body style="margin-top: 50px;">
<jsp:include page="common/navbar.jsp" flush="false" />
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2" id="sidebar" style="padding: 0;">
            <jsp:include page="common/sidebar.jsp" flush="false" />
        </div>
        <div class="col-sm-9 col-md-10">
            <h3 class="page-header header">主菜单<small>Welcome </small></h3>
            <div class="row">
                <div class="col-md-4">
                    <!--上传文件-->
                    <label class="control-label" for="input-1"></label>
                    <input id = 'input-1' type="file" class=“file-loading”></br>


                    <button type="submit" class="btn btn-primary btn-block" id="vote">新建投票</button>

                    <p id="transaction"></p><br>
                    <span id="status"></span>
                </div>

                <div class="col-md-5">
                    <div>
                        <label for="contractAddress">合约 地址</label>
                        <input type="text" id="contractAddress" placeholder="Contract Address">
                    </div>
                    <div>
                        <form class="form-horizontal">
                            <label for="address1">投票人地址</label>
                            <input type="text" id="address1" placeholder="Vote Address"><br>

                            <label class="checkbox-inline" id="address2">
                                <input type="checkbox" name ="address" id="inlineCheckbox1" value="0x3da1ed43a912c26254cd7883ebbf5b930d22bb42"> user1
                            </label>
                            <label class="checkbox-inline">
                                <input type="checkbox" name ="address" id="inlineCheckbox2" value="0xfd9dd2937283c322b25b9de6005834c9c2ba8106"> user2
                            </label>
                            <label class="checkbox-inline">
                                <input type="checkbox" name ="address" id="inlineCheckbox3" value="0x98a1352c0613f900a922074036b80f4470620f4f"> user3
                            </label>
                        </form>
                        <button type="submit" class="btn btn-primary" onclick="sendMess()" >通知投票人</button>
                    </div>
                </div>

            </div>

            <div>
                <button id="getCount" onclick="getCount()">getCount</button>
                <button id="listContract" onclick="listContract()">listContract</button>
                <ul id="list">

                </ul>
            </div>

        </div>
    </div>
</div>

<script type="text/javascript" src="../../../public/js/bignumber.js/bignumber.min.js"></script>
<script type="text/javascript" src="../../../public/js/dist/web3.js"></script>
<script type="text/javascript" src="../../../public/js/util.js"></script>
<script type="text/javascript" src="../../../public/js/backend/index.js"></script>
</body>
</html>