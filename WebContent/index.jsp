<!DOCTYPE html>
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Interactive Organization Chart Demo</title>
  <link rel="stylesheet" href="css/bootstrap.min.css" />
  <link rel="stylesheet" href="css/jquery.jOrgChart.css" />
  <link rel="stylesheet" href="css/custom.css" />
  <link href="css/prettify.css" type="text/css" rel="stylesheet" />
  <link rel="stylesheet" href="fancybox/jquery.fancybox.css" type="text/css" />
  <link rel="stylesheet" type="text/css" href="css/custom.form.css" media="all" />
  <script type="text/javascript" src="js/prettify.js"></script>

  <!-- jQuery includes -->
  <script type="text/javascript" src="http://code.jquery.com/jquery-1.7.1.js"></script>
  <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
  <!-- jQuery autocomplete stylesheet -->
  <link rel="stylesheet" href="//code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
  <!-- Bootstrap switch -->
  <link rel="stylesheet" href="css/bootstrap-switch.css">
  <script type="text/javascript" src="js/bootstrap-switch.js"></script>
  <!-- jQuery Form Plugin -->
  <!-- <script src="//oss.maxcdn.com/jquery.form/3.50/jquery.form.min.js"></script> -->
  <script type="text/javascript" src="fancybox/jquery.fancybox.js"></script>
  <script src="js/jquery.jOrgChart.js"></script>
  <script src="js/index.js"></script>
</head>

