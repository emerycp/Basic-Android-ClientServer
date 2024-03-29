package com.emerycprimeau;

import com.emerycprimeau.exception.*;
import com.emerycprimeau.model.Token;
import com.emerycprimeau.model.User;
import com.emerycprimeau.transfer.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/") // reçoit tout ce qui est dans /api/
public class WebService {

    private BD bd = new BD();
    public WebService(){

    }

    //region Exemple

    //  -> /api/hello/paramName
    @GET @Path("hello/{name}")
    public String helloGET(
            @PathParam("name") String nom
            //@QueryParam("option") String option
    ){
        //if (option==nul) throw new IllegalAccessException();
        return "Hello " + nom;
    }

    //  -> /api/hello/paramName
    @POST
    @Path("hello/{name}")
    public String helloPOST(

            @PathParam("name") String nom
            //@QueryParam("option") String option
    ){
        return "Coucou " + nom;
    }

    @POST
    @Path("null")
    public LoginResponse toLogin(LoginRequest login){
        System.out.println("POST SignIn " + login.user);
        LoginResponse t = new LoginResponse();
        t.Id = Integer.parseInt(UUID.randomUUID().toString());
        t.emailCleaned = login.user;
        return t;
    }

    //endregion


    // --------------------- Code ------------------------- //

    @POST
    @Path("init")
    public void toInit () {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Init complété!");
        bd.InitUsers();
    }

    @POST
    @Path("test")
    public Boolean create (String user) throws NoMatch {
        System.out.println("STRINNNNNNNGGGG      Nouveau utilisateur au nom de -> " + user);
        return true;
    }

    @POST
    @Path("create")
    public Boolean create (LoginRequest user) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Nouveau utilisateur au nom de -> " + user.user);
        return bd.create(user);
    }

    @POST
    @Path("click")
    public User toClick (LoginRequest logR) throws NoMatch {
        System.out.println("Utilisateur suivant ajouter une piece! -> " + logR.user);
        return bd.toClick(logR);
    }

    @GET
    @Path("howmany/{id}")
    public User howMany (@PathParam("id")int id) throws NoMatch {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Utilisateur suivant souhaite savoir combien il y en a au total -> " + id);
        return bd.howMany(id);
    }


    @POST
    @Path("createCookie")
    public Response createCookie () {
        String id = UUID.randomUUID().toString();
        String token = UUID.randomUUID().toString();
        NewCookie nC = new NewCookie(BD.Cookie, token, "/","", "id token", 604800, true);
        Token t = new Token(token, id, 604800);
        BD.listToken.add(t);
        System.out.println("Creation Token " + t.token + ". Creation cookie " + nC.getValue());
        return Response.ok(new Gson().toJson(t), MediaType.APPLICATION_JSON).cookie(nC).build();
    }

    @GET
    @Path("deleteCookie")
    public Response toDelete (@CookieParam(BD.Cookie)Cookie cookie) throws TokenNotFound {

        for(Token u: BD.listToken)
        {
            if(cookie.getValue().equals(u.token))
            {
                System.out.println("Suppression Token et Cookie " + u.token);
                BD.listToken.remove(u);
                NewCookie aSupprimer = new NewCookie(BD.Cookie, null, "/", null, null, 0, true);

                return Response.ok(new Gson().toJson(true), MediaType.APPLICATION_JSON).cookie(aSupprimer).build();
            }
        }
        throw new TokenNotFound("NoU");
    }

}
