var keySize = 256;
let ivSize = 128;
var iterations = 100;

let accountName = getCookie("username");
let idOfEachText = 0;
let websocket;
let password = "Secret Password";
let websocket;

var wsUri = "ws://" + document.location.host + "/prattle/chat/" + accountName;
var log;

function init(){
    log = document.getElementById("log");

    let goOnline = document.getElementById("goOnline");
    let goOffline = document.getElementById("goOffline");

    goOnline.addEventListener("click", createWebSocket, false);
    goOffline.addEventListener("click", deleteWebSocket, false);
}

function createWebSocket(){
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt){onOpen(evt)};
    websocket.onclose = function(evt){onClose(evt)};
    websocket.onmessage = function(evt){onMessage(evt)};
    websocket.onerror = function(evt) { onError(evt) };
}

function deleteWebSocket() {
    websocket.close();

}

function onOpen(evt){

}


function onClose(evt) {
    writeToScreen("DISCONNECTED");
}

function onMessage(evt) {
    idOfEachText++; // TODO: CHANGE TO INTEGRATE INTO THE DATABASE
    document.getElementsByClassName(idOfEachText).contentEditable = "true";

    var message = JSON.parse(evt.data);
    var decrypted;

    if(message.content === 'Connected!') {
        decrypted = 'Connected!';

        writeToScreen( "<span id=" + "'" + idOfEachText + "'>" + message.from + " : " + displayTime(message.timestamp.toString()) + ":"
            + decrypted.toString(CryptoJS.enc.Utf8) + "</span> <button class=" + "'" + idOfEachText + "' " +
            "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
            "<button contenteditable > &#10503 </button>");
    } else {
        decrypted = decrypt(message.content, password);
        writeToScreen("<span id=" + "'" + idOfEachText + "'>" +
            message.from + " : " + displayTime(message.timestamp.toString()) + ":"
            + decrypted.toString(CryptoJS.enc.Utf8) + "</span> <button class=" + "'" + idOfEachText + "' " +
            "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
            "<button contenteditable > &#10503 </button>");
    }

    // websocket.close();
}

