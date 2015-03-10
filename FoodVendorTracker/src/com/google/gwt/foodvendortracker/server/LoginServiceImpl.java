package com.google.gwt.foodvendortracker.server;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.foodvendortracker.client.LoginInfo;
import com.google.gwt.foodvendortracker.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService
{
  public LoginInfo login(String requestUri) 
  {
    UserService userService 	= 		UserServiceFactory.getUserService()		;
    LoginInfo loginInfo 		=  		new LoginInfo()							;
    User user					= 		userService.getCurrentUser()			;
    

    if (user != null) 
    {
      loginInfo.setLoggedIn(true)											;
      loginInfo.setEmailAddress(user.getEmail())							;
      loginInfo.setNickname(user.getNickname())								;
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri))		;
    } 
    else 
    {
      loginInfo.setLoggedIn(false)											;
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri))			;
    }
    return loginInfo;
  }

}
