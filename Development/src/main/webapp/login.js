function fieldsNotEmpty(username, password) {
    return username.trim() !== "" || password.trim()  !== "";
}

function login(){
    var username = document.loginForm.username.value;
    var password = document.loginForm.password.value;

    let displayMessage = document.getElementById("message");

    if(fieldsNotEmpty(username,password)){
        fetch('http://localhost:8080/prattle/rest/user/getUser/'+ username)
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                if(data.password === password){
                    localStorage.setItem("username", username)
                    window.location.href = "index.html";
                } else {
                    throw new Error();
                }
            })
            .catch((error)=> {
                displayMessage.innerText = "Invalid credential.";
            })
    } else {
        displayMessage.innerText = "Field(s) must not be empty.";
    }

}



