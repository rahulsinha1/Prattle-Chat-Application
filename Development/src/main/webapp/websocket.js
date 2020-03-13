var ws;

document.getElementById("welcoming").innerText = "Welcome";

function connect() {
    var username = document.getElementById("username").value;

    var host = document.location.host;
    // var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + "/prattle/chat/" + username);

    ws.onmessage = function(event) {
    var log = document.getElementById("log");
        console.log(event.data);
        var message = JSON.parse(event.data);
        //TODO: Issue with textbox.
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function send() {
    var content = document.getElementById("msg").value;
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}
