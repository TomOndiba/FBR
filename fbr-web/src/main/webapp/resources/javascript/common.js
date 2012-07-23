// common js file

var clientAppId = "247266912049837";
var appSecret = "5624e24f6f98a2835da033422ba93798";
var state = "fbr123fbr";

//friends_about_me,
var permissions = "user_about_me,user_birthday,user_education_history,user_location,user_relationships,user_work_history,email";


function loginToFacebookServer() {
	var fbrform = document.forms['homeForm'];
//	fbrform.action = 'homeBean.loginToFacebook';
	fbrform.action = '#{homeBean.loginToFacebook}';
	fbrform.submit();
    return false;
}

function validateAccessToken() {
	var codeObj = document.getElementById('dashboardForm:code');  
	
	var accessTokenObj = document.getElementById('dashboardForm:accessToken');
	
	if(codeObj == null || accessTokenObj == null || codeObj.value == "" || accessTokenObj.value == ""){
		//alert('codeObj.value:'+codeObj.value);
		//alert('accessTokenObj.value:'+accessTokenObj.value);
		alert("Facebook Access Token Invalid or Expired.\n\nPlease login to Facebook Reader.");
		window.location.href = "http://localhost:8080/fbr-web/home.jsf";
	}
	
	
}


function loginToFacebookClient() {
	var fbCodeUrl = "https://www.facebook.com/dialog/oauth";
	var redirect_uri = "http://localhost:8080/fbr-web/dashboard.jsf";
	
	window.location.href = fbCodeUrl + 
		"?client_id=" + clientAppId +
		"&redirect_uri=" + redirect_uri +
		"&state=" + state +
		"&scope=" + permissions ;
	
    return true;
}


function logoutFromFacebookClient() {
	var logoutToken = document.getElementById('dashboardForm:accessToken').value;
	var fbLogoutUrl = "https://www.facebook.com/logout.php";
	var redirect_uri = "http://localhost:8080/fbr-web/logout.jsf";
	window.location.href = fbLogoutUrl + "?next=" + redirect_uri + "&access_token=" + logoutToken ;
    return true;
}

function getAccessToken() {
	var redirect_uri = "http://localhost:8080/fbr-web/dashboard.jsf&state=fbr123fbr" ;
	var code = getUrlVars()["code"];
	
	var url = "https://graph.facebook.com/oauth/access_token?" +
	   "client_id="+ clientAppId +
	   "&redirect_uri=" + redirect_uri +
	   "&client_secret="+ appSecret +
	   "&code="+ code;
	   
	   
	window.location.href = url;
    return false;
}

/**
 * Get URL parameters & values with jQuery. Ex: To get the value of code
 * var code = getUrlVars()["code"];
 */
function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)  {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
    
}
