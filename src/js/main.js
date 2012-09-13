
function init(){
	$("#inputBox").focus();
	$("#inputBox").width($("#chatWindow").width());
	setInterval(pollServer,500);	
	setInterval(rotateDiv,30);
}
	
var angle = 0;
function rotateDiv(){
	angle+=5;
	$("#chatWindow").css("transform", "rotateX(" + angle+ "deg)");
	$("#inputBox").css("transform", "rotateX(" + angle+ "deg)");
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