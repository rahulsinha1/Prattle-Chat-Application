function login(){
    var username = document.loginForm.username.value;
    var password = document.loginForm.password.value;

    let displayMessage = document.getElementById("message");

    // Grab username and password from the database and do a check.
    // if(localStorage.getItem('username') === username &&
    //     localStorage.getItem('password') === password){
    if(true){
        /**
         * Cookies:
         * Expires
         * Domain
         * Path
         * Secure
         * Name = Value
         */
        Cookies.set("username", username);
        Cookies.set("password", password);

        window.location.href = "chat_room.html";
    } else {
        displayMessage.innerText = "Incorrect credential.";
    }
}



