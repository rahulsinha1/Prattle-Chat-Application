let createForm = document.getElementById("create_group_form");
let addUserGroupForm = document.getElementById("adduser_group_section");
let viewAllGroupUserIsApartOf = document.getElementById("view_group_section");
let deleteGroupForm = document.getElementById("delete_group_form");
let moderatorOfGroup = document.getElementById("moderatorOfGroup");
let selectedGroup = moderatorOfGroup.value;

checkCookie();

closeAllDisplay();


function logout() {
    setCookie("username", "", 365);
    window.location.href = 'login.html';
}

function closeAllDisplay(){
    createForm.style.display = "none";
    viewAllGroupUserIsApartOf.style.display = "none";
    deleteGroupForm.style.display = "none";
    addUserGroupForm.style.display = "none";
}

function createGroup(){
    closeAllDisplay();
    createForm.style.display = "block";
}

function addUserToGroup() {
    closeAllDisplay();
    addUserGroupForm.style.display = "block";
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

function submitGroupCreation(){
    let groupName = document.createGroupForm.groupName.value;
    let description = document.createGroupForm.description.value;
    let displayMessage = document.getElementById("create_group_message");
    displayMessage.innerText = "";
    if( groupName.trim() !== ""){
        const group_data = {
            "name": groupName,
            "description": description,
            "createdBy": localStorage.getItem('username'),
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
            displayMessage.innerText = "Group already exist. Try another group name.";
        } else {
            displayMessage.innerText = "Successfully Created";
        }
    }).catch((e)=>{
            displayMessage.innerText = "Unsuccessfully Created";
    })
    }
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

function checkCookie() {
    var username=getCookie("username");
    if (username != "") {
        document.getElementById("welcoming").innerText = "Welcome " + username ;
    }else{
        window.location.href = '404.html';
    }
}

function setCookie(cookie_name, cookie_value, exdays){
    var dt = new Date();
    dt.setTime(dt.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+dt.toUTCString();
    document.cookie = cookie_name + "=" + cookie_value + "; " + expires;
}



function clear(){
}