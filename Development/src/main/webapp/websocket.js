document.getElementById("welcoming").innerText = "Welcome " + localStorage.getItem('username');


function connect() {
    var username = document.getElementById("username").value;

    var host = document.location.host;
    // var pathname = document.location.pathname;

    ws = new WebSocket("ws://" + host + "/prattle/chat/" + username);

    ws.onmessage = function(event) {
        var log = document.getElementById("log");
        console.log(event);
        var message = JSON.parse(event.data);
        var decryptedBytes = CryptoJS.AES.decrypt(message.content, "My Secret Passphrase");
        var plaintext = decryptedBytes.toString(CryptoJS.enc.Utf8);
        if (typeof message.timestamp !== 'undefined')
            log.innerHTML += message.from + " : " + message.timestamp.toString().substring(0,5) + " : " + message.content + ":" + plaintext + "\n";
        else
            log.innerHTML += message.from + " : " + message.content + "\n";
    };
}

function goOnline() {
//     ws.onopen = function(event) {
//     var log = document.getElementById("log");
//     var message = JSON.parse(event.data);
//     log.innerHTML += message.from + " : " + message.content + "\n";
// };
}

function goOffline() {
    // ws.onclose = function (event) {
    //     var log = document.getElementById("log");
    //     var message = JSON.parse(event.data);
    //     log.innerHTML += message.from + " : Disconnected!" + "\n";
    // };
}

function send() {
    var content = document.getElementById("msg").value;
    var encrypted = CryptoJS.AES.encrypt(content, "Secret Passphrase");
    var json = JSON.stringify({
        "content":content
    });

    ws.send(json);
}

function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}


