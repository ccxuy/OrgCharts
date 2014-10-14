<!DOCTYPE html>
<html>

<head>
  <title>Interactive Organization Chart Employee Profile</title>
  <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7; IE=EmulateIE9">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no" />
  <link rel="stylesheet" type="text/css" href="css/style.css" media="all" />
  <link rel="stylesheet" type="text/css" href="css/demo.css" media="all" />
</head>

<body>
  <div class="container">
    <!-- freshdesignweb top bar -->
    <div class="freshdesignweb-top">
      <a href="index.jsp" target="_blank">Home</a>
      <span class="right">
      </span>
      <div class="clr"></div>
    </div>
    <!--/ freshdesignweb top bar -->
    <header>
      <h1>
        <span>Interactive Organization Chart</span>Employee Profile</h1>
    </header>

<%
String add_strg = (String)request.getParameter("AddString");
//String add_strg = request.getParameter("AddString");
System.out.println("add_strg: "+add_strg); 
session.setAttribute("empchart",add_strg); 
%>   

    <div class="form">
      <form id="profile" method="GET" action="AddChart.do">

        <p class="contact">
          <label for="name">Name</label>
        </p>
        <input id="name" name="name" placeholder="First and last name" value="" required="" tabindex="1" type="text">

        <p class="contact">
          <label for="empid">Employee ID</label>
        </p>
        <input id="empid" name="empid" placeholder="111111" tabindex="2" type="text">

        <p class="contact">
          <label for="title">Title</label>
        </p>
        <input id="title" name="title" placeholder="Project Manager" tabindex="2" type="text">

        <p class="contact">
          <label for="location">Work Location</label>
        </p>
        <input id="location" name="location" placeholder="North Pearl Room 222" tabindex="2" type="text">

        <p class="contact">
          <label for="department">Department</label>
        </p>
        <input id="department" name="department" placeholder="Informatics" tabindex="2" type="text">

        <p class="contact">
          <label for="gender">Gender</label>
        </p>
        <select class="select-style gender" name="gender">
          <option value="m">Male</option>
          <option value="f">Female</option>
          <option value="others">Other</option>
        </select>
        <br>
        <br>

        <p class="contact">
          <label for="email">Email</label>
        </p>
        <input id="email" name="email" placeholder="userid@health.state.ny.us" type="email">

        <p class="contact">
          <label for="phone">Phone number</label>
        </p>
        <input id="phone" name="phone" placeholder="phone number" type="text">
        <p class="contact">
          <label for="image">Image</label>
        </p>
        <input id="image" type="file" name="image" size="chars">
        <br>
        <input class="buttom" name="submit" id="submit" tabindex="5" value="Save" type="submit">
      </form>
    </div>
  </div>

</body>

</html>
