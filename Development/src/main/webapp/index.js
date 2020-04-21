let createForm = document.getElementById("create_group_form");
let updateForm = document.getElementById("update_group_section");
let addInviteUserGroupForm = document.getElementById("add_invite_user_section");
let deleteGroup = document.getElementById("delete_group_section");
let detailGroup = document.getElementById("details_group_section");
let searchAny = document.getElementById("search_field_section");
let updateStatus = document.getElementById("update_status_form");

let username = getCookie("username");

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

    fetch('rest/user/search/'+ searchUserInput)
        .then((response) => {
            return response.json();
        })
        .then((userData) => {

            search_result.innerHTML = "";

            for (user in userData){
                search_result.innerHTML += '<span> Username: ' + userData[user].username +  '</span> ' +
                    '<button onclick=messageUser(' + '"' + userData[user].username  + '"' + ')> Message User</button> <br>' +
                     '</span> ' +'<button onclick=followUser('+'"'+userData[user].username+'"'+')> Follow User</button> <br>';
            }
        })
        .catch((error) => {
            displayMessage.innerText = error;
        });
}


function followUser(username)
{
    let displayMessage = document.getElementsByName("search_message");
    let accountName = getCookie("username");
    fetch('rest/user/followUser/' + accountName  + '/' + username, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                        }).then((response)=>{
                            if(!response.ok){
                                displayMessage.innerText = "Error while following ";
                            } else {
                                displayMessage.innerText = "Successfully followed User ";
                            }
                        }).catch((e)=>{
                displayMessage.innerHTML = "Error in following user" + "<br />";
            })

}

/**
 * Messages user.
 * @param userToSend
 */
function messageUser(userToSend){
    let promptMessage = prompt("What would you like to tell " + userToSend + " :");

    var encrypted = encrypt(promptMessage, secret_password);
    var json = JSON.stringify({
        "to": userToSend,
        "content": encrypted
    });

    websocket.send(json);
}

/**
 * Search group.
 */
