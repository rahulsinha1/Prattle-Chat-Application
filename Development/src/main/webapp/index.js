let createForm = document.getElementById("create_group_form");
let updateForm = document.getElementById("update_group_section");
let addInviteUserGroupForm = document.getElementById("add_invite_user_section");
// let viewAllGroupUserIsApartOf = document.getElementById("view_group_section");
// let deleteGroupForm = document.getElementById("delete_group_form");
// let moderatorOfGroup = document.getElementById("moderatorOfGroup");


let username = getCookie("username");
let secret_password = "Secret Password";
var keySize = 256;
var iterations = 100;

document.getElementById("welcoming").innerHTML = "Welcome " + username;

closeAllDisplay();

/**
 * Close all display tag.
 */
function closeAllDisplay(){
    createForm.style.display = "none";
    updateForm.style.display = "none";
    addInviteUserGroupForm.style.display = "none";

    // viewAllGroupUserIsApartOf.style.display = "none";
    //  deleteGroupForm.style.display = "none";
}

/**
 * Gets the cookie.
 * @param cname is the cookie name.
 * @returns {string} the cookie of the username.
 */
function getCookie(cname){
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)===' ') c = c.substring(1);
        if (c.indexOf(name) === 0) return c.substring(name.length,c.length);
    }
    return "";
}

/**
 * Sets the cookie once the user logs in.
 * @param cookie_name is the name of the cookie.
 * @param cookie_value is the value of the cookie.
 * @param exdays is the expiration days of the cookie.
 */
function setCookie(cookie_name, cookie_value, exdays){
    var dt = new Date();
    dt.setTime(dt.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+dt.toUTCString();
    document.cookie = cookie_name + "=" + cookie_value + "; " + expires;
}

/**
 * Logout the user.
 */
function logout() {
    setCookie("username", "", 365);
    window.location.href = 'login.html';
}

// CREATING A GROUP SECTION
/**
 * Display the Create Group form.
 */
function createGroupButton(){
    closeAllDisplay();
    createForm.style.display = "block";
}

/**
 * The submit button that sends the information to the controller.
 */
function submitGroupCreation(){
    let groupName = document.createGroupForm.groupName.value;
    let description = document.createGroupForm.description.value;
    let displayMessage = document.getElementById("create_group_message");
    displayMessage.innerHTML = "";
    if( groupName.trim() !== ""){
        const group_data = {
            "name": groupName,
            "description": description,
            "createdBy": username,
            "isGroupPrivate": false,
            "password": "",
        };
        fetch('http://localhost:8080/prattle/rest/group/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(group_data),
        }).then((response)=>{
            if(!response.ok){
                displayMessage.innerHTML = "Unsuccessfully Created" + "<br />";
            } else {
                displayMessage.innerHTML = "Successfully Created" + "<br />";
            }
        }).catch((e)=>{
            displayMessage.innerHTML = "Unsuccessfully Created" + "<br />";
        })
    }
}

//UPDATE A GROUP SECTION
/**
 * Displays the Update Group Form.
 */
function updateGroupButton(){
    closeAllDisplay();
    document.getElementById("group_to_be_updated").style.display = "none";

    updateForm.style.display = "block";

    let modOfGroup = document.getElementById("modOfGroup");
    let displayMessage = document.getElementById("update_group_message");

    // TODO: change fetch code to Modarator
    fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ username)
        .then((response) => {
            return response.json();
        })
        .then((groupData) => {
            modOfGroup.innerHTML = "";
            for(group in groupData){
                modOfGroup.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
            }
        })
        .catch((error) => {
            displayMessage.innerHTML = error + "<br />";
        })
}

/**
 * Select the group the user wants to update.
 */
function selectedGroup() {
    let selectedGroup = document.getElementById("modOfGroup").value;

    if(selectedGroup !== ""){
        document.getElementById("group_to_be_updated").style.display = "block";
        document.getElementById("groupName").innerText = selectedGroup;
    } else {
        document.getElementById("update_group_message").innerText = "Must Select A Group.";
    }
}

/**
 * Submit the Group Update.
 */
function submitGroupUpdate() {
    let selectedGroup = document.getElementById("modOfGroup").value;
    let updated_description = document.updateGroup.description.value;
    let updated_isPrivate = document.updateGroup.isPrivate.value;
    let update_password = document.updateGroup.group_password.value;


    if(selectedGroup !== "") {
        const group_data = {
            "name": selectedGroup,
            "description": updated_description,
            "createdBy": getCookie('username'),
            "isGroupPrivate": updated_isPrivate,
            "password": update_password,
        };

        fetch('http://localhost:8080/prattle/rest/group/updateGroup/' + selectedGroup, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(group_data),
        }).then((response) => {
            if (!response.ok) {
                displayMessage.innerText = "Unsuccessfully Updated";
            } else {
                displayMessage.innerText = "Successfully Updated";
            }
        }).catch((e) => {
            displayMessage.innerText = "Unsuccessfully Updated";
        })
    } else {
        document.getElementById("update_group_message").innerText = "Must Select A Group.";
    }
}

