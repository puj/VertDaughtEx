
function init(){
	$("#inputBox").focus();
	$("#inputBox").width($("#chatWindow").width());
	setInterval(pollServer,500);	
}
	

function pollServer(){

	$.ajax({
	  url: "/users",
	  dataType: 'json'
	}).complete(showUsers);
}

function sendMessage(){
	var inputBox = $("#inputBox");
	if(inputBox.val().length == 0){
		return;
	}
	var inputData = inputBox.val();
	inputBox.val("");

	$.ajax({
	  type: 'POST',
	  url: "/message",
	  data: inputData,
	  dataType: "json"
	});

	inputBox.empty();
}

function setTitle(){
	$("#chatWindow").append("OMG AHWE TERMS  C-HAT ~!! !L!!!<br>");
	$("#chatWindow").append("=========================================<br>");
}

function showUsers(data){
	var chatWindow = $("#chatWindow");
	chatWindow.empty();
	setTitle();

	if(!data || !data.users || data.users.length == 0){
		chatWindow.append("No users connected...");
		return;
	}

	var userList = data.users;
	var userListString = "";
	for(var user in userList){
		userListString += (user + (user == userList[userList.length-1]?"":", "));
	}
	chatWindow.append(userList.length + " user(s) connected : "  + userListString);
}

function handleKeyPress(e){
	var key=e.keyCode || e.which;
	if (key==13){
		sendMessage();
	}
}