function groupSearch() {
    let searchGroupInput = document.searchGroupForm.searchGroup.value;

    fetch('rest/group/search/'+ searchGroupInput)
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

/**
 * Join group
 * @param groupName
 */
function joinGroup(groupName) {
    let displayMessage = document.getElementsByName("search_message");

    if( groupName !== ""){
            fetch('rest/group/addUser/' + groupName + '/' + username, {
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
        fetch('rest/group/create', {
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

    fetch('rest/group/getGroupUserIsModOf/'+ username)
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

    let displayMessage = document.getElementById("update_group_message");


    if(selectedGroup !== "") {
        const group_data = {
            "name": selectedGroup,
            "description": updated_description,
            "createdBy": getCookie('username'),
            "isGroupPrivate": updated_isPrivate,
            "password": update_password,
        };

        fetch('rest/group/updateGroup/' + selectedGroup, {
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
/**
 * Display the Add/Invite/Remove User.
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
        remove_user.style.display = "none";
        add_user.style.display = "block";

        let modOfGroupForAdd = document.getElementById("modOfGroupForAdd");
        let displayMessage = document.getElementById("add_user_group_message");

        fetch('rest/group/getGroupUserIsModOf/'+ username)
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
        remove_user.style.display = "none";
        invite_user.style.display = "block";

        let listOfGroups = document.getElementById("listOfGroups");
        let displayMessage = document.getElementById("invite_user_group_message");

        fetch('rest/group/getAllUserGroups/'+ username)
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

        fetch('rest/group/getGroupUserIsModOf/'+ username)
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
    let addAdMod = document.addUserForm.moderator.checked;

    if( groupName !== ""){
        if(usernameToBeAdded.trim() !== "" ){
            if(addAdMod){
                fetch('rest/group/addModerator/' + groupName + '/' + usernameToBeAdded, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }).then((response)=>{
                    if(!response.ok){
                        displayMessage.innerText = "Error while adding Moderator " + usernameToBeAdded + " to " + groupName;
                    } else {
                        displayMessage.innerText = "Successfully Added Moderator " + usernameToBeAdded + " to " + groupName;
                    }
                });
            } else {
                fetch('rest/group/addUser/' + groupName + '/' + usernameToBeAdded, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }).then((response)=>{
                    if(!response.ok){
                        displayMessage.innerText = "Error while adding user " + usernameToBeAdded + " to " + groupName;
                    } else {
                        displayMessage.innerText = "Successfully Added user " + usernameToBeAdded + " to " + groupName;
                    }
                });
            }
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
        websocket.onmessage = function(event){
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

        notifyUserOfInvite(usernameToBeInvited, groupName);
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
    let removeAsMod = document.removeUserForm.moderator.checked;


    if(groupName !== ""){
        if(usernameToBeRemoved.trim() !== "" ){
            if(removeAsMod){
                fetch('rest/group/removeModerator/' + groupName + '/' + usernameToBeRemoved, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }).then((response)=>{
                    if(!response.ok){
                        displayMessage.innerText = "Error while removing Moderator " + usernameToBeRemoved + " from " + groupName;
                    } else {
                        displayMessage.innerText = "Successfully Removed Moderator " + usernameToBeRemoved + " from " + groupName;
                    }
                });
            } else {
                fetch('rest/group/removeUser/' + groupName + '/' + usernameToBeRemoved, {
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
            }
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

    fetch('rest/group/getGroupUserIsModOf/'+ username)
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

}

/**
 * Submit Group Deletion Button.
 */
function submitGroupDeletion() {
    let selectedGroupToDelete = document.getElementById("modOfGroupToDelete").value;
    let displayMessage = document.getElementById("delete_group_message");


    if(selectedGroupToDelete !== "") {
        fetch('rest/group/deleteGroup/' + selectedGroupToDelete, {
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
    document.getElementById("group_info").innerHTML = "";


    let groupDetails = document.getElementById("groupDetails");
    let displayMessage = document.getElementById("details_group_message");

    fetch('rest/group/getAllUserGroups/'+ username)
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


    fetch('rest/group/getGroupDetails/'+ group)
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
        fetch('rest/group/removeUser/' + groupName + '/' + username, {
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

    websocket.send(json);
}

function onUserLogin() {
    getCircle();
    fetch('rest/message/getMessages/'+username)
        .then((response)=> {
            return response.json();
    }).then((messages) => {
        for(message in messages) {
            if(messages[message].deleted === false) {
                printMessage(messages[message]);
            }
        }
    }).catch((error)=> {
        console.log(error);
    });
}

function getCircle()
{
let accountName = getCookie("username");
let followers = document.getElementById("followers");
let following = document.getElementById("following");
followers.innerHTML ="<h3><u>" + "Followers" + "</u></h3>";
following.innerHTML ="<h3><u>" + "Following" + "</u></h3>";

fetch('rest/user/getFollowers/'+ accountName)
        .then((response) => {
            return response.json();
        })
        .then((userData) => {

            for (user in userData){
                followers.innerHTML += userData[user].username +
                    '::' + userData[user].status+ "<br />";
            }
        })
        .catch((error) => {
            followers.innerText = "Error";
        });


fetch('rest/user/getFollowing/'+ accountName)
        .then((response) => {
            return response.json();
        })
        .then((followData) => {

            for (user in followData){
                following.innerHTML += followData[user].username +
                     '::' + followData[user].status+ "<br />";
            }
        })
        .catch((error) => {
            following.innerText = "Error";
        });

}



function updateUserStatus(){
    let accountName = getCookie("username");
    let status = document.getElementById("status").value;
    let displayMessage = document.getElementById("update_status_message");
    displayMessage.innerHTML = "";

        fetch('rest/user/setStatus/' + accountName  + '/' + status, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                    }).then((response)=>{
                        if(!response.ok){
                            displayMessage.innerText = "Error while updating status ";
                        } else {
                            displayMessage.innerText = "Successfully updated status ";
                        }
                    }).catch((e)=>{
            displayMessage.innerHTML = "Error in updating status" + "<br />";
        })

    }