function onError(evt){
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend() {
    var content = document.getElementById("msg").value;

    var userToSendTo = document.getElementById("username").value;

    var encrypted = encrypt(content, password);
    var json = JSON.stringify({
        "to": userToSendTo === "" ? null : userToSendTo,
        "content": encrypted
    });

    websocket.send(json);
}


function writeToScreen(message){
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    log.appendChild(pre);
}

/**
 * Searches for user.
 */
function userSearch() {
    let searchUserInput = document.searchUserForm.searchUser.value.trim();
    let displayMessage = document.getElementsByName("search_message");
    let search_result = document.getElementById("search_result");

    fetch('http://localhost:8080/prattle/rest/user/search/'+ searchUserInput)
        .then((response) => {
            return response.json();
        })
        .then((userData) => {

            search_result.innerHTML = "";

            for (user in userData){
                search_result.innerHTML += '<span> Username: ' + userData[user].username +  '</span> ' +
                    '<button onclick=messageUser(' + '"' + userData[user].username  + '"' + ')> Message User</button> <br>';
            }
        })
        .catch((error) => {
            displayMessage.innerText = error;
        });
}

//MsgWindow
function messageUser(userToSend){
    let myWindow =  window.open("",userToSend,"width=500,height=500");

    let header = '<html>' +
        '<head>' +
        '    <title>Chat</title>' +
        '    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.9-1/crypto-js.js"></script>' +
        '    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">' +
        '    <link rel="stylesheet" href="style.css">' +
        '    <script src="session.js"></script>\n' +
        '</head>' +
        '<body>';


    let writeToDoc = '<h3>Message ' + userToSend + '</h3> ' +
        '<div class="textarea" contenteditable="false" id="log"></div> ' +
        '<input type="text" size="51" id="msg" placeholder="Message"/>' +
        '<button onclick=sentTo(' + '"' + userToSend + '"' + ')>Send</button>';


    let bottomParts = '</body>' +
        '<script src="websocket.js"></script>' +
        '<script src="index.js"></script>' +
        '<script src="https://cdn.jsdelivr.net/npm/js-cookie@2/src/js.cookie.min.js"></script>' +
        '</html>';

    myWindow.document.write(header + writeToDoc + bottomParts);
}

function sentTo(userToSendTo) {
    var content = document.getElementById("msg").value;

    var encrypted = encrypt(content, password);
    var json = JSON.stringify({
        "to": userToSendTo,
        "content": encrypted
    });

    websocket.send(json);
}


window.addEventListener("load", init, false);

// let ws = new WebSocket("ws://" + document.location.host + "/prattle/chat/" + accountName);

// ws.onmessage = function (event) {
//     idOfEachText++; // TODO: CHANGE TO INTEGRATE INTO THE DATABASE
//     document.getElementsByClassName(idOfEachText).contentEditable = "true";
//
//     var log = document.getElementById("log");
//     var message = JSON.parse(event.data);
//     var decrypted;
//
//     if(message.content === 'Connected!') {
//         decrypted = 'Connected!';
//
//         log.innerHTML += "<span id=" + "'" + idOfEachText + "'>" +
//             message.from + " : " + displayTime(message.timestamp.toString()) + ":"
//             + decrypted.toString(CryptoJS.enc.Utf8) + "</span> <button class=" + "'" + idOfEachText + "' " +
//             "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
//             "<button contenteditable > &#10503 </button>" + "<br />";
//     } else {
//         decrypted = decrypt(message.content, password);
//
//         log.innerHTML += "<span id=" + "'" + idOfEachText + "'>" +
//             message.from + " : " + displayTime(message.timestamp.toString()) + ":"
//             + decrypted.toString(CryptoJS.enc.Utf8) + "</span> <button class=" + "'" + idOfEachText + "' " +
//             "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
//             "<button contenteditable > &#10503 </button>" + "<br />";
//
//         // if (typeof message.timestamp !== 'undefined') {
//         //     log.innerHTML += "<span id=" + "'" + idOfEachText + "'>" +
//         //         message.from + " : " + displayTime(message.timestamp.toString()) + ":"
//         //         + decrypted.toString(CryptoJS.enc.Utf8) + "</span> <button class=" + "'" + idOfEachText + "' " +
//         //         "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
//         //         "<button contenteditable > &#10503 </button>" + "<br />";
//         // } else {
//         //     log.innerHTML += "<span id=" + "'" + idOfEachText + "'>" +
//         //         message.from + " : " + message.content + "</span> <button class="
//         //         + "'" + idOfEachText + "' " + "contenteditable onclick=copy(" + idOfEachText + ")>&#x2398</button>" +
//         //         "<button contenteditable > &#10503 </button>" + "<br />";
//         // }
//     }
// };

// function send() {
//     var content = document.getElementById("msg").value;
//     var userToSendTo = document.getElementById("username").value;
//
//     var encrypted = encrypt(content, password);
//     var json = JSON.stringify({
//         "to": userToSendTo === "" ? null : userToSendTo,
//         "content": encrypted
//     });
//
//     ws.send(json);
// }


/**
 * Logout the user.
 */
function logout() {

    setCookie("username", "", 365);
    window.location.href = 'login.html';
}

/**
 * Sets the cookie once the user logs in.
 * @param cookie_name is the name of the cookie.
 * @param cookie_value is the value of the cookie.
 * @param exdays is the expiration days of the cookie.
 */
function setCookie(cookie_name, cookie_value, exdays){
    var dt = new Date();
    dt.setTime(dt.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+dt.toUTCString();
    document.cookie = cookie_name + "=" + cookie_value + "; " + expires;
}

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

function copy(id) {
    const textToCopy = document.getElementById(id).innerText;

    navigator.clipboard.writeText(textToCopy).then(r => {
        alert("Copied Successfully");
    }, function () {
        alert("Copied Unsuccessfully");
    }) ;
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

