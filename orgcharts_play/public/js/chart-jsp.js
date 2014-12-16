$.ajaxSetup({
    cache: false
});

//window.location.href.split('/')[0] also work.
// For image ajax.
// var baseurl = window.location.protocol+"//"+window.location.host+"/"+window.location.pathname.split("/")[1]+"/";
var url = window.location.href;
var baseurl = url.substring(0,url.lastIndexOf('/')+1);
var chartid = getUrlParameter('chartid');
// alert(baseurl);

function getUrlParameter(sParam){
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}

function ajax0() {
    console.log("load file");
    $('#org').children().remove();
    return $.ajax({
        type: "GET",
        data: {
            chartid: chartid
        },
        url: "../chart/"+chartid,
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
    console.log("done with init_tree");
}

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
    console.log("reset_forms");
}

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
            url: "../employee/EditData/",
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
                    image = baseurl + "../employee/image/?empId=" + id;
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
        var temp = node.children("div.empinfo");
        // var temp1 = node.children("div.opciones");
        temp.detach();
        // temp1.detach();
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
            d = new Date();
            // alert(image+"&ts="+d.getTime());
            // node.children("div.empinfo").append(append_image);
            // var nodeimage = $("<div class='node-image'></div>")
            //     //.attr("imgurl",image+"&ts="+d.getTime())
            //     .attr("imgurl",image)
            //     //.css("background-image","url('" + image + "&ts="+d.getTime() + "')")
            //     .appendTo(node);
            node.attr("imgurl",image);

            var nodeimage_icon = $('<i class="fa fa-picture-o node-image-icon"></i>')
                .appendTo(node);
            // Old way to add image as node backround.
            // node.attr("imgurl",image+"&ts="+d.getTime());
            // node.css("background-image","url('" + image + "&ts="+d.getTime() + "')");
            // node.css("background-size","106px");
        }
        // node.append(temp);
        // node.append(temp1);
    });
}

var click_flag = true;
var node_to_edit;

//for reset form
var current_form;
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
        }
        // I dont know what this code do, seems like repeat the first if for unit node.
        // else {
        //     var unitname_field = $("#edit_unit_name");
        //     var name = unitname_field.val();

        //     node_to_edit.find("> .label_node:eq(0)").text(name);
        //     unitname_field.val("");
        //     node_to_edit.find("> .label_node:eq(0)").attr('id', 'un');
        //     node_to_edit.addClass('unit');
        // }

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
                $node.find("li").last().append(append_last)
                // Add description no matter exist or not, so we can edit it later.
                // if (last != "") {
                //     //append_last = "<ul>" + append_last + "</ul>";
                //     $node.find("li").last().append(append_last)
                // }

                //node type
                //append_type = "<ul>" + append_type + "</ul>"
                //$node.find("li").last().append(append_type);
            } else {
                $node.find("ul:eq(0)").append(append_text);
                $node.find("li").last().append(append_last);
                // Add description no matter exist or not, so we can edit it later.
                // if (last != "") {
                //     $node.find("li").last().append(append_last);
                // }
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
                $node.find("li").last().append(append_last);

                //node type
                //append_type = "<ul>" + append_type + "</ul>"
                //$node.find("li").last().append(append_type);
            } else {
                $node.find("ul:eq(0)").append(append_text);
                $node.find("li").last().append(append_last);
                // Add description no matter exist or not, so we can edit it later.
                // if (last != "") {
                //     $node.find("li").last().append(append_last);
                // }
                //$node.find("li").last().append(append_type);
            }
        }

        init_tree();
        $.fancybox.close();
    });

    //add/edit employee button
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
            $( "#edit_employee_radio" ).trigger( "click" );
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

    // new employee
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
                    url: "../employee/RetrieveEmpData/",
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
                    url: "../employee/EditData/",
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
                if (node_to_edit.find("img").length != 0) {
                    $("#edit_node_image").val(image);
                }
                var node = $("#chart").find("div." + add_to_node);
                if(node.attr("imgurl")){
                    imgurl = node.attr("imgurl");
                    //console.log("imgurl:"+imgurl);
                    $(".emp-image").attr("src",imgurl);
                    $(".emp-image-box").show();
                }else{
                    $(".emp-image-box").hide();
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

    //add new employee
    $('form#add_employee_form').submit(function (e){
        e.preventDefault();
        current_form = $(this)

        var formData = new FormData(this);
        var $node  = $("li." + add_to_node + ":not('.temp')");
        $.ajax({
            type: "POST",
            url: "../employee/",
            cache: false,
            data: formData,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(respose, text, xhr) {
                console.log("AddEmployee>POST>AddNode.do:"+text+", "+respose);
                empId = respose.id;
                if(empId>0){
                    current_form[0].reset();
                    $node.attr("id", empId);
                    var $div = $("#chart").find("div." + add_to_node);
                    $div.attr("id", empId);
                    get_emp(empId, $div);
                    reset_forms();
                    $.fancybox.close();
                }else{
                    console.log("ERROR: AddEmployee> empId"+empId);
                    alert("ERROR happened while AddEmployee: empId"+empId);
                }
            },
            error: function(xhr, textstatus, ethrown) {
                alert(textstatus+", "+ethrown +xhr.status);
            }
        })
        

        //return false;
    });

    //add current employee
    $("input[type=button][id=add_curr]").click(function() {
        var $node = $("li." + add_to_node + ":not('.temp')");
        $node.attr("id", id_num);
        var $div = $("#chart").find("div." + add_to_node);
        $div.attr("id", id_num);
        get_emp(id_num, $div);
        reset_forms();
        $.fancybox.close();
    });

    //edit employee
    $('form#edit_employee_form').submit(function (e){
        e.preventDefault();

        var formData = new FormData(this);
        var $node  = $("li." + add_to_node + ":not('.temp')");
        empId = $node.attr("id");
        formData.append("emp_Id", empId);
        $.ajax({
            type: "PUT",
            url: "../employee/",
            cache: false,
            data: formData,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(respose, text, xhr) {
                console.log("EditEmployee>POST>EditNode.do:"+text+", "+respose);
            },
            error: function(xhr, textstatus, ethrown) {
                alert(textstatus+", "+ethrown +xhr.status);
            }
        })

        $(this)[0].reset();
        var $div = $("#chart").find("div." + add_to_node);
        get_emp(empId, $div);
        reset_forms();
        $.fancybox.close();
        //return false;
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



  // Update Chart
  $("#update_button").click(function() {
    var updatestrg = $('#org').html();
    $.ajax({
      type: 'Post',
      dataType: 'text',
      data: {
        chartid: chartid,
        UptString: updatestrg
      },
      url: '../chart/xml/',
      success: function(respose, text, xhr) {
        alert("Successfully Saved");
        //location.reload();
      },
      error: function(xhr, textstatus, ethrown) {
        alert(textstatus + ", " + ethrown);
      }
    });
  });

});
