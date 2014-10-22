/**
 * jQuery org-chart/tree plugin.
 *
 * Author: Héctor Vela
 * http://twitter.com/vellonce
 *
 * Original PlugIn Author: Wes Nolte
 * http://twitter.com/wesnolte
 *
 * Based on the work of Mark Lee
 * http://www.capricasoftware.co.uk
 *
 * Copyright (c) 2011 Wesley Nolte
 * Dual licensed under the MIT and GPL licenses.
 *
 */
(function($) {
    var jOrgChart_time = 0;
    var buildnode_time = 0;
    var jOrgChart_timer;
    $.fn.jOrgChart = function(options) {
        jOrgChart_timer = window.setInterval(function() {
            jOrgChart_time++;
        }, 1);
        var opts = $.extend({}, $.fn.jOrgChart.defaults, options);
        var $appendTo = $(opts.chartElement);
        console.log("starting jOrgChart");
        // build the tree
        $this = $(this);
        var $container = "";
        $container = $("<div class='" + opts.chartClass + "'/>");
        if ($this.is("ul")) {
            console.log("going into build node from if");
            buildNode($this.find("li:first"), $container, 0, opts);
            $appendTo.append($container);
        } else if ($this.is("li")) {
            console.log("going into build node from else");
            buildNode($this, $container, 0, opts);
        }
        console.log("finished build node");


        // add drag and drop if enabled
        if (opts.dragAndDrop) {
            console.log("enable drag and drop");
            var $divNode = $('div.node:not(.temp)').not(".disabled");
            var $nodeParts = $divNode.not(".child");
            $divNode.draggable({
                cursor: 'move',
                distance: 40,
                helper: 'clone',
                opacity: 0.8,
                revert: 'invalid',
                revertDuration: 100,
                snap: 'div.node.expanded',
                snapMode: 'inner',
                stack: 'div.node'
            });

            $nodeParts.droppable({
                accept: '.node',
                activeClass: 'drag-active',
                hoverClass: 'drop-hover'
            });


            var $nodeCU = $("div.node.child");
            $nodeCU.droppable({
                accept: '.child',
                activeClass: 'drag-active',
                hoverClass: 'drop-hover'
            });

            // Drag start event handler for nodes
            $divNode.bind("dragstart", function handleDragStart(event, ui) {
                console.log("bind drag start");
                var sourceNode = $(this);
                sourceNode.parentsUntil('.node-container')
                    .find('*')
                    .filter('.node')
                    .droppable('disable');
            });

            // Drag stop event handler for nodes
            $divNode.bind("dragstop", function handleDragStop(event, ui) {
                console.log("bind drag stop");
                /* reload the plugin */
                //init_tree();
                $(opts.chartElement).children().remove();
                $this.jOrgChart(opts);
                get_emp_info();
            });

            // Drop event handler for nodes
            $divNode.bind("drop", function handleDropEvent(event, ui) {
                console.log("bind drop");
                var targetID = $(this).data("tree-node");
                //alert(targetID);
                var targetLi = $this.find("li").filter(function() {
                    return $(this).data("tree-node") === targetID;
                });
                //alert("targetLi: "+targetLi.attr("class"));
                var targetUl = targetLi.children('ul');

                var sourceID = ui.draggable.data("tree-node");
                //alert(sourceID);
                var sourceLi = $this.find("li").filter(function() {
                    return $(this).data("tree-node") === sourceID;
                });
                //alert("sourceLi: "+sourceLi.attr("class"));
                var sourceUl = sourceLi.parent('ul');
                var newName = "";
                var oldName = "";
                var type = "";
                var num = "";
                var classList = targetLi.attr('class').split(/\s+/);
                //changes the old class name to new class name to prevent cloning
                $.each(classList, function(index, item) {
                    if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
                        newName = item;
                    }

                });
                classList = sourceLi.attr('class').split(/\s+/);
                $.each(classList, function(index, item) {
                    if (item != "temp" && item != "node" && item != "child" && item != "ui-draggable" && item != "ui-droppable" && item != "position" && item != "unit") {
                        oldName = item;
                    }
                    if (item == "unit" || item == "position") {
                        type = item;
                    }

                });
                //alert(newName);
                //alert(oldName);
                num = parseInt(oldName.split('_')[oldName.split('_').length - 1], 10);
                //alert(num);
                adjustChildren(sourceUl, num, 0);
                if (targetUl.length > 0 || targetUl.children().length > 0) {
                    //alert(targetUl.length);
                    newName = newName + "_" + (targetUl.children().length + 1);
                    //alert("new name"+newName);
                    sourceLi.removeClass(oldName);
                    sourceLi.removeClass(type);
                    sourceLi.addClass(newName);
                    sourceLi.addClass(type);
                    targetUl.append(sourceLi);
                } else {
                    newName = newName + "_1";
                    //alert("new name"+newName);
                    sourceLi.removeClass(oldName);
                    sourceLi.removeClass(type);
                    sourceLi.addClass(newName);
                    sourceLi.addClass(type);
                    targetLi.append("<ul></ul>");
                    targetLi.children('ul').append(sourceLi);
                }

                //Removes any empty lists
                if (sourceUl.children().length === 0) {
                    sourceUl.remove();
                }

            }); // handleDropEvent

        } // Drag and drop
        clearInterval(jOrgChart_timer);
        console.log("finished jorgChart");
        console.log("", jOrgChart_time);
    };
    // Option defaults
    $.fn.jOrgChart.defaults = {
        chartElement: 'body',
        depth: -1,
        chartClass: "jOrgChart",
        dragAndDrop: false
    };

    var nodeCount = 0;

    function removeNode($node, opts, $nodeDiv) {
        console.log("in remove node");
        if ($nodeDiv.hasClass("temp")) {
            if (click_flag) {
                $node.remove();
                click_flag = true;
                $(opts.chartElement).children().remove();
                $this.jOrgChart(opts);
            }

        }
        console.log("dome with remove node");
    }

    // Method that recursively builds the tree
    function buildNode($node, $appendTo, level, opts) {
        var build_timer = window.setInterval(function() {
            buildnode_time = buildnode_time + 1;
        }, 1);
        console.log("in build node");
        var $table = $("<table cellpadding='0' cellspacing='0' border='0'/>");
        var $tbody = $("<tbody/>");
        console.log("constructing container");
        // Construct the node container(s)
        var $nodeRow = $("<tr/>").addClass("node-cells");
        var $nodeCell = $("<td/>").addClass("node-cell").attr("colspan", 2);
        var $childNodes = $node.children("ul:first").children("li");
        var $nodeDiv;
        if ($childNodes.length > 1) {
            $nodeCell.attr("colspan", $childNodes.length * 2);
        }
        console.log("drawing the node");
        // Draw the node
        console.log("getting node contents");
        // Get the contents - any markup except li and ul allowed
        var $nodeContent = $node.clone()
            .children("ul,li")
            .remove()
            .end()
            .html();

        console.log("incrementing node count");
        //Increaments the node count which is used to link the source list and the org chart
        nodeCount++;

        console.log("appending data");
        $node.data("tree-node", nodeCount);
        $nodeDiv = $("<div>").addClass("node")
            .data("tree-node", nodeCount)
            .append($nodeContent);

        // Expand and contract nodes
        if ($childNodes.length > 0) {
            $nodeDiv.click(function() {
                var $this = $(this);
                var $tr = $this.closest("tr");

                if ($tr.hasClass('contracted')) {
                    $this.css('cursor', 'n-resize');
                    $tr.removeClass('contracted').addClass('expanded');
                    $tr.nextAll("tr").css('visibility', '');

                    // Update the <li> appropriately so that if the tree redraws collapsed/non-collapsed nodes
                    // maintain their appearance
                    $node.removeClass('collapsed');
                } else {
                    $this.css('cursor', 's-resize');
                    $tr.removeClass('expanded').addClass('contracted');
                    $tr.nextAll("tr").css('visibility', 'hidden');

                    $node.addClass('collapsed');
                }
            });
        }
        $nodeDiv.append(
                "<div class='opciones hidden overlay'>" +
                "</div>")
            .mouseenter(function() {
                $(this).find(".opciones").toggle();
            }).mouseleave(function() {
                $(this).find(".opciones").toggle();
            });

        var append_text = "<li class='temp'></li>";
        var $list_element = $node.clone()
            .children("ul,li")
            .remove()
            .end();
        //         add temporal nodes
        //        console.log("add temp nodes");
        //        if ($childNodes.length > 0) {
        //            if($node.find("ul:eq(0)").find("> li.temp").size()==0){
        //                $nodeDiv.mouseenter(function(){
        //                    if(!$list_element.hasClass("temp")){
        //                        $node.find("ul:eq(0)").append(append_text);
        //                        var classList = $node.attr('class').split(/\s+/);
        //                        $.each(classList, function(index,item) {
        //
        //                            $node.find("ul:eq(0) li.temp").addClass(item);
        //
        //                        });
        //
        //                        $(opts.chartElement).children().remove();
        //                        $this.jOrgChart(opts);
        //                    }
        //                });
        //            }
        //        }else{
        //
        //            $nodeDiv.mouseenter(function(){
        //                if(!$list_element.hasClass("temp")){
        //                    if($node.find("ul").size()==0){
        //                        append_text = "<ul>" + append_text + "</ul>";
        //                        $node.append(append_text);
        //                    }else{
        //                        $node.find("ul:eq(0)").append(append_text);
        //                    }
        //                    //add the parent class to the temp node
        //                    var classList = $node.attr('class').split(/\s+/);
        //                    $.each(classList, function(index,item) {
        //
        //                        $node.find("ul:eq(0) li.temp").addClass(item);
        //
        //                    });
        //                    $(opts.chartElement).children().remove();
        //                    $this.jOrgChart(opts);
        //                }
        //            });
        //        }
        //        $nodeDiv.mouseleave(function(){
        //            removeNode($node, opts, $nodeDiv);
        //        });
        //removeNode($node, opts, $nodeDiv);

        $nodeCell.append($nodeDiv);
        $nodeRow.append($nodeCell);
        $tbody.append($nodeRow);
        console.log("drawing tree");
        if ($childNodes.length > 0) {
            // if it can be expanded then change the cursor
            //$nodeDiv.css('cursor','n-resize');

            // recurse until leaves found (-1) or to the level specified
            if (opts.depth == -1 || (level + 1 < opts.depth)) {
                var $downLineRow = $("<tr/>");
                var $downLineCell = $("<td/>").attr("colspan", $childNodes.length * 2);
                $downLineRow.append($downLineCell);

                // draw the connecting line from the parent node to the horizontal line
                $downLine = $("<div></div>").addClass("line down");
                $downLineCell.append($downLine);
                $tbody.append($downLineRow);

                // Draw the horizontal lines
                var $linesRow = $("<tr/>");
                $childNodes.each(function() {
                    var $left = $("<td>&nbsp;</td>").addClass("line left top");
                    var $right = $("<td>&nbsp;</td>").addClass("line right top");
                    $linesRow.append($left).append($right);
                });

                // horizontal line shouldn't extend beyond the first and last child branches
                $linesRow.find("td:first")
                    .removeClass("top")
                    .end()
                    .find("td:last")
                    .removeClass("top");

                $tbody.append($linesRow);
                var $childNodesRow = $("<tr/>");
                $childNodes.each(function() {
                    var $td = $("<td class='node-container'/>");
                    $td.attr("colspan", 2);
                    // recurse through children lists and items
                    buildNode($(this), $td, level + 1, opts);
                    $childNodesRow.append($td);
                });

            }
            $tbody.append($childNodesRow);
        }


        // any classes on the LI element get copied to the relevant node in the tree
        // apart from the special 'collapsed' class, which collapses the sub-tree at this point

        if ($node.attr('class') != undefined) {
            var classList = $node.attr('class').split(/\s+/);
            $.each(classList, function(index, item) {
                if (item == 'collapsed') {

                    $nodeRow.nextAll('tr').css('visibility', 'hidden');
                    $nodeRow.removeClass('expanded');
                    $nodeRow.addClass('contracted');
                    $nodeDiv.css('cursor', 's-resize');
                } else {
                    $nodeDiv.addClass(item);
                }
            });
        }
        // any ids on the LI element also get copied to the relevant node in the tree.
        if ($node.attr('id') != undefined) {
            var classList = $node.attr('id').split(/\s+/);
            $.each(classList, function(index, item) {
                $nodeDiv.attr('id', item);
            });
        }

        if (!$nodeDiv.hasClass("temp")) {
            $nodeDiv.find(".opciones:eq(0)").append("<span class='add' href='#fancy'></span>");
            $nodeDiv.find(".opciones:eq(0)").append("<span class='edit' href='#fancy_edit'></span>");
            if ($nodeDiv.hasClass("child")) {
                $nodeDiv.find(".opciones:eq(0)").append("<span class='del'></span>");
            }
            if ($nodeDiv.hasClass("position")) {
                $nodeDiv.find(".opciones:eq(0)").append("<span class='emp' href='#fancy_employee'></span>");
            }
        }
        $table.append($tbody);
        $appendTo.append($table);

        /* Prevent trees collapsing if a link inside a node is clicked */
        $nodeDiv.children('a, span').click(function(e) {
            e.stopPropagation();
        });
        clearInterval(build_timer);
        console.log("", buildnode_time);
    }

    //    $.when(buildNode()).done(function(){
    //      clearInterval(build_timer);
    //      console.log("",buildnode_time);
    //    });    
})(jQuery);
