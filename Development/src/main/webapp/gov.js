let secret_password = "Secret Password";
var messages = document.getElementById("messages");
var keySize = 256;
var iterations = 100;

function logout() {
    window.location.href = 'login.html';
}

function refresh() {
    messages.innerHTML = "";
    username = document.getElementById("username").value;
    fetch('http://localhost:8080/prattle/rest/message/getMessages/'+username)
        .then((response)=> {
        return response.json();
}).then((messages) => {
        for(message in messages) {
            printMessage(messages[message]);
    }
}).catch((error)=> {
        console.log(error);
});
}

function printMessage(message) {
    idOfEachText = message.id;
    var decrypted;
    let messageContent = message.content;
    let timestamp = message.timestamp.toString();
    let sender = message.from;
    let receiver = message.to;
    if(messageContent === 'Connected!' || messageContent === 'Disconnected!') {
        decrypted = messageContent;

        writeToScreen( "<span id=" + "'" + idOfEachText + "'>"
                       + sender + " : "
                       + displayTime(timestamp) + " : "
                       + decrypted.toString(CryptoJS.enc.Utf8)
                       + "</span> ");
    } else {
        decrypted = decrypt(messageContent, secret_password);

        writeToScreen("<span id=" + "'" + idOfEachText + "'>"
                      + sender + " >>> "
                      + receiver + " : "
                      + displayTime(timestamp) + " : "
                      + decrypted.toString(CryptoJS.enc.Utf8)
                      + "</span>");
    }
}

function writeToScreen(message){
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    messages.appendChild(pre);
}

function displayTime(currentUserTimePreference) {
    var splitData = currentUserTimePreference.split(" ");
    return splitData[0].substring(0, 5) + " " + splitData[1];
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

    });

    return decrypted;
}