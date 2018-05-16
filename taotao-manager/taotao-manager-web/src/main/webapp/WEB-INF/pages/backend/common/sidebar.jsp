<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%--<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>--%>

<ul class="side-menu">
  <li><a class="nav-header" href="#">
      <%-- aria-hidden 图标的可访问性--%>
      <%--当焦点落到该输入框时，读屏软件就会读出aria-label里的内容，即“用户名”。--%>
        <i class="left glyphicon glyphicon-home" aria-hidden="true"></i>系统设置
        <i class="right glyphicon glyphicon-chevron-down" aria-hidden="true"></i></a>
    <ul class="sub-menu" style="display: block;">
      <li><a href="http://localhost:8080/backend/index">首页</a></li>
      <li><a href="http://localhost:8080/backend/options/general">节点信息</a></li>
      <%--<li><a href="${g.domain}/backend/options/post">撰写/阅读</a></li>--%>
      <%--<li><a class="last" href="${g.domain}/backend/options/email">邮件评论</a></li>--%>
    </ul>
   </li>
   <%--<shiro:hasAnyRoles name="admin,editor">--%>
   <li><a class="nav-header" href="#">
        <i class="left glyphicon glyphicon-send" aria-hidden="true"></i>数据分块
        <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
        </a>
     <ul class="sub-menu">
      <li><a href="http://localhost:8080/backend/pretreatment/">数据分块</a></li>
      <li><a href="http://localhost:8080/backend/posts">上传数据块</a></li>
      <li><a class="last" href="http://localhost:8080/backend/categorys">数据块签名</a></li>
    </ul>
  </li>
  <li><a class="nav-header" href="#">
        <i class="left glyphicon glyphicon-film" aria-hidden="true"></i>完整性验证
        <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
        </a>
     <ul class="sub-menu">
      <li><a href="http://localhost:8080/backend/verify/index">挑战--证明</a></li>
      <li><a class="last" href="http://localhost:8080/backend/uploads/edit">添加</a></li>
    </ul>
  </li>
  <%--</shiro:hasAnyRoles>--%>
  
  <%--<shiro:hasRole name="admin">--%>
  <li><a class="nav-header" href="#">
        <i class="icon glyphicon glyphicon-file" aria-hidden="true"></i>Swarm
        <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i>
        </a>
     <ul class="sub-menu">
      <li><a href="${g.domain}/backend/pages">上传</a></li>
      <li><a class="last" href="${g.domain}/backend/pages/edit">下载</a></li>
    </ul>
  </li>

  <%--</shiro:hasRole>--%>
  
  <%--<shiro:authenticated>--%>
    <li><a class="nav-header" href="#">
       <i class="left glyphicon glyphicon-user" aria-hidden="true"></i>用户
       <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i></a>
       <ul class="sub-menu">
        <%--<shiro:hasRole name="admin">--%>
          <li><a href="http://localhost:8080/backend/address">所有用户</a></li>
          <li><a href="http://localhost:8080/backend/address/create">创建地址</a></li>
        <%--</shiro:hasRole>--%>
        <li><a class="last" href="http://localhost:8080/backend/address/my">我的个人资料</a></li>
      </ul>
    </li>
  <%--</shiro:authenticated>--%>
  
  <%--<shiro:hasRole name="admin">--%>
   <li><a class="nav-header" href="#">
    <i class="left glyphicon glyphicon-wrench" aria-hidden="true"></i>工具
    <i class="right glyphicon glyphicon-chevron-up" aria-hidden="true"></i></a>
    <ul class="sub-menu">
      <li><a href="${g.domain}/backend/tool/ehcache">缓存监控</a></li>
      <li><a href="${g.domain}/backend/tool/import">导入</a></li>
      <li><a class="last" href="${g.domain}/backend/tool/output">导出</a></li>
    </ul>
   </li>
  <%--</shiro:hasRole>--%>
</ul>
<script type="text/javascript" src="../../../../public/js/backend/sidebar.js"></script>