<%@ page import="es.uca.pfc.User" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | Menu</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>

</head>

<body>

<g:if test="${isLogin.toString().equals('true')}">
	<div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">FAQ's</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <div class="row">
                <div class="col-lg-12">
                	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	                	<g:each in="${faqListInstance}" var="faqInstance" status="i">
							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="${'heading'+(i+1)}">
									<h4 class="panel-title">
										<g:if test="${i == 0}">
											<a role="button" data-toggle="collapse" data-parent="#accordion" href="${'#collapse'+(i+1)}" aria-expanded="true" aria-controls="${'collapse'+(i+1)}">
									          ${faqInstance.enunciado}
									        </a>
										</g:if>
										<g:else>
											<a role="button" data-toggle="collapse" data-parent="#accordion" href="${'#collapse'+(i+1)}" aria-expanded="false" aria-controls="${'collapse'+(i+1)}">
									          ${faqInstance.enunciado}
									        </a>
								        </g:else>
									</h4>
								</div>
								<g:if test="${i == 0}">
									<div id="${'collapse'+(i+1)}" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="${'heading'+(i+1)}">
										<div class="panel-body">
											${faqInstance.respuesta}
										</div>
									</div>
								</g:if>
								<g:else>
									<div id="${'collapse'+(i+1)}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="${'heading'+(i+1)}">
										<div class="panel-body">
											${faqInstance.respuesta}
										</div>
									</div>
								</g:else>
							</div>
	                	</g:each>
                	</div>
                </div>

            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
</g:if>
<g:else>
	No template FAQS
</g:else>

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>
</body>

</html>