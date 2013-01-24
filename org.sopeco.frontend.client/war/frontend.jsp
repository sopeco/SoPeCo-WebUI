<%@ page import="javax.servlet.*" %>
<%@ page import="org.sopeco.frontend.client.helper.MavenInfos" %>

<!doctype html>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">

	<!-- Internet Explorer have to use the latest render mode -->
	<meta http-equiv="X-UA-Compatible" content="IE=edge" >
	
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
   
    <link type="text/css" rel="stylesheet" href="Org_sopeco_frontend.css">

    <!--[if IE]>
	<link rel="stylesheet" type="text/css" href="frontend_style_ie.css" />
	<![endif]-->

    
    <title>SoftwarePerformanceCockpit FrontEnd</title>
   
    <script type="text/javascript">
      var buildInfo = "<%= MavenInfos.getInfoString(application) %>";
    </script>
    <script type="text/javascript" language="javascript" src="sopeco_frontend/sopeco_frontend.nocache.js"></script>
    
  </head>


  <body>

    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>

   
   <img src="images/loader_circle.gif" id="loadingIndicator" />
   
  </body>
</html>