$(document).ready(function(){
    // Set global vars
    var hostname = "localhost:5000";
    var username = "antonio";
    var appid = "1234";

    // load config when the page loads
    $.ajax({
        url: "http://" + hostname + "/get/" + username + "/" + appid + "/",
        context: document.body
    }).done(function(data) {
            //$( this ).addClass( "done" );
            $("#config_textarea").text(JSON.stringify(data));

        }).fail(function(jqXHR, textStatus){
            alert("FAILED")
        });

    // Sync button
    $("#sync_button").click(function(){
        postConfigData();
    });

    var postConfigData = function(){
        config = $("#config_textarea").text();
        alert(config)
        $.ajax({
            type: "POST",
            url: "http://" + hostname + "/set/" + username + "/" + appid + "/",
            data: config,
            contentType: "application/json",
            context: document.body
        }).done(function(data) {
                //$( this ).addClass( "done" );
                $("#config_textarea").text(data);

            }).fail(function(jqXHR, textStatus){
                alert("FAILED")
            });
    }
});