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



window.addEventListener("load", init, false);

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

