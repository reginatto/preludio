<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.preludio.services.profile.Profile"%>
<%@page import="com.preludio.services.search.QuestionSearchIndex"%>

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
		</div>
	</div>

	<div class="row-fluid">
		<div class="span12">
			<div class="hero-unit">
				<form name="main" id="main" action="/profile" method="POST" class="form-horizontal">
				<div class="row-fluid">
					<div class="span8 offset2">

						<fieldset>
							<h2>Sign Up</h2><hr/>
							<div class="control-group">
								<label class="control-label" for="<%= Profile.FULL_NAME %>">Full Name</label>
								<div class="controls">
									<input type="text" class="input-large" name="<%= Profile.FULL_NAME %>" id="<%= Profile.FULL_NAME %>" autofocus/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="<%= Profile.EMAIL %>">Email</label>
								<div class="controls">
									<input type="text" class="input-large" name="<%= Profile.EMAIL %>" id="<%= Profile.EMAIL %>" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="<%= Profile.PASSWORD %>">Password</label>
								<div class="controls">
									<input type="password" class="input-large" name="<%= Profile.PASSWORD %>" id="<%= Profile.PASSWORD %>" />
								</div>
							</div>
							<div class="form-actions">
								<input type="submit" value=" Sign Up " class="btn btn-primary" />
								<input type="reset" value=" Cancel " class="btn" />
							</div>
						</fieldset>

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
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.min.js"></script>
    <script src="../js/google_analytics.js"></script>
    <script src="../js/profile.signup.js"></script>
  </body>
</html>
