<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.preludio.services.search.QuestionSearchIndex"%>
<%@page import="com.preludio.services.Message"%>
<%@page import="com.preludio.services.question.Question"%>
<%@page import="com.preludio.services.topic.Topic"%>
<%@page import="com.preludio.services.answer.Answer"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.preludio.services.Context"%>
<%@page import="com.preludio.services.infra.auth.Authtoken"%>
<%@page import="com.preludio.services.answer.vote.Vote"%>
<%@page import="com.preludio.services.profile.Profile"%>
<%@page import="com.preludio.web.HTTP"%>
<%@page import="com.preludio.services.infra.data.Document"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.preludio.services.System"%>
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
	Message questionObj = (Message) request.getAttribute(Question.RESOURCE_NAME);
	String disabled = new String();
	if (ctx == null) {
		disabled = "disabled";
	}
%>
			<ul class="media-list">
			<li class="media">
				<div class="media-object pull-left">
					<img src="../img/anon_topic.png" width="64" height="64"/>
				</div>
				<div class="media-body">
					<small><strong><%= questionObj.get(Topic.NAME) %></strong></small>
					<h4 class="media-heading"><%= questionObj.get(Question.TITLE) %></h4>
					<%= questionObj.get(Question.DETAILS) %>&nbsp;<br/>
					<small>
						<i class="icon-user"></i> asked by <a href="<%= questionObj.get(Document.AUTHOR) %>"><%= questionObj.get(Profile.FULL_NAME) %></a><br/>
						<i class="icon-comment"></i> 0 comments |
						<i class="icon-retweet"></i> share |
						<i class="icon-time"></i> <%= new SimpleDateFormat("HH:mm dd/MMM/yyyy").format(new Date(new Long(questionObj.get(System.TIMESTAMP)))) %>
					</small>
					<hr/>
				</div>
			</li>
<%
	String answerPlaceholder = "Add your answer";
	if (ctx == null) {
		answerPlaceholder = "Log in to add your answer";
	}
%>
			<li class="media">
				<div class="media-object pull-left">
					<img src="../img/void.png" width="64" height="1"/>
				</div>
				<div class="media-body">
					<form name="main" method="POST" action="/answer">
					<div class="row-fluid">
						<div class="span12">
							<textarea class="ckeditor <%= disabled %>" name="<%= Answer.CONTENT %>" rows="3" placeholder="<%= answerPlaceholder %>" <%= disabled %>></textarea>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12 text-right">
							<br/><input type="submit" value=" Add Answer " class="btn btn-primary <%= disabled %>" <%= disabled %>/>
						</div>
					</div>
					<input type="hidden" name="<%= Question.ID %>" value="<%= questionObj.get(Question.ID) %>" />
					</form>
					<hr/>
				</div>
			</li>
