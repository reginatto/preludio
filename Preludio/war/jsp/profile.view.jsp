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
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../css/preludio.css" rel="stylesheet" media="screen">
    <title>Preludio</title>
  </head>

  <body>
<div class="container">
	<div class="row-fluid my-nav-bar">
		<div class="span3">
			<p class="lead">&nbsp;Preludio</p>
		</div>
		<div class="span6 text-center">
			<form name="main" method="GET" action="/question" class="form-inline">
			<div class="input-append">
				<span class="text-left">
				<input type="text" id="<%= QuestionSearchIndex.QUERY_STRING %>" name="<%= QuestionSearchIndex.QUERY_STRING %>" data-items="10" data-minLength="2" placeholder="Search for advice" />
				<input class="btn btn-primary" type="submit" value="Search" />
				</span>
			</div>
			</form>
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
<%
	Message profileObj = (Message) request.getAttribute(Profile.RESOURCE_NAME);

	String profileImage = profileObj.get(Profile.IMAGE_KEY);
	if (!profileImage.equals("")) {
		profileImage = "/usr_img/" + profileImage;
	} else {
		profileImage = "../img/anon_user.png";
	}
%>
			<ul class="media-list">
			<li class="media">
				<div class="media-object pull-left">
					<img class="img-circle" src="<%= profileImage %>" width="64" height="64"/>
				</div>
				<div class="media-body">
					<h4 class="media-heading"><%= profileObj.get(Profile.FULL_NAME) %></h4>
<%
	String description = new String();
	description = profileObj.get(Profile.DESCRIPTION);
	if (description.equals("")) {
		if (ctx != null) {
			if (ctx.getLoggedInUserProfileId().equals(profileObj.get(Profile.ID))) {
				description = "No profile description. Add one <a href=\"" + ctx.getLoggedInUserProfileId() + "?" + Profile.EDITING + "=1\">here</a>.";
			} else {
				description = "No profile description.";
			}
		} else {
			description = "No profile description.";
		}
	}

	String joinDate = new String("sometime ago");
	String joinTimestamp = profileObj.get(System.TIMESTAMP);
	if (!joinTimestamp.equals("")) {
		joinDate = new SimpleDateFormat("dd MMM yyyy").format(new Date(new Long(joinTimestamp)));
	}
%>
					<%= description %>&nbsp;<br/>
<%
	String location = profileObj.get(Profile.LOCATION);
	if (!location.equals("")) {
%>
					<small>
						<i class="icon-map-marker"></i> <strong><%= location %></strong>
					</small><br/>
<%
	}
%>
					<small>
						<i class="icon-time"></i> Joined <%= joinDate %>
					</small>
					<hr/>
				</div>
			</li>
			<li class="media">
				<div class="media-object pull-left">
					<img src="../img/void.png" width="64" height="1"/>
				</div>
				<div class="media-body">
					Here goes a rationale on how many points you have
					<hr/>
				</div>
			</li>
			<li class="media">
				<div class="media-object pull-left">
					<img src="../img/void.png" width="64" height="1"/>
				</div>
				<div class="media-body">
					Here goes a feed with latest questions and answers you have interacted
					<hr/>
				</div>
			</li>
			</ul>
		</div>

		<div class="span3">
			<div class="well">
				<ul class="nav nav-list">
					<li><a href="/feed"><i class="icon-list-alt"></i> News Feed</a></li>
<%
	if (ctx != null) {
		if (ctx.getLoggedInUserProfileId().equals(profileObj.get(Profile.ID))) {
%>
			<li><a href="<%= ctx.getLoggedInUserProfileId() %>?<%= Profile.EDITING %>=1"><i class="icon-user"></i> Edit your profile</a></li>
<%
		}
	}
%>
				</ul>
			</div>
			<div class="well">
				<strong>Profile Status</strong><p/>
				<span class="badge badge-success badge-points"><%= profileObj.get(Profile.POINTS) %></span> points<br/>
			</div>
			<div class="well">
				<strong>Profile is 60% complete</strong><p/>
				<div class="progress progress-small">
					<div class="bar" style="width: 60%;"></div>
				</div>
				<div class="text-right">
					<small>Click here to improve</small>
				</div>
			</div>
		</div>
		<div class="span1"></div>
	</div>

	<hr/>

	<div class="footer text-center">
		<p>&copy; Preludio 2013</p>
	</div>

</div>

	<form name="logoutForm" action="/logout" method="POST"><input type="hidden" name="<%= HTTP.FORCE_METHOD_FIELD %>" value="<%= HTTP.DELETE_METHOD %>" /></form>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/google_analytics.js"></script>
    <script src="../js/search.typeahead.js"></script>
  </body>
</html>
