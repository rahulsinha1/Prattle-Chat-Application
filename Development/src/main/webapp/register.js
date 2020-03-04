
function register(){
    var fname = document.getElementById("fname").value;
    var lname = document.getElementById("lname").value;
    var username = document.getElementById("register_username").value;
    var password = document.getElementById("register_password").value;
    var confirm_password = document.getElementById("confirm_password").value;

    localStorage.setItem('name', fname + " " + lname);
    localStorage.setItem('register_username', username);
    localStorage.setItem('register_password', password);

    alert("You've been successfully registered.");
    window.location.href = "index.html";
}
