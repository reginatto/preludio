<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.preludio.services.profile.Profile"%>
<%@page import="com.preludio.services.search.QuestionSearchIndex"%>
<%@page import="com.preludio.services.Message"%>
<%@page import="com.preludio.services.question.Question"%>
<%@page import="java.util.List"%>
<%@page import="com.preludio.services.infra.auth.Authtoken"%>
<%@page import="com.preludio.services.Context"%>
<%@page import="com.preludio.web.HTTP"%>


<%@page import="com.preludio.services.System"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.preludio.services.infra.data.Document"%><html>
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

<%
	List<Message> questions = (List<Message>) request.getAttribute(Question.RESOURCE_NAME);
%>

	<div class="row-fluid">
		<div class="span1"></div>
		<div class="span7">
			<ul class="media-list">
<%
	for (Message question : questions) {
		String profileImage = question.get(Profile.IMAGE_KEY);
		if (!profileImage.equals("")) {
			profileImage = "/usr_img/" + profileImage;
		} else {
			profileImage = "../img/anon_user.png";
		}
%>
			<li class="media">
				<a class="pull-left" href="<%= question.get(Document.AUTHOR) %>">
					<img class="img-circle" src="<%= profileImage %>" width="64" height="64"/>
				</a>
				<div class="media-body">
					<small><strong><a href="<%= question.get(Document.AUTHOR) %>"><%= question.get(Profile.FULL_NAME) %></a></strong> asked a <strong>question</strong>.</small>
					<a href="<%= question.get(Question.ID) %>"><h4 class="media-heading"><%= question.get(Question.TITLE) %></h4></a>
					<%= question.get(Question.DETAILS) %>&nbsp;<br/>
					<small>
						<i class="icon-comment"></i> <%= question.get(Question.ANSWERS) %> answers |
						<i class="icon-retweet"></i> share |
						<i class="icon-time"></i> <%= new SimpleDateFormat("HH:mm dd/MMM/yyyy").format(new Date(new Long(question.get(System.TIMESTAMP)))) %>
					</small>
				</div>
			</li>
			<hr/>
<%
	}
%>
		</ul>
		</div>

		<div class="span3">
			<div class="well">
				<ul class="nav nav-list">
					<li class="active"><a href="#"><i class="icon-list-alt"></i> News Feed</a></li>
					<li><a href="/question/ask"><i class="icon-question-sign"></i> Ask a Question</a></li>
				</ul>
			</div>
			<div class="well">
				<small>
					<strong>Topics you might be interested:</strong><p/>
					<i class="icon-folder-open"></i> Dementia / Alzheimer's<br/>
					<i class="icon-folder-open"></i> Exercises<br/>
					<i class="icon-folder-open"></i> Shopping<br/>
					<i class="icon-folder-open"></i> Arthritis<br/>
				</small>
			</div>
			<div class="well">
				<small>
					<strong>People you might follow:</strong><p/>
					<i class="icon-user"></i> Brenda Reginatto<br/>
					<i class="icon-user"></i> John Doe<br/>
					<i class="icon-user"></i> Mariah Carey<br/>
					<i class="icon-user"></i> Louis Walsh<br/>
				</small>
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