<body onload="prettyPrint();">
  <div class="topbar">
    <div class="topbar-inner">
      <div class="container">
        <a class="brand" href="#">Interactive Organization Chart Demo</a>
        <div class="pull-right">
          <input type="checkbox" name="edit-checkbox" checked>
        </div>
        <div></div>
        <div class="pull-right">
          <div class="alert-message info" id="update_button">Save Chart</div>
        </div>
        <div></div>
        <div class="pull-right">
          <div class="alert-message info" id="show-list">Show List</div>
          <pre class="prettyprint lang-html" id="list-html" style="display:none"></pre> 
        </div>
      </div>
    </div>
  </div>

  <!-- check for session to display chart -->
  <script>
  //          $.ajax({
  //                  type: "GET",
  //                  data: {Name:null, Node:"root",Location:null,Email:null,Title:null,Image:null,Phone:null},

  //                  url : "AddNode.do",
  //                  success: function (respose, text, xhr)
  //                   {
  //                      //alert(text+", "+respose);
  //                      //location.reload();
  //                    },
  //                    error: function(xhr, textstatus, ethrown)
  //                    {
  //                      alert(textstatus+", "+ethrown +xhr.status);
  //                      //location.reload();
  //                  }

  //                });
  </script>

  <% if (session.getAttribute( "empchart") != null){
      System.out.println(session.getAttribute( "empchart"));%>
  <%=session.getAttribute( "empchart")%>
  <% } else { %>
  <%} %>

  <!-- Display chart -->
  <ul id='org' style='display:none'>
  </ul>
  <div id="chart" class="orgChart"></div>

  <!-- Edit node Form -->
  <div id="fancy_edit" class="hidden">
    <div id="fancy_edit_unit" class="cform">
      <h1>Edit Unit Profile</h1>
      <form id="edit_unit_form" method="GET" action=".">

        <p class="contact">
          <label for="unit name">Unit Name</label>
        </p>
        <input type="text" placeholder="Unit Name" name="edit_unit_name" id="edit_unit_name" required/>
        <br>
        <br>

        <p class="contact">
          <label for="unit discription">Unit Description</label>
        </p>
        <input type="text" placeholder="Unit Description" name="edit_unit_description" id="edit_unit_description" />
        <br>
        <input id="edit_node" class="buttom" type="button" value="Save" />
      </form>
    </div>
    <div id="fancy_edit_position" class="cform">
      <h1>Edit Position Profile</h1>
      <form id="edit_unit_form" method="GET" action=".">

        <p class="contact">
          <label for="position name">Position Name</label>
        </p>
        <input type="text" placeholder="Position Name" name="edit_position_name" id="edit_position_name" required/>
        <br>
        <br>

        <p class="contact">
          <label for="position discription">Position Description</label>
        </p>
        <input type="text" placeholder="Position Description" name="edit_position_description" id="edit_position_description" />
        <br>
        <input id="edit_node" class="buttom" type="button" value="Save" />
      </form>
    </div>
  </div>

  <!-- Add new Unit or Position Form -->
  <div id="fancy" class="hidden">
    <div class="cform">
      <h1>Add Unit/Position</h1>
      <form id="add_node_form" method="GET" action=".">
        <p class="contact">
          <label for="chart_type">Chart Type</label>
        </p>
        <label for="unit">
          <input type="radio" class="chart_type" id="unit" name="chart_type" value="unit" />unit</label>
        <label for="position">
          <input type="radio" class="chart_type" id="position" name="chart_type" value="position" />position</label>
        <br>
      </form>
    </div>
    <div id="fancy_unit" class="cform">
      <!--        <h1>New Unit/Position Profile</h1>       -->
      <form id="add_unit_form" method="GET" action=".">

        <p class="contact">
          <label for="unit name">Unit Name</label>
        </p>
        <input type="text" placeholder="Unit Name" name="new_unit_name" id="new_unit_name" required/>
        <br>
        <br>

        <p class="contact">
          <label for="unit discription">Unit Description</label>
        </p>
        <input type="text" placeholder="Unit Description" name="new_unit_description" id="new_unit_description" />
        <br>
        <input id="add_node" class="buttom" type="button" value="Add" />
      </form>
    </div>
    <div id="fancy_position" class="cform">
      <!--        <h1>New Unit/Position Profile</h1>       -->
      <form id="add_position_form" method="GET" action=".">

        <p class="contact">
          <label for="Position name">Position Name</label>
        </p>
        <input type="text" placeholder="Position Name" name="new_position_name" id="new_position_name" required/>
        <br>
        <br>

        <p class="contact">
          <label for="Position discription">Position Description</label>
        </p>
        <input type="text" placeholder="Position Description" name="new_position_description" id="new_position_description" />
        <br>
        <input id="add_node" class="buttom" type="button" value="Add" />
      </form>
    </div>
  </div>

  <!-- Add/edit employee Form -->
  <div id="fancy_employee" class="hidden">
    <div id="add_employee" class="cform">
      <h1>Add Employee</h1>
      <form id="add_employee_form" method="GET" action=".">
        <p class="contact">
          <label for="chart_type">Employee Type</label>
        </p>
        <label>
          <input type="radio" class="employee_type" id="new_employee" name="employee_type" value="new_employee" />new employee</label>
        <label>
          <input type="radio" class="employee_type" id="curr_employee" name="employee_type" value="curr_employee" />current employee</label>

      </form>
    </div>
    <div id="edit_employee" class="cform">
      <h1>edit/delete Employee</h1>
      <form id="edit_employee_option_form" method="GET" action=".">
        <p class="contact">
          <label for="chart_type">edit/delete</label>
        </p>
        <label>
          <input type="radio" class="action" id="edit_employee" name="change" value="edit_employee" />edit employee</label>
        <label>
          <input type="radio" class="action" id="delete_employee" name="change" value="delete_employee" />delete employee</label>

      </form>
    </div>

    <div id="fancy_new_employee" class="cform">
      <!--        <h1>New Employee Profile</h1>       -->
      <form id="add_employee_form" method="GET" action=".">
        <p class="contact">
          <label for="name">First Name<span style="color:red;">*</span></label>
        </p>
        <input type="text" placeholder="First name" name="new_first_name" id="new_first_name" required/>

        <p class="contact">
          <label for="name">Last Name<span style="color:red;">*</span></label>
        </p>
        <input type="text" placeholder="Last name" name="new_last_name" id="new_last_name" required/>

        <p class="contact">
          <label for="employee title">Employee Title</label>
        </p>
        <input type="text" placeholder="Employee Title" name="new_node_title" id="new_node_title" />

        <p class="contact">
          <label for="location">Location</label>
        </p>
        <input type="text" placeholder="Location Name" name="new_node_location" id="new_node_location" />
        <br>

        <p class="contact">
          <label for="email">Email</label>
        </p>
        <input type="text" placeholder="Email Name" name="new_node_email" id="new_node_email" />
        <br>


        <p class="contact">
          <label for="phone">Phone Number</label>
        </p>
        <input type="text" placeholder="Phone Number" name="new_node_phone" id="new_node_phone" />
        <br>


        <p class="contact">
          <label for="image">Image</label>
        </p>
        <input id="new_node_image" type="file" name="new_node_image" size="chars">
        <br>
        <input id="add_employee" class="buttom" type=submit value="Add" />
      </form>
    </div>

    <div id="fancy_edit_employee" class="cform">
      <!--         <h1>Edit Employee Profile</h1>       -->
      <form id="edit_employee_form" action="EditNode.do" method="post" enctype="multipart/form-data">
        <p class="contact">
          <label for="edit_first_name">First Name<span style="color:red;">*</span></label>
        </p>
        <input type="text" placeholder="First name" name="edit_first_name" id="edit_first_name" required/>

        <p class="contact">
          <label for="edit_last_name">Last Name<span style="color:red;">*</span></label>
        </p>
        <input type="text" placeholder="Last name" name="edit_last_name" id="edit_last_name" required/>


        <p class="contact">
          <label for="edit_node_title">Employee Title</label>
        </p>
        <input type="text" placeholder="Employee Title" name="emp_title" id="edit_node_title" />
        <br>


        <p class="contact">
          <label for="edit_node_location">Location</label>
        </p>
        <input type="text" placeholder="Location" name="location" id="edit_node_location" />
        <br>

        <p class="contact">
          <label for="edit_node_email">Email</label>
        </p>
        <input type="text" placeholder="Email Name" name="email" id="edit_node_email" />
        <br>


        <p class="contact">
          <label for="edit_node_phone">Phone Number</label>
        </p>
        <input type="text" placeholder="Phone Number" name="phone" id="edit_node_phone" />
        <br>


        <p class="contact">
          <label for="edit_node_image">Image</label>
        </p>
        <input id="edit_node_image" type="file" name="image" size="chars">
        <br>
        <input id="edit_emp" class="buttom" type=submit value="Save" />
        <br>
        <div id="output"></div>

      </form>
    </div>
    <div id="fancy_delete_employee" class="cform">
      <!--         <h1>Edit Employee Profile</h1>       -->
      <form id="delete_employee_form" method="GET" action=".">
        <input id="delete_emp" class="buttom" type="button" value="Delete" />
      </form>
    </div>
    <div id="fancy_curr_employee" class="cform">
      <!--         <h1>Edit Employee Profile</h1>       -->
      <form id="add_curr_employee_form" method="GET" action=".">
        <p class="contact">
          <label for="curr_emp">Employee Name</label>
        </p>
        <input id="employees" />
        <input id="add_curr" class="buttom" type="button" value="Add" disabled/>
      </form>
    </div>
  </div>
  <script>
  jQuery(document).ready(function() {
    // Enable edit button
    $("[name='edit-checkbox']").bootstrapSwitch();

    /* Custom jQuery */
    $("#show-list").click(function(e) {
      e.preventDefault();

      $('#list-html').toggle('fast', function() {
        if ($(this).is(':visible')) {
          $('#show-list').text('Hide List');
          $(".topbar").fadeTo('fast', 0.9);
        } else {
          $('#show-list').text('Show List ');
          $(".topbar").fadeTo('fast', 1);
        }
      });
    });

    $('#list-html').text($('#org').html());

    $("#org").bind("DOMSubtreeModified", function() {
      $('#list-html').text('');

      $('#list-html').text($('#org').html());

      prettyPrint();
    });
  });


  // Update Chart
  $("#update_button").click(function() {
    var updatestrg = $('#org').html();
    $.ajax({
      type: 'Post',
      dataType: 'text',
      data: {
        UptString: updatestrg
      },
      url: 'UpdateChart.do',
      success: function(respose, text, xhr) {
        alert("Successfully Saved");
        //location.reload();
      },
      error: function(xhr, textstatus, ethrown) {
        alert(textstatus + ", " + ethrown);
      }
    });
  });
  </script>


</body>

</html>
