function fieldsNotEmpty(username, password) {
    return username.trim()!== "" && password.trim()!== "";
}

function login(){
    let username = document.loginForm.username.value;
    let password = document.loginForm.password.value;

    let displayMessage = document.getElementById("message");

    if(fieldsNotEmpty(username,password)){
        fetch('http://localhost:8080/prattle/rest/user/getUser/'+ username)
            .then((response) => {
                console.log(response);
                return response.json();
            })
            .then((data) => {
                if(data.password === password){
                    setCookie("username",username,365);
                    window.location.href = "index.html";
                }
            })
            .catch((error)=> {
                displayMessage.innerText = error;
            })
    } else {
        displayMessage.innerText = "Field(s) must not be empty.";
    }
}

function setCookie(cookie_name, cookie_value, exdays){
    var dt = new Date();
    dt.setTime(dt.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+dt.toUTCString();
    document.cookie = cookie_name + "=" + cookie_value + "; " + expires;
}




