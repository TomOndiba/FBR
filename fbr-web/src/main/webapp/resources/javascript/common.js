// common js file

// Pal
// var HOST_PREFIX = "http://localhost:8080";
//var clientAppId = "247266912049837";
//var appSecret = "5624e24f6f98a2835da033422ba93798";

// Kodi
var HOST_PREFIX = "http://71.225.132.109:8090";
var clientAppId = "358829927522628";
var appSecret = "5d470688943d6c6c9f941cc900b5d7de";

var docBase = "/fbr-web";

var fbCodeUrl = "https://www.facebook.com/dialog/oauth";
var fbAccessTokenUrl = "https://graph.facebook.com/oauth/access_token";
var fbLogoutUrl = "https://www.facebook.com/logout.php";

// friends_about_me,
var permissions = "user_about_me,user_birthday,user_education_history,user_location,user_relationships,user_work_history,email";

var state = "fbr123fbr";

function loginToFacebookServer() {
	var fbrform = document.forms['homeForm'];
	// fbrform.action = 'homeBean.loginToFacebook';
	fbrform.action = '#{homeBean.loginToFacebook}';
	fbrform.submit();
	return false;
}

function validateAccessToken() {
	var codeObj = document.getElementById('dashboardForm:code');

	var accessTokenObj = document.getElementById('dashboardForm:accessToken');

	if (codeObj == null || accessTokenObj == null || codeObj.value == ""
			|| accessTokenObj.value == "") {
		// alert('codeObj.value:'+codeObj.value);
		// alert('accessTokenObj.value:'+accessTokenObj.value);
		alert("Invalid Access to FBR and Facebook.\n\nPlease login to Facebook Reader.");
		window.location.href = HOST_PREFIX + docBase + "/home.jsf";
	}

}

function loginToFacebookClient() {

	var redirect_uri = HOST_PREFIX + docBase + "/dashboard.jsf";

	window.location.href = fbCodeUrl + "?client_id=" + clientAppId
			+ "&redirect_uri=" + redirect_uri + "&state=" + state + "&scope="
			+ permissions;

	return true;
}

function logoutFromFacebookClient() {
	var logoutToken = document.getElementById('dashboardForm:accessToken').value;

	var fbr_logout_uri = HOST_PREFIX + docBase + "/logout.jsf";
	
	var logoutUri = fbLogoutUrl + "?next=" + fbr_logout_uri	+ "&access_token=" + logoutToken;
	
	window.location.href = logoutUri;
	
	return false;
}

function getAccessToken() {
	var redirect_uri = HOST_PREFIX + docBase + "/dashboard.jsf&state=fbr123fbr";
	var code = getUrlVars()["code"];

	var url = fbAccessTokenUrl + "?client_id=" + clientAppId + "&redirect_uri="
			+ redirect_uri + "&client_secret=" + appSecret + "&code=" + code;

	window.location.href = url;
	return false;
}

/**
 * Get URL parameters & values with jQuery. Ex: To get the value of code var
 * code = getUrlVars()["code"];
 */
function getUrlVars() {
	var vars = [], hash;
	var hashes = window.location.href.slice(
			window.location.href.indexOf('?') + 1).split('&');
	for ( var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
	return vars;

}
