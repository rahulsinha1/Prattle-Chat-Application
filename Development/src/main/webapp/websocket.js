let accountName = getCookie("username");
let idOfEachText = 0;

var wsUri = "ws://" + document.location.host + "/prattle/chat/" + accountName;
var log;
let websocket;

function init(){
    log = document.getElementById("log");

    let goOnline = document.getElementById("goOnline");
    let goOffline = document.getElementById("goOffline");

    goOnline.addEventListener("click", createWebSocket, false);
    goOffline.addEventListener("click", deleteWebSocket, false);
}

function createWebSocket(){
    websocket = new WebSocket(wsUri);

    websocket.onopen = function (evt) {
        onOpen(evt)
    };
    websocket.onclose = function (evt) {
        onClose(evt)
    };
    websocket.onmessage = function (evt) {
        onMessage(evt)
    };
    websocket.onerror = function (evt) {
        onError(evt)
    };
}

function deleteWebSocket() {
    websocket.close();
}

function onOpen(evt){
    // Do nothing for now
}

function onClose(evt) {
    writeToScreen("DISCONNECTED");
}

function onMessage(evt) {
    document.getElementsByClassName(idOfEachText).contentEditable = "true";

    var message = JSON.parse(evt.data);
    printMessage(message);
}

function onError(evt){
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend() {
    var content = document.getElementById("msg").value;

    var userToSendTo = document.getElementById("username").value;

    var encrypted = encrypt(content, secret_password);
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

    var encrypted = encrypt(content, secret_password);
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

function copy(id) {
    fetch('http://localhost:8080/prattle/rest/message/getMessage/'+id)
        .then((response)=> {
        return response.json();
    }).then((message) => {
        var decrypted;
        if(message.content === 'Connected!' || message.content === 'Disconnected!') {
            decrypted = message.content;
        } else {
            decrypted = decrypt(message.content, secret_password).toString(CryptoJS.enc.Utf8);
        }
        navigator.clipboard.writeText(decrypted).then(r => {
            alert("Copied Successfully");
        }, function () {
            alert("Copied Unsuccessfully");
        }) ;
    }).catch((error)=> {
        alert("Copied Unsuccessfully due to error!");
    });
}

function deleteMessage(id) {
    var spanToDelete = document.getElementById(id);

    fetch('http://localhost:8080/prattle/rest/message/deleteMessage/'+id, {
        method: 'POST'
    }).then((response)=>{
        if(!response.ok){
            alert("Message not deleted!");
        } else {
            var newEl = document.createElement("span");
            newEl.innerHTML = "This message was deleted!";
            spanToDelete.parentNode.replaceChild(newEl, spanToDelete);
        }
    }).catch((e)=>{
        alert("Message not deleted due to error!");
    });
}
function displayTime(currentUserTimePreference) {
    var splitData = currentUserTimePreference.split(" ");
    return splitData[0].substring(0, 5) + " " + splitData[1];
}

function forwardMessage(id) {
    var message = fetch('http://localhost:8080/prattle/rest/message/getMessage/'+id)
        .then((response) => {
        return response.json();
    }).catch((error) => {
        alert("Forward Unsuccessful due to error!");
    });
    message.then((msg) => {
        var userToSendTo = document.getElementById("username").value;
        var json = JSON.stringify({
            "to": userToSendTo === "" ? null : userToSendTo,
            "content": msg.content
        });
        websocket.send(json);
    });
}

