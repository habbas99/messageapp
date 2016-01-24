<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MessageApp Login</title>
<script type="text/javascript">
  var isAuthenticated = <%= session.getAttribute("messageapp.isAuthenticated") %>;
  if(isAuthenticated) {
     window.location.href = "/app/index.html";
  }
</script>
<script src="/assets/js/jquery.min.js"></script>
<link rel="stylesheet" href="/assets/css/bootstrap.min.css"></link>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
	    	<div class="navbar-header">
	     	 	<a class="navbar-brand">MessageApp</a>
	    	</div>
	    </div>
  	</nav>
	<div class="container">
		<div id="logoutAlert" class="alert alert-info" role="alert" hidden>
			Successfully logged out from your MessageApp account.
		</div>
  		<div class="row">
	    	<form method="post" action="/authenticator?type=login">
		  		<div class="form-group">
			    	<label for="username">User Name</label>
		   	 		<input type="text" class="form-control" id="username" name="username" placeholder="User Name">
			  	</div>
			  	<div class="form-group">
			    	<label for="password">Password</label>
			    	<input type="password" class="form-control" id="password" name="password" placeholder="Password">
			  	</div>
			  	<button type="submit" value="submit" class="btn btn-default">Sign In</button>
			</form>
 		</div>
	</div>
</body>
<script>
	var params = location.search;
	if(params && params.indexOf("type=logout") >= 0) {
		$("#logoutAlert").show();
	}
</script>
</html>