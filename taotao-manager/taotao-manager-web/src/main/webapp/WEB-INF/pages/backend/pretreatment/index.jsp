<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE Html>
<html>
<head>
    <jsp:include page="../../common/bootstrap.jsp" flush="false" />
    <link rel="stylesheet" href="../../../../public/css/fileinput/fileinput.css">

</head>
<body style="margin-top: 50px;">
<jsp:include page="../common/navbar.jsp" flush="false" />
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2" id="sidebar" style="padding: 0;">
            <jsp:include page="../common/sidebar.jsp" flush="false" />
        </div>
        <div class="col-sm-9 col-md-10">
            <h3 class="page-header header">主菜单<small>Welcome </small></h3>


            <div class="row">
                <%--当屏幕为小屏幕(768px,992px)时自动使用class="col-sm-3"
                当屏幕为中屏(992px,1200px)时自动使用 class="col-md-3" --%>
                <div class="col-sm-4 col-md-4">
                    <div class="databox">

                        <%--<form class="form-horizontal required-validate" action="${ctx}/save?callbackType=confirmTimeoutForward" enctype="multipart/form-data" method="post" onsubmit="return iframeCallback(this, pageAjaxDone)">--%>

                            <%--<div class="form-group">--%>
                                <%--<label for="input-1" class="col-md-1 control-label">项目封面</label>--%>
                                <%--<div class="col-md-10 tl th">--%>
                                    <input id = 'input-1' type="file" name = "file" class=“file-loading”></br>
                                    <%--&lt;%&ndash;<input type="file" name="image" class="projectfile" value="${deal.image}" />&ndash;%&gt;--%>
                                    <%--<p class="help-block">支持jpg、jpeg、png、gif格式，大小不超过2.0M</p>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group text-center ">--%>
                                <%--<div class="col-md-10 col-md-offset-1">--%>
                                    <%--<button type="submit" class="btn btn-primary btn-lg">保存</button>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</form>--%>
                        <!--上传文件-->
                        <%--<label class="control-label">上传文件：</label>--%>
                    </div>
                </div>
                <div class="col-sm-4 col-md-4">
                    <div class="">
                        <form class="" method="post" action="/backend/pretreatment/cutfile" enctype="">

                                <%--<input type="file" name="file"/>--%>
                                <%--<input type="submit"/>--%>
                            <div>
                                <input type="hidden" value="${fileUrl}" id="fileurl" name="fileurl"/>
                            </div>
                            <div class="form-group">
                                <label for="blockSize">数据块大小</label>
                                <input type="text" class="form-control" id="blockSize" name="blockSize">
                            </div>
                            <div class="form-group">
                                <label for="sectorSize">数据段大小</label>
                                <input type="text" class="form-control" id="sectorSize" name="sectorSize">
                            </div>

                            <div class="form-group">
                                <label for="sectors">数据段数量</label>
                                <input type="text" class="form-control" id="sectors" name="sectors">
                            </div>

                            <div class="form-group">
                                <label for="date">截止日期</label> &nbsp;&nbsp;
                                <input  id="date" type="date" name="date1">
                            </div>
                            <%--<div class="col-sm-4 col-md-4">--%>

                            <%--</div>--%>
                            <%--<a href="/backend/pretreatment/cutfile">--%>
                                <%--<button class="btn btn-default">文件切分</button>--%>
                            <%--</a>--%>

                            <button type="submit" class="btn btn-primary">数据签名</button>

                        </form>

                    </div>
                </div>

            </div>

            <br/>
            <br/>
            <br/>
            <div class="row col-sm-9 col-md-9">

                <table class="table table-hover">
                    <tr>
                        <th>数据段号</th>
                        <th>数据签名</th>
                        <th>hash</th>
                    </tr>
                    <c:forEach items="${tags}" var="li" begin="0" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>${li}</td>
                            <td></td>
                        </tr>
                    </c:forEach>

<%--                <%
                    List<City> citielist = (List<City>) request.getAttribute("cityList");
                    for(int i=0;i<citielist.size();i++)
                    {
                        City city = citielist.get(i);
                %>
                <tr>
                    <th><%=i+1%></th>
                    <th><%=city.getCityname()%></th>
                </tr>
                <%
                    }
                %>--%>
                </table>

            </div>

        </div>
    </div>
</div>
<script type="text/javascript" src="../../../../public/js/util.js"></script>
<script type="text/javascript" src="../../../../public/js/backend/pretreatment.js"></script>
<script type="text/javascript" src="../../../../public/js/fileinput/fileinput.js"></script>
<script type="text/javascript" src="../../../../public/js/fileinput/locales/zh.js"></script>
<script>

    $("#cutFile").click(function () {

//        var fileurl = $("#fileurl").val();
        $.ajax({
            type: "GET",
            <%--url: "${pageContext.request.contextPath}/backend/pretreatment/cutfile",--%>
            url: "/backend/pretreatment/cutfile",
            data: {"fileurl":$("#fileurl").val()},
            dataType: "json",
            success: function(data){
//                $('#resText').empty();   //清空resText里面的所有内容
//                var html = '';
//                $.each(data, function(commentIndex, comment){
//                    html += '<div class="comment"><h6>' + comment['username']
//                        + ':</h6><p class="para"' + comment['content']
//                        + '</p></div>';
//                });
//                $('#resText').html(html);
                if(data == "success"){
                    alert("suceeess");
                }
            }
        });
    })

</script>
</body>
</html>