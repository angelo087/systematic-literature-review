<!DOCTYPE html>
<html lang="en">

    <head>

        <%-- Head Meta --%>
		<g:render template="headMeta" contextPath="/"/>
	
        <title>Register</title>

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

        <!-- Favicon and touch icons -->
        <link rel="shortcut icon" href="assets/ico/favicon.png">

    </head>

    <body>

        <!-- Top content -->
        <div class="top-content">
        	
            <div class="inner-bg">
                <div class="container">
                	
                    <div class="row">
                        <div class="col-sm-8 col-sm-offset-2 text">
                            <h1><strong>Systematic Literature Review</strong></h1>
                            <div class="description">
                            	<p>
	                            	Sign up now
                            	</p>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                    	<div class="col-sm-3"></div>
                        <div class="col-sm-6">
                        	
                        	<div class="form-box">
	                        	<div class="form-top">
	                        		<div class="form-top-left">
	                        			<h3>Sign up now</h3>
	                            		<p>Fill in the form below to get instant access:</p>
	                        		</div>	                        		
	                        		<div class="form-top-right">
	                        			<i class="fa fa-key"></i>
	                        		</div>
	                        		<g:if test='${flash.message}'>
	                        			<div id="divError" style="clear: both;" class="alert alert-danger" role="alert"><i class="fa fa-remove fa-fw"></i> ${flash.message}</div>
									</g:if>
	                            </div>
	                            <div class="form-bottom">
	                            	<g:form role="form" controller="register" action="registerUser" method="post" class="login-form">
				                    	<div class="form-group">
				                    		<label class="sr-only" for="j_email_mend">Email Mendeley</label>
				                        	<input type="text" name="j_email_mend" placeholder="Email Mendeley..." class="form-username form-control" id="j_email_mend" required="required" value="${emailMend}">
				                        </div>
				                        <div class="form-group">
				                        	<label class="sr-only" for="j_pass_mend">Password Mendeley</label>
				                        	<input type="password" name="j_pass_mend" placeholder="Password Mendeley..." class="form-password form-control" id="j_pass_mend">
				                        </div>
  				                        <div class="form-group">
				                        	<label class="sr-only" for="j_pass_mend_rep">Repeat Password Mendeley</label>
				                        	<input type="password" name="j_pass_mend_rep" placeholder="Repeat Password Mendeley..." class="form-password form-control" id="j_pass_mend_rep">
				                        </div>
				                        <button type="submit" class="btn" onclick="loading('Registrando usuario...');">Sign me now!</button>
				                    </g:form>
			                    </div>
		                    </div>
		                	                        
                        </div>
                        <div class="col-sm-3"></div>
                    </div>
                    
                </div>
            </div>
            
        </div>

        <!-- Footer -->
        <footer>
        	<div class="container">
        		<div class="row">
        			
        			<div class="col-sm-8 col-sm-offset-2">
        				<div class="footer-border"></div>
        				<p>Systematic Literature Review</p>
        			</div>
        			
        		</div>
        	</div>
        </footer>

        <!-- Javascript -->
	    <g:render template="javascript" contextPath="/"/>
        <!-- <script src="assets/js/jquery-1.11.1.min.js"></script>
        <script src="assets/bootstrap/js/bootstrap.min.js"></script> -->
        <script src="${resource(dir: 'js', file: 'jquery.backstretch.min.js')}"></script>
        <script src="${resource(dir: 'js', file: 'scriptsRegister.js')}"></script>
        
        <!--[if lt IE 10]>
            <script src="../js/placeholder.js"></script>
        <![endif]-->

    </body>

</html>