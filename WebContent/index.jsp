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
  <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
  <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/jquery-ui.min.js"></script>
  <!-- jQuery autocomplete stylesheet -->
  <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.1/themes/smoothness/jquery-ui.css"/>

  <script type="text/javascript" src="fancybox/jquery.fancybox.js"></script>
  <script src="js/jquery.jOrgChart.js"></script>
  <script>
  $.ajaxSetup({
    cache: false
  });

  function ajax0() {
    console.log("load file");
    $('#org').children().remove();
    return $.ajax({
      type: "GET",
      data: {
        fileName: ""
      },
      url: "LoadChart.do",
      success: function(respose, text, xhr) {
        //if(respose!=$('#org').html()){
        //alert("in here");
        $('#org').append(respose);
        //init_tree();
        //location.reload();
        //          alert(respose);
        //          alert(text);
        //alert(xhr.text());
        //}
        //else{
        //alert("text has NOT changed.");
        console.log("finished loading file");
        //}
      },
      error: function(xhr, textstatus, ethrown) {
        alert(textstatus + ", " + ethrown + xhr.status);
        //location.reload();
      }
    });
  }

  var node_type = "";
  var id_num = "";
  var load = true;
  var opts = {
    chartElement: '#chart',
    dragAndDrop: true
  };

  function init_tree() {
    console.log("in init_tree");
    if (load == true) {
      load = false;
      $.when(ajax0()).done(function() {
        $("#chart").html("");
        $("#org").jOrgChart(opts);
        get_emp_info();
        reset_forms();
      });
    } else {
      $("#chart").html("");
      $("#org").jOrgChart(opts);
      get_emp_info();
    }
  };

  //resets all forms to initial state
  function reset_forms() {
    $("#fancy_unit").hide();
    $("#fancy_position").hide();
    $("#fancy_new_employee").hide();
    $("#fancy_edit_unit").hide();
    $("#fancy_edit_position").hide();
    $("#fancy_edit_employee").hide();
    $("#add_employee").hide();
    $("#edit_employee").hide();
    $("#fancy_delete_employee").hide();
    $("#fancy_curr_employee").hide();
    $("input[type=radio][name=chart_type]").attr('checked', false);
    $("input[type=radio][name=employee_type]").attr('checked', false);
    $("input[type=radio][name=change]").attr('checked', false);
    $("#add_curr").attr("disabled", true);
    $("#employees").val("");
    console.log("done with init_tree");
  };

  //recusive method to adjust the class values of all the decendent nodes of $ul after one child node has been removed.
  function adjustChildren($ul, startnum, cutPoint) {
    if ((cutPoint == 0 && ($ul.children().length) - 1 > 0) || cutPoint != 0) {
      //alert("in first if");
      var toRemove = "";
      var type = "";
      var toAdd = "";
      var classes;
      var part1 = "";
      var part2 = "";
      var part3 = "";
      //alert($ul.children().length);
      var childList = $ul.children().slice(startnum);
      //alert(childList.length);
      $.each(childList, function(index, item) {
        //alert($(item));
        var classList = $(item).attr('class').split(/\s+/);
        $.each(classList, function(index, item) {
          if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
            toRemove = item;
          }
          if (item == "unit" || item == "position") {
            type = item;
          }
        });
        classes = toRemove.split('_');
        part1 = toRemove.substring(0, toRemove.length - (1 + 2 * cutPoint));
        part2 = parseInt(classes[classes.length - (1 + cutPoint)], 10) - 1;
        if (cutPoint != 0) {
          part3 = toRemove.substring(toRemove.length - (1 + cutPoint));
        }
        toAdd = part1 + part2 + part3;
        $(item).removeClass(toRemove);
        $(item).removeClass(type);
        $(item).addClass(toAdd);
        $(item).addClass(type);

        if ($(item).children('ul').children().length > 0) {
          //alert("in if");
          adjustChildren($(item).children('ul'), 0, cutPoint + 1);
        }
      });
    }
  };

  $.fn.exists = function() {
    return this.length !== 0;
  };

  //get employee infromation for all employees in the org chart from the emp table in the database and puts in info in the nodes where the employees are located respectively
  function get_emp_info() {
    //gets stored data for employee
    var empList = $("#chart").find("div.position");
    //alert(empList.length);
    $.each(empList, function(index, item) {
      //alert($(item).attr("class"));
      if ($(item).attr("id") != null) {
        //alert('in node if');
        empId = $(item).attr('id');
        var node = $(item);
        get_emp(empId, node);
      }
    });
    emplist = "";
    return;
  }

  /**gets the employee information from the database of the specific employee with the specified id and puts the info
     in the specified node
  */
  function get_emp(id, node) {
    var fn;
    var ln;
    var email;
    var image;
    //alert(empId);
    function ajax3() {
      return $.ajax({
        type: "GET",
        data: {
          empId: id
        },
        url: "getEditedData.do",
        success: function(respose, text, xhr) {
          //alert(text+","+respose);
          //location.reload();
          //first_name
          if (respose.split(',')[0] != " ") {
            fn = respose.split(',')[0];
          } else {
            fn = "";
          }
          //last_name
          if (respose.split(',')[1] != " ") {
            ln = respose.split(',')[1];
          } else {
            ln = "";
          }
          //email
          if (respose.split(',')[2] != " ") {
            email = respose.split(',')[2];
          } else {
            email = "";
          }
          //image
          if (respose.split(',')[3] != "null") {
            image = respose.split(',')[3];
          } else {
            image = "";
          }

          //alert(temp);
        },
        error: function(xhr, textstatus, ethrown) {
          alert(textstatus + ", " + ethrown + xhr.status);
          //location.reload();
        }
      });
    }
    $.when(ajax3()).done(function() {
      //              if(node.children("div[class=empinfo]").length>0){
      //                node.children("div[class=empinfo]").remove();
      //              }
      var nodeText = "<div class='empinfo'></span>";

      //for employee first name
      var append_first = "<span class='label_node' id='fn'>" + fn + "</span>" + "<br>";

      //for employee last name
      var append_last = "<span class='label_node' id='ln'>" + ln + "</span>" + "<br>";

      //for employee email
      var append_email = "<span class = 'label_node' id='email'>" + email + "</span>" + "<br>";

      //for emp image
      var append_image = "<img src = '" + image + "' alt = 'emp_image'>";

      //add to employee
      var temp = node.children("div.info");
      var temp1 = node.children("div.opciones");
      temp.detach();
      temp1.detach();
      node.append(nodeText);
      if (fn != "") {
        node.children("div.empinfo").append(append_first);
      }
      if (ln != "") {
        node.children("div.empinfo").append(append_last);
      }
      if (email != "") {
        node.children("div.empinfo").find("li").last().append(append_email);
      }
      if (image != "") {
        node.children("div.empinfo").append(append_image);
      }
      node.append(temp);
      node.append(temp1);
    });
  }

  var click_flag = true;
  var node_to_edit;

  //for emp
  var node_to_title;
  //for deleting an added node
  var node_to_add;
  var title_to_add;
  var empId;
  $(document).on("ready", function() {
    init_tree();
    var add_to_node, del_node, classList;

    // Edit node
    $(".edit").live("click", function(e) {
      classList = $(this).parent().parent().attr('class').split(/\s+/);
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
          add_to_node = item;
          //console.log(".edit", add_to_node);
        }

      });

      node_type = "";
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
          del_node = item;
          console.log(del_node);
        }
        if (item == "position" || item == "unit") {
          node_type = item;
        }
      });
      node_to_edit = $("li." + del_node + ":not('.temp')");
      console.log(node_to_edit.attr("class"));
      console.log(node_type);
      if (node_type == "unit") {
        $("#fancy_edit_employee").hide();
        $("#fancy_edit_position").hide();
        $("#fancy_edit_unit").show();
        $("#edit_unit_name").val(node_to_edit.find("> .label_node[id=un]").text());
        $("#edit_unit_description").val(node_to_edit.find("> .label_node[id=ud]").text());
      } else if (node_type == "position") {
        $("#fancy_edit_employee").hide();
        $("#fancy_edit_unit").hide();
        $("#fancy_edit_position").show();
        $("#edit_position_name").val(node_to_edit.find("> .label_node[id=pn]").text());
        $("#edit_position_description").val(node_to_edit.find("> .label_node[id=pd]").text());
      } else {
        $("#fancy_edit_employee").hide();
        $("#fancy_edit_position").hide();
        $("#fancy_edit_unit").show();
        $("#edit_unit_name").val(node_to_edit.find("> .label_node:eq(0)").text());
      }

    }).fancybox({

      maxWidth: 550,
      maxHeight: 300,
      fitToView: false,
      width: '70%',
      height: '70%',
      autoSize: false,
      closeClick: false,
      openEffect: 'elastic',
      closeEffect: 'elastic',
      afterClose: function() {
        reset_forms();
      }
    });


    $("input[type=button][id=edit_node]").click(function() {
      if (node_type == "unit") {
        var unitname_field = $("#edit_unit_name");
        var name = unitname_field.val();

        var unitdes_field = $("#edit_unit_description");
        var description = unitdes_field.val();

        node_to_edit.find("> .label_node[id=un]").text(name);
        unitname_field.val("");
        node_to_edit.find("> .label_node[id=ud]").text(description);
        unitdes_field.val("");

      }
      if (node_type == "position") {
        var unitname_field = $("#edit_position_name");
        var name = unitname_field.val();

        var unitdes_field = $("#edit_position_description");
        var description = unitdes_field.val();

        node_to_edit.find("> .label_node[id=pn]").text(name);
        unitname_field.val("");
        node_to_edit.find("> .label_node[id=pd]").text(description);
        unitdes_field.val("");
      } else {
        var unitname_field = $("#edit_unit_name");
        var name = unitname_field.val();

        node_to_edit.find("> .label_node:eq(0)").text(name);
        unitname_field.val("");
        node_to_edit.find("> .label_node:eq(0)").attr('id', 'un');
        node_to_edit.addClass('unit');
      }

      $.fancybox.close();
      init_tree();

    });


    // Add Node
    $(".add").live("click", function() {
      click_flag = false;
      node_type = "";
      classList = $(this).parent().parent().attr('class').split(/\s+/);
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
          add_to_node = item;
        }
      });
    }).fancybox({
      maxWidth: 550,
      maxHeight: 300,
      fitToView: false,
      width: '70%',
      height: '70%',
      autoSize: false,
      closeClick: false,
      openEffect: 'elastic',
      closeEffect: 'elastic',
      afterClose: function() {
        click_flag = true;
        reset_forms();
      }
    });

    $("input[type=radio][name=chart_type]").click(function() {
      //alert("clicked");
      node_type = $(this).val();
      //alert(node_type);
      if (node_type == "position") {
        $("#fancy_unit").hide();
        $("#fancy_position").show();
      } else {
        $("#fancy_position").hide();
        $("#fancy_unit").show();
      }
    });

    $("input[type=button][id=add_node]").click(function() {
      //e.preventDefault();

      //aggregate consumer unit, add li to the list, and refresh tree
      //for image
      // var img_file = $("#new_node_image");
      // var empimage = $("#new_node_image").val();
      // var textImg = img_file.val();
      // img_file.val("");
      var append_type = "";
      var $node = $("li." + add_to_node + ":not('.temp')");
      var childs = $node.find("ul:eq(0) > li:not('.temp')").size();
      if (node_type == "position") {
        //for position name
        var type_nodo = "";
        var text_field = $("#new_position_name");
        var texto = text_field.val();
        text_field.val("");

        //for position discription
        var last_field = $("#new_position_description");
        var last = last_field.val();
        last_field.val("");


        //var childs = $node.find("ul:eq(0) > li:not('.temp')").size();     
        childs++;

        //for position name
        type_nodo += "child " + add_to_node + "_" + childs;
        var append_text = "<li class='" + type_nodo + " position'>";
        append_text += "<span class='label_node' id='pn'>" + texto + "</span>" + "<br>";

        //for position discription
        var append_last = "<span class='label_node' id='pd'>" + last + "</span>" + "<br>";

        //for node type
        //append_type="<div id='type'><span class='label_node' id='type'>" + node_type + "</span></div>" + "<br>";
        if ($node.find("ul").size() == 0) {
          //unit name
          append_text = "<ul>" + append_text + "</ul>";
          $node.append(append_text);

          //unit discription
          if (last != "") {
            //append_last = "<ul>" + append_last + "</ul>";
            $node.find("li").last().append(append_last)
          }

          //node type
          //append_type = "<ul>" + append_type + "</ul>"
          //$node.find("li").last().append(append_type);
        } else {
          $node.find("ul:eq(0)").append(append_text);
          if (last != "") {
            $node.find("li").last().append(append_last);
          }
          //$node.find("li").last().append(append_type);
        }
      } else {
        //for unit name
        var type_nodo = "";
        var text_field = $("#new_unit_name");
        var texto = text_field.val();
        text_field.val("");

        //for unit discription
        var last_field = $("#new_unit_description");
        var last = last_field.val();
        last_field.val("");

        //var $node = $("li."+add_to_node+":not('.temp')");
        //var childs = $node.find("ul:eq(0) > li:not('.temp')").size();
        childs++;

        //for unit name
        type_nodo += "child " + add_to_node + "_" + childs;
        var append_text = "<li class='" + type_nodo + " unit'>";
        append_text += "<span class='label_node' id='un'>" + texto + "</span>" + "<br>";

        //for unit discription
        var append_last = "<span class='label_node' id='ud'>" + last + "</span>" + "<br>";

        //for node type
        //append_type="<span class='label_node' id='type'>" + node_type + "</span>" + "<br>";
        if ($node.find("ul").size() == 0) {
          //unit name
          append_text = "<ul>" + append_text + "</ul>";
          $node.append(append_text);

          //unit discription
          if (last != "") {
            //append_last = "<ul>" + append_last + "</ul>";
            $node.find("li").last().append(append_last);
          }

          //node type
          //append_type = "<ul>" + append_type + "</ul>"
          //$node.find("li").last().append(append_type);
        } else {
          $node.find("ul:eq(0)").append(append_text);
          if (last != "") {
            $node.find("li").last().append(append_last);
          }
          //$node.find("li").last().append(append_type);
        }
      }

      init_tree();
      $.fancybox.close();
    });

    //add/edit employee
    $(".emp").live("click", function(e) {
      click_flag = false;
      node_type = "";
      classList = $(this).parent().parent().attr('class').split(/\s+/);
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
          add_to_node = item;
        }
      });
      if ($(this).parent().parent().attr('id') != null) {
        $("#edit_employee").show();
        $("#fancy_new_employee").hide();
        $("#add_employee").hide();
      } else {
        $("#edit_employee").hide();
        $("fancy_new_employee").hide();
        $("#add_employee").show();
      }

    }).fancybox({
      maxWidth: 550,
      maxHeight: 2000,
      fitToView: false,
      width: '70%',
      height: '70%',
      autoSize: false,
      closeClick: false,
      openEffect: 'elastic',
      closeEffect: 'elastic',
      afterClose: function() {
        click_flag = true;
        reset_forms();
      }
    });

    $("input[type=radio][name=employee_type]").click(function() {
      //alert("clicked");
      //alert(node_type);
      if ($(this).val() == "new_employee") {
        $("#fancy_curr_employee").hide();
        $("#fancy_new_employee").show();
      } else {
        $("#fancy_new_employee").hide();
        var emp_list = null;
        var options = "";
        var change = "";
        var tag = "";

        function ajax3() {
          return $.ajax({
            type: "GET",
            url: "RetrieveEmpData.do",
            success: function(respose, text, xhr) {
              emp_list = respose.split(",");
            },
            error: function(xhr, textstatus, ethrown) {
              alert(textstatus + ", " + ethrown + xhr.status);
              //location.reload();
            }
          });
        }
        $.when(ajax3()).done(function() {
          options = emp_list;
          $.each(emp_list, function(index, item) {
            tag = item.split(":");
            change = {
              "value": tag[1] + " " + tag[2],
              "id": tag[0]
            };
            options[index] = change;
          });
          $("#employees").autocomplete({
            source: options,
            appendTo: "#fancy_curr_employee",
            select: function(event, ui) {
              $("input[type=button][id=add_curr]").prop('disabled', false);
              id_num = ui.item.id;
            },
          });
          $("#fancy_curr_employee").show();
        });
      }
    });

    $("input[type=radio][name=change]").click(function() {
      //alert("clicked");
      //alert(node_type);
      if ($(this).val() == "edit_employee") {
        $("#fancy_new_employee").hide();
        $("#fancy_delete_employee").hide();
        var fn = "";
        var ln = "";
        var email = "";
        var image = "";
        node_to_edit = $("li." + add_to_node + ":not('.temp')");
        var empId = node_to_edit.attr("id");

        function ajax2() {
          return $.ajax({
            type: "GET",
            data: {
              empId: empId
            },
            url: "getEditedData.do",
            success: function(respose, text, xhr) {
              //alert(text+","+respose);
              //location.reload();
              fn = respose.split(',')[0];
              ln = respose.split(',')[1];
              email = respose.split(',')[2];
              image = respose.split(',')[3];
              //alert(temp);
            },
            error: function(xhr, textstatus, ethrown) {
              alert(textstatus + ", " + ethrown + xhr.status);
              //location.reload();
            }
          });
        }
        $.when(ajax2()).done(function() {
          $("#fancy_edit_unit").hide();
          $("#fancy_edit_employee").show();
          $("#edit_first_name").val(fn);
          $("#edit_last_name").val(ln);
          $("#edit_node_title").val(node_to_edit.find("> .label_node[id=title]").text());
          $("#edit_node_location").val(node_to_edit.find("> .label_node[id=loc]").text());
          $("#edit_node_email").val(email);
          $("#edit_node_phone").val(node_to_edit.find("> .label_node[id=phone]").text());
          empId = node_to_edit.find("> .label_node[id=empId]").text();
          if (node_to_edit.find("img").length != 0) {
            $("#edit_node_image").val(image);
          }
        });
        $("#fancy_edit_employee").show();
      } else {
        $("#fancy_new_employee").hide();
        $("#fancy_delete_employee").show();
        $("#fancy_edit_employee").hide();
      }
    });

    $("input[type=button][id=delete_emp]").click(function() {
      var $node = $("li." + add_to_node + ":not('.temp')");
      var $div = $("#chart").find("div." + add_to_node);
      $node.removeAttr("id");
      $node.children("div[class='info']").remove();
      $div.removeAttr("id");
      $div.children("div[class='empinfo']").remove();
      $div.children("div[class='info']").remove();
      init_tree();
      $.fancybox.close();
    });

    $("input[type=button][id=add_employee]").click(function() {
      //for employee first name
      var text_field = $("#new_first_name");
      var texto = "";
      if (text_field.val() != "") {
        texto = text_field.val();
      } else {
        texto = " ";
      }
      text_field.val("");
      //for employee last name
      var last_field = $("#new_last_name");
      var last = "";
      if (last_field.val() != "") {
        last = last_field.val();
      } else {
        last = " ";
      }
      last_field.val("");
      //for employee title
      var text_title = $("#new_node_title");
      var titletexto = text_title.val();
      text_title.val("");
      var $node = $("li." + add_to_node + ":not('.temp')");
      var $div = $("#chart").find("div." + add_to_node);
      //for employee first name
      //var append_text = "<span class='label_node' id='fn'>" + texto + "</span>" + "<br>";

      //for employee last name
      //var append_last="<span class='label_node' id='ln'>" + last + "</span>" + "<br>";

      //for employee title
      var append_title = "";
      append_title += "<span class = 'label_node' id='title'>" + titletexto + "</span>" + "<br>";

      //for employee location
      var loc_field = $("#new_node_location");
      var location = loc_field.val();
      loc_field.val("");
      var append_location = "";
      append_location += "<span class = 'label_node' id='loc'>" + location + "</span>" + "<br>";

      //for employee email
      var email_field = $("#new_node_email");
      //alert(email_field.val());
      var email = "";
      if (email_field.val() != "") {
        email = email_field.val();
      } else {
        email = " ";
      }
      email_field.val("");
      //var append_email="";
      //append_email += "<span class = 'label_node' id='email'>" + email + "</span>" + "<br>";

      //for employee phone
      var phone_field = $("#new_node_phone");
      var phone = phone_field.val();
      phone_field.val("");
      var append_phone = "";
      append_phone += "<span class = 'label_node' id='phone'>" + phone + "</span>" + "<br>";

      //for employee image
      var img_field = $("#new_node_image");
      //alert(img_field.val());
      var img = "";
      //if(img_field.val()!=""){
      img = img_field.val();
      //}
      img_field.val("");

      //for node type
      //append_type="<span class='label_node' id='type'>" + node_type + "</span>" + "<br>";

      //for emp image
      //var append_image = "";
      //append_image += "<img src = '" + img + "' alt = 'emp_image'>";

      //var text_field = $("#new_node_name");
      //var empname = $("#new_node_name").val();            
      //var texto = text_field.val();
      //text_field.val(""); 
      //var append_Id="";
      //$node.append(append_text);
      //                         if(last!=""){
      //                          $node.append(append_last);
      //                         }
      //for emp title
      $node.append("<div class=info>");
      $node.append("</div>");
      if (titletexto != "") {
        //make sure the title is appended at the end of the list NOT before
        $node.children("div.info").append(append_title);
      }
      if (location != "") {
        $node.children("div.info").append(append_location);
      }
      //$node.find("li").last().append(append_email);
      if (phone != "") {
        $node.children("div.info").append(append_phone);
      }
      //$node.children("div.info").append(append_type);
      //                      if(img!=""){
      //                        $node.append(append_image);
      //                      }
      $div.append($node.children("div.info"));
      //$node.children("div.info").children("span[id=type]").hide();
      function ajax1() {
        return $.ajax({
          type: "GET",
          data: {
            firstName: texto,
            lastName: last,
            Image: img,
            Email: email
          },
          url: "AddNode.do",
          success: function(respose, text, xhr) {
            //alert(text+","+respose);
            //location.reload();
            empId = respose;
            //alert(empId);
          },
          error: function(xhr, textstatus, ethrown) {
            alert(textstatus + ", " + ethrown + xhr.status);
            //location.reload();
          }
        });
      }
      $.when(ajax1()).done(function() {
        //alert(empId);
        //append_Id = "<span class='label_node' id='empId'>" + empId + "</span>" + "<br>";
        $node.attr("id", empId);
        $div.attr("id", empId);
        get_emp(empId, $div);
        //$('span[id=empId]').hide();
        //init_tree();
        reset_forms();
      });
      $.fancybox.close();
    });

    //add current employee
    $("input[type=button][id=add_curr]").click(function() {
      var $node = $("li." + add_to_node + ":not('.temp')");
      var $div = $("#chart").find("div." + add_to_node);
      $node.attr("id", id_num);
      $div.attr("id", id_num);
      get_emp(id_num, $div);
      reset_forms();
      $.fancybox.close();
    });

    //edit employee
    $("input[type=button][id=edit_emp]").click(function() {
      node_to_edit = $("li." + add_to_node + ":not('.temp')");
      var loc_field = $("#edit_node_location");
      var locval = $("#edit_node_location").val();
      var location = loc_field.val();

      var email_field = $("#edit_node_email");
      var emailval = $("#edit_node_email").val();
      var email = email_field.val();

      var phone_field = $("#edit_node_phone");
      var phoneval = $("#edit_node_phone").val();
      var phone = phone_field.val();

      var firstname_field = $("#edit_first_name");
      var nameval = $("#edit_first_name").val();
      var texto = firstname_field.val();

      var lastname_field = $("#edit_last_name");
      var last = lastname_field.val();

      var img_field = $("#edit_node_image");
      var imgval = $("#edit_node_image").val();
      var image = img_field.val();

      var title_field = $("#edit_node_title");
      var titleval = $("#edit_node_title").val();
      var titletexto = title_field.val();

      //ajax call to edit node info in the database   
      $.ajax({
        type: "GET",
        data: {
          firstName: texto,
          lastName: last,
          Email: email,
          Image: image,
          emp_Id: empId
        },
        url: "EditNode.do",
        success: function(respose, text, xhr) {
          //alert(text+", "+respose);
        },
        error: function(xhr, textstatus, ethrown) {
          //alert(textstatus+", "+ethrown +xhr.status);
        }
      });
      var dn = '';
      //first name
      firstname_field.val("");
      //last name
      lastname_field.val("");
      //employee title
      if (node_to_edit.find("> .label_node[id=title]").exists()) {
        if (titletexto != "") {
          node_to_edit.find("> .label_node[id=title]").text(titletexto);
        } else {
          node_to_edit.find("> .label_node[id=title]").remove();
        }
      } else {
        if (titletexto != "") {
          dn = "<span class='label_node' id='title'>" + titletexto + "</span><br>";
          node_to_edit.last().append(dn);
        }
      }
      title_field.val("");
      //employee phone
      if (node_to_edit.find("> .label_node[id=phone]").exists()) {
        if (phone != "") {
          node_to_edit.find("> .label_node[id=phone]").text(phone);
        } else {
          node_to_edit.find("> .label_node[id=phone]").remove();
        }
      } else {
        if (phone != "") {
          dn = "<span class='label_node' id='phone'>" + phone + "</span><br>";
          node_to_edit.append(dn);
        }
      }
      phone_field.val("");
      //employee email
      email_field.val("");
      //employee location
      if (node_to_edit.find("> .label_node[id=loc]").exists()) {
        alert("in if");
        if (location != "") {
          alert("in second if");
          node_to_edit.find("> .label_node[id=loc]").text(location);
        } else {
          alert("in second else");
          node_to_edit.find("> .label_node[id=loc]").remove();
        }
      } else {
        alert("in else");
        if (location != "") {
          dn = "<span class='label_node' id='loc'>" + location + "</span><br>";
          node_to_edit.last().append(dn);
        }
      }
      loc_field.val("");
      //employee image
      if (image != "" && node_to_edit.find("img").length != 0) {
        node_to_edit.find("img").attr("src").text(image);
      } else if (image != "" && node_to_edit.find("img").length == 0) {
        var edit_image = "<img src = '" + image + "' alt = 'emp_image'>";
        node_to_edit.append(edit_image);
      } else if (image == "" && node_to_edit.find("img").length != 0) {
        node_to_edit.remove(node_to_edit.find("img"));
      }
      img_field.val("");
      get_emp(node_to_edit.attr("id"), node_to_edit);
      reset_forms();
      $.fancybox.close();

    });

    // Delete Node
    $(".del").live("click", function(e) {
      var nodo = $(this);
      var nodeDiv = null;

      nodeDiv = nodo.parent().parent();

      var cu = nodeDiv.find("a").attr("rel");
      classList = nodeDiv.attr('class').split(/\s+/);
      $.each(classList, function(index, item) {
        if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
          del_node = item;
        }
      });
      node_to_edit = $("li." + del_node + ":not('.temp')");
      //concider removing only if node is not a position node with an employee in it
      if (node_to_edit.attr("id") == null) {
        if (node_to_edit.children('ul').children().length == 0) {
          //if node is a leaf node remove the node completely
          var $ul = node_to_edit.parent();
          var childNum = del_node.split("_")[del_node.length - 1];
          node_to_edit.remove();
          node_to_edit.removeData();
          adjustChildren($ul, childNum, 0);
        } else {
          //if node is not leaf node remove all information but keep the actural node
          node_to_edit.children("span").remove();
          node_to_edit.children("div:not('.opciones')").remove();
        }
        init_tree();
      }

    });



    //Form behavior for emptitle
    //This will get val of employee title when adding and editing nodes
    $("#new_node_title, #edit_node_title").on("keyup", function(evt) {
      var id = $(this).attr("id");
      if ($(this).val() != '') {
        if (id == "new_node_title") {
          $("#add_node").show();

        } else {
          $("#edit_node").show();
        }
      } else {
        if (id == "new_node_title") {
          $("#add_node").hide();
        } else {
          $("#edit_node").hide();
        }
      }
    });

    //Form behavior for node name
    //This will get val for employee name when adding and editing nodes
    $("#new_node_name, #edit_node_name").on("keyup", function(evt) {
      var id = $(this).attr("id");
      if ($(this).val() != '') {

        if (id == "new_node_name") {
          $("#add_node").show();
        } else {
          $("#edit_node").show();
        }
      } else {
        if (id == "new_node_name") {
          $("#add_node").hide();
        } else {
          $("#edit_node").hide();
        }
      }
    });



  });
  </script>