//ADD/INVITE USERS SECTION
let conn = new WebSocket("ws://" + document.location.host + "/prattle/chat/" + accountName);

/**
 * Display the Add/Invite User.
 */
function addUserToGroup() {
    closeAllDisplay();
    document.getElementById("add_user").style.display = "none";
    document.getElementById("invite_user").style.display = "none";

    document.getElementById("Add").checked = false;
    document.getElementById("Invite").checked = false;

    addInviteUserGroupForm.style.display = "block";
}

/**
 * Select the option for the user.
 */
function selectOption(){
    let radios = document.getElementsByName("Add_Invite");
    let add_user = document.getElementById("add_user");
    let invite_user = document.getElementById("invite_user");

    let selectedOption;

    for (var i = 0, length = radios.length; i < length; i++) {
        if (radios[i].checked) {
            // do whatever you want with the checked radio
            selectedOption = radios[i].value;

            // only one radio can be logically checked, don't check the rest
            break;
        }
    }

    if(selectedOption === "1"){
        // Add USER to Group
        invite_user.style.display = "none";
        add_user.style.display = "block";

        let modOfGroupForAdd = document.getElementById("modOfGroupForAdd");
        let displayMessage = document.getElementById("add_user_group_message");

        // TODO: change fetch code to Modarator
        fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ username)
            .then((response) => {
                return response.json();
            })
            .then((groupData) => {
                modOfGroupForAdd.innerText = "";
                for(group in groupData){
                    modOfGroupForAdd.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
                }
            })
            .catch((error) => {
                displayMessage.innerText = error;
            })

    } else {
        // Invite USER to Group
        add_user.style.display = "none";
        invite_user.style.display = "block";

        let listOfGroups = document.getElementById("listOfGroups");
        let displayMessage = document.getElementById("invite_user_group_message");

        fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ username)
            .then((response) => {
                return response.json();
            })
            .then((groupData) => {
                listOfGroups.innerText = "";
                for(group in groupData){
                    listOfGroups.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
                }
            })
            .catch((error) => {
                displayMessage.innerText = error;
            })
    }
}

/**
 * Submits the add user to group.
 */
function submitAddUserToGroup(){
    let groupName = document.getElementById("modOfGroupForAdd").value;
    let usernameToBeAdded = document.addUserForm.username.value;
    let displayMessage = document.getElementById("add_user_group_message");

    if( groupName !== ""){
        if(usernameToBeAdded.trim() !== "" ){
            fetch('http://localhost:8080/prattle/rest/group/addUser/' + groupName + '/' + usernameToBeAdded, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then((response)=>{
                if(!response.ok){
                    displayMessage.innerText = "Error while adding user " + usernameToBeAdded + " to " + groupName;
                } else {
                    displayMessage.innerText = "Successfully Add user " + usernameToBeAdded + " to " + groupName;
                }
            });
        } else {
            displayMessage.innerText = "Please input a username."
        }
    } else {
        displayMessage.innerText = "Please select a group."
    }
}

/**
 * Submit invitation to add user to group.
 */
function submitInviteUserToGroup(){
    let groupName = document.getElementById("listOfGroups").value;
    let usernameToBeInvited = document.inviteUserForm.username.value;
    let displayMessage = document.getElementById("invite_user_group_message");

    if( groupName !== ""){
        notifyUserOfInvite(usernameToBeInvited, groupName);

        conn.onmessage = function(event){
            let notification = document.getElementById("notification");
            notification.style.color = "red";
            let decrypted;

            let notifyMessage = JSON.parse(event.data);
            decrypted = decrypt(notifyMessage.content, secret_password);

            if (typeof notifyMessage.timestamp !== 'undefined') {
                notification.innerText += displayTime(notifyMessage.timestamp.toString()) + " : "
                    + decrypted.toString(CryptoJS.enc.Utf8) + "\n";
            } else {
                notification.innerText += notifyMessage.from + " : " + notifyMessage.content + "\n";
            }
        }
    } else {
        displayMessage.innerText = "Please select a group."
    }


}

/**
 * Notify user in the notification section.
 * @param usernameToBeInvited use to notify
 * @param group group to notify.
 */
function notifyUserOfInvite(usernameToBeInvited, group){
    var message = username + " has invited " + usernameToBeInvited + " to join Group " + group;
    var encrypted = encrypt(message, secret_password);

    var json = JSON.stringify({
        "to": usernameToBeInvited,
        "content": encrypted
    });

    conn.send(json);
}


/**
 * Encrypt the message.
 * @param msg
 * @param pass
 * @returns {string}
 */
function encrypt(msg, pass) {
    var salt = CryptoJS.lib.WordArray.random(128 / 8);

    var key = CryptoJS.PBKDF2(pass, salt, {
        keySize: keySize / 32,
        iterations: iterations
    });

    var iv = CryptoJS.lib.WordArray.random(128 / 8);

    var encrypted = CryptoJS.AES.encrypt(msg, key, {
        iv: iv,
        padding: CryptoJS.pad.Pkcs7,
        mode: CryptoJS.mode.CBC
    });

    var transitmessage = salt.toString() + iv.toString() + encrypted.toString();
    return transitmessage;
}

/**
 * Decrypt the message.
 * @param transitmessage
 * @param pass
 * @returns {PromiseLike<ArrayBuffer>}
 */
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

    })
    return decrypted;
}


