$(document).ready(function(){
    // Set global vars
    var hostname = "localhost:5000";
    var username = "fabio";
    var appid = "1234";

    // load config when the page loads
    $.ajax({
        url: "http://" + hostname + "/get/" + username + "/" + appid + "/",
        context: document.body
    }).done(function(jsonObj) {
            //$( this ).addClass( "done" );
            jsonString = JSON.stringify(jsonObj, null, '\t');
            $("#config_textarea").text(jsonString);

        }).fail(function(jqXHR, textStatus){
            alert("FAILED")
        });

    // Sync button
    $("#sync_button").click(function(){
        $.ajax({
            type: "POST",
            url: "http://" + hostname + "/set/" + username + "/" + appid + "/",
            data: $("#config_textarea").val(),
            contentType: "application/json",
            context: document.body
        }).done(function(data) {
                //$( this ).addClass( "done" );
                alert(data);
                $('#device_iframe')[0].contentWindow.location.reload(); // refresh app in the device
            }).fail(function(jqXHR, textStatus){
                alert("FAILED")
            });
    });
});