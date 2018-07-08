<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.8.0.js"></script>
</head>
<body>
hah asdksalkd;lsak;dksakddksal;dk;sad;kl

<input id="getCodeBtn" type="button" class="getCodeBtn" value="111">
</body>
<script type="text/javascript">
var head = {"token":""};
var param = {"body":{"phoneNumber":"17621236608",
	                                "type":"1"            },
             "head":head};
$.ajax({
	type: "post",
	url: "http://192.168.0.114:8080/QingShansProject/getCode",
	async: true,
	traditional: true,
	data: param,
	dataType: "json",
	success: function(data) {
		alert("1");
	},
	error: function(data) {
		alert("2");
	}
});
</script>
</html>