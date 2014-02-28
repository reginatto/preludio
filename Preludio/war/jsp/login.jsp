<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.preludio.services.search.QuestionSearchIndex"%>
<%@page import="com.preludio.services.profile.Profile"%>

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
		</div>
		<div class="span3 text-right">
			<div class="btn-group">
				<a href="/signup" class="btn btn-success">Get Started Today</a>
			</div>
			&nbsp;
		</div>
	</div>

	<div class="row-fluid">
		<div class="span12">
			<div class="hero-unit">
				<form name="main" method="POST" action="/auth">
				<h2>Log In</h2>
				<div class="row-fluid">
					<div class="span8">
						<div class="row-fluid">
							<div class="span3">
								<label class="control-label" for="<%= Profile.EMAIL %>">Email</label>
							</div>
							<div class="span5">
								<input type="text" class="input-large" name="<%= Profile.EMAIL %>" autofocus/>
							</div>
						</div>
						<div class="row-fluid">
							<div class="span3">
								<label class="control-label" for="<%= Profile.PASSWORD %>">Password</label>
							</div>
							<div class="span5">
								<input type="password" class="input-large" name="<%= Profile.PASSWORD %>" />
							</div>
						</div>
						<div class="row-fluid">
							<div class="span8 text-center">
								<input type="submit" value=" Log In " class="btn btn-primary" />
							</div>
						</div>
					</div>
					<div class="span4">
						<div class="row text-center">
							<a href="#" class="btn btn-warning btn-block">Forgot Password?</a>
						</div>
						<p/>
						<div class="row text-center">
							<a href="/signup" class="btn btn-success btn-block">No Account? Get Started Today!</a>
						</div>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>

	<hr/>

	<div class="footer text-center">
		<p>&copy; Preludio 2013</p>
	</div>

</div>

	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="../js/bootstrap.min.js"></script>
    <script src="../js/google_analytics.js"></script>
  </body>
</html>
