let createForm = document.getElementById("create_group_form");
let updateForm = document.getElementById("update_group_section");
let addInviteUserGroupForm = document.getElementById("add_invite_user_section");
let deleteGroup = document.getElementById("delete_group_section");
let detailGroup = document.getElementById("details_group_section");
let searchAny = document.getElementById("search_field_section");

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
    deleteGroup.style.display = "none";
    detailGroup.style.display = "none";
    searchAny.style.display = "none";
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

// SEARCH
/**
 * Display the search form.
 */
function searchButton() {
    closeAllDisplay();
    searchAny.style.display = "block";

    document.getElementById("search_user").style.display = "none";
    document.getElementById("search_group").style.display = "none";

    document.getElementById("User").checked = false;
    document.getElementById("Group").checked = false;

}

/**
 * Select the option between search user or search group,
 */
function selectSearchArea(){
    let radios = document.getElementsByName("SearchArea");
    let search_user = document.getElementById("search_user");
    let search_group = document.getElementById("search_group");
    let displayMessage = document.getElementsByName("search_message");
    let search_result = document.getElementById("search_result");


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
        // Displays Search USER
        search_user.style.display = "block";
        search_group.style.display = "none";
        displayMessage.innerText = "";
        search_result.innerHTML = "";
    } else {
        // Displays Search Group
        search_user.style.display = "none";
        search_group.style.display = "block";
        displayMessage.innerText = "";
        search_result.innerHTML = "";
    }
}

/**
 * Searches for user.
 */
function userSearch() {
    let searchUserInput = document.searchUserForm.searchUser.value.trim();
    let displayMessage = document.getElementsByName("search_message");
    let search_result = document.getElementById("search_result");

    fetch('http://localhost:8080/prattle/rest/user/search/'+ searchUserInput)
        .then((response) => {
            return response.json();
        })
        .then((userData) => {

            search_result.innerHTML = "";

            for (user in userData){
                search_result.innerHTML += '<span> Username: ' + userData[user].username +  '</span> ' +
                    '<button onclick=messageUser(' + '"' + userData[user].username  + '"' + ')> Message User</button> <br>';
            }
        })
        .catch((error) => {
            displayMessage.innerText = error;
        });
}

function groupSearch() {
    let searchGroupInput = document.searchGroupForm.searchGroup.value;

    fetch('http://localhost:8080/prattle/rest/group/search/'+ searchGroupInput)
        .then((response) => {
            return response.json();
        })
        .then((groupData) => {
            console.log(groupData);

            search_result.innerHTML = "";

            for (group in groupData){
                search_result.innerHTML += '<span> Group Name: ' + groupData[group].name +  '<br>' +
                    'Group Description: ' + groupData[group].description +  '<br>' +
                    'Created By: ' + groupData[group].createdBy + '</span> <br> ' +
                    '<button onclick=joinGroup(' + '"' + groupData[group].name  + '"' + ')>Join Group</button> <br>';
            }
        })
        .catch((error) => {
            displayMessage.innerText = error;
        });

}

