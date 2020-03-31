var ws;
var keySize = 256;
var ivSize = 128;
var iterations = 100;

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

var password = "Secret Password";


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

    ws.onmessage = function (event) {
        var log = document.getElementById("log");
        console.log(event);
        var message = JSON.parse(event.data);
        var decrypted;
        if(message.content === 'Connected!')
            decrypted = 'Connected!';
        else
            decrypted = decrypt(message.content, password);
        if (typeof message.timestamp !== 'undefined') {
            log.innerHTML +=
                message.from + " : " + displayTime(message.timestamp.toString()) + ":"
                + decrypted.toString(CryptoJS.enc.Utf8) + "\n";
        } else {
            log.innerHTML += message.from + " : " + message.content + "\n";
        }
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

  var encrypted = encrypt(content, password);
    var json = JSON.stringify({
        "to": username === "" ? null : username,
        "content": encrypted
    });

    ws.send(json);
}

function encrypt(msg, pass) {
    var salt = CryptoJS.lib.WordArray.random(128 / 8);

    var key = CryptoJS.PBKDF2(pass, salt, {
        keySize: keySize / 32,
        iterations: iterations
    });

    var iv = CryptoJS.lib.WordArray.random(128 / 8);

    var encrypted = CryptoJS.AES.encrypt(msg, key, {
        iv: iv,
        padding: CryptoJS.pad.Pkcs7,
        mode: CryptoJS.mode.CBC

    });
    var transitmessage = salt.toString() + iv.toString() + encrypted.toString();
    return transitmessage;
}

function decrypt(transitmessage, pass) {
    var salt = CryptoJS.enc.Hex.parse(transitmessage.substr(0, 32));
    var iv = CryptoJS.enc.Hex.parse(transitmessage.substr(32, 32))
    var encrypted = transitmessage.substring(64);

    var key = CryptoJS.PBKDF2(pass, salt, {
        keySize: keySize / 32,
        iterations: iterations
    });
    var decrypted = CryptoJS.AES.decrypt(encrypted, key, {
        iv: iv,
        padding: CryptoJS.pad.Pkcs7,
        mode: CryptoJS.mode.CBC

    })
    return decrypted;
}

function displayTime(currentUserTimePreference) {
    var splitData = currentUserTimePreference.split(" ");
    return splitData[0].substring(0, 5) + " " + splitData[1];
}