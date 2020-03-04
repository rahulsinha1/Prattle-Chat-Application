function login(){
    var username = document.getElementById("login_username").value;
    var password = document.getElementById("login_password").value;

    if(localStorage.getItem('register_username') === username &&
        localStorage.getItem('register_password') === password){
        alert("Login Success");
        window.location.href = "chat_room.html";
    } else {
        alert("Incorrect credential.")
    }
}
