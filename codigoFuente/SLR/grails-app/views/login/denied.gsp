<html>

<head>
		<title>Acceso denegado</title>

        <!-- CSS -->
        <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
        <link rel="stylesheet" href="bower_components/bootstrap/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="bower_components/font-awesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="css/form-elements.css">
        <link rel="stylesheet" href="css/style.css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <%-- CSS --%>
	    <g:render template="css" contextPath="/"/>

</head>

<body>

<div class="container">
    <div class="row">
    <div class="error-template">
	    <h1>Systematic Literature Review</h1>
	    <h2>Acceso denegado</h2>
	    <div class="error-details" style="padding-top: 20px;">
			Has intentado acceder a alguna página no autorizada o ha habido algún problema con la
			página que deseas acceder.
	    </div>
	    <div class="error-actions" style="padding-top: 20px;">
	    	<g:link class="btn btn-primary" controller="index" action="index">
	    		<i class="fa fa-home"></i> Home
	    	</g:link>
	    </div>
	</div>
    </div>
</div>

</body>

</html>