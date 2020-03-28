let groupForm = document.getElementById("create_group_form");
let viewGroup = document.getElementById("view_group_section");

closeAllDisplay();

function closeAllDisplay(){
    groupForm.style.display = "none";
    viewGroup.style.display = "none";
}

function createGroup(){
    closeAllDisplay();
    groupForm.style.display = "block";
}

function viewGroupMethod(){
    closeAllDisplay()
    viewGroup.style.display = "block";

    fetch('http://localhost:8080/prattle/rest/group/getUser/'+ localStorage.getItem('username'))
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            console.log(data);
        })
        .catch((error)=> {
            displayMessage.innerText = error;
        })
}

function submitGroupCreation(){
    let groupName = document.groupForm.groupName.value;
    let description = document.groupForm.description.value;
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

        fetch('http://localhost:8080/prattle/rest/group/addModerator/' + groupName + '/' + localStorage.getItem('username'),{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(group_data),
        }).then((response)=>{
            if(!response.ok){
            displayMessage.innerText = "Group already exist. Try another group name.";
        } else {
            displayMessage.innerText = "Successfully Created group with moderator";
        }
    }).catch((e)=>{
            displayMessage.innerText = "Unsuccessfully Created";
    })

        fetch('http://localhost:8080/prattle/rest/group/addUser/' + groupName + '/' + localStorage.getItem('username'),{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(group_data),
        }).then((response)=>{
            if(!response.ok){
            displayMessage.innerText = "Group already exist. Try another group name.";
        } else {
            displayMessage.innerText = "Successfully Created group with moderator";
        }
    }).catch((e)=>{
            displayMessage.innerText = "Unsuccessfully Created";
    })
    }
}
function clear(){

}

function viewGroupButton(){
    closeAllDisplay()
    viewAllGroupUserIsApartOf.style.display = "block";
    let displayMessage = document.getElementById("view_group_message");
    let groupsParticipation = document.getElementById("groupsParticipation");
    fetch('http://localhost:8080/prattle/rest/group//getAllUserGroups//'+ localStorage.getItem('username'))
        .then((response) => {
        return response.json();
})
.then((groupData) => {
        console.log(groupData);
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
