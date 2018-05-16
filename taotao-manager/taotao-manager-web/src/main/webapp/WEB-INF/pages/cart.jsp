
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="jquery-1.12.3.js"></script>

<script type="text/javascript">
	
	$(function(){
		$(".delete").click(function(){
			
			var $tr = $(this).parent().parent();
			var title = $.trim($tr.find("td:first").text());
			var flag = confirm("确定要删除" + title + "的信息吗?");
			alert(flag);
			
			if(flag){
				alert("111");
				return true;
			}
		   alert("222");
			return false;
		});
		});
		
	
</script>

</head>
<body>

	<center>
		
		<br><br>
		<div id="bookNumber">您的购物车中共有 22 本书</div>
		<table cellpadding="10">
			<tr>
				<td>Title</td>
				<td>Quantity</td>
				<td>Price</td>
				<td>&nbsp;</td>
			</tr>
			
			<c:forEach items="${sessionScope.ShoppingCart.items }" var="item">
				<tr>
					<td>${item.book.title }</td>
					<td> 
						<input class="${item.quantity }" type="text" size="1" name="${item.book.id }" value="${item.quantity }"/>
					</td>
					<td>${item.book.price }</td>
					<td><a href="bookServlet?method=remove&pageNo=${param.pageNo }&id=${item.book.id }" class="delete">删除</a></td>
				</tr>
			</c:forEach>
			
			<tr>
				<td colspan="4" id="totalMoney">总金额: ￥ ${ sessionScope.ShoppingCart.totalMoney}</td>
			</tr>
			
			<tr>
				<td colspan="4">
					<a href="bookServlet?method=getBooks&pageNo=${param.pageNo }">继续购物</a>
					&nbsp;&nbsp;
					
					<a href="bookServlet?method=clear">清空购物车</a>
					&nbsp;&nbsp;
					
					<a href="bookServlet?method=forwardPage&page=cash">结账</a>
					&nbsp;&nbsp;
				</td>
			</tr>
			
		</table>
		
	</center>
	
	<br><br>
	
	
</body>
</html>