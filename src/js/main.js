
function init(){
	setTitle();
	$("#inputBox").focus();
	$("#inputBox").width($("#chatWindow").width());
	setInterval(pollServer,500);	
}

function pollServer(){


	$.ajax({
	  url: "/updates",
	  dataType: 'json'
	}).complete(showMessages);
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
	
	var userObj = $.parseJSON(data.responseText);
	if(!data || !data.responseText || !userObj ){
		chatWindow.append("No users connected...");
		return;
	}



	var userList = userObj.users;
	var userListString = "";
	for(var user in userList){
		userListString += (userList[user] + (user == userList.length-1?"":", "));
	}
	chatWindow.append(userList.length + " user(s) connected : "  + userListString);
}

function handleKeyPress(e){
	var key=e.keyCode || e.which;
	if (key==13){
		sendMessage();
	}
}

function showMessages(data){
	var chatWindow = $("#chatWindow");

	var messageObj = $.parseJSON(data.responseText);
	var messages = messageObj.messages;
	
	for(var m in messages){
		console.log(messages[m].username);
		var username = messages[m].username?messages[m].username:'Unknown';
		var messageData = messages[m].messageData?messages[m].messageData:'Unknown';
		chatWindow.append(username + " : " + messageData + "<br>");
	}
	
}