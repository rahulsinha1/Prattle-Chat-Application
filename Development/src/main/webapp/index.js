let groupForm = document.getElementById("create_group_form");
let viewGroup = document.getElementById("view_group");

closeAllDisplay();

function closeAllDisplay(){
    groupForm.style.display = "none";
    viewGroup.style.display = "none";
}

function createGroup(){
    closeAllDisplay();
    groupForm.style.display = "block";
}

function viewGroup(){
    closeAllDisplay()
    viewGroup.style.display = "block";

    fetch('http://localhost:8080/prattle/rest/group/getUser/'+ username)
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
    }
}



function clear(){

}
