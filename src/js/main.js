
function init(){
	$("#inputBox").focus();
	$("#inputBox").width($("#chatWindow").width());
	setInterval(pollServer,500);	
	setInterval(rotateDiv,30);
}
	
var angle = 0;
var shrink = true;
var maxAngle = 65;
function rotateDiv(){

	if(angle > maxAngle){
		shrink = true;
	}

	if(angle < 0){
		shrink = false;
	}
	if(shrink){
		angle-=1;
	}else{
		angle+=1;
	}
	$("#chatWindow").css("transform", "rotateX(" + angle+ "deg)");
	$("#inputBox").css("transform", "rotateX(" + angle+ "deg)");

	var maxOffset = 300;
	$("#chatWindow").offset({left:(maxOffset+maxOffset*Math.sin((360/maxAngle)*(angle)/50))} )
	$("#inputBox").offset({left:(maxOffset+maxOffset*Math.sin((360/maxAngle)*(angle)/50))} )


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