<html>

<head>
	<title>Hola Libros</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>

<body>
	<h1>Prueba Books</h1>
	
	<div id="idTemplate" style="border: solid red thin; overflow: hidden; width: auto;">
		<g:include controller='slr' action='actionTemplate' />
	</div>
	
	<script type="text/javascript">
	$(document).ready(
            function() {
                setInterval(function() { 
                $('#idTemplate').load('/SLR/slr/actionTemplate');
                }, 5000);
            });
	</script>
	
</body>

</html>