function search() {
   document.getElementById("search-result").innerHTML = "";
  var x = document.getElementById("search").value;
  var table = document.createElement('table');
   var arrValue = new Array();
   table.setAttribute('id', 'userTable');
    var tr = table.insertRow(-1);
     arrHead = [];
     arrValue =[];
  fetch('http://localhost:8080/prattle/rest/user/search/'+ x)
          .then((response) => {
          return response.json();
  })
  .then((userData) => {
          console.log(userData);
      arrHead = ['UserName', 'First Name', 'Last Name'];
      for(user in userData)
          arrValue.push([userData[user].username,userData[user].firstName,userData[user].lastName]);

    for (var h = 0; h < arrHead.length; h++) {
                var th = document.createElement('th');
                th.innerHTML = arrHead[h];
                tr.appendChild(th);
            }
        for (var c = 0; c <= arrValue.length - 1; c++) {
                    tr = table.insertRow(-1);

                    for (var j = 0; j < arrHead.length; j++) {
                        var td = document.createElement('td');
                        td = tr.insertCell(-1);
                        td.innerHTML = arrValue[c][j];
                    }
                }

                document.getElementById("search-result").appendChild(table);
  })
  .catch((error)=> {
          document.getElementById("search-result").innerHTML = "No users found";
  })




  }







//
//
// function deleteGroup() {
//     closeAllDisplay();
//     deleteGroupForm.style.display = "block";
//
//     let displayMessage = document.getElementById("delete_group_message");
//
//     var modOfGroup = new Set();
//     // GET LIST OF GROUPS USER IS MODERATOR OF
//     fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ localStorage.getItem('username'))
//         .then((response) => {
//             return response.json();
//         })
//         .then((groupData) => {
//             console.log(groupData);
//             for ( group in groupData){
//                 for(let i = 0; i < groupData[group].moderators.length; i++){
//                     if(groupData[group].moderators[i] === localStorage.getItem('username')) {
//                         modOfGroup.add(group);
//                     }
//                 }
//             }
//         })
//         .catch((error)=> {
//             displayMessage.innerText = error;
//         })
//     modOfGroup.forEach((group) =>{
//         moderatorOfGroup.innerHTML += "<option value=" + group.key + ">" + group.value + "</option>";
//     })
//     //Save Selected
//     selectedGroup = moderatorOfGroup.value;
// }
//
// function viewGroupButton(){
//     closeAllDisplay()
//     viewAllGroupUserIsApartOf.style.display = "block";
//     let displayMessage = document.getElementById("view_group_message");
//     let groupsParticipation = document.getElementById("groupsParticipation");
//     fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ localStorage.getItem('username'))
//         .then((response) => {
//             return response.json();
//         })
//         .then((groupData) => {
//             console.log(groupData);
//             groupsParticipation.innerHTML = "";
//             for(group in groupData){
//                 groupsParticipation.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
//             }
//         })
//         .catch((error)=> {
//             displayMessage.innerText = error;
//         })
// }
//
//
// function deleteGroupButton(){
//     let displayMessage = document.getElementById("delete_group_message");
//     // DELETE A GROUP
//     fetch('http://localhost:8080/prattle/rest/deleteGroup/' + selectedGroup, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: JSON.stringify(group_data),
//     }).then((response)=>{
//         displayMessage.innerText = response.statusText;
//     }).catch((error) => {
//         displayMessage.innerText = error.toString();
//     })
// }
//
// function submitGroupView(){
//     console.log(groupsParticipation.value)
// }
//

