<!-- jQuery -->
<script src="${resource(dir: 'bower_components', file: 'jquery/dist/jquery.min.js')}"></script>

<!-- Bootstrap Core JavaScript -->
<script src="${resource(dir: 'bower_components', file: 'bootstrap/dist/js/bootstrap.min.js')}"></script>

<!-- Metis Menu Plugin JavaScript -->
<script src="${resource(dir: 'bower_components', file: 'metisMenu/dist/metisMenu.min.js')}"></script>

<!-- Custom Theme JavaScript -->
<script src="${resource(dir: 'dist', file: 'js/sb-admin-2.js')}"></script>

<script src="${resource(dir: 'js', file: 'jquery.range.js')}"></script>

<script src="${resource(dir: 'bower_components', file: 'raphael/raphael-min.js')}"></script>
<script src="${resource(dir: 'bower_components', file: 'morrisjs/morris.min.js')}"></script>
<%--<script src="${resource(dir: 'js', file: 'morris-data.js')}"></script> --%>

<script src="${resource(dir: 'js', file: 'jquery.blockUI.js')}"></script>

<script type="text/javascript">
	function loading(sms)
	{
		var text = '<h3>';
	
		text = text.concat(sms);
		text = text.concat("</h3>");
		
		$.blockUI({ 
			message: text
	       });
	}
	function closeModalWithMessage(name,msg)
	{
		//$('#modal').modal('hide');
		$('#'+name).modal('hide');
		loading(msg);
	}
</script>

<!-- Tokenizer -->
<script src="${resource(dir: 'dist', file: 'js/bootstrap-tokenfield.js')}"></script>
<script src="${resource(dir: 'dist', file: 'js/scrollspy.js')}"></script>
<script src="${resource(dir: 'dist', file: 'js/affix.js')}"></script>
<script src="${resource(dir: 'dist', file: 'js/typeahead.bundle.min.js')}"></script>
<script src="${resource(dir: 'dist', file: 'js/docs.min.js')}"></script>
