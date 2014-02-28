<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


<%@page import="com.preludio.services.Context"%>
<%@page import="com.preludio.services.infra.auth.Authtoken"%>
<%@page import="com.preludio.web.HTTP"%>
<%@page import="com.preludio.services.search.QuestionSearchIndex"%><html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="description" content="Great technology helping family members better care for their older relatives.">
	<meta name="keywords" content="preludio, caregiving, care, family care, older relatives, home care, caregiving knowledge, caregiving support, caregiving technology, dementia care, caregiving expert" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="css/preludio.css" rel="stylesheet" media="screen">
    <title>Preludio</title>
  </head>

  <body>

<div class="container-semifluid">
	<div class="row-fluid my-nav-bar-home">
		<div class="span3">
			<p class="lead">&nbsp;Preludio</p>
		</div>
		<div class="span6 text-center"></div>
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
</div>


<form name="main" method="GET" action="/question" class="form-inline">
<div class="home-banner-grey">
<div class="container-semifluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="hero-unit">
				<div class="row-fluid">
					<div class="span12">
						<h1>Preludio</h1>
						<p class="lead">Great technology helping family members better care for their older relatives.</p>
					</div>
				</div>
				<div class="row-fluid">
					<div class="span12">
						<input type="text" class="input-x2large input-large-font" id="<%= QuestionSearchIndex.QUERY_STRING %>" name="<%= QuestionSearchIndex.QUERY_STRING %>" data-items="10" data-minLength="2" placeholder="Example: What is dementia?" />
						<input class="btn btn-large btn-primary" type="submit" value="Search for Advice" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
</form>

<div class="home-banner-white">
<div class="container-semifluid">
	<div class="row-fluid">
		<div class="span4">
			<p class="lead text-center">All the tools you need for managing care.</p>
		</div>
		<div class="span4">
			<p class="lead text-center">Your best source of knowledge for family caregiving.</p>
		</div>
		<div class="span4">
			<p class="lead text-center">Peace of mind for you and your family.</p>
		</div>
	</div>
	<div class="row-fluid">
		<div class="span4">
			<img src="img/screenshot_02_pretty_small.png" width="300" height="225" />
		</div>
		<div class="span4">
			<img src="img/screenshot_03_pretty_small.png" width="300" height="225" />
		</div>
		<div class="span4">
			<img src="img/screenshot_01_pretty_small.png" width="300" height="225" />
		</div>
	</div>
	<div class="row-fluid">
		<div class="span4">
			<br/><p>Keep in touch and engaged with family and friends. Understand and monitor all aspects of care.</p>
		</div>
		<div class="span4">
			<br/><p>Access experts' opinions, ask questions, share experiences. Collaborate and connect with other carers.</p>
		</div>
		<div class="span4">
			<br/><p>Feel reassured that you are doing the right things. Be recognized for your progress as a carer.</p>
		</div>
	</div>
</div>
</div>

<div class="home-banner-grey">
<div class="container-semifluid">
	<div class="row-fluid">
		<div class="span12 text-center">
			<h2>Some of the caregiving advice people find here:</h2><br/>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span2"></div>
		<div class="span8">
			<ul class="media-list">
				<li class="media">
					<a class="pull-left" href="/profile/BrendaReginatto">
						<img class="img-circle" src="/usr_img/AMIfv95JN53Th3msneDcDnXLBE_bR5xl-wgmFOkxtAXF3hRy1coPfCvBRqp6BNsMt-tU-lRYdBPAx5Z3Me1YGb_hn2XT3MYSyiSccf8r0D1USonJ-whkB2gRfdowFbSF51IMx8v3PnXkhLynQHYRIrF4pbGQM5EfSYIu1B2vAe9tijFctq5h2Vo" width="64" height="64"/>
					</a>
					<div class="media-body">
						<small><strong><a href="/profile/BrendaReginatto">Brenda Reginatto</a></strong> asked a <strong>question</strong>.</small>
						<a href="/question/What-should-I-do-when-my-mother-repeats-the-same-questions-over-and-over-again"><h4 class="media-heading">What should I do when my mother repeats the same questions over and over again?</h4></a>
						My mother has dementia and tends to repeat the same questions all the time. I try to be patient and answer every time, but this can be very frustrating. Is there anything else I could do?&nbsp;<br/>
					</div>
				</li>
				<br/><hr/><br/>
				<li class="media">
					<a class="pull-right" href="/profile/BrendaReginatto">
						<img class="img-circle" src="/usr_img/AMIfv95JN53Th3msneDcDnXLBE_bR5xl-wgmFOkxtAXF3hRy1coPfCvBRqp6BNsMt-tU-lRYdBPAx5Z3Me1YGb_hn2XT3MYSyiSccf8r0D1USonJ-whkB2gRfdowFbSF51IMx8v3PnXkhLynQHYRIrF4pbGQM5EfSYIu1B2vAe9tijFctq5h2Vo" width="64" height="64"/>
					</a>
					<div class="media-body text-right">
						<small><strong><a href="/profile/BrendaReginatto">Brenda Reginatto</a></strong> asked a <strong>question</strong>.</small>
						<a href="/question/How-can-I-help-my-mother-going-up-and-down-the-stairs-after-a-hip-replacement"><h4 class="media-heading">How can I help my mother going up and down the stairs after a hip replacement?</h4></a>
						My mother had a hip replacement about 3 weeks ago and is now back home. Her bedroom is upstairs and I'm not sure how to help her going up and down the stairs.&nbsp;<br/>
					</div>
				</li>
				<br/><hr/><br/>
				<li class="media">
					<a class="pull-left" href="/profile/BrendaReginatto">
						<img class="img-circle" src="/usr_img/AMIfv95JN53Th3msneDcDnXLBE_bR5xl-wgmFOkxtAXF3hRy1coPfCvBRqp6BNsMt-tU-lRYdBPAx5Z3Me1YGb_hn2XT3MYSyiSccf8r0D1USonJ-whkB2gRfdowFbSF51IMx8v3PnXkhLynQHYRIrF4pbGQM5EfSYIu1B2vAe9tijFctq5h2Vo" width="64" height="64"/>
					</a>
					<div class="media-body">
						<small><strong><a href="/profile/BrendaReginatto">Brenda Reginatto</a></strong> asked a <strong>question</strong>.</small>
						<a href="/question/Why-is-mom-always-feeling-cold"><h4 class="media-heading">Why is mom always feeling cold?</h4></a>
						My mom is 85 and she is always complaining she is feeling cold and asking to put on more clothes. Is this normal? Should I let her wear a lot of clothes even when it is warm?&nbsp;<br/>
					</div>
				</li>
			</ul>
		</div>
		<div class="span2"></div>
	</div>

</div>
</div>

<div class="home-banner-white">
<div class="container-semifluid">
	<div class="row-fluid">
		<div class="span12 text-center">
			<br/>
			<a class="btn-large btn-success" href="/signup">Sign Up And Get Started Today</a>
		</div>
	</div>
</div>
</div>

<div class="container-semifluid">
	<hr/>
	<div class="footer text-center">
		<p>&copy; Preludio 2013</p>
	</div>
</div>

	<form name="logoutForm" action="/logout" method="POST"><input type="hidden" name="<%= HTTP.FORCE_METHOD_FIELD %>" value="<%= HTTP.DELETE_METHOD %>" /></form>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/google_analytics.js"></script>
    <script src="js/search.typeahead.js"></script>
  </body>
</html>
