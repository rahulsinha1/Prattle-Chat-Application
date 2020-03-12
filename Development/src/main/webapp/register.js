/**
 * Delays the next line.
 * @param ms millisecond.
 * @returns {Promise<unknown>}
 */
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

/**
 * Validates that the information is not empty.
 * @param first_name is the first name of the user.
 * @param last_name is the last name of the user.
 * @param username is the username.
 * @param confirm_password is the confirm password.
 * @param password is the password of the user.
 * @returns {boolean} true if fields are not empty.
 */
function fieldsNotEmpty(first_name, last_name, username, confirm_password, password) {
    return first_name.length !== 0 || last_name.length !== 0 || username.length !== 0 ||
        confirm_password.length !== 0 || password.length !== 0;
}

/**
 * Registers the user.
 * @returns {Promise<void>}
 */
async function register(){
    let first_name = document.registerForm.fname.value;
    let last_name = document.registerForm.lname.value;
    let username = document.registerForm.username.value;
    let password = document.registerForm.password.value;
    let confirm_password = document.registerForm.confirm_password.value;
    let timezone = document.registerForm.timezone.value;

    let displayMessage = document.getElementById("message");

    displayMessage.innerText = "";

     if(fieldsNotEmpty(first_name, last_name, username, confirm_password, password)){
         if(password === confirm_password){

             // Send information to the database.
             localStorage.setItem(username, username);
             localStorage.setItem(password, password);

             displayMessage.innerText = "Successfully Registered";
             await sleep(2000);
             // window.location.href = "index.html"
         } else {
             displayMessage.innerText = "Password and Confirm Password are not the same.";
         }
     } else {
         displayMessage.innerText = "Field(s) cannot be empty.";
     }
}
