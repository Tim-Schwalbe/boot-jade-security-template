String.prototype.capitalize = function () {
    return this.charAt(0).toUpperCase() + this.slice(1);
}

var paginatorOptions = {bootstrapMajorVersion: 3};

var lastAttachmentDeleteTrigger = undefined;

$(function () {
    $(".rest").restfulizer();

    $(document).on("click", ".rest-post", function(e) {
        e.preventDefault();
        var updateId = $(this).data("target-id");

        $.ajax({
            url: $(this).attr("href"),
            type: "POST",
            success: function (data) {
                if (typeof updateId != "undefined" && $("#" + updateId).length > 0) {
                    $("#" + updateId).load(window.location.href + " #" + updateId + "-button");
                    $("#worklog").load(window.location.href + " #worklog-ajax");
                    $("#all-activity").load(window.location.href + " #all-activity-ajax");
                }
            }
        });
    });

    // Bind to StateChange Event
    History.Adapter.bind(window, 'statechange', function () {
        var State = History.getState();

        if (State.hash.indexOf(window.CONTEXT_PATH + "/tickets") == 0) {
            //Load new page part into table wrapper
            $("#ticket-table-wrapper").load(State.hash + " #ticket-table");
        }
    });

    if (window.CURRENT_PAGE != "" && window.TOTAL_PAGES != "") {

        $.extend(paginatorOptions,{
            currentPage: parseInt(window.CURRENT_PAGE),
            totalPages: parseInt(window.TOTAL_PAGES),
            onPageClicked: function (event, originalEvent, type, page) {
                event.preventDefault();
                var queryString = jQuery.query.set("page", page).toString();
                History.pushState("", "", queryString);
            }
        });

        $('#paginator').bootstrapPaginator(paginatorOptions);
    }

    $("#upload_form").on("submit", function (e) {
        if ($("#fileupload-input").val() == "") {
            e.preventDefault();
        }
        else {
            $("#progressbar").fadeIn(300);
        }
    });

    var profileProductAdd = $("#profile-product-add");
    var profileProducts = $("#profile-products");
    var profileTypeBox = $("#profile-type-box");
    var oldDescriptionValue = "";

    if (profileTypeBox.length > 0) {
        function profileProductCheckboxChange() {
            var profileType = profileTypeBox.find("input[type=radio]:checked").val();
            var profileTypeGroup = $("#profile-type-group");

            profileTypeGroup.find(".profile-type-field").css("display", "none");
            profileTypeGroup.find(".profile-type-" + profileType).css("display", "block");
        }

        profileProductCheckboxChange();

        profileTypeBox.find("input[type=radio]").change(function () {
            profileProductCheckboxChange();
        });
    }

    $("#ticket-assign-profile").click(function (e) {
        $.ajax({
            url: $("#ticket-assign-profile").attr("data-action"),
            data: $("#assignModal").find("form").serialize(),
            type: "POST",
            success: function (assignee) {
                $("#ticket-assignee a").html(assignee.firstName + " " + assignee.lastName);
                $("#agent-select").val(assignee.id);
                $('#assignModal').modal('hide');
            }
        });
    });


    $("p.ticket-ajax.inline-textarea").editable({type: 'textarea', action: "click"}, function (e) {
        if (oldDescriptionValue != e.value) {
            $.ajax({
                url: $(e.target).data("action-url"),
                data: $(e.target).data("name") + "=" + e.value.replace(/\r\n|\r|\n/g, "<br />"),
                type: "POST",
                success: function (data) {
                    mindbitAlert("Updated " + $(e.target).data("name").capitalize());
                },
                error: function (error) {
                    console.log(error);
                }
            });
        }
    });

    $(document).on('focus', 'p.ticket-ajax.inline-textarea textarea', function () {
        setCaretAtEnd($(this).get(0));
    });

    $(".chosen-select").chosen({width: "100%"});

    function mindbitAlert(message) {
        $('.top-right').notify({
            message: {text: message}
        }).show();
    }

    if ($("#notification li").length > 0) {
        $("#notification li").each(function (index, element) {
            mindbitAlert($(element).text());
        });
    }

    $(document).on('focus', 'p.ticket-ajax.inline-textarea textarea', function () {
        oldDescriptionValue = $("p.ticket-ajax.inline-textarea textarea").val();
    });

    $(document).on('focus keyup', 'p.ticket-ajax.inline-textarea textarea', function () {
        calculateHeight($(this).get(0));
    });

    function calculateContentHeight(ta, scanAmount) {
        var origHeight = ta.style.height,
            height = ta.offsetHeight,
            scrollHeight = ta.scrollHeight,
            overflow = ta.style.overflow;

        if (height >= scrollHeight) {
            ta.style.height = (height + scanAmount) + 'px';
            ta.style.overflow = 'hidden';
            if (scrollHeight < ta.scrollHeight) {
                while (ta.offsetHeight >= ta.scrollHeight) {
                    ta.style.height = (height -= scanAmount) + 'px';
                }
                while (ta.offsetHeight < ta.scrollHeight) {
                    ta.style.height = (height++) + 'px';
                }
                ta.style.height = origHeight;
                ta.style.overflow = overflow;
                return height;
            }
        } else {
            return scrollHeight;
        }
    }

    function calculateHeight(ta) {
        var style = (window.getComputedStyle) ? window.getComputedStyle(ta) : ta.currentStyle;

        taLineHeight = parseInt(style.lineHeight, 10);
        taHeight = calculateContentHeight(ta, taLineHeight);
        numberOfLines = Math.floor(taHeight / taLineHeight);

        $(ta).css("height", taLineHeight * numberOfLines + 1 + "px");
    };

    function setCaretAtEnd(elem) {
        var elemLen = elem.value.length;
        // For IE Only
        if (document.selection) {
            // Set focus
            elem.focus();
            // Use IE Ranges
            var oSel = document.selection.createRange();
            // Reset position to 0 & then set at end
            oSel.moveStart('character', -elemLen);
            oSel.moveStart('character', elemLen);
            oSel.moveEnd('character', 0);
            oSel.select();
        }
        else if (elem.selectionStart || elem.selectionStart == '0') {
            // Firefox/Chrome
            elem.selectionStart = elemLen;
            elem.selectionEnd = elemLen;
            elem.focus();
        } // if
    }

    $("form#comment-form").submit(function (e) {
        e.preventDefault();
        $.ajax({
            url: $(this).attr("action"),
            data: $(this).serialize(),
            type: $(this).attr("method"),
            success: function (data) {
                $("#comments").load($("form#comment-form").data("reload") + " #comment-ajax");
                $("#all-activity").load(window.location.href + " #all-activity-ajax");
                $("#comment-form")[0].reset();
                $("#comment-form").find("button").addClass("disabled");
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $(".ticket-watch-link").click(function (e) {
        $.ajax({
            url: $(this).data("action"),
            type: $(this).data("method"),
            success: function (data) {
                var watchLabel = $("#ticket-watch-counter");
                var watchLink = $(".ticket-watch-link");
                var watchStopLink = $(".ticket-watch-stop-link");
                watchLabel.html(parseInt(watchLabel.text()) + 1);
                watchLink.removeClass("active");
                watchStopLink.addClass("active");
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $(".ticket-watch-stop-link").click(function (e) {
        $.ajax({
            url: $(this).data("action"),
            type: $(this).data("method"),
            success: function (data) {
                var watchLabel = $("#ticket-watch-counter");
                var watchLink = $(".ticket-watch-link");
                var watchStopLink = $(".ticket-watch-stop-link");
                watchLabel.html(parseInt(watchLabel.text()) - 1);
                watchStopLink.removeClass("active");
                watchLink.addClass("active");
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $("form#ticket-add-attachment").submit(function (e) {
        e.preventDefault();
        $.ajax({
            url: $(this).attr("action"),
            type: $(this).attr("method"),
            data: new FormData(this),
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                var json = JSON.parse(data);
                var attachments = json.attachments;
                var attachmentDiv = $("#ticket-attachments");
                var attachmentList = attachmentDiv.find("div.list-group");
                var attachmentListItems = attachmentList.find("div.list-group-item");

                $.each(attachments, function (index, value) {
                    var found = false;

                    if (attachmentListItems.length > 0) {
                        attachmentListItems.each(function (i, element) {
                            if ($(element).find("span.attachment-filename").text() == value) {
                                found = true;
                            }
                        });
                    }

                    if (!found) {
                        attachmentList.append("<div class=\"pointer list-group-item\" onclick=\"window.location.href='" + $("form#ticket-add-attachment").attr('action') + "'\" href=\"javascript:void(0);\" title=\"" + value + "\"><span class=\"delete-modal-button pull-right glyphicon glyphicon-trash\" data-target=\"#attachmentDeleteModal\" data-toggle=\"modal\" onclick=\"event.stopPropagation();$('#attachmentDeleteModal').modal('show');lastAttachmentDeleteTrigger=$(this);\"><form class=\"hidden\" method=\"post\" aria-hidden=\"true\" action=\"" + $("form#ticket-add-attachment").attr('action') + "/" + value + "/delete\"></form></span><span class=\"glyphicon glyphicon-file\">&nbsp;</span><span class=\"attachment-filename\">" + value + "</span></div>");
                    }
                });

                attachmentDiv.removeClass("hidden");

                var attachmentModal = $('#attachmentModal');
                attachmentModal.modal('hide');
                attachmentModal.find(".fileinput-remove-button").trigger('click');
                mindbitAlert("Your attachments got uploaded successfully!");
            },
            error: function (error) {
                mindbitAlert("Please try again uploading your attachments!");
            }
        });
    });

    $(".popover-trigger").popover();


    $("#ticket-delete-attachment-button").click(function (e) {
        if (typeof lastAttachmentDeleteTrigger != 'undefined') {
            var formElement = lastAttachmentDeleteTrigger.find('form.hidden');

            $.ajax({
                url: formElement.attr("action"),
                type: formElement.attr("method"),
                data: formElement.serialize(),
                success: function (data) {
                    formElement.closest(".list-group-item").remove();
                    $("#attachmentDeleteModal").modal('hide');
                    mindbitAlert("Your attachments got deleted successfully!");

                    var attachmentDiv = $("#ticket-attachments");
                    var attachmentList = attachmentDiv.find("div.list-group div.list-group-item");
                    if (attachmentList.length <= 0 || attachmentList.size() <= 0) {
                        attachmentDiv.addClass("hidden");
                    }
                },
                error: function (error) {
                    mindbitAlert("Please try again uploading your attachments!");
                }
            });
        }
    });

    $(document).on("click", ".status-change-link", function (e) {
        $.ajax({
            url: $(this).data("action"),
            type: $(this).data("method"),
            data: $(e.target).data("name") + "=" + $(e.target).data("value"),
            success: function (data) {
                var json = JSON.parse(data);
                var ticket = JSON.parse(json.ticket);
                var triggeredBy = JSON.parse(json.triggeredBy);
                var newStatus = JSON.parse(json.newStatus);
                var transition = JSON.parse(json.transition);

                $("#ticket-transitions").load(window.location.href + " #ticket-transition-list")
                $("#ticket-resolutiontype-label").html(ticket.resolutionType);
                $("#ticket-status-label").html(newStatus.name);
                $("#ticket-updatedat-label").html(ticket.prettyUpdatedAt);

                $("#transitions").load(window.location.href + " #transitions-ajax");
                $("#all-activity").load(window.location.href + " #all-activity-ajax");
            },
            error: function (error) {
                console.log(error);
            }
        });
    });

    $("#ticket-navigation ul li a").click(function (e) {
        e.preventDefault();
        var activeClass = "active";
        var dis = $(this);

        //Show menu item as active and deactive other
        $("#ticket-navigation ul li").removeClass(activeClass);
        $(this).parent().addClass(activeClass);

        var dropDownParent = $(this).parent().parent().parent().parent();

        if (dropDownParent.hasClass("panel-collapse")) {
            dropDownParent.parent().addClass(activeClass);
        }

        $("#paginator-wrapper").load(dis.attr("href") + " #paginator-box", function () {
            var totalPages = parseInt($("#totalPages").text());
            if (!isNaN(totalPages) && totalPages > 1) {
                $.extend(paginatorOptions, {
                    currentPage: 1,
                    totalPages: totalPages,
                });
                $("#paginator").bootstrapPaginator(paginatorOptions);
                $("#paginator").show();
            }
            else {
                $("#paginator").hide();
            }
        });
        History.pushState("", "", dis.attr("href"));
    });

    $('[data-toggle="tooltip"]').tooltip();

    $(document).on("click", "#activity .btn-group button", function() {
        $("#activity .btn-group button").removeClass("btn-primary").addClass("btn-default");
        $(this).removeClass("btn-default").addClass("btn-primary");
    });

    $(document).ajaxSuccess(resizeContent);

    var calendarOptions = {
        maxDate: new Date(),
        locale: moment.locale(),
        stepping: 15
    };

    $('#ticket-worklog-from').datetimepicker(calendarOptions);
    $('#ticket-worklog-to').datetimepicker(
        $.extend(calendarOptions, { useCurrent: false })
    );
    $("#ticket-worklog-from").on("dp.change", function (e) {
        $('#ticket-worklog-to').data("DateTimePicker").minDate(e.date);
    });
    $("#ticket-worklog-to").on("dp.change", function (e) {
        $('#ticket-worklog-from').data("DateTimePicker").maxDate(e.date);
    });

    $("#ticket-add-worklog").submit(function(e) {
        e.preventDefault();

        $.ajax({
            url: $(this).attr("action"),
            type: $(this).attr("method"),
            data: $(this).serialize(),
            success: function (data) {
                $("#worklog").load(window.location.href + " #worklog-ajax");
                $("#all-activity").load(window.location.href + " #all-activity-ajax");
                $(this).get(0).reset();
                $('#worklogModal').modal('hide');
            }
        });
    });
});


$(document).ready(resizeContent);
$(window).load(resizeContent);
$(window).resize(resizeContent);

function resizeContent() {
    var content = $("#content");
    if (content.length > 0 && content.height() + 130 < $(window).height() - $("#header").height()) {
        var mainHeight = $(window).height() - $("#header").height() - $("#footer").height() - 12;
        $("#main").css("height", mainHeight + "px");
        $("#main > .container").css("height", mainHeight + "px");
    }
    else {
        $("#main").css("height", "auto");
        $("#main > .container").css("height", "auto");
    }
}

function scrollToDiv(divSelector) {
    if (typeof divSelector != 'undefined' && $(divSelector).length > 0) {
        $('html, body').animate({
            scrollTop: $(divSelector).offset().top
        }, 1500, 'swing');
    }
}

function deleteAttachment(e) {
    e.stopPropagation();
}


