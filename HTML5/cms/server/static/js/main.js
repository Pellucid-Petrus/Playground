$(document).ready(function(){
    console.log("CIAO");
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
    $("button").click(function(){
        $("#div1").load("demo_test.txt");
    });

});