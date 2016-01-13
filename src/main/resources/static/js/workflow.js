$(function(){
    $("#add-node-button").click(function(e) {
        e.preventDefault();

        var nodeClone = $("#node-clone").find(".list-group-item").clone(true, true);
        var nextNodeId = 1;
        if ($("#nodes").find(".list-group .list-group-item").length > 0) {
            nextNodeId = parseInt($("#nodes").find(".list-group .list-group-item:last-child input.input-node-id").val()) + 1;
        }

        nodeClone.addClass("node-" + nextNodeId);
        nodeClone.find(".node-label").html(nextNodeId + ":");
        var nodeName = $(this).closest("form").find("input[name='status']").val();
        nodeClone.find(".node-name").html(nodeName);
        var nodeIdInput = nodeClone.find(".input-node-id");
        var nodeNameInput = nodeClone.find(".input-node-name");
        nodeIdInput.val(nextNodeId);
        nodeIdInput.attr("name", nodeIdInput.attr("name").replace("NODE_ID", nextNodeId));
        nodeNameInput.val(nodeName);
        nodeNameInput.attr("name", nodeNameInput.attr("name").replace("NODE_ID", nextNodeId));

        $("#nodes-none").hide();
        $("#nodes").find(".list-group").append(nodeClone);

        var transitionListClone = $("#transition-list-clone").find(".transition-list").clone(true, true);
        transitionListClone.attr("id", "transition-list-" + nextNodeId);
        $("#transitions").append(transitionListClone);

        recreateDestinations();

        $(this).closest(".modal").modal('hide');
        $(this).closest("form")[0].reset();
    });

    $("#edit-node-button").click(function(e) {
        e.preventDefault();

        var editModal = $("#editNodeModal");
        var nodeId = editModal.find("input.input-node-id").val();
        var newNodeName = editModal.find("input.input-node-name").val();

        $("#nodes").find(".list-group .list-group-item.node-" + nodeId).find(".node-name").html(newNodeName);
        $("#nodes").find(".list-group .list-group-item.node-" + nodeId).find("input.input-node-name").val(newNodeName);

        recreateDestinations();

        $(this).closest(".modal").modal('hide');
        $(this).closest("form")[0].reset();
    });

    $(document).on("click", ".edit-node-modal-trigger", function(e) {
        e.stopPropagation();
        var oldNodeName = $(this).closest(".list-group-item").find(".node-name").text();
        var nodeTriggerId = $(this).closest(".list-group-item").find("input.input-node-id").val();
        $("#editNodeModal").find("input.input-node-id").val(nodeTriggerId);
        $("#editNodeModal").find("input.input-node-name").val(oldNodeName);
    });

    $(document).on("click", ".delete-node-button", function(e) {
        e.stopPropagation();
        var nodeId = $(this).closest('.list-group-item').find("input.input-node-id").val();
        $(this).closest('.list-group-item').remove();;
        $("#transition-list-" + nodeId).remove();

        var counter = 0;
        $("#transitions").find(".transition-list").each(function() {
            counter++;
            $(this).attr("id", "transition-list-" + counter);
            if ($(this).find(".list-group-item").length > 0) {
                var transitionNameInput = $(this).find("input.input-transition-name");
                var transitionNodeIdInput = $(this).find("input.input-node-id");
                var transitionIdInput = $(this).find("input.input-transition-id");
                var transitionDestinationInput = $(this).find("input.input-transition-destination");
                transitionNameInput.attr("name", transitionNameInput.attr('name').replace(/transition\[\d+\]/, "transition[" + counter + "]"));
                transitionNodeIdInput.val(counter);
                transitionNodeIdInput.attr("name", transitionNodeIdInput.attr('name').replace(/transition\[\d+\]/, "transition[" + counter + "]"));
                transitionIdInput.attr("name", transitionDestinationInput.attr('name').replace(/transition\[\d+\]/, "transition[" + counter + "]"));
                transitionDestinationInput.attr("name", transitionDestinationInput.attr('name').replace(/transition\[\d+\]/, "transition[" + counter + "]"));
                if (transitionDestinationInput.val() == nodeId) {
                    transitionDestinationInput.val("");
                    $(this).find(".transition-destination").html("");
                }
            }
        });

        counter = 0;
        $("#nodes").find(".list-group .list-group-item").each(function(index) {
            counter++;
            $(this).find(".node-label").html(counter + ":");
            $(this).find("input.input-node-id").val(counter);
            $(this).attr("class", "list-group-item clearfix node-" + counter);
        });

        if ($("#nodes").find(".list-group .list-group-item").length == 0) {
            $("#nodes-none").show();
        }

        recreateDestinations();
    });

    $(document).on("click", ".new-transition-modal-button", function() {
        var nodeTriggerId = parseInt($(this).closest(".transition-list").attr("id").split("-")[2]);
        $("#newTransitionModal").find("input.input-node-id").val(nodeTriggerId);
    });

    $(document).on("click", "#nodes .list-group-item", function() {
        var nodeId = $(this).find("input.input-node-id").val();
        $("#transitions-headline").html("Transitions for Status: " + nodeId);
        $("#transitions-none").hide();
        $(".transition-list").removeClass("active");
        $("#transition-list-" + nodeId).addClass("active");
    });

    $("#newNodeModal form, #editNodeModal form, #newTransitionModal form, #editTransitionModal form").submit(function(e) {
        e.preventDefault();
    });

    $("#new-transition-button").click(function(e) {
        e.preventDefault();

        //create proper transition clone
        var nodeId = $(this).closest('form').find('input.input-node-id').val();
        var transitionClone = $("#transition-clone").find(".list-group-item").clone(true, true);
        var nextTransitionId = 1;
        if ($("#nodes").find(".list-group .transition-list-" + nodeId + " .list-group-item").length > 0) {
            nextTransitionId = parseInt($("#transitions").find(".list-group #transition-list-" + nodeId + " .list-group-item:last-child input.input-transition-id").val()) + 1;
        }

        //prepare transition clone
        transitionClone.addClass("transition-" + nextTransitionId);
        transitionClone.find(".transition-label").html(nextTransitionId + ":");
        var permissionSelect = $(this).closest("form").find("select.input-transition-permissions");
        var selectedPermissions = permissionSelect.val();
        var selectedLabels = "";
        $.each(selectedPermissions, function(index, value) {
            var selectOptionText = permissionSelect.find("option[value='"+value+"']").text();
            selectedLabels += "<span class='label label-default'><input type='hidden' name='transition["+nodeId+"]["+nextTransitionId+"][permissions][]' value='"+value+"' />"+selectOptionText+"</span> ";
        });
        var transitionName = $(this).closest("form").find("input[name='name']").val();
        var transitionDestination = $(this).closest("form").find("select[name='destination'] option:selected").val();
        var transitionDestinationText = $(this).closest("form").find("select[name='destination'] option:selected").text();
        transitionClone.find(".transition-name").html(transitionName);
        transitionClone.find(".transition-destination").html("<span class='label label-primary'>" + transitionDestinationText + "</span>");
        transitionClone.find(".transition-permissions").html(selectedLabels);

        //set inputs
        var transitionNameInput = transitionClone.find("input.input-transition-name");
        var transitionNodeIdInput = transitionClone.find("input.input-node-id");
        var transitionIdInput = transitionClone.find("input.input-transition-id");
        var transitionDestinationInput = transitionClone.find("input.input-transition-destination");
        transitionNameInput.val(transitionName);
        transitionNameInput.attr("name", transitionNameInput.attr('name').replace("NODE_ID", nodeId));
        transitionNodeIdInput.val(nodeId);
        transitionNodeIdInput.attr("name", transitionNodeIdInput.attr('name').replace("NODE_ID", nodeId));
        transitionIdInput.val(nextTransitionId);
        transitionIdInput.attr("name", transitionDestinationInput.attr('name').replace("NODE_ID", nodeId));
        transitionDestinationInput.val(transitionDestination);
        transitionDestinationInput.attr("name", transitionDestinationInput.attr('name').replace("NODE_ID", nodeId));

        //hide empty transitions text and append new clone
        $("#transitions-none").hide();
        $("#transitions").find("#transition-list-" + nodeId + " .list-group").append(transitionClone);

        //close and reset modal
        $(this).closest(".modal").modal('hide');
        $(this).closest("form")[0].reset();
        $(this).closest("form").find(".chosen-select").trigger("chosen:updated");
    });

    $(document).on("click", ".edit-transition-modal-trigger", function(e) {
        e.stopPropagation();
        var oldTransitionName = $(this).closest(".list-group-item").find(".transition-name").text();
        var oldDestination = $(this).closest(".list-group-item").find("input.input-transition-destination").val();
        var nodeTriggerId = parseInt($(this).closest(".transition-list").attr("id").split("-")[2]);
        var transitionTriggerId = $(this).closest(".list-group-item").find("input.input-transition-id").val();

        var editPermissionsInput = $("#editTransitionModal").find("select.input-transition-permissions");
        $(this).closest(".list-group-item").find("span.transition-permissions span.label").each(function() {
            var oldPermission = $(this).find('input').val();
            editPermissionsInput.find("option[value=" + oldPermission + "]").prop('selected', true);
        });

        editPermissionsInput.trigger("chosen:updated");
        $("#editTransitionModal").find("input.input-node-id").val(nodeTriggerId);
        $("#editTransitionModal").find("input.input-transition-id").val(transitionTriggerId);
        $("#editTransitionModal").find("input.input-transition-name").val(oldTransitionName);
        if (oldDestination != "") {
            $("#editTransitionModal").find("select.input-transition-destination option[value=" + oldDestination + "]").prop("selected", true);
        }
    });

    $("#edit-transition-button").click(function(e) {
        e.preventDefault();

        //create proper transition clone
        var nodeId = $(this).closest('form').find('input.input-node-id').val();
        var transitionId = $(this).closest('form').find("input.input-transition-id").val();
        var transitionElement = $("#transition-list-" + nodeId).find(".transition-" + transitionId);

        //prepare transition clone
        transitionElement.find(".transition-label").html(transitionId + ":");
        var permissionSelect = $(this).closest("form").find("select.input-transition-permissions");
        var selectedPermissions = permissionSelect.val();
        var selectedLabels = "";
        $.each(selectedPermissions, function(index, value) {
            var selectOptionText = permissionSelect.find("option[value='"+value+"']").text();
            selectedLabels += "<span class='label label-default'><input type='hidden' name='transition["+nodeId+"]["+transitionId+"][permissions][]' value='"+value+"' />"+selectOptionText+"</span> ";
        });
        var transitionName = $(this).closest("form").find("input[name='name']").val();
        var transitionDestination = $(this).closest("form").find("select[name='destination'] option:selected").val();
        var transitionDestinationText = $(this).closest("form").find("select[name='destination'] option:selected").text();
        transitionElement.find(".transition-name").html(transitionName);
        transitionElement.find(".transition-destination").html("<span class='label label-primary'>" + transitionDestinationText + "</span>");
        transitionElement.find(".transition-permissions").html(selectedLabels);

        //set inputs
        var transitionNameInput = transitionElement.find("input.input-transition-name");
        var transitionNodeIdInput = transitionElement.find("input.input-node-id");
        var transitionIdInput = transitionElement.find("input.input-transition-id");
        var transitionDestinationInput = transitionElement.find("input.input-transition-destination");
        transitionNameInput.val(transitionName);
        transitionNameInput.attr("name", transitionNameInput.attr('name').replace("NODE_ID", nodeId));
        transitionNodeIdInput.val(nodeId);
        transitionNodeIdInput.attr("name", transitionNodeIdInput.attr('name').replace("NODE_ID", nodeId));
        transitionIdInput.val(transitionId);
        transitionIdInput.attr("name", transitionDestinationInput.attr('name').replace("NODE_ID", nodeId));
        transitionDestinationInput.val(transitionDestination);
        transitionDestinationInput.attr("name", transitionDestinationInput.attr('name').replace("NODE_ID", nodeId));

        //close and reset modal
        $(this).closest(".modal").modal('hide');
        $(this).closest("form")[0].reset();
        $(this).closest("form").find(".chosen-select").trigger("chosen:updated");
    });

    function recreateDestinations() {
        var destinationOptions = {};
        $("#nodes").find(".list-group .list-group-item").each(function() {
            var optionValue = $(this).find(".input-node-id").val();
            var optionText = $(this).find(".node-name").text();
            destinationOptions[optionValue] = optionText;
        });

        var newTransitionSelect = $('#new-transition-destination-select');
        var editTransitionSelect = $('#edit-transition-destination-select');
        newTransitionSelect.html("");
        editTransitionSelect.html("");
        $.each(destinationOptions, function(val, text) {
            var option = "<option value='"+val+"'>"+text+"</option>";
            newTransitionSelect.append(option);
            editTransitionSelect.append(option);
        });
    }
});