</head>

<body onload="prettyPrint();">
  <div class="topbar">
    <div class="topbar-inner">
      <div class="container">
        <a class="brand" href="#">Interactive Organization Chart Demo</a>
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
<% if (session.getAttribute( "empchart") != null){ System.out.println(session.getAttribute( "empchart"));%>
  <%=session.getAttribute( "empchart")%>
<% } else { %>

<% } %>

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
        <input type="radio" class="employee_type" id="curr_employee" name="employee_type" value="curr_employee" />current employee
        </label>

    </form>
  </div>
  <div id="edit_employee" class="cform">
    <h1>edit/delete Employee</h1>
    <form id="edit_employee_form" method="GET" action=".">
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
        <label for="name">First Name</label>
      </p>
      <input type="text" placeholder="First name" name="new_first_name" id="new_first_name" required/>

      <p class="contact">
        <label for="name">Last Name</label>
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
      <input id="add_employee" class="buttom" type="button" value="Add" />
    </form>
  </div>
  <div id="fancy_edit_employee" class="cform">
    <!--         <h1>Edit Employee Profile</h1>       -->
    <form id="edit_employee_form" method="GET" action=".">
      <p class="contact">
        <label for="name">First Name</label>
      </p>
      <input type="text" placeholder="First name" name="edit_first_name" id="edit_first_name" required/>

      <p class="contact">
        <label for="name">Last Name</label>
      </p>
      <input type="text" placeholder="Last name" name="edit_last_name" id="edit_last_name" required/>


      <p class="contact">
        <label for="employee title">Employee Title</label>
      </p>
      <input type="text" placeholder="Employee Title" name="emp_title" id="edit_node_title" />
      <br>


      <p class="contact">
        <label for="location">Location</label>
      </p>
      <input type="text" placeholder="Location" name="location" id="edit_node_location" />
      <br>

      <p class="contact">
        <label for="email">Email</label>
      </p>
      <input type="text" placeholder="Email Name" name="email" id="edit_node_email" />
      <br>


      <p class="contact">
        <label for="phone">Phone Number</label>
      </p>
      <input type="text" placeholder="Phone Number" name="phone" id="edit_node_phone" />
      <br>


      <p class="contact">
        <label for="image">Image</label>
      </p>
      <input id="edit_node_image" type="file" name="image" size="chars">
      <br>
      <input id="edit_emp" class="buttom" type="button" value="Save" />

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
