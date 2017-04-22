<!DOCTYPE html>
<html lang="en">

<head>

    <%-- Head Meta --%>
    <g:render template="headMeta" contextPath="/"/>

    <title>SLR | Task Searchs</title>
    
    <%-- CSS --%>
    <g:render template="css" contextPath="/"/>

</head>

<body>

	<div id="wrapper">
		<%-- Head --%>
        <g:render template="head" contextPath="/"/>
        	
		<div id="page-wrapper">
		
			<g:include controller='index' action='loadTaskSearchs' />
		
        </div>
        
	</div>

<%-- JavaScript --%>
<g:render template="javascript" contextPath="/"/>

<script type="text/javascript">
	$(document).ready(
	           function() {
	               setInterval(function() { 
	               $('#page-wrapper').load('/SLR/index/loadTaskSearchs');
	               }, 10000);
	           });
</script>

</body>

</html>