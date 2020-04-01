let createForm = document.getElementById("create_group_form");
let updateForm = document.getElementById("update_group_section");
// let addUserGroupForm = document.getElementById("adduser_group_section");
// let viewAllGroupUserIsApartOf = document.getElementById("view_group_section");
// let deleteGroupForm = document.getElementById("delete_group_form");
// let moderatorOfGroup = document.getElementById("moderatorOfGroup");
// let selectedGroup = moderatorOfGroup.value;

closeAllDisplay();

/**
 * Close all display tag.
 */
function closeAllDisplay(){

    createForm.style.display = "none";
    updateForm.style.display = "none";
    // viewAllGroupUserIsApartOf.style.display = "none";
    //  deleteGroupForm.style.display = "none";
    //  addUserGroupForm.style.display = "none";
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
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}

/**
 * Checks for cookie.
 */
function checkCookie() {
    var username=getCookie("username");
    if (username != "") {
        document.getElementById("welcoming").innerText = "Welcome " + username ;
    }else{
        window.location.href = '404.html';
    }
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



/**
 * Display the Add/Invite User.
 */
function addUserToGroup() {
    closeAllDisplay();
    addUserGroupForm.style.display = "block";
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
    displayMessage.innerText = "";
    if( groupName.trim() !== ""){
        const group_data = {
            "name": groupName,
            "description": description,
            "createdBy": getCookie('username'),
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
                displayMessage.innerText = response.error().statusText;
            } else {
                displayMessage.innerText = "Successfully Created";
            }
        }).catch((e)=>{
            displayMessage.innerText = "Unsuccessfully Created";
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
    var username = getCookie("username");

    fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ username)
        .then((response) => {
            return response.json();
        })
        .then((groupData) => {
            modOfGroup.innerText = "";
            for(group in groupData){
                modOfGroup.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
            }
        })
        .catch((error) => {
            displayMessage.innerText = error;
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

}

function deleteGroup() {
    closeAllDisplay();
    deleteGroupForm.style.display = "block";

    let displayMessage = document.getElementById("delete_group_message");

    var modOfGroup = new Set();
    // GET LIST OF GROUPS USER IS MODERATOR OF
    fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ localStorage.getItem('username'))
        .then((response) => {
            return response.json();
        })
        .then((groupData) => {
            console.log(groupData);
            for ( group in groupData){
                for(let i = 0; i < groupData[group].moderators.length; i++){
                    if(groupData[group].moderators[i] === localStorage.getItem('username')) {
                        modOfGroup.add(group);
                    }
                }
            }
        })
        .catch((error)=> {
            displayMessage.innerText = error;
        })
    modOfGroup.forEach((group) =>{
        moderatorOfGroup.innerHTML += "<option value=" + group.key + ">" + group.value + "</option>";
    })
    //Save Selected
    selectedGroup = moderatorOfGroup.value;
}

function viewGroupButton(){
    closeAllDisplay()
    viewAllGroupUserIsApartOf.style.display = "block";
    let displayMessage = document.getElementById("view_group_message");
    let groupsParticipation = document.getElementById("groupsParticipation");
    fetch('http://localhost:8080/prattle/rest/group/getAllUserGroups/'+ localStorage.getItem('username'))
        .then((response) => {
        return response.json();
    })
    .then((groupData) => {
            console.log(groupData);
        groupsParticipation.innerHTML = "";
        for(group in groupData){
            groupsParticipation.innerHTML += "<option value=" + groupData[group].name + ">" + groupData[group].name + "</option>";
        }
    })
    .catch((error)=> {
            displayMessage.innerText = error;
    })
}


function deleteGroupButton(){
    let displayMessage = document.getElementById("delete_group_message");
    // DELETE A GROUP
    fetch('http://localhost:8080/prattle/rest/deleteGroup/' + selectedGroup, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(group_data),
    }).then((response)=>{
        displayMessage.innerText = response.statusText;
}).catch((error) => {
        displayMessage.innerText = error.toString();
})
}

function submitGroupView(){
    console.log(groupsParticipation.value)
}



function submitAddUserGroupCreation(){
    let newusername = document.addUserGroupForm.newusername.value;
    let groupname = document.addUserGroupForm.groupname.value;
    let displayMessage = document.getElementById("adduser_group_message");
    if( newusername.trim() !== "" && groupname.trim() !== ""){
        fetch('http://localhost:8080/prattle/rest/group/addUser/' + groupname + '/' + newusername, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        }).then((response)=>{
            if(!response.ok){
            displayMessage.innerText = "Error while adding user " + newusername;
        } else {
            displayMessage.innerText = "Successfully Add user " + newusername;
        }
    }).catch((e)=>{
            displayMessage.innerText = "Unsuccessfully Created";
    })
    }
}