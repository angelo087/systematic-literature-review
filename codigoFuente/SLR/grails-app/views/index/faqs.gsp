<%@ page import="es.uca.pfc.User" %>
<!DOCTYPE html>
<html lang="en">

<head>

	<%-- Head Meta --%>
    <g:render template="headMeta" contextPath="/"/>

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
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading1">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse1" aria-expanded="true" aria-controls="collapse1">
            							¿Qué es una revisión sistemática de la literatura?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse1" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="heading1">
								<div class="panel-body">
									Una revisión sistemática de la literatura (<b>Systematic Literature Review</b>) es un medio para evaluar e interpretar una investigación disponible relativa a una determinada área de interés.
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading2">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse2" aria-expanded="false" aria-controls="collapse2">
            							¿Quién puede realizar un SLR?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse2" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading2">
								<div class="panel-body">
									Cualquier persona que esté involucrada en alguna investigación. Toda investigación comienza con el estudio del estado del arte de un tópico o tema específico. Es algo muy común tanto para estudiantes pre-doctorales como para los ya doctores.
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading3">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse3" aria-expanded="false" aria-controls="collapse3">
            							¿Cómo surge un SLR en la Ingeniería del Software?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse3" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading3">
								<div class="panel-body">
									<b>Barbara Kitchenham</b> propuso un conjunto de directrices para llevar a cabo estudios de la literatura en Ingeniería del Software. Estas directrices están basadas en otras metodologías similares en disciplinas como las ciencias sociales o como en la medicina. Podemos ver su guía en el siguiente 
									<a href="https://www.elsevier.com/__data/promis_misc/525444systematicreviewsguide.pdf">enlace</a>
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading4">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse4" aria-expanded="false" aria-controls="collapse4">
            							¿Cuantas etapas contiene una revisión sistemática de la literatura?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse4" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading4">
								<div class="panel-body">
									Barbara Kitchenham propone tres etapas:
									<ol>
										<li>Planificación de la Revisión</li>
										<li>Ejecución de la Revisión</li>
										<li>Difusión de los Resultados.</li>
									</ol>
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading5">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse5" aria-expanded="false" aria-controls="collapse5">
            							¿Qué pasos debo realizar para crear un SLR?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse5" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading5">
								<div class="panel-body">
									Debes seguir los siguientes pasos:
									<ol>
										<li><b>Crear un SLR.</b> Para ello, debes dirigirte a la lista de SLR creados y darle botón a Crear SLR. A continuación saldrá una ventana
										popup donde podrás insertar el nombre y justificación del mismo.</li>
										<li>Una vez creado, deberás crear las <b>preguntas de investigación</b> a las cuáles debes dar respuestas con tu SLR.
										estas preguntas pueden ser creadas en <i class="btn btn-outline btn-primary btn-circle fa fa-question"></i> donde deberás introducir simplemente
										el enunciado de la cuestión(es).</li>
										<li>A continuación deberás crear los <b>criterios de clasificación</b> de las referencias que encuentres más adelante, para ello deberás hacer click sobre el botón <i class="btn btn-outline btn-primary btn-circle fa fa-bookmark"></i>
										e introducir el criterio que quieras. Destacar que todo SLR pondrá a sus referencias el criterio <b>included</b> por defecto. Para crear un criterio simplemente introduce el nombre y descripción del mismo.</li>
										<li>Todas las referencias tiene unos atributos comunes, pero puedes personalizarlas insertando nuevos <b>atributos específicos</b>, para ello
										cliquea sobre <i class="btn btn-outline btn-primary btn-circle glyphicon glyphicon-tags"></i> e inserta los que te sean conveniente. Debes introducir
										el nombre y tipo de campo (entero, texto o lista)</li>
										<li>Es la hora de <b>realizar búsquedas</b>, para ello hacemos click en <i class="btn btn-outline btn-primary btn-circle glyphicon glyphicon-search"></i> y esto hará que
										accedamos a la página principal de las búsquedas del SLR. Hacemos click en Crear y nos saldrá una pantalla donde debemos indicar los parámetros
										de búsquedas que consideremos convenientes. Una vez hecho click en Crear búsqueda, debemos esperar a que finalice.</li>
										<li>Podemos ver el progreso de búsqueda en <i class="fa fa-tasks fa-fw"></i> arriba en el menú principal.</li>
										<li>Una vez terminado el progreso, podemos ver el resultado obtenido en las notificaciones <i class="fa fa-envelope fa-fw"></i> que nos llevará
										a la pantalla de búsquedas realizadas para el SLR en cuestión y haciendo click en sus referencias.</li>
										<li>Podemos filtrar los resultados y modificar su información haciendo click en la referencia correspondiente.</li>
										<li>Para ver los gráficos o <b>exportar la información</b>, debemos ir al menú principal de los SLR y hacer click en cualquiera de los siguientes
										botones <p><i class="btn btn-outline btn-success btn-circle fa fa-file-excel-o"></i> <i class="btn btn-outline btn-success btn-circle fa fa-file-word-o"></i> <i class="btn btn-outline btn-success btn-circle fa fa-file-code-o"></i> <i class="btn btn-outline btn-success btn-circle glyphicon glyphicon-stats"></i> </p> </li>
									</ol>
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading6">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse6" aria-expanded="false" aria-controls="collapse6">
            							¿Cómo se ha realizado los gráficos de un SLR?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse6" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading6">
								<div class="panel-body">
									<b>Google Chart</b> dispone de una API donde puedes pasarle los datos por Javascript y, automáticamente, coloca el gráfico
									donde le indiques, puedes obtener más información en este <a href="https://developers.google.com/chart/">enlace</a>.
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading7">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse7" aria-expanded="false" aria-controls="collapse7">
            							Actualmente, ¿En cuántos motores de búsquedas podemos encontrar información?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse7" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading7">
								<div class="panel-body">
									Podemos realizar búsquedas en cuatro motores de búsquedas especializados en articulos y referencias bibliográficas:
									<ul>
										<li><b>ACM Digital Library</b></li>
										<li><b>IEEE Computer Society</b></li>
										<li><b>Science Direct</b></li>
										<li><b>Springer Link</b></li>
									</ul>
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading8">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse8" aria-expanded="false" aria-controls="collapse8">
            							¿Qué hace falta para registrarme en la aplicación?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse8" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading8">
								<div class="panel-body">
									Lo primero que deberá de saber, es que esta aplicación sincroniza con la aplicación de Mendeley, por lo que
									para registrarse deberá poseer una cuenta en <a href="https://www.mendeley.com/">Mendeley</a> y hacer click en el botón de registro, donde deberá
									introducir el email y la contraseña de acceso.
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading9">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse9" aria-expanded="false" aria-controls="collapse9">
            							¿Qué es Mendeley?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse9" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading9">
								<div class="panel-body">
									<b>Mendeley</b> es un gestor de referencias bibliográficas, 
									una herramienta gratuita y multiplataforma (Windows, Linux y Mac) que combina 
									una versión local con una versión web, pudiendo sincronizar ambas, permitiendo 
									de este modo que se pueda consultar la información desde cualquier ordenador con 
									conexión a Internet. Puedes obtener más información <a href="https://www.mendeley.com/">aquí</a>.
								</div>
							</div>
            			</div>
            			<%-- FAQ --%>
            			<div class="panel panel-default">
            				<div class="panel-heading" role="tab" id="heading10">
            					<h4 class="panel-title">
            						<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse10" aria-expanded="false" aria-controls="collapse10">
            							No recuerdo la contraseña de Mendeley, ¿Qué debo hacer?
            						</a>
            					</h4>
            				</div>
            				<div id="collapse10" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading10">
								<div class="panel-body">
									Simplemente deberá acceder a la página de Mendeley y seguir las instrucciones que proporciona
									Mendeley para recuperar el acceso a su cuenta, posteriormente, podrá volver a esta aplicación
									y conectar sin problemas.									
								</div>
							</div>
            			</div>
            			
            			
            		</div>
            	</div>
            </div>


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