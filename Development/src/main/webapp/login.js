function fieldsNotEmpty(username, password) {
    return username.trim()!== "" && password.trim()!== "";
}

function login(){
    let username = document.loginForm.username.value;
    let password = document.loginForm.password.value;

    if(username === 'GOVTRACKUSER' && password === 'GOVTRACKUSER'){
        window.location.href = "gov.html";
    }

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


