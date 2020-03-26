var ws;

document.getElementById("welcoming").innerText = "Welcome " + localStorage.getItem('username');

document.addEventListener('DOMContentLoaded', function () {
    var checkbox = document.querySelector('input[type="checkbox"]');
    checkbox.addEventListener('change', function () {
        if (checkbox.checked) {
            // do this
            console.log('Checked');
            goOnline();
        } else {
            // do that
            console.log('Not checked');
            goOffline();
        }
    });
});

function connect() {
    // var username = document.getElementById("username").value;
    //
    // var host = document.location.host;
    // // var pathname = document.location.pathname;
    //
    // ws = new WebSocket("ws://" + host + "/prattle/chat/" + username);
    //
    // ws.onmessage = function(event) {
    //     var log = document.getElementById("log");
    //     var message = JSON.parse(event.data);
    //     log.innerHTML += message.from + " : " + message.content + "\n";
    // };
}

function goOnline() {
    console.log('Username:' + localStorage.getItem('username'));
    var username = localStorage.getItem('username');
    var host = document.location.host;

    // var pathname = document.location.pathname;
    ws = new WebSocket("ws://" + host + "/prattle/chat/" + username);
    ws.onmessage = function(event) {
        var log = document.getElementById("log");
        var message = JSON.parse(event.data);
        log.innerHTML += message.from + " : " + message.content + "\n";
    };
    ws.onclose = function() {
        var log = document.getElementById("log");
        log.innerHTML += username + " : Disconnected!" + "\n";
    }
//     ws.onopen = function(event) {
//     var log = document.getElementById("log");
//     var message = JSON.parse(event.data);
//     log.innerHTML += message.from + " : " + message.content + "\n";
// };
}

function goOffline() {
    ws.close();
}

function send() {
    var content = document.getElementById("msg").value;
    var username = document.getElementById("username").value;
    var json = JSON.stringify({
        "to":username === ""? null: username,
        "content":content
    });
    console.log(json);
    ws.send(json);
}

function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}
