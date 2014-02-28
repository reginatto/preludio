<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.preludio.services.search.QuestionSearchIndex"%>
<%@page import="com.preludio.services.topic.Topic"%>
<%@page import="com.preludio.services.question.Question"%>
<%@page import="com.preludio.services.Context"%>
<%@page import="com.preludio.services.infra.auth.Authtoken"%>
<%@page import="com.preludio.web.HTTP"%>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../../css/preludio.css" rel="stylesheet" media="screen">
    <title>Preludio</title>
  </head>

  <body>
<div class="container">
	<div class="row-fluid my-nav-bar">
		<div class="span3">
			<p class="lead">&nbsp;Preludio</p>
		</div>
		<div class="span6 text-center">
		</div>
		<div class="span3 text-right">
<%
	Context ctx = (Context) session.getAttribute(Authtoken.TOKEN);
	if (ctx != null) {
%>
			<div class="dropdown">
				<a class="dropdown-toggle btn" data-toggle="dropdown" href="#"><%= ctx.getLoggedInUserProfileFullName() %>&nbsp;<span class="caret"></span></a>&nbsp;
				<ul class="dropdown-menu text-left" role="menu" aria-labelledby="dLabel">
					<li><a tabindex="-1" href="<%= ctx.getLoggedInUserProfileId() %>"><i class="icon-user"></i> My Profile</a></li>
					<li class="divider"></li>
					<li><a tabindex="-1" href="#" title="Logout" onclick="javascript:document.logoutForm.submit();return false;">Logout</a></li>
				</ul>
			</div>
<%
	} else {
%>
			<div class="btn-group">
				<a href="/login" class="btn">Log In</a>
				<a href="/signup" class="btn btn-success">Get Started Today</a>
			</div>
			&nbsp;
<%
	}
%>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span1"></div>
		<div class="span7">
			<form name="main" method="POST" action="/admin/bulkUploadQuestion">
			<div class="row-fluid">
				<div class="span12">
				    <h2>Question Bulk Upload</h2>
					<div class="row-fluid">
						<div class="span3">
							<label class="control-label">String</label>
						</div>
						<div class="span9">
							<textarea class="my-block-level" name="string" rows="10"></textarea>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12 text-center">
							<br/><input type="submit" value=" Bulk Upload " class="btn btn-primary" />
						</div>
					</div>
				</div>
			</div>
		    </form>
		</div>

		<div class="span3">
		</div>
		<div class="span1"></div>
	</div>

	<hr/>

	<div class="footer text-center">
		<p>&copy; Preludio 2013</p>
	</div>

</div>

	<form name="logoutForm" action="/logout" method="POST"><input type="hidden" name="<%= HTTP.FORCE_METHOD_FIELD %>" value="<%= HTTP.DELETE_METHOD %>" /></form>
  </body>
</html>
