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

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../css/preludio.css" rel="stylesheet" media="screen">
    <title>Preludio</title>
	<script src="https://maps.googleapis.com/maps/api/js?sensor=false&libraries=places"></script>
	<script src="../js/profile.edit.js"></script>
	<script type="text/javascript">
		function getLocationInputId() {
			return '<%= Profile.LOCATION %>';
		}
		google.maps.event.addDomListener(window, 'load', profileEditLocationInitialize);
	</script>
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

    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    String profileImage = profileObj.get(Profile.IMAGE_KEY);
	if (!profileImage.equals("")) {
		profileImage = "/usr_img/" + profileImage;
	} else {
		profileImage = "../img/anon_user.png";
	}
%>
			<form name="main" method="POST" action="<%= blobstoreService.createUploadUrl(ctx.getLoggedInUserProfileId()) %>" enctype="multipart/form-data">
			<input type="hidden" name="<%= HTTP.FORCE_METHOD_FIELD %>" value="<%= HTTP.PUT_METHOD %>" />
			<ul class="media-list">
			<li class="media">
				<div class="media-object pull-left">
					<div class="fileupload fileupload-new" data-provides="fileupload">
						<div class="row-fluid">
							<div class="span12">
								<div class="fileupload-preview thumbnail" style="width: 64px; height: 64px;">
									<img src="<%= profileImage %>" width="64" height="64"/>
								</div>
							</div>
						</div>
						<div class="row-fluid">
							<div class="span12 text-center">
								<span class="btn btn-file">
									<span class="fileupload-new"><small>Change</small></span>
									<span class="fileupload-exists"><small>Change</small></span>
									<input name="<%= Profile.IMAGE %>" type="file" />
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="media-body">
					<h4 class="media-heading"><%= profileObj.get(Profile.FULL_NAME) %></h4>
<%
	String joinDate = new String("sometime ago");
	String joinTimestamp = profileObj.get(System.TIMESTAMP);
	if (!joinTimestamp.equals("")) {
		joinDate = new SimpleDateFormat("dd MMM yyyy").format(new Date(new Long(joinTimestamp)));
	}

	String description = new String();
	String descriptionPH = new String();
	description = profileObj.get(Profile.DESCRIPTION);
	if (description.equals("")) {
		descriptionPH = "Example: Family caregiver looking after my mother.";
	}
%>
					<small>
						<i class="icon-time"></i> Joined <%= joinDate %>
					</small>
					<hr/>
					<div class="row-fluid">
						<div class="span3">
							<label class="control-label" for="<%= Profile.DESCRIPTION %>">Description</label>
						</div>
						<div class="span9">
							<textarea class="ckeditor" name="<%= Profile.DESCRIPTION %>" rows="3" placeholder="<%= descriptionPH %>" autofocus><%= description %></textarea>
							<br/>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span3">
							<label class="control-label" for="<%= Profile.LOCATION %>">Current location</label>
						</div>
						<div class="span9">
							<input type="text" class="input-large" name="<%= Profile.LOCATION %>" id="<%= Profile.LOCATION %>" value="<%= profileObj.get(Profile.LOCATION) %>" />
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12 text-right">
							<input type="submit" value=" Save " class="btn btn-primary" />
						</div>
					</div>
				</div>
			</li>
			</ul>
			</form>
		</div>

		<div class="span3">
			<div class="well">
				<ul class="nav nav-list">
					<li><a href="/feed"><i class="icon-list-alt"></i> News Feed</a></li>
					<li><a href="<%= ctx.getLoggedInUserProfileId() %>?<%= Profile.EDITING %>=1"><i class="icon-user"></i> Edit your profile</a></li>
				</ul>
			</div>
			<div class="well">
				<strong>Profile Status</strong><p/>
				<span class="badge badge-success badge-points"><%= profileObj.get(Profile.POINTS) %></span> points<br/>
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