function joinGroup(groupName) {
    let displayMessage = document.getElementsByName("search_message");

    if( groupName !== ""){
            fetch('http://localhost:8080/prattle/rest/group/addUser/' + groupName + '/' + username, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then((response)=>{
                if(!response.ok){
                    displayMessage.innerText = "Error while adding user " + username + " to " + groupName;
                } else {
                    displayMessage.innerText = "Successfully Added user " + username + " to " + groupName;
                }
            });
        } else {
        displayMessage.innerText = "Please select a group."
    }
}

//MsgWindow
function messageUser(userToSend){
    let myWindow =  window.open("",userToSend,"width=500,height=500");

    let header = '<html>' +
        '<head>' +
        '    <title>Chat</title>' +
        '    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.9-1/crypto-js.js"></script>' +
        '    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">' +
        '    <link rel="stylesheet" href="style.css">' +
        '    <script src="session.js"></script>\n' +
        '</head>' +
        '<body>';


    let writeToDoc = '<h3>Message</h3> ' +
        '<div class="textarea" contenteditable="false" id="log"></div> ' +
        '<input type="text" size="51" id="msg" placeholder="Message"/>' +
        '<button onclick=sentTo(' + '"' + userToSend + '"' + ')>Send</button>';


    let bottomParts = '</body>' +
        '<script src="websocket.js"></script>' +
        '<script src="index.js"></script>' +
        '<script src="https://cdn.jsdelivr.net/npm/js-cookie@2/src/js.cookie.min.js"></script>' +
        '</html>';

    myWindow.document.write(header + writeToDoc + bottomParts);
}

function sentTo(userToSendTo) {
    console.log(userToSendTo)
    // var content = document.getElementById("msg").value;
    //
    // var encrypted = encrypt(content, password);
    // var json = JSON.stringify({
    //     "to": userToSendTo,
    //     "content": encrypted
    // });
    //
    // websocket.send(json);
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

    fetch('http://localhost:8080/prattle/rest/group/getGroupUserIsModOf/'+ username)
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

//ADD&INVITE USERS SECTION
let conn = new WebSocket("ws://" + document.location.host + "/prattle/chat/" + username);

/**
 * Display the Add/Invite User.
 */
function addUserToGroup() {
    closeAllDisplay();
    document.getElementById("add_user").style.display = "none";
    document.getElementById("invite_user").style.display = "none";
    document.getElementById("remove_user").style.display = "none";

    document.getElementById("Add").checked = false;
    document.getElementById("Invite").checked = false;
    document.getElementById("Remove").checked = false;

    addInviteUserGroupForm.style.display = "block";
}

/**
 * Select the option for the user.
 */
function selectOption(){
    let radios = document.getElementsByName("Add_Invite");
    let add_user = document.getElementById("add_user");
    let invite_user = document.getElementById("invite_user");
    let remove_user = document.getElementById("remove_user");

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

        fetch('http://localhost:8080/prattle/rest/group/getGroupUserIsModOf/'+ username)
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

    } else if(selectedOption === "2"){
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
    } else {
        // Remove USER to Group
        invite_user.style.display = "none";
        add_user.style.display = "none";
        remove_user.style.display = "block";

        let modOfGroupForRemove = document.getElementById("modOfGroupForRemove");
        let displayMessage = document.getElementById("remove_user_group_message");

        fetch('http://localhost:8080/prattle/rest/group/getGroupUserIsModOf/'+ username)
            .then((response) => {
                return response.json();
            })
            .then((groupData) => {
                modOfGroupForRemove.innerText = "";
                for(group in groupData){
                    modOfGroupForRemove.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
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
 * Submit remove user from group.
 */
function submitRemoveUserFromGroup(){
    let groupName = document.getElementById("modOfGroupForRemove").value;
    let usernameToBeRemoved = document.removeUserForm.username.value;
    let displayMessage = document.getElementById("remove_user_group_message");

    if(groupName !== ""){
        if(usernameToBeRemoved.trim() !== "" ){
            fetch('http://localhost:8080/prattle/rest/group/removeUser/' + groupName + '/' + usernameToBeRemoved, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            }).then((response)=>{
                if(!response.ok){
                    displayMessage.innerText = "Error while removing user " + usernameToBeRemoved + " from " + groupName;
                } else {
                    displayMessage.innerText = "Successfully Removed user " + usernameToBeRemoved + " from " + groupName;
                }
            });
        } else {
            displayMessage.innerText = "Please input a username."
        }

    }else {
        displayMessage.innerText = "Please select a group."
    }
}

// DELETE GROUP
/**
 * Displays the Delete Group Section.
 */
function deleteGroupButton(){
    closeAllDisplay();
    deleteGroup.style.display = "block";

    let modOfGroupToDelete = document.getElementById("modOfGroupToDelete");
    let displayMessage = document.getElementById("delete_group_message");

    fetch('http://localhost:8080/prattle/rest/group/getGroupUserIsModOf/'+ username)
        .then((response) => {
            return response.json();
        })
        .then((groupData) => {
            modOfGroupToDelete.innerHTML = "";
            for(group in groupData){
                modOfGroupToDelete.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
            }
        })
        .catch((error) => {
            displayMessage.innerHTML = error + "<br />";
        })

    console.log(modOfGroupToDelete.value)

}

/**
 * Submit Group Deletion Button.
 */
function submitGroupDeletion() {
    let selectedGroupToDelete = document.getElementById("modOfGroupToDelete").value;
    let displayMessage = document.getElementById("delete_group_message");


    if(selectedGroupToDelete !== "") {
        fetch('http://localhost:8080/prattle/rest/group/deleteGroup/' + selectedGroupToDelete, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        }).then((response) => {
            if (!response.ok) {
                displayMessage.innerText = "Unsuccessfully Deletion";
            } else {
                displayMessage.innerText = "Successfully Deletion";
            }
        }).catch((e) => {
            displayMessage.innerText = "Unsuccessfully Deletion";
        })
    } else {
        displayMessage.innerText = "Must Select A Group.";
    }
}

// GROUP DETAILS
/**
 * Displays all group user is apart of.
 */
function detailGroupButton(){
    closeAllDisplay();
    detailGroup.style.display = "block";

    let groupDetails = document.getElementById("groupDetails");
    let displayMessage = document.getElementById("details_group_message");

    fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ username)
        .then((response) => {
            return response.json();
        })
        .then((groupData) => {
            groupDetails.innerHTML = "";
            for(group in groupData){
                groupDetails.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
            }
        })
        .catch((error) => {
            displayMessage.innerText = error;
        })
}

/**
 * Request the details of that group which will allow users to remove themselves from the group.
 */
function submitGroupDetails(){

    let group = document.getElementById("groupDetails").value;
    let displayMessage = document.getElementById("details_group_message");
    let group_info = document.getElementById("group_info");


    fetch('http://localhost:8080/prattle/rest/group/getGroupDetails/'+ group)
        .then((response) => {
            return response.json();
        })
        .then((groupData) => {
            group_info.innerHTML = '<span>Group Name: ' + groupData.name + '</span>' + "</br>" +
                '<span> Created By: ' + groupData.createdBy + '</span>' + "</br>" +
                '<span> Private: ' + groupData.isGroupPrivate + '</span>' + "</br>" +
                '<span> Description: ' + groupData.description + '</span>' + "</br>" +
                '<button onclick=leaveGroup(' + '"' + groupData.name + '"' + ')> Leave Group</button>';
        })
        .catch((error) => {
            displayMessage.innerText = error;
        })
}

/**
 * Allows users to leave a group.
 * @param groupName the name of the group the user wants to leave.
 */
function leaveGroup(groupName) {
    let displayMessage = document.getElementById("details_group_message");

    if(group !== "") {
        fetch('http://localhost:8080/prattle/rest/group/removeUser/' + groupName + '/' + username, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        }).then((response) => {
            if (!response.ok) {
                displayMessage.innerText = "You Weren't Removed Successfully";
            } else {
                displayMessage.innerText = "You Were Removed Successfully";
            }
        }).catch((e) => {
            displayMessage.innerText = "There Was An Error in Removing You.";
        })
    } else {
        displayMessage.innerText = "Must Select A Group.";
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

