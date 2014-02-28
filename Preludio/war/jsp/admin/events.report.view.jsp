<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.preludio.services.search.QuestionSearchIndex"%>
<%@page import="com.preludio.services.Message"%>
<%@page import="com.preludio.services.Context"%>
<%@page import="com.preludio.services.infra.auth.Authtoken"%>
<%@page import="com.preludio.services.profile.Profile"%>
<%@page import="com.preludio.services.System"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.preludio.web.HTTP"%>
<%@page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>


<%@page import="java.util.List"%>
<%@page import="com.preludio.services.admin.event.Event"%>
<%@page import="com.preludio.services.question.Question"%>
<%@page import="com.preludio.services.answer.Answer"%>
<%@page import="com.preludio.services.answer.vote.Vote"%><html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../../../css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../../../css/preludio.css" rel="stylesheet" media="screen">
    <link href="../../../css/datepicker.css" rel="stylesheet" media="screen">
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
		<div class="span10">
			<form name="main" method="GET" action="/admin/events/report">
			<div class="row-fluid">
				<div class="span3">
					<label class="control-label">From date</label>
				</div>
				<div class="span9">
					<input type="text" class="input-medium" name="from" id="from" data-date-format="dd/mm/yyyy" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span3">
					<label class="control-label">To date</label>
				</div>
				<div class="span9">
					<input type="text" class="input-medium" name="to" id="to" data-date-format="dd/mm/yyyy" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span3">
					<label class="control-label">Who</label>
				</div>
				<div class="span9">
					<input type="text" class="input-large" name="who" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span3">
					<label class="control-label">What</label>
				</div>
				<div class="span9">
					<select name="what">
						<option value="">Any event</option>
						<option value="<%= Profile.RESOURCE_NAME %>:POST">Sign Up</option>
						<option value="<%= Authtoken.RESOURCE_NAME %>:POST">Log In</option>
						<option value="<%= Profile.RESOURCE_NAME %>:GET">View Profile</option>
						<option value="<%= Profile.RESOURCE_NAME %>:PUT">Update Profile</option>
						<option value="<%= Question.RESOURCE_NAME %>:POST">Ask Question</option>
						<option value="<%= Question.RESOURCE_NAME %>:GET">View Question</option>
						<option value="<%= QuestionSearchIndex.RESOURCE_NAME %>:GET">Search Question</option>
						<option value="<%= Answer.RESOURCE_NAME %>:POST">New Answer</option>
						<option value="<%= Vote.RESOURCE_NAME %>">Vote</option>
					</select>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span3">
					<label class="control-label">Object</label>
				</div>
				<div class="span9">
					<input type="text" class="input-large" name="object" />
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12 text-center">
					<input type="submit" value=" Search Events " class="btn btn-primary" />
				</div>
			</div>
			</form>
		</div>

		<div class="span1"></div>
	</div>

	<hr/>

<%
	List<Event> events = (List<Event>) request.getAttribute("events");
%>
	<div class="row-fluid">
		<div class="span12">
<%
	if (events != null) {
%>
			<h3><strong><%= events.size() %></strong> events found</h3>
<%
	}
%>
			<table class="table table-condensed table-hover">
			<thead>
				<tr>
					<th>Who</th>
					<th>What</th>
					<th>Object</th>
					<th>When</th>
				</tr>
			</thead>
			<tbody>
<%
	if (events != null) {
		for (Event event : events) {
			
%>
				<tr>
					<td><%= event.getUser() %></td>
					<td><%= event.getDescription() %></td>
					<td><%= event.getObject() %></td>
					<td><%= new SimpleDateFormat("HH:mm:ss dd/MMM/yyyy").format(new Date(new Long(event.getTimestamp()))) %></td>
				</tr>
<%
		}
	}
%>
			</tbody>
			</table>
		</div>
	</div>

	<hr/>

	<div class="footer text-center">
		<p>&copy; Preludio 2013</p>
	</div>

</div>

	<form name="logoutForm" action="/logout" method="POST"><input type="hidden" name="<%= HTTP.FORCE_METHOD_FIELD %>" value="<%= HTTP.DELETE_METHOD %>" /></form>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="../../../js/bootstrap.min.js"></script>
    <script src="../../../js/bootstrap-datepicker.js"></script>
    <script>$('#from').datepicker();$('#to').datepicker();</script>
  </body>
</html>
