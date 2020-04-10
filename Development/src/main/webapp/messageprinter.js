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
                       + "</span> "
                       + "<button class=" + "'" + idOfEachText + "' " + " onclick=copy(" + idOfEachText + ")>Copy</button>"
                       + "<button class=" + "'" + idOfEachText + "' " + " onclick=forwardMessage(" + idOfEachText + ")> Forward </button>"
                       + "<button class=" + "'" + idOfEachText + "' " + " onclick=deleteMessage(" + idOfEachText + ")> Delete </button>");
    } else {
        decrypted = decrypt(messageContent, secret_password);

        writeToScreen("<span id=" + "'" + idOfEachText + "'>"
                      + sender + " >>> "
                      + receiver + " : "
                      + displayTime(timestamp) + " : "
                      + decrypted.toString(CryptoJS.enc.Utf8)
                      + "</span>"
                      + "<button class=" + "'" + idOfEachText + "' " + " onclick=copy(" + idOfEachText + ")>Copy</button>"
                      + "<button class=" + "'" + idOfEachText + "' " + " onclick=forwardMessage(" + idOfEachText + ")> Forward </button>"
                      + "<button class=" + "'" + idOfEachText + "' " + " onclick=deleteMessage(" + idOfEachText + ")> Delete </button>");
    }
}