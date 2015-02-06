$.ajaxSetup({
    cache: false
});

//window.location.href.split('/')[0] also work.
// For image ajax.
// var baseurl = window.location.protocol+"//"+window.location.host+"/"+window.location.pathname.split("/")[1]+"/";
var url = window.location.href;
var baseurl = url.substring(0, url.lastIndexOf('/') + 1);
var chartid = getUrlParameter('chartid');
var chartRestUrlBase = "../";
// alert(baseurl);

function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) {
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
        url: chartRestUrlBase + "../chart/xml/" + chartid,
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
        error: function(msg) {
            alert(msg.status + ", " + msg.statusText + "\n" + msg.responseText);
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
        reset_forms();
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
    $(".cform textarea").val("");
    $("#btn_remove_all_field_extra").click();
    reset_moreNodesWarning();
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
            url: chartRestUrlBase + "../employee/EditData/",
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
                    image = baseurl + chartRestUrlBase + "../employee/image/?empId=" + id;
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
        var nodeText = "<div class='empinfo'></div>";

        //for employee first name
        var append_first = "<div class='label_node' id='fn'>" + fn + "</div>" + "<br>";

        //for employee last name
        var append_last = "<div class='label_node' id='ln'>" + ln + "</div>" + "<br>";

        //for employee email
        var append_email = "<div class = 'label_node' id='email'>" + email + "</div>" + "<br>";

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
            node.attr("imgurl", image);

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

function reset_moreNodesWarning(){
    // console.log("reset_moreNodesWarning");
    $('.contracted').next().addClass('warn-more-nodes');
    $('.contracted').next().css('visibility', '');
    $('.contracted').next().each(function(index){
        var nodeLineDownParent = $(this).children().first();
    // console.log('.contracted');
    // console.log(nodeLineDownParent[0]);
        if( nodeLineDownParent.children(".warn-block").length > 0){
        }else{
            nodeLineDownParent.append( "<a class='warn-block'>Expand</a>" );
        }
        nodeLineDownParent.children(".warn-block").css('display', '');
    });
    $('.expanded').next().each(function(){
        var nodeLineDownParent = $(this).children().first();
    // console.log('.contracted');
    // console.log(nodeLineDownParent[0]);
        nodeLineDownParent.children(".warn-block")
            .css('display', 'none');
    });
}

function setUpdateXMLButtonOn(){
    $("#update_button").attr('disabled', true);
    $('#update_button').addClass("disabled");

}

function setUpdateXMLButtonOff(){
    $("#update_button").attr('disabled', false);
    $('#update_button').removeClass("disabled");

    //Disable edit
    // opts.dragAndDrop = false;
    
}
setUpdateXMLButtonOff();

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


    $(".unclickable").live("click", function(e){
        alert("This should be unclickable...");
    });

    // Edit node icon
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


    // Add Node
    $(".add").live("click", function(e) {
        e.stopPropagation();
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


    // Edit node details
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
            append_text += "<div class='unclickable'></div>";
            append_text += "<div class='label_node bold' id='pn'>" + texto + "</div>" + "<br>";

            //for position discription
            var append_last = "<div class='label_node' id='pd'>" + last + "</div>" + "<br>";

            //for node type
            //append_type="<div id='type'><div class='label_node' id='type'>" + node_type + "</div></div>" + "<br>";
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
            append_text += "<div class='unclickable'></div>";
            append_text += "<div class='label_node bold' id='un'>" + texto + "</div>" + "<br>";

            //for unit discription
            var append_last = "<div class='label_node' id='ud'>" + last + "</div>" + "<br>";

            //for node type
            //append_type="<div class='label_node' id='type'>" + node_type + "</div>" + "<br>";
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
            $("#edit_employee_radio").trigger("click");
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

    // load employee when click 
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
                    url: chartRestUrlBase + "../employee/RetrieveEmpData/",
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
            $(".emp-image").attr("src", "");
            $("#fancy_new_employee").hide();
            $("#fancy_delete_employee").hide();
            var empBean;
            node_to_edit = $("li." + add_to_node + ":not('.temp')");
            var empId = node_to_edit.attr("id");

            function ajax2() {
                return $.ajax({
                    type: "GET",
                    data: {
                        empId: empId
                    },
                    url: chartRestUrlBase + "../employee/" + empId,
                    success: function(respose, text, xhr) {
                        //alert(text+","+respose);
                        //location.reload();
                        empBean = respose;
                    },
                    error: function(msg) {
                        onErrorMessage(msg);
                    }
                });
            }
            $.when(ajax2()).done(function() {
                reset_extrafields();
                $("#fancy_edit_unit").hide();
                $("#fancy_edit_employee").show();
                $("#edit_first_name").val(empBean['firstName']);
                $("#edit_last_name").val(empBean['lastName']);
                $("#edit_node_title").val(empBean['employeeTitle']);
                $("#edit_node_location").val(empBean['location']);
                $("#edit_node_email").val(empBean['email']);
                $("#edit_node_phone").val(empBean['phone']);
                $("#edit_node_fax").val(empBean['fax']);
                if (node_to_edit.find("img").length != 0) {
                    $("#edit_node_image").val(image);
                }
                var node = $("#chart").find("div." + add_to_node);
                if (node.attr("imgurl")) {
                    imgurl = node.attr("imgurl");
                    //console.log("imgurl:"+imgurl);
                    $(".emp-image").attr("src", imgurl);
                    $(".emp-image-box").show();
                } else {
                    $(".emp-image-box").hide();
                }
                if ("undefined" != empBean['extraString']) {
                    $("#edit_extra").val(empBean['extraString']);
                    $("#btn_update_field_from_extra").click();
                    // ko.mapping.fromJS(JSON.parse(empBean['extraString']),ExtrafieldsModel);
                    // ExtrafieldsModel.extrafields(empBean['extraString']);
                    // ko.mapping.fromJSON(empBean['extraString'],null,ExtrafieldsModel.extrafields);
                    // ExtrafieldsModel.extrafields = empBean['extraString'];
                    // ExtrafieldsModel(JSON.parse(empBean['extraString']));
                    // console.log(ExtrafieldsModel.extrafields);
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
    $('form#add_employee_form').submit(function(e) {
        e.preventDefault();
        current_form = $(this);

        $("#btn_new_save_extra").click();
        var formData = new FormData(this);
        //ko.toJSON(ExtrafieldsModel)
        formData.append("new_extra", $('textarea#new_extra').val());
        console.log(formData);
        var $node = $("li." + add_to_node + ":not('.temp')");
        $.ajax({
            type: "POST",
            url: chartRestUrlBase + "../employee/",
            cache: false,
            data: formData,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(respose, text, xhr) {
                console.log("AddEmployee>POST>AddNode.do:" + text + ", " + respose);
                empId = respose.id;
                if (empId > 0) {
                    current_form[0].reset();
                    $node.attr("id", empId);
                    var $div = $("#chart").find("div." + add_to_node);
                    $div.attr("id", empId);
                    get_emp(empId, $div);
                    reset_forms();
                    $.fancybox.close();
                } else {
                    console.log("ERROR: AddEmployee> empId" + empId);
                    alert("ERROR happened while AddEmployee: empId" + empId);
                }
            },
            error: function(msg) {
                alert(msg.status + ", " + msg.statusText + "\n" + msg.responseText);
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
    $('form#edit_employee_form').submit(function(e) {
        e.preventDefault();

        $("#btn_edit_save_extra").click();
        $("#btn_remove_all_field_extra").click();
        var formData = new FormData(this);
        formData.append("edit_extra", $('textarea#edit_extra').val());
        var $node = $("li." + add_to_node + ":not('.temp')");
        empId = $node.attr("id");
        formData.append("emp_Id", empId);
        $.ajax({
            type: "PUT",
            url: chartRestUrlBase + "../employee/",
            cache: false,
            data: formData,
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            success: function(respose, text, xhr) {
                console.log("EditEmployee>POST>EditNode.do:" + text + ", " + respose);
                var $div = $("#chart").find("div." + add_to_node);
                get_emp(empId, $div);
            },
            error: function(msg) {
                onErrorMessage(msg);
            }
        })

        $(this)[0].reset();
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

        node_type = "";
        //WTF is pervious guy doing...
        // $.each(classList, function(index, item) {
        //     if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
        //         del_node = item;
        //         console.log(del_node);
        //     }
        //     if (item == "position" || item == "unit") {
        //         node_type = item;
        //     }
        // });
        if( $( nodeDiv ).hasClass( "unit" ) ){
            node_type = "unit";
        }else if( $( nodeDiv ).hasClass( "position" ) ){
            node_type = "position";
        }else{
            console.log("unkown node type!!!");
            console.log(nodeDiv[0]);
        }

        node_to_edit = $("li." + del_node + ":not('.temp')");
        //consider removing only if node is not a position node with an employee in it
        if (node_to_edit.attr("id") == null) {
            if (node_to_edit.children('ul').children().length == 0) {
                //if node is a leaf node remove the node completely
                var $ul = node_to_edit.parent();
                var childNum = del_node.split("_")[del_node.length - 1];
                node_to_edit.remove();
                node_to_edit.removeData();
                adjustChildren($ul, childNum, 0);
            } else {
                // if node is not leaf node remove all information but keep the actural node
                // node_to_edit.children("div").remove();
                // node_to_edit.children("div:not('.opciones')").remove();
                console.log("node_type="+node_type);
                if (node_type == "unit") {
                    node_to_edit.find("> .label_node[id=un]").text("");
                    node_to_edit.find("> .label_node[id=ud]").text("");
                    $('.toast').text('More nodes below, only empty current node.')
                        .fadeIn(400).delay(3000).fadeOut(400);
                }
                if (node_type == "position") {
                    node_to_edit.find("> .label_node[id=pn]").text("");
                    node_to_edit.find("> .label_node[id=pd]").text("");
                    $('.toast').text('More nodes below, only empty current node.')
                        .fadeIn(400).delay(3000).fadeOut(400);
                }
            }
            init_tree();
        }else{
            $('.toast').text('Unable to perform delete because an employee has located inside this node.')
                .fadeIn(400).delay(3000).fadeOut(400);
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
        if("disabled"===$("#update_button").attr("disabled")){
            return;
        }
        setUpdateXMLButtonOn();
        var updatestrg = $('#org').html();
        $.ajax({
            type: 'Post',
            dataType: 'text',
            data: {
                chartid: chartid,
                UptString: updatestrg
            },
            url: chartRestUrlBase + '../chart/xml/',
            success: function(respose, text, xhr) {
                alert("Successfully Saved");
                setUpdateXMLButtonOff();
            },
            error: function(msg) {
                alert(msg.status + ", " + msg.statusText + "\n" + msg.responseText);
                setUpdateXMLButtonOff();
            }
        });
    });


    // Enable edit button
    var lastSwitchState = null;
    var isRollbackSwitchState = false;
    $("#editSwitch").bootstrapSwitch();
    $("#editSwitch").bootstrapSwitch('setReadOnly', true);
    $("#editSwitch").click(function(){
        e.preventDefault();
        $("#editSwitch").bootstrapSwitch('toggleState');
    });
    $("#editSwitch").on('switch-change', function(e, data) {
        // console.log("isRollbackSwitchState="+isRollbackSwitchState);
        // console.log(lastSwitchState);
        // console.log(data);
        // console.log($("#editSwitch").bootstrapSwitch('state'));
        if(null == lastSwitchState
            || lastSwitchState != !$("#editSwitch").bootstrapSwitch('state')){
            lastSwitchState = !$("#editSwitch").bootstrapSwitch('state');
        }
        if(true===isRollbackSwitchState){
            lastSwitchState = null;
            isRollbackSwitchState = false;
            return;
        }
        var $element = $(data.el);
        value = data.value;
        if (value === true) {
            ajaxRequestPermission("enable");
        } else if (value === false) {
            ajaxRequestPermission("disable");
        } else {
            console.log("#editSwitch ERROR: " + e, $element, value);
        }
    });
    ajaxRequestPermission("status");


    //editString is "enable" or "disable" or other specified in server side.
    function ajaxRequestPermission(editString) {
        $("#editSwitch").bootstrapSwitch('setDisabled', true);
        return $.ajax({
            type: "GET",
            data: {
                edit: editString
            },
            url: chartRestUrlBase + "../chart/edit/" + chartid,
            success: function(respose, text, xhr) {
                $("#editSwitch").bootstrapSwitch('setDisabled', false);
                if (editString === "enable") {
                    $("#editSwitch").bootstrapSwitch('setState', true);
                    setUpdateXMLButtonOff();
                    ajaxRequestPermission("status");
                } else if (editString === "disable") {
                    $("#editSwitch").bootstrapSwitch('setState', false);
                    setUpdateXMLButtonOn();
                    ajaxRequestPermission("status");
                } else if (editString === "status") {
                    if(respose['isLocked']==="true" && respose['isOwner']==="true"){
                        $("#editSwitch").bootstrapSwitch('setState', true);
                        setUpdateXMLButtonOff();
                    }else{
                        $("#editSwitch").bootstrapSwitch('setState', false);
                        setUpdateXMLButtonOn();
                    }
                }
            },
            error: function(msg) {
                onErrorMessage(msg);
                // console.log("lastSwitchState="+lastSwitchState);
                if(null!=lastSwitchState){
                    isRollbackSwitchState = true;
                    $("#editSwitch").bootstrapSwitch('setState', lastSwitchState);
                    $("#editSwitch").bootstrapSwitch('setDisabled', false);
                }
            }
        });
    }

    function onErrorMessage(msg) {
        if (msg.responseText.match("^{")) {
            var responseJson = JSON.parse(msg.responseText);
            if (undefined === responseJson['lock'] || null === responseJson['lock']) {
                alert(responseJson['msg']);
            } else {
                alert(responseJson['msg'] + " Lock by: " + responseJson['lock']['userId']);
            }
        } else {
            alert(msg.status + ", " + msg.statusText + "\n" + msg.responseText);
        }
    }



    var initialData = [{
        type: "",
        text: ""
    }];

    var ExtrafieldsModel = function(extrafields) {
        var self = this;
        self.extrafields = ko.observableArray(ko.utils.arrayMap(extrafields, function(extrafield) {
            return {
                type: extrafield.type,
                text: extrafield.text
            };
        }));
        self.addExtraField = function() {
            self.extrafields.push({
                type: "",
                text: ""
            });
        };
        self.removeExtraField = function(extrafield) {
            self.extrafields.remove(extrafield);
        };
        self.updateFromField = function() {
            self.extrafields(
                JSON.parse($("#edit_extra").val())
            );
        };
        self.removeAll = function() {
            self.extrafields.removeAll();
            self.addExtraField();
        };
        self.save = function() {
            self.lastSavedJson(JSON.stringify(ko.toJS(self.extrafields), null, 2));
        };
        self.lastSavedJson = ko.observable("");
    };
    ko.applyBindings(new ExtrafieldsModel(initialData));

    function reset_extrafields() {
        $(".cform textarea").val("");
        // $(".resetExtraField").click();
        $("#btn_remove_all_field_extra").click();
    };

    $(document).on('click','.warn-block',function(event){
        $(this).parent().parent().prev().find(".node").click();
        reset_moreNodesWarning();
    });
    $(document).on('click','.node',function(event){
        reset_moreNodesWarning();
    });


});
