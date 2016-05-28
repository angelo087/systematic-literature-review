<%@ page import="es.uca.pfc.User" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SLR | Graphs</title>

	<%-- CSS --%>
    <g:render template="css" contextPath="/"/>

</head>

<body>

    <div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Gráficos ${slrInstance.title}</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <div class="row">
            
            	<div class="bs-example bs-example-tabs" style="width: 100%;">
            		<ul id="myTab" class="nav nav-tabs">
            			<li class="active"><a href="#criterion" data-toggle="tab">Criterion</a></li>
            			<li><a href="#engine" data-toggle="tab">Engines</a></li>
            			<li><a href="#search" data-toggle="tab">Searchs</a></li>
            			<li><a href="#type" data-toggle="tab">Type Document</a></li>
            			<li><a href="#language" data-toggle="tab">Language</a></li>
            			<li><a href="#department" data-toggle="tab">Department</a></li>
            		</ul>
            	</div>
            	<div id="myTabContent" class="tab-content" align="left">

            		<div class="tab-pane fade in active" id="criterion" style="padding-top: 25px;">
	            		<div class="row">
	            			<div class="col-lg-1"></div>            		
	            			<div class="col-lg-10">
	            				<div class="panel panel-default">
			                        <div class="panel-heading">
			                            Total Referencias por Criterio
			                        </div>
			                        <div class="panel-body" style="overflow: hidden;">
			                            <div id="chart_div" align="center"></div>
			                        </div>
			                    </div>
	            			</div>
	                		<div class="col-lg-1"></div>
						</div>
						<div class="row">
	            			<div class="col-lg-1"></div>            		
	            			<div class="col-lg-10">
	            				<div class="panel panel-default">
			                        <div class="panel-heading">
			                            Total Referencias por Criterio
			                        </div>
			                        <div class="panel-body" style="overflow: hidden;">
			                            <div id="chart_div_2" align="center"></div>
			                        </div>
			                    </div>
	            			</div>
	                		<div class="col-lg-1"></div>
						</div>
						<div class="row">
	            			<div class="col-lg-1"></div>            		
	            			<div class="col-lg-10">
	            				<div class="panel panel-default">
			                        <div class="panel-heading">
			                            Total Referencias por Criterio
			                        </div>
			                        <div class="panel-body" style="overflow: hidden;">
			                            <div id="chart_div_3" align="center" style="float:left; clear: both;"></div>
			                        </div>
			                    </div>
	            			</div>
	                		<div class="col-lg-1"></div>
						</div>
						<div class="row">
	            			<div class="col-lg-1"></div>            		
	            			<div class="col-lg-10">
	            				<div class="panel panel-default">
			                        <div class="panel-heading">
			                            Total Referencias por Criterio
			                        </div>
			                        <div class="panel-body" style="overflow: hidden;">
			                            <div id="chart_div_4" align="center" style="float:left; clear: both;"></div>
			                        </div>
			                    </div>
	            			</div>
	                		<div class="col-lg-1"></div>
						</div>
						<div class="row">
	            			<div class="col-lg-1"></div>            		
	            			<div class="col-lg-10">
	            				<div class="panel panel-default">
			                        <div class="panel-heading">
			                            Total Referencias por Criterio
			                        </div>
			                        <div class="panel-body" style="overflow: hidden;">
			                            <div id="chart_div_5" align="center" style="float:left; clear: both;"></div>
			                        </div>
			                    </div>
	            			</div>
	                		<div class="col-lg-1"></div>
						</div>
            		</div>

            		<div class="tab-pane fade" id="engine" style="padding-top: 25px;">
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Engine
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_6" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Engine
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_7" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Engine
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_8" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Engine
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_9" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Engine
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_10" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
            		</div>
            		<div class="tab-pane fade" id="search" style="padding-top: 25px;">
            			<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Búsquedas por Referencia incluida
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_26" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Búsquedas por Referencia incluida
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_27" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
            		</div>
            		<div class="tab-pane fade" id="type" style="padding-top: 25px;">
            			<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Tipo Documento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_16" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Tipo Documento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_17" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Tipo Documento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_18" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Tipo Documento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_19" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Tipo Documento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_20" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
            		</div>
            		<div class="tab-pane fade" id="language" style="padding-top: 25px;">
           				<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Idioma
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_21" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Idioma
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_22" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por idioma
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_23" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Idioma
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_24" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Idioma
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_25" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
            		</div>
            		<div class="tab-pane fade" id="department" style="padding-top: 25px;">
            			<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Departamento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_11" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Departamento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_12" align="center"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Departamento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_13" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Departamento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_14" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
						<div class="row">
							<div class="col-lg-1"></div>            		
							<div class="col-lg-10">
								<div class="panel panel-default">
									<div class="panel-heading">
										Total Referencias por Departamento
									</div>
									<div class="panel-body" style="overflow: hidden;">
										<div id="chart_div_15" align="center" style="float:left; clear: both;"></div>
									</div>
								</div>
							</div>
							<div class="col-lg-1"></div>
						</div>
            		</div>
            	</div>

            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
    
    

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>
    <g:render template="graphsGoogle" contextPath="/graphs"/>
</body>

</html>
