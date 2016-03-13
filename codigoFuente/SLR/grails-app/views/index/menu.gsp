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

    <div id="wrapper">

        <%-- Head --%>
        <g:render template="head" contextPath="/"/>
        
        <div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Menú Principal</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            
            <%-- Menu center --%>
            <g:render template="menucenter" contextPath="/"/>
            
            <div class="row">
                <div class="col-lg-8">
                    
                    <%-- Chat --%>
                    <g:render template="logger" contextPath="/"/>
                    
                </div>

                <div class="col-lg-4">
                    
                    <g:render template="friendsconnect" contextPath="/"/>
                    <g:render template="lastusers" contextPath="/"/>
                    <g:render template="lastslrs" contextPath="/"/>
                    <g:render template="statgraphics" contextPath="/"/>
                    
                </div>

            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <%-- JavaScript --%>
    <g:render template="javascript" contextPath="/"/>

</body>

</html>
