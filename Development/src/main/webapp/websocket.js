var keySize = 256;
let ivSize = 128;
var iterations = 100;

let accountName = getCookie("username");
let idOfEachText = 0;

let ws;


// var pathname = document.location.pathname;

/**
 * Gets the cookie.
 * @param cname is the cookie name.
 * @returns {string} the cookie of the username.
 */
function getCookie(cname){
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)===' ') c = c.substring(1);
        if (c.indexOf(name) === 0) return c.substring(name.length,c.length);
    }
    return "";
}

let checkbox = document.querySelector('input[type="checkbox"]');


document.addEventListener('DOMContentLoaded', function () {

    checkbox.addEventListener('change', function () {
        if (checkbox.checked) {
            // do this
            goOnline();
        } else {
            // do that
            goOffline();
        }
    });
});

var password = "Secret Password";

function goOnline() {
    ws = new WebSocket("ws://" + document.location.host + "/prattle/chat/" + accountName);

    ws.onopen = function() {
        var log = document.getElementById("log");
        log.innerHTML += accountName + " : Connected!" + "<br />";
    }

    ws.onmessage = function (event) {
        idOfEachText++; // TODO: CHANGE TO INTEGRATE INTO THE DATABASE
        document.getElementsByClassName(idOfEachText).contentEditable = "true";

        var log = document.getElementById("log");
        var message = JSON.parse(event.data);
        var decrypted;
        if(message.content === 'Connected!')
            decrypted = 'Connected!';
        else
            decrypted = decrypt(message.content, password);
        if (typeof message.timestamp !== 'undefined') {
            log.innerHTML += "<span id=" + "'" +  idOfEachText + "'>" +
                message.from + " : " + displayTime(message.timestamp.toString()) + ":"
                + decrypted.toString(CryptoJS.enc.Utf8) + "</span> <button class=" + "'" +  idOfEachText + "' " +
                "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
                "<button contenteditable > &#10503 </button>" + "<br />";
        } else {
            log.innerHTML += "<span id=" + "'" +  idOfEachText + "'>" +
                message.from + " : " + message.content + "</span> <button class="
                + "'" +  idOfEachText + "' " + "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
                "<button contenteditable > &#10503 </button>" + "<br />";
        }
    };

    ws.onclose = function() {
        var log = document.getElementById("log");
        log.innerHTML += accountName + " : Disconnected!" + "<br />";
    }
}



function copy(id) {
    const textToCopy = document.getElementById(id).innerText;

    navigator.clipboard.writeText(textToCopy).then(r => {
        alert("Copied Successfully");
    }, function () {
        alert("Copied Unsuccessfully");
    }) ;
}

// <span id="1"> This is a test </span> <button class="1" contenteditable onclick="copy('1')">&#x2398</button> <button contenteditable > &#10503</button>

function goOffline() {
    ws.close();
}

function send() {
    var content = document.getElementById("msg").value;
    var userToSendTo = document.getElementById("username").value;

    var encrypted = encrypt(content, password);
    var json = JSON.stringify({
        "to": userToSendTo === "" ? null : userToSendTo,
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