<%
	List<Map<String, String>> answers = (List<Map<String, String>>) request.getAttribute(Answer.RESOURCE_NAME);
	if (answers != null) {
		int i = 0;
		for (Map<String, String> answer : answers) {
%>
			<li class="media">
				<div class="media-object pull-left">
					<div class="my-well text-center">
						<table width="64">
							<tr>
								<td>
									<span class="lead"><%= answer.get(Answer.VOTES) %></span><br/><small>votes</small>
								</td>
<%
	if (answer.containsKey(Vote.ID)) {
		// vote already exists, any change is an update
		String voteUpStyle = new String("btn btn-mini");
		String voteDownStyle = new String("btn btn-mini");
		String voteUpTitle = new String();
		String voteDownTitle = new String();

		if (answer.get(Vote.VOTE).equals(Vote.VOTE_UP)) {
			voteUpStyle += " disabled";
			voteUpTitle = "You voted UP this answer";
			voteDownTitle = "Vote DOWN this answer";
		} else if (answer.get(Vote.VOTE).equals(Vote.VOTE_DOWN)) {
			voteUpStyle += " btn-success";
			voteDownStyle += " disabled";
			voteUpTitle = "Vote UP this answer!";
			voteDownTitle = "You voted DOWN this answer";
		}
%>
								<form name="voteForm<%= i %>" action="/vote" method="POST">
								<input type="hidden" name="<%= HTTP.FORCE_METHOD_FIELD %>" value="<%= HTTP.PUT_METHOD %>" />
								<input type="hidden" name="<%= Answer.ID %>" value="<%= answer.get(Answer.ID) %>" />
								<input type="hidden" name="<%= Question.ID %>" value="<%= questionObj.get(Question.ID) %>" />
								<input type="hidden" name="<%= Vote.ID %>" value="<%= answer.get(Vote.ID) %>" />
								<input type="hidden" name="<%= Vote.VOTE %>" value="" />
								<td>
									<div class="btn-group btn-group-vertical">
										<a class="<%= voteUpStyle %> <%= disabled %>" title="<%= voteUpTitle %>" href="#" onclick="javascript:document.voteForm<%= i %>.<%= Vote.VOTE %>.value='<%= Vote.VOTE_UP %>';document.voteForm<%= i %>.submit();return false;"><i class="icon-chevron-up icon-white"></i></a>
										<a class="<%= voteDownStyle %> <%= disabled %>" title="<%= voteDownTitle %>" href="#" onclick="javascript:document.voteForm<%= i %>.<%= Vote.VOTE %>.value='<%= Vote.VOTE_DOWN %>';document.voteForm<%= i %>.submit();return false;"><i class="icon-chevron-down"></i></a>
									</div>
								</td>
								</form>
<%
	} else {
		// vote doesn't exist yet, any change is a create
		String voteDownStyle = new String("btn btn-mini");
		String voteDownTitle = new String("Vote DOWN this answer");
		if (answer.get(Answer.VOTES).equals("0")) {
			voteDownStyle += " disabled";
			voteDownTitle = "";
		}
%>
								<form name="voteForm<%= i %>" action="/vote" method="POST">
								<input type="hidden" name="<%= Answer.ID %>" value="<%= answer.get(Answer.ID) %>" />
								<input type="hidden" name="<%= Question.ID %>" value="<%= questionObj.get(Question.ID) %>" />
								<input type="hidden" name="<%= Vote.VOTE %>" value="" />
								<td>
									<div class="btn-group btn-group-vertical">
										<a class="btn btn-mini btn-success <%= disabled %>" title="Vote UP this answer!" href="#" onclick="javascript:document.voteForm<%= i %>.<%= Vote.VOTE %>.value='<%= Vote.VOTE_UP %>';document.voteForm<%= i %>.submit();return false;"><i class="icon-chevron-up icon-white"></i></a>
										<a class="<%= voteDownStyle %> <%= disabled %>" title="<%= voteDownTitle %>" href="#" onclick="javascript:document.voteForm<%= i %>.<%= Vote.VOTE %>.value='<%= Vote.VOTE_DOWN %>';document.voteForm<%= i %>.submit();return false;"><i class="icon-chevron-down"></i></a>
									</div>
								</td>
								</form>
<%
	}
%>
							</tr>
						</table>
					</div>
				</div>
				<div class="media-body">
					<div class="row-fluid">
						<div class="span12">
							<small>
							<i class="icon-user"></i> <strong>Answered by <a href="<%= answer.get(Document.AUTHOR) %>"><%= answer.get(Profile.FULL_NAME) %></a></strong><br/>
							<%= answer.get(Answer.CONTENT) %>
							</small>
							&nbsp;<br/>
							<small>
								<i class="icon-comment"></i> 0 comments |
								<i class="icon-time"></i> <%= new SimpleDateFormat("HH:mm dd/MMM/yyyy").format(new Date(new Long(answer.get(System.TIMESTAMP)))) %>
							</small>
						</div>
					</div>
					<hr/>
				</div>
			</li>
<%
			i++;
		}
	}
%>
			</ul>
		</div>

		<div class="span3">
			<div class="well">
				<ul class="nav nav-list">
<%
	if (ctx != null) {
%>
					<li><a href="/feed"><i class="icon-list-alt"></i> News Feed</a></li>
					<li><a href="/question/ask"><i class="icon-question-sign"></i> Ask a Question</a></li>
<%
	} else {
%>
					<li><a href="/login"><i class="icon-question-sign"></i> Log in to Ask or Answer a Question</a></li>
<%
	}
%>
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
    <script src="../ckeditor/ckeditor.js"></script>
    <script src="../js/search.typeahead.js"></script>
  </body>
</